package com.banana.visual.service.impl;

import com.banana.visual.VO.UserBaseInfoVO;
import com.banana.visual.VO.UserInfoVO;
import com.banana.visual.context.UserContext;
import com.banana.visual.entity.mongo.LoginInfo;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.enums.UserRolesEnum;
import com.banana.visual.repository.DepartmentRepository;
import com.banana.visual.repository.JobRepository;
import com.banana.visual.repository.LoginInfoRepository;
import com.banana.visual.repository.UserRepository;
import com.banana.visual.service.UserService;
import com.banana.visual.utils.PasswordLevelUtil;
import com.banana.visual.utils.StrUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private LoginInfoRepository loginInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private JobRepository jobRepository;

    @Override
    public User getUserByUid(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user;
    }

    @Override
    public UserBaseInfoVO getUserBaseInfoByUid(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        UserBaseInfoVO userBaseInfoVO = new UserBaseInfoVO();
        BeanUtils.copyProperties(user, userBaseInfoVO);
        // 加入权限
        String[] roles = new String[1];
        roles[0] = user.getPermission();
        userBaseInfoVO.setPermission(roles);
        userBaseInfoVO.setDepId(jobRepository.findById(user.getJid()).orElse(null).getDepId());
        return userBaseInfoVO;
    }

    @Override
    public UserInfoVO getUserInfoByUid(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);

        userInfoVO.setDepId(jobRepository.findById(user.getJid()).orElse(null).getDepId());

        // 获取最近5条登陆记录
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setUid(uid);
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withMatcher("uid", ExampleMatcher.GenericPropertyMatchers.contains());
        Example<LoginInfo> example = Example.of(loginInfo, matcher);

        Sort sort = Sort.by(Sort.Order.desc("loginTimestamp"));
        Pageable pageable = PageRequest.of(0, 5, sort);

        List<LoginInfo> loginInfoList = loginInfoRepository.findAll(example, pageable).toList();

        userInfoVO.setLoginInfoList(loginInfoList);

        // 获取密码强度
        userInfoVO.setPasswordLevel(PasswordLevelUtil.checkPassword(user.getPassword()));
        // 获取部门名
        userInfoVO.setDepName(departmentRepository.getDeptNameByJobId(user.getJid()));
        // 获取岗位名
        userInfoVO.setJobName(jobRepository.findById(user.getJid()).orElse(null).getJobName());

        return userInfoVO;
    }

    @Override
    public List<UserInfoVO> getUserInfoList() {
        List<User> userList = userRepository.findAll();
        List<UserInfoVO> userInfoVOList = new ArrayList<>();

        String permission = UserContext.get().getPermission();
        Long depId = getUserBaseInfoByUid(UserContext.get().getUid()).getDepId();

        for (User user : userList) {
            // 如果不是管理员
            if (!UserRolesEnum.HIGH.getPermission().equals(permission)) {
                // 部门不匹配
                if (!Objects.equals(getUserBaseInfoByUid(user.getUid()).getDepId(), depId)) {
                    continue;
                }
            }

            UserInfoVO userInfoVO = new UserInfoVO();
            // 编号
            userInfoVO.setUid(user.getUid());
            // 用户名
            userInfoVO.setUsername(user.getUsername());
            // 真名
            userInfoVO.setRealName(user.getRealName());
            // 头像
            userInfoVO.setAvatar(user.getAvatar());
            // 权限
            userInfoVO.setPermission(user.getPermission());
            // 获取部门名
            userInfoVO.setDepName(departmentRepository.getDeptNameByJobId(user.getJid()));
            // 获取岗位名
            userInfoVO.setJobName(jobRepository.findById(user.getJid()).orElse(null).getJobName());
            userInfoVOList.add(userInfoVO);
        }
        return userInfoVOList;
    }

    @Override
    public String addUser(User user) {
        String password = StrUtil.getRandomString(8);
        user.setPassword(password);
        userRepository.saveAndFlush(user);
        return password;
    }

    @Override
    public boolean updateUser(User user) {
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public boolean deleteUser(Long uid) {
        userRepository.deleteById(uid);
        return true;
    }

    @Override
    public boolean updatePassword(Long uid, String oldPassword, String newPassword) {
        User user = userRepository.findById(uid).orElse(null);
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            updateUser(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateAvatar(Long uid, String avatarURL) {
        User user = userRepository.findById(uid).orElse(null);
        user.setAvatar(avatarURL);
        userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public String resetPassword(Long uid) {
        User user = userRepository.findById(uid).orElse(null);
        String newPassword = StrUtil.getRandomString(8);
        user.setPassword(newPassword);
        return newPassword;
    }
}
