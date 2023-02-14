package com.banana.visual.interceptor;

import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.context.UserContext;
import com.banana.visual.entity.mysql.User;
import com.banana.visual.utils.IpUtil;
import com.banana.visual.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@Configuration
public class LoginInterceptor implements HandlerInterceptor, WebMvcConfigurer {

    @Value("${shiro.mustLoginFlag}")
    private Boolean mustLoginFlag;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        if (mustLoginFlag && token != null) {
            Long uid = new Long(JwtUtil.getClaim(token, ShiroConstant.UID));
            String permission = JwtUtil.getClaim(token, "permission");
            User user = new User();
            // TODO 目前只设置了uid和permission参数
            user.setUid(uid);
            user.setPermission(permission);
            UserContext.set(user);
        }

        // 记录请求信息
        log.info("[{}] 请求者uid=[{}] 权限=[{}] 发起者IP=[{}]", request.getRequestURI(),
                UserContext.get().getUid(), UserContext.get().getPermission(), IpUtil.getIp2(request));

        return Boolean.TRUE;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
