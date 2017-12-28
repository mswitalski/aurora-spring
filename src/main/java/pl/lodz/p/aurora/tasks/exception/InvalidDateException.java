package pl.lodz.p.aurora.tasks.exception;

import java.util.HashSet;
import java.util.Set;

public class InvalidDateException extends RuntimeException {

    private Set<ERROR> errors = new HashSet<>();

    public enum ERROR {
        DATE_BEFORE_TODAY
    }

    public InvalidDateException(String message, Set<ERROR> errors) {
        super(message);
        this.errors = errors;
    }

    public Set<ERROR> getErrors() {
        return errors;
    }
}
