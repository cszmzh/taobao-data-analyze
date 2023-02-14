package com.banana.visual.handler;

import com.banana.visual.VO.ResultVO;
import com.banana.visual.exception.SelfDefineException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一异常处理
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理 SelfDefineException 异常
     */
    @ResponseBody
    @ExceptionHandler(SelfDefineException.class)
    public Object selfDefineExceptionHandler(HttpServletRequest request, SelfDefineException e) {
        log.error("【业务异常】code:" + e.getCode() + "，msg:" + e.getMessage());
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(e.getCode());
        resultVO.setMsg(e.getMessage());
        resultVO.setData(request.getRequestURL().toString());
        return resultVO;
    }

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultVO errorHandler(Exception ex) {
        ResultVO resultVO = new ResultVO();
        resultVO.setCode(500);
        resultVO.setMsg(ex.getMessage());
        return resultVO;
    }
}
