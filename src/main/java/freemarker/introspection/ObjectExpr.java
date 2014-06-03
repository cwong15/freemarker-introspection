package freemarker.introspection;

import java.util.Collections;
import java.util.List;

import freemarker.core.TemplateObject;

class ObjectExpr implements Expr {
    private ExprType type;
    private TemplateObject parentNode;
    private Object value;

    ObjectExpr(ExprType type, TemplateObject parentNode, Object value) {
        this.type = type;
        this.parentNode = parentNode;
        this.value = value;
    }

    public ExprType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public List<Expr> getParams() {
        return Collections.emptyList();
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
}
