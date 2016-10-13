package freemarker.core;

/**
 * Represents a parameter from a template object
 */
public class Param {
    private ParameterRole role;
    private Object value;

    Param(ParameterRole role, Object value) {
        this.role = role;
        this.value = value;
    }

    public Param(Param templateParam, Object value) {
        this.role = templateParam.getParameterRole();
        this.value = value;
    }

    public ParameterRole getRole() {
        return role;
    }

    public Object getValue() {
        return value;
    }

    public Expression asExpression() {
        return (Expression) value;
    }

    private ParameterRole getParameterRole() {
        return role;
    }
}
