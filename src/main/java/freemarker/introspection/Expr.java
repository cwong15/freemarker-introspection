package freemarker.introspection;

import java.util.List;

/**
 * Represents a Freemarker expression. A primitive or scalar (string, int etc)
 * will get wrapped into an Expr of type VALUE.  
 */
public interface Expr {
    /** The type of this expression */
    ExprType getType();

    /** 
     * Returns a list of this expression's parameters. The number and type of
     * parameters depends on the type of the expression.
     */
    List<Expr> getParams();

    /** The start column location of this expression in the template */
    int getBeginColumn();

    /** The start line location of this expression in the template */
    int getBeginLine();

    /** The end column location of this expression in the template */
    int getEndColumn();

    /** The end line location of this element in the template */
    int getEndLine();

    /**
     * Visits this element. The visit() method of the visitor is called on this
     * expression.
     * 
     * @param visitor an implementation of an ExprVisitor
     */
    void accept(ExprVisitor visitor);
}
