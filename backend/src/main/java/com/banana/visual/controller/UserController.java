package com.banana.visual.controller;

import com.alibaba.fastjson.JSONObject;
import com.banana.visual.VO.ResultVO;
import com.banana.visual.VO.UserInfoVO;
import com.banana.visual.context.UserContext;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.service.UserService;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户业务控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/16
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    User JSON2User(JSONObject body) {
        User user = new User();
        if (body.getString("uid") != null) {
            user = userService.getUserByUid(Long.valueOf(body.getString("uid"))); // 获取数据库中User
        }
        if (body.getString("permission") != null) {
            user.setPermission(body.getString("permission"));
        }
        if (body.getString("username") != null) {
            user.setUsername(body.getString("username"));
        }
        if (body.getString("realName") != null) {
            user.setRealName(body.getString("realName"));
        }
        if (body.getString("jid") != null) {
            user.setJid(Long.valueOf(body.getString("jid")));
        }
        if (body.getString("phone") != null) {
            user.setPhone(body.getString("phone"));
        }
        if (body.getString("email") != null) {
            user.setEmail(body.getString("email"));
        }
        if (body.getString("signature") != null) {
            user.setSignature(body.getString("signature"));
        }
        return user;
    }

    @PostMapping("/checkLogin")
    public ResultVO checkLogin() {
        ResultVO resultVO = new ResultVO(HttpStatus.SC_OK, "success");
        return resultVO;
    }

    @GetMapping("/getUserBaseInfo")
    public ResultVO getUserBaseInfo() {
        Long uid = UserContext.get().getUid();
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(HttpStatus.SC_OK);
        resultVO.setMsg("success");
        resultVO.setData(userService.getUserBaseInfoByUid(uid));
        return resultVO;
    }

    @PostMapping("/getUserInfoByUid")
    public ResultVO getUserInfoByUid(@RequestBody JSONObject body) {
        Long uid = Long.valueOf(body.getString("uid"));
        UserInfoVO userInfoVO = userService.getUserInfoByUid(uid);
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(HttpStatus.SC_OK);
        resultVO.setMsg("success");
        resultVO.setData(userInfoVO);
        return resultVO;
    }

    @GetMapping("/getUserInfoList")
    public ResultVO getUserInfoList() {
        List<UserInfoVO> userInfoVOList = userService.getUserInfoList();
        return new ResultVO(HttpStatus.SC_OK, "success", userInfoVOList);
    }

    @PostMapping("/saveUserInfo")
    public ResultVO saveUserInfo(@RequestBody JSONObject body) {
        User user = JSON2User(body);
        boolean res = userService.updateUser(user);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_INSERT_EXCEPTION);
    }

    @PostMapping("/addUser")
    public ResultVO addUser(@RequestBody JSONObject body) {
        User user = JSON2User(body);
        String password = userService.addUser(user);
        return new ResultVO(HttpStatus.SC_OK, "success", password);
    }

    @PostMapping("/deleteUser")
    public ResultVO deleteUser(@RequestBody JSONObject body) {
        Long uid = Long.valueOf(body.getString("uid"));
        boolean res = userService.deleteUser(uid);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_DELETION_EXCEPTION);
    }

    @PostMapping("/saveAvatar")
    public ResultVO saveAvatar(@RequestBody JSONObject body) {
        String avatarURL = body.getString("avatarURL");
        System.out.println(body.getLong("uid"));
        Long uid = body.getLong("uid");
        boolean res = userService.updateAvatar(uid, avatarURL);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_UPDATE_EXCEPTION.getCode(), "头像更新失败，请重试！");
    }

    @PostMapping("/savePassword")
    public ResultVO savePassword(@RequestBody JSONObject body) {
        String oldPassword = body.getString("oldPassword");
        String newPassword = body.getString("newPassword");
        boolean res = userService.updatePassword(UserContext.get().getUid(), oldPassword, newPassword);
        if (res) {
            return new ResultVO(HttpStatus.SC_OK, "success");
        }
        return new ResultVO(ResultEnum.DATA_UPDATE_EXCEPTION.getCode(), "旧密码错误，若遗忘请联系管理员重置！");
    }

    @PostMapping("/resetPassword")
    public ResultVO resetPassword(@RequestBody JSONObject body) {
        Long uid = Long.valueOf(body.getString("uid"));
        String newPassword = userService.resetPassword(uid);
        return new ResultVO(HttpStatus.SC_OK, "success", newPassword);
    }
}
