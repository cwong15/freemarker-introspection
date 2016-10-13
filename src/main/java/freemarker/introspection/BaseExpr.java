package freemarker.introspection;

import java.util.List;

import freemarker.core.Expression;

class BaseExpr implements Expr {
    private ExprType type;
    private ParamRole role;
    Expression expr;

    // populated lazily as needed
    List<Expr> params = null;

    BaseExpr(ExprType type, ParamRole role, Expression expr) {
        this.type = type;
        this.role = role;
        this.expr = expr;
    }

    public ExprType getType() {
        return type;
    }

    public ParamRole getRole() {
        return role;
    }

    public List<Expr> getParams() {
        if (params == null) {
            params = IntrospectionClassFactory.getParams(expr);
        }
        return params;
    }

    public Expr paramByRole(ParamRole role) {
        for (Expr e : getParams()) {
            if (e.getRole() == role) {
                return e;
            }
        }
        return null;
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
