package nordgym.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "This user doesn't exists")
public class UserNotFoundException extends NullPointerException {
    private int statusCode;

    public UserNotFoundException() {
        this.statusCode = 404;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
