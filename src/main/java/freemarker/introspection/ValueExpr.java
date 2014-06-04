package freemarker.introspection;

/** 
 * Represents a "simple" Freemarker expression. A primitive or scalar (String,
 * int etc) is wrapped in this interface.
 */
public interface ValueExpr extends Expr {
    /** Returns the underlying value */
    Object getValue();
}
