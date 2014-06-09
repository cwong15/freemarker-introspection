package freemarker.introspection;

/**
 * Represents a failure to access a specific field by name.
 */
class InaccessibleFieldException extends RuntimeException {
    private static final long serialVersionUID = -8543629406822958212L;

    InaccessibleFieldException(String field, Exception cause) {
        super("Could not access field " + field, cause);
    }
}
