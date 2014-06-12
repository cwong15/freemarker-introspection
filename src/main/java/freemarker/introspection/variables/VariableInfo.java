package freemarker.introspection.variables;

import java.util.ArrayList;
import java.util.List;

import freemarker.introspection.Expr;

/**
 * Represents information about the variables found in the template
 */
public class VariableInfo {
    private String name;
    private VariableType type = VariableType.UNKNOWN;
    private List<Object> values = new ArrayList<Object>();
    private Expr variable;

    /** name of the variable */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** type of variable */
    public VariableType getType() {
        return type;
    }

    public void setType(VariableType type) {
        this.type = type;
    }

    /** default values associated with the variable */
    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    /** underlying variable node */
    public Expr getVariable() {
        return variable;
    }

    public void setVariable(Expr variable) {
        this.variable = variable;
    }

    public String toString() {
        return String.format("%s (%s)", name, type);
    }
}
