package seoul.bulletin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
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
