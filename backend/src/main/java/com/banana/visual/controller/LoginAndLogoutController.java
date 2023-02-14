package com.banana.visual.controller;

import com.alibaba.fastjson.JSONObject;
import com.banana.visual.VO.ResultVO;
import com.banana.visual.service.LoginAndLogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * 登录控制层
 *
 * @author github@bananaza
 * Created by bananaza on 2022/4/16
 */
@RestController
@RequestMapping("/access")
public class LoginAndLogoutController {

    @Autowired
    private LoginAndLogoutService loginAndLogoutService;

    @PostMapping(value = "/login", produces = "application/json; charset=utf-8")
    public ResultVO<Map<String, Object>> login(@RequestBody JSONObject body, HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {
        String username = body.getString("username");
        String password = body.getString("password");
        return loginAndLogoutService.login(username, password, request, response);
    }
}
