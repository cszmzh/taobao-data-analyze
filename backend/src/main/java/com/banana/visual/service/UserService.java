package com.banana.visual.service;

import com.banana.visual.VO.UserBaseInfoVO;
import com.banana.visual.VO.UserInfoVO;
import com.banana.visual.entity.mysql.User;

import java.util.List;

public interface UserService {
    /**
     * 1.根据uid获取用户所有信息
     *
     * @param uid
     * @return 完整用户信息
     */
    User getUserByUid(Long uid);

    /**
     * 2.根据用户名获取用户信息
     *
     * @param username
     * @return 完整用户信息
     */
    User getUserByUsername(String username);

    /**
     * 3.根据uid获取用户基本信息
     *
     * @param uid
     * @return 用户基本信息
     */
    UserBaseInfoVO getUserBaseInfoByUid(Long uid);

    /**
     * 4.根据uid获取用户信息
     *
     * @param uid
     * @return
     */
    UserInfoVO getUserInfoByUid(Long uid);

    List<UserInfoVO> getUserInfoList();

    /**
     * 5.更新用户信息
     *
     * @param user
     * @return
     */
    boolean updateUser(User user);

    String addUser(User user);

    /**
     * 6.删除用户
     *
     * @param uid
     * @return
     */
    boolean deleteUser(Long uid);

    /**
     * 7.更新用户密码
     *
     * @param uid
     * @param oldPassword
     * @param newPassword
     * @return
     */
    boolean updatePassword(Long uid, String oldPassword, String newPassword);

    /**
     * 8.更新用户头像
     */
    boolean updateAvatar(Long uid, String avatarURL);

    /**
     * 9.重置密码
     */
    String resetPassword(Long uid);
}
