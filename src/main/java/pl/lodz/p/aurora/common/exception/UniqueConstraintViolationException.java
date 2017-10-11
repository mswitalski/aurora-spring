package pl.lodz.p.aurora.common.exception;

/**
 * Unchecked exception thrown in situation when any of unique constraints on table was violated.
 */
public class UniqueConstraintViolationException extends RuntimeException {

    private final String entityName;
    private final String fieldName;

    public UniqueConstraintViolationException(Throwable cause, String entityName, String fieldName) {
        super(
                "Unique constraint was violated in entity " + entityName + " on field: " + fieldName,
                cause
        );
        this.entityName = entityName;
        this.fieldName = fieldName;
    }

    public UniqueConstraintViolationException(Throwable cause, String entityName) {
        super(
                "Unique constraint was violated in entity " + entityName + " on unknown field",
                cause
        );
        this.entityName = entityName;
        this.fieldName = null;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
