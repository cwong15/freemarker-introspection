package freemarker.introspection;

/**
 * Visitor interface for traversing expressions. Visitor implementations are
 * responsible for traversing subexpressions.
 */
public interface ExprVisitor {
    /** method to be called on the expression. */
    void visit(Expr expr);
}
