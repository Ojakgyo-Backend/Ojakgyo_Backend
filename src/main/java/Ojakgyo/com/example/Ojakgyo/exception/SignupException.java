package Ojakgyo.com.example.Ojakgyo.exception;

import lombok.Getter;

@Getter
public class SignupException extends RuntimeException{
    private final ErrorCode errorCode;

    public SignupException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}