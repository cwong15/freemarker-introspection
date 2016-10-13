package freemarker.introspection;

/**
 * Represents a Freemarker expression. A primitive or scalar (string, int etc)
 * will get wrapped into an Expr of type VALUE.  
 */
public interface Expr extends TemplateNode {
    /** The type of this expression */
    ExprType getType();

    /** The role of this expression */
    ParamRole getRole();

    /**
     * Visits this element. The visit() method of the visitor is called on this
     * expression.
     * 
     * @param visitor an implementation of an ExprVisitor
     */
    void accept(ExprVisitor visitor);
}
