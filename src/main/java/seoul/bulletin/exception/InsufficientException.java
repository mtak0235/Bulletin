package seoul.bulletin.exception;

import lombok.Getter;

@Getter
public class InsufficientException extends RuntimeException{

    private int errorCode;
    private String errorMsg;

    public InsufficientException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InsufficientException(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public InsufficientException(int errorCode) {
        this.errorCode = errorCode;
    }

}
