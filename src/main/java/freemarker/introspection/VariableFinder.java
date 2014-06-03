package freemarker.introspection;

import java.util.LinkedHashSet;
import java.util.Set;

public class VariableFinder implements ElementVisitor, ExprVisitor {
    private Element root;
    private Set<String> variables;

    public VariableFinder(Element root) {
        this.root = root;
        variables = new LinkedHashSet<String>();
    }

    public VariableFinder seek() {
        root.accept(this);
        return this;
    }

    public Set<String> getVariables() {
        return variables;
    }

    public void visit(Element element) {
        switch (element.getType()) {
            case ASSIGNMENT:
            case BLOCK_ASSIGNMENT:
                // TODO: allow local variable scraping. For now, ignore local vars.
                break;
            case INCLUDE:
                // TODO: optionally chase includes
                break;
            default:
                // scan all expr
                for (Expr e : element.getParams()) {
                    scanExpressions(e);
                }
                break;
        }
    }

    private void scanExpressions(Expr expr) {
        if (expr != null) {
            expr.accept(this);
        }
    }

    public void visit(Expr expr) {
        switch (expr.getType()) {
            case IDENTIFIER:
            case DOT:
                variables.add(expr.toString());
                break;
            default:
                for (Expr e : expr.getParams()) {
                    scanExpressions(e);
                }
        }
    }
}
