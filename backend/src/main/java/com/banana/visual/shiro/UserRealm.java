package com.banana.visual.shiro;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.service.UserService;
import com.banana.visual.shiro.jwt.JwtToken;
import com.banana.visual.utils.JwtUtil;
import com.banana.visual.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 自定义Realm
 */
@Service
@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * RefreshToken过期时间
     */
    @Value("${shiro.refreshTokenExpireTime}")
    private long refreshTokenExpireTime;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 大坑，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的（授权）
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return (AuthorizationInfo) principals;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。（认证）
     *
     * @param auth
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得account，用于和数据库进行对比
        String uid = JwtUtil.getClaim(token, ShiroConstant.UID);
        String serviceType = JwtUtil.getClaim(token, ShiroConstant.SERVICETYPE);
        // 帐号为空
        if (StringUtils.isBlank(uid)) {
            throw new AuthenticationException("Token中帐号为空");
        }
        User user = (User) redisUtil.get(ShiroConstant.PREFIX_SHIRO_USER + uid);
        if (user == null) {
            // 如果数据库中还没有就说明账号不存在
            // 查询用户是否存在
            user = userService.getUserByUid(new Long(uid));
            if (user == null) {
                throw new AuthenticationException("该帐号不存在");
            }
            //redis中在设置一次
            redisUtil.set(ShiroConstant.PREFIX_SHIRO_USER + user.getUid().toString(), user, refreshTokenExpireTime);
        }
        // 开始认证，要AccessToken认证通过，且Redis中存在RefreshToken，且两个Token时间戳一致
        if (JwtUtil.verify(token)) {
            if (redisUtil.hasKey(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + uid + ":" + serviceType)) {
                // 获取RefreshToken的时间戳
                String currentTimeMillisRedis = redisUtil.get(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + uid + ":" + serviceType).toString();
                // 获取AccessToken时间戳，与RefreshToken的时间戳对比
                if (JwtUtil.getClaim(token, ShiroConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                    return new SimpleAuthenticationInfo(token, token, "userRealm");
                }
            } else {
                throw new AuthenticationException("Token授权异常");
            }
        }
        throw new TokenExpiredException("Token已过期");
    }
}