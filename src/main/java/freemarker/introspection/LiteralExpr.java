package freemarker.introspection;

/**
 * Represents a special case of an Expr: a literal. Provides a way to get the 
 * literal value of the expression.
 */
public interface LiteralExpr {
    /**
     * Returns the raw value of the expression
     */
    Object getValue();
}
