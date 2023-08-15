package Ojakgyo.com.example.Ojakgyo.exception;

public enum ErrorCode {

    DUPLICATED_EMAIL(400, "이미 존재하는 E-mail입니다."),

    FAIL_VERIFY(400, "인증 번호가 틀렸습니다."),
    NOT_VERIFIED_PHONE(400, "인증되지 않은 번호입니다."),
    NOT_SEND_MESSAGE(400, "인증 번호가 전송되지 않았습니다."),

    USER_NOT_EXIST(400, "해당 유저를 찾을 수 없습니다."),

    LOCKER_NOT_EXIST(400, "존재하지 않는 락커 ID 입니다.");

    private final int status;
    private final String message;

    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}