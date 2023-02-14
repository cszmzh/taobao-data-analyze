package com.banana.visual.exception;

import com.banana.visual.enums.ResultEnum;
import lombok.Getter;

@Getter
public class SelfDefineException extends RuntimeException {

    private Integer code;

    private String message;

    public SelfDefineException(ResultEnum resultEnum) {
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
        this.message = resultEnum.getMessage();
    }

    public SelfDefineException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SelfDefineException(String message) {
        this.message = message;
    }

}
