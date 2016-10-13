package freemarker.introspection;

import java.util.Collections;
import java.util.List;

import freemarker.core.TemplateObject;

class ObjectExpr implements ValueExpr {
    private ExprType type;
    private ParamRole role;
    private TemplateObject parentNode;
    private Object value;

    ObjectExpr(ExprType type, ParamRole role, TemplateObject parentNode, Object value) {
        this.type = type;
        this.role = role;
        this.parentNode = parentNode;
        this.value = value;
    }

    public ExprType getType() {
        return type;
    }

    public ParamRole getRole() {
        return role;
    }

    public Object getValue() {
        return value;
    }

    public List<Expr> getParams() {
        return Collections.emptyList();
    }

    public Expr paramByRole(ParamRole role) {
        return null;
    }

    public int getBeginColumn() {
        return parentNode.getBeginColumn();
    }

    public int getBeginLine() {
        return parentNode.getBeginLine();
    }

    public int getEndColumn() {
        return parentNode.getEndColumn();
    }

    public int getEndLine() {
        return parentNode.getEndLine();
    }

    public void accept(ExprVisitor visitor) {
        visitor.visit(this);
    }

    public String toString() {
        return value.toString();
    }
}
