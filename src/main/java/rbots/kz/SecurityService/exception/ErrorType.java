package rbots.kz.SecurityService.exception;

import lombok.Getter;
import lombok.ToString;

@ToString
public enum ErrorType {

    USER_ALREADY_EXIST(1001, "User already exist");

    @Getter
    private final int code;

    @Getter
    private final String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
