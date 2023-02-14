package com.banana.visual.service.impl;

import com.banana.visual.VO.LoginInfoVO;
import com.banana.visual.VO.ResultVO;
import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.entity.mongo.LoginInfo;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.repository.LoginInfoRepository;
import com.banana.visual.service.LoginAndLogoutService;
import com.banana.visual.service.UserService;
import com.banana.visual.utils.IpUtil;
import com.banana.visual.utils.JwtUtil;
import com.banana.visual.utils.RedisUtil;
import com.banana.visual.utils.ResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class LoginAndLogoutServiceImpl implements LoginAndLogoutService {
    @Autowired
    private LoginInfoRepository loginInfoRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * RefreshToken过期时间
     */
    @Value("${shiro.refreshTokenExpireTime}")
    private long refreshTokenExpireTime;

    @Override
    public ResultVO<Map<String, Object>> login(String username, String password, HttpServletRequest request, HttpServletResponse response) throws IOException {

        // 查询数据库中的帐号信息
        User user = userService.getUserByUsername(username);

        // 若用户不存在，返回错误信息
        if (user == null) {
            return ResultGenerator.genFailResult(ResultEnum.USER_NOT_EXIST);
        }

        // 密码进行AES解密
        // 因为密码加密是以帐号+密码的形式进行加密的，所以解密后的对比是帐号+密码(loginFlag=1 免密登录)
        if (password.equals(user.getPassword())) {
            // 清除可能存在的Shiro权限信息缓存
            if (redisUtil.hasKey(ShiroConstant.PREFIX_SHIRO_CACHE + user.getUid().toString())) {
                redisUtil.del(ShiroConstant.PREFIX_SHIRO_CACHE + user.getUid().toString());
            }
            // 1:pc端 2:移动端
            String serviceType = "1";
            if (!StringUtils.isEmpty(request.getHeader("serviceType"))) {
                serviceType = request.getHeader("serviceType");
            }
            // 设置RefreshToken，时间戳为当前时间戳，直接设置即可(不用先删后设，会覆盖已有的RefreshToken)
            String currentTimeMillis = String.valueOf(System.currentTimeMillis());
            redisUtil.set(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + user.getUid().toString() + ":" + serviceType, currentTimeMillis, refreshTokenExpireTime);

            //把用户设置到redis中,不需要重复查询
            redisUtil.set(ShiroConstant.PREFIX_SHIRO_USER + user.getUid().toString(), user, refreshTokenExpireTime);

            // 从Header中Authorization返回AccessToken，时间戳为当前时间戳
            String token = JwtUtil.sign(user.getUid().toString(), user.getPermission(), currentTimeMillis, serviceType);
            response.setHeader("Authorization", token);
            response.setHeader("Access-Control-Expose-Headers", "Authorization");

            // 返回UID 用户名 头像链接 权限
            LoginInfoVO loginInfoVO = new LoginInfoVO();
            loginInfoVO.setUid(user.getUid());
            loginInfoVO.setUsername(user.getAvatar());
            loginInfoVO.setAvatar(user.getAvatar());
            loginInfoVO.setPermission(user.getPermission());
            loginInfoVO.setToken(token);

            log.info("【账号登录】uid=[{}]", user.getUid().toString());

            // 登录记录存储到数据库
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUid(user.getUid());
            loginInfo.setIp(IpUtil.getIp2(request));
            loginInfo.setLoginTimestamp(System.currentTimeMillis());
            loginInfoRepository.save(loginInfo);

            return new ResultVO(HttpStatus.OK.value(), "登录成功", loginInfoVO);
        } else {
            return ResultGenerator.genFailResult(ResultEnum.USER_PASSWORD_ERROR);
        }
    }
}