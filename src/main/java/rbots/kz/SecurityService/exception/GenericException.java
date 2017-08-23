package rbots.kz.SecurityService.exception;

import lombok.Getter;

public class GenericException extends Exception {

    @Getter
    protected int code;

    public GenericException(int code, String message) {
        super(message);
        this.code = code;
    }

    public GenericException(ErrorType errorType) {
        this(errorType.getCode(), errorType.getMessage());
    }

}
