package freemarker.introspection;

import java.util.List;

import freemarker.core.Expression;

class BaseExpr implements Expr {
    private ExprType type;
    Expression expr;

    // populated lazily as needed
    List<Expr> params = null;

    BaseExpr(ExprType type, Expression expr) {
        this.type = type;
        this.expr = expr;
    }

    public ExprType getType() {
        return type;
    }

    public List<Expr> getParams() {
        if (params == null) {
            params = IntrospectionClassFactory.getParams(expr);
        }
        return params;
    }

    public int getBeginColumn() {
        return expr.getBeginColumn();
    }

    public int getBeginLine() {
        return expr.getBeginLine();
    }

    public int getEndColumn() {
        return expr.getEndColumn();
    }

    public int getEndLine() {
        return expr.getEndLine();
    }

    public void accept(ExprVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return expr.toString();
    }
}
