package com.banana.visual.shiro.jwt;

import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.banana.visual.constants.ShiroConstant;
import com.banana.visual.utils.IpUtil;
import com.banana.visual.utils.JsonConvertUtil;
import com.banana.visual.utils.JwtUtil;
import com.banana.visual.utils.RedisUtil;
import com.banana.visual.VO.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class JwtFilter extends BasicHttpAuthenticationFilter {

    @Autowired
    private RedisUtil redisUtil;
    /**
     * RefreshToken过期时间
     */
    private long refreshTokenExpireTime;

    /**
     * 是否需要开启任何请求必须登录才可访问,开发时候可以为false,生产与测试必须为true.
     */
    private boolean mustLoginFlag;

    /**
     * 初始化
     *
     * @param refreshTokenExpireTime 刷新token时间
     * @param mustLoginFlag          是否需要登录
     */
    public JwtFilter(long refreshTokenExpireTime, boolean mustLoginFlag) {
        this.refreshTokenExpireTime = refreshTokenExpireTime;
        this.mustLoginFlag = mustLoginFlag;
    }

    /**
     * 是否允许访问
     * 这里我们详细说明下为什么最终返回的都是true，即允许访问
     * 例如我们提供一个地址 GET /article
     * 登入用户和游客看到的内容是不同的
     * 如果在这里返回了false，请求会被直接拦截，用户看不到任何东西
     * 所以我们在这里返回true，Controller中可以通过 subject.isAuthenticated() 来判断用户是否登入
     * 如果有些资源只有登入用户才能访问，我们只需要在方法上面加上 @RequiresAuthentication 注解即可
     * 但是这样做有一个缺点，就是不能够对GET,POST等请求进行分别过滤鉴权(因为我们重写了官方的方法)，但实际上对应用影响不大
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 查看当前Header中是否携带Authorization属性(Token)，有的话就进行登录认证授权
        if (this.isLoginAttempt(request, response)) {
            try {
                // 进行Shiro的登录UserRealm
                this.executeLogin(request, response);
            } catch (Exception e) {
                // 认证出现异常，传递错误信息msg
                String msg = e.getMessage();
                // 获取应用异常(该Cause是导致抛出此throwable(异常)的throwable(异常))
                Throwable throwable = e.getCause();
                if (throwable instanceof SignatureVerificationException) {
                    // 该异常为JWT的AccessToken认证失败(Token或者密钥不正确)
                    //msg = "Token或者密钥不正确(" + throwable.getMessage() + ")";
                    msg = "登录已过期";
                } else if (throwable instanceof TokenExpiredException) {
                    // 该异常为JWT的AccessToken已过期，判断RefreshToken未过期就进行AccessToken刷新
                    if (this.refreshToken(request, response)) {
                        return true;
                    } else {
                        // msg = "Token已过期(" + throwable.getMessage() + ")";
                        msg = "登录已过期";
                    }
                } else {
                    // 应用异常不为空
                    if (throwable != null) {
                        // 获取应用异常msg
                        msg = throwable.getMessage();
                    }
                }
                /*
                  错误两种处理方式
                  1. 将非法请求转发到/401的Controller处理，抛出自定义无权访问异常被全局捕捉再返回Response信息
                  2. 无需转发，直接返回Response信息
                  一般使用第二种(更方便)
                 */
                // 直接返回Response信息
                this.response401(request, response, msg);
                return false;
            }
        } else {
            // 没有携带Token
            HttpServletRequest httpRequest = WebUtils.toHttp(request);
            // 获取当前请求类型
            String httpMethod = httpRequest.getMethod();
            // 获取当前请求URI
            String requestURI = httpRequest.getRequestURI();
            log.info("当前请求[{}] Authorization属性(Token)为空 请求类型=[{}] IP=[{}]", requestURI, httpMethod,
                    IpUtil.getLocalIp(httpRequest));
            // mustLoginFlag = true 开启任何请求必须登录才可访问
            if (mustLoginFlag) {
                this.response401(request, response, "请先登录");
                return false;
            }
        }

        return true;
    }

    /**
     * 这里我们详细说明下为什么重写
     * 可以对比父类方法，只是将executeLogin方法调用去除了
     * 如果没有去除将会循环调用doGetAuthenticationInfo方法
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        this.sendChallenge(request, response);
        return false;
    }

    /**
     * 检测Header里面是否包含Authorization字段，有就进行Token登录认证授权
     *
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean isLoginAttempt(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        return token != null;
    }

    /**
     * 进行AccessToken登录认证授权
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        JwtToken token = new JwtToken(this.getAuthzHeader(request));

        // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获
        this.getSubject(request, response).login(token);

        // 如果没有抛出异常则代表登入成功，返回true
        return true;
    }

    /**
     * 获取请求渠道 1pc 2移动
     *
     * @param request
     * @return
     */
    private String getServiceType(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String serviceType = "1";
        if (!StringUtils.isEmpty(httpRequest.getHeader("serviceType"))) {
            serviceType = httpRequest.getHeader("serviceType");
        }
        return serviceType;
    }

    /**
     * 此处为AccessToken刷新，进行判断RefreshToken是否过期，未过期就返回新的AccessToken且继续正常访问
     * synchronized 并发请求的时候会出错，目前先添加synchronized处理--可以后期优化--https://www.sundayfine.com/jwt-refresh-token/解决方案
     */
    private synchronized boolean refreshToken(ServletRequest request, ServletResponse response) {
        // 拿到当前Header中Authorization的AccessToken(Shiro中getAuthzHeader方法已经实现)
        String token = this.getAuthzHeader(request);
        String serviceType = getServiceType(request);
        // 获取当前Token的帐号信息
        String uid = JwtUtil.getClaim(token, ShiroConstant.UID);
        String permission = JwtUtil.getClaim(token, ShiroConstant.PERM);

        String oldRedisToken;
        try {
            oldRedisToken = (String) redisUtil.get("token_blacklist:" + token);
        } catch (Exception e) {
            return false;
        }

        if (StringUtils.isNotEmpty(oldRedisToken)) {
            // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setHeader("Authorization", oldRedisToken);
            httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
            return true;
        }
        // 判断Redis中RefreshToken是否存在
        if (redisUtil.hasKey(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + uid + ":" + serviceType)) {
            // Redis中RefreshToken还存在，获取RefreshToken的时间戳
            String currentTimeMillisRedis = redisUtil.get(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + uid + ":" + serviceType).toString();
            // 获取当前AccessToken中的时间戳，与RefreshToken的时间戳对比，如果当前时间戳一致，进行AccessToken刷新
            if (JwtUtil.getClaim(token, ShiroConstant.CURRENT_TIME_MILLIS).equals(currentTimeMillisRedis)) {
                // 获取当前最新时间戳
                String currentTimeMillis = String.valueOf(System.currentTimeMillis());
                // 读取配置文件，获取refreshTokenExpireTime属性
                // 设置RefreshToken中的时间戳为当前最新时间戳，且刷新过期时间重新为30分钟过期(配置文件可配置refreshTokenExpireTime属性)
                redisUtil.set(ShiroConstant.PREFIX_SHIRO_REFRESH_TOKEN + uid + ":" + serviceType,
                        currentTimeMillis, refreshTokenExpireTime);
                // 刷新AccessToken，设置时间戳为当前最新时间戳
                String newToken = JwtUtil.sign(uid, permission, currentTimeMillis, serviceType);
                //并发请求下会造成token过期
                redisUtil.set("token_blacklist:" + token + ":" + serviceType, newToken, 50L);
                // 将新刷新的AccessToken再次进行Shiro的登录
                JwtToken jwtToken = new JwtToken(newToken);
                // 提交给UserRealm进行认证，如果错误他会抛出异常并被捕获，如果没有抛出异常则代表登入成功，返回true
                this.getSubject(request, response).login(jwtToken);
                // 最后将刷新的AccessToken存放在Response的Header中的Authorization字段返回
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.setHeader("Authorization", newToken);
                httpServletResponse.setHeader("Access-Control-Expose-Headers", "Authorization");
                return true;
            }
        }
        return false;
    }

    /**
     * 无需转发，直接返回Response信息
     */
    private void response401(ServletRequest req, ServletResponse resp, String msg) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        httpServletResponse.setStatus(HttpStatus.OK.value());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        try (PrintWriter out = httpServletResponse.getWriter()) {
            String data = JsonConvertUtil.objectToJson(new ResultVO<>(HttpStatus.UNAUTHORIZED.value(), "无权访问，" + msg, null));
            out.append(data);
        } catch (IOException e) {
            log.error("直接返回Response信息出现IOException异常:" + e.getMessage());
            // throw new CustomException("直接返回Response信息出现IOException异常:" + e.getMessage());
        }
    }

    /**
     * 对跨域提供支持
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}
