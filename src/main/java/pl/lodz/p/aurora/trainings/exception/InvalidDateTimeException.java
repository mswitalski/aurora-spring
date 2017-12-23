package pl.lodz.p.aurora.trainings.exception;

import java.util.HashSet;
import java.util.Set;

public class InvalidDateTimeException extends RuntimeException {

    private Set<ERROR> errors = new HashSet<>();

    public enum ERROR {
        START_BEFORE_NOW,
        END_BEFORE_NOW,
        END_BEFORE_EQUAL_START
    }

    public InvalidDateTimeException(String message, Set<ERROR> errors) {
        super(message);
        this.errors = errors;
    }

    public Set<ERROR> getErrors() {
        return errors;
    }
}
