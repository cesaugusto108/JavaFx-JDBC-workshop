package model.exceptions;

import java.io.Serial;
import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -3252172256517071140L;

    private final Map<String, String> ERRORS = new HashMap<>();

    public Map<String, String> getERRORS() {

        return ERRORS;
    }

    public ValidationException(String message) {

        super(message);
    }

    public void addError(String fieldName, String messageError) {

        ERRORS.put(fieldName, messageError);
    }
}
