package model.exceptions;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3252172256517071140L;

    private Map<String, String> errors = new HashMap<>();

    public Map<String, String> getErrors() {
        return errors;
    }

    public ValidationException(String message) {
        super(message);
    }

    public void addError(String fieldName, String messageError) {
        errors.put(fieldName, messageError);
    }
}
