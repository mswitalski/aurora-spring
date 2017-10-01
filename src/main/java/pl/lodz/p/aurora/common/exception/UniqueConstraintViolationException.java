package pl.lodz.p.aurora.common.exception;

import com.google.common.base.Joiner;

import java.util.Set;

/**
 * Unchecked exception thrown in situation when any of unique constraints on table was violated.
 */
public class UniqueConstraintViolationException extends RuntimeException {

    private final String entityName;
    private final Set<String> fieldsNames;

    public UniqueConstraintViolationException(Throwable cause, String entityName, Set<String> fieldsNames) {
        super(
                "Unique constraint was violated in entity " + entityName + " on fields: "
                        + Joiner.on(", ").skipNulls().join(fieldsNames),
                cause
        );
        this.entityName = entityName;
        this.fieldsNames = fieldsNames;
    }

    public UniqueConstraintViolationException(String entityName, Set<String> fieldsNames) {
        super(
                "Unique constraint was violated in entity " + entityName + " on fields: "
                        + Joiner.on(", ").skipNulls().join(fieldsNames)
        );
        this.entityName = entityName;
        this.fieldsNames = fieldsNames;
    }

    public String getEntityName() {
        return entityName;
    }

    public Set<String> getFieldsNames() {
        return fieldsNames;
    }
}
