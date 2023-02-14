package com.banana.visual.service;

import com.banana.visual.VO.ResultVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface LoginAndLogoutService {

    ResultVO<Map<String, Object>> login(String username, String password, HttpServletRequest request,
                                        HttpServletResponse response) throws IOException;
}
