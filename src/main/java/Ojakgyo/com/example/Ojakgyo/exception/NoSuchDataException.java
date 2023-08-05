package Ojakgyo.com.example.Ojakgyo.exception;
import lombok.Getter;

@Getter

public class NoSuchDataException extends RuntimeException {
    private final ErrorCode errorCode;

    public NoSuchDataException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
