package com.banana.visual.handler;

import com.banana.visual.VO.ResultVO;
import com.banana.visual.enums.ResultEnum;
import com.banana.visual.exception.SelfDefineException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Slf4j
public class BaseControllerExceptionHandler {

    /**
     * 捕捉所有Shiro异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public ResultVO<String> handle401(ShiroException e) {
        log.error("无权访问======message:{}", e.getMessage());
        return new ResultVO(HttpStatus.UNAUTHORIZED.value(), "登录已过期", "");
    }

    /**
     * 单独捕捉Shiro(UnauthorizedException)异常
     * 该异常为访问有权限管控的请求而该用户没有所需权限所抛出的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseBody
    public ResultVO<String> handle401(UnauthorizedException e) {
        log.error("无权访问:当前Subject没有此请求所需权限======message:{}", e.getMessage());
        return new ResultVO(HttpStatus.UNAUTHORIZED.value(), "无权访问", "");
    }

    /**
     * 单独捕捉Shiro(UnauthenticatedException)异常
     * 该异常为以游客身份访问有权限管控的请求无法对匿名主体进行授权，而授权失败所抛出的异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    @ResponseBody
    public ResultVO<String> handle401(UnauthenticatedException e) {
        log.error("无权访问:当前Subject是匿名Subject，请先登录======message:{}", e.getMessage());
        return new ResultVO(HttpStatus.UNAUTHORIZED.value(), "无权访问", "");
    }

    /**
     * 捕捉校验异常(MethodArgumentNotValidException)
     *
     * @return
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResultVO validException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        String message = fieldErrors.get(0).getDefaultMessage();
        return new ResultVO(ResultEnum.ERROR_IN_PARAM.getCode(), message, "");
    }


    /**
     * 捕捉404异常
     *
     * @return
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public ResultVO<String> handle(NoHandlerFoundException e) {
        log.error("404异常======message:{}", e.getMessage());
        return new ResultVO(HttpStatus.NOT_FOUND.value(), e.getMessage(), "");
    }

    /**
     * 系统全局异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO<Exception> defaultErrorHandler(HttpServletRequest req, Exception e) {
        ResultVO<Exception> response;
        log.error("occur bie error-->>errorUrl:{}----->>>errorMessage:{}", req.getRequestURL(), e);
        //数据库异常
        if (e instanceof DataIntegrityViolationException) {
            response = new ResultVO<>(ResultEnum.SERVER_ERROR.getCode(), ResultEnum.SERVER_ERROR.getMessage(), null);
            return response;
        }
        String msg = "发生错误请联系管理员";
        if (StringUtils.isBlank(e.getMessage())) {
            response = new ResultVO<>(ResultEnum.SERVER_ERROR.getCode(), msg, null);
        } else {
            response = new ResultVO<>(ResultEnum.SERVER_ERROR.getCode(), e.getMessage(), null);
        }
        //运行时异常
        return response;
    }

    /**
     * 自定义异常
     *
     * @param req
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(SelfDefineException.class)
    public ResultVO handle(HttpServletRequest req, SelfDefineException e) {
        log.error("自定义异常======message:{}，errorUrl:{}", e.getMessage(), req.getRequestURL());
        return new ResultVO(e.getCode(), e.getMessage(), "");
    }

    /**
     * 参数校验异常
     *
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(ConstraintViolationException.class)
    public ResultVO constraintViolationExceptionHandler(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        return new ResultVO(ResultEnum.ERROR_IN_PARAM.getCode(),
                ResultEnum.ERROR_IN_PARAM.getMessage(), msgList);
    }
}
