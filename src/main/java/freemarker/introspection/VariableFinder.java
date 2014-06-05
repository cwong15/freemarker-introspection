package freemarker.introspection;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class provides the ability to scan a Freemarker template for variables
 * used. This helps you to figure out what variables it will request from the
 * model during processing.  
 * 
 * <p>Sample usage:
 * <pre>
 * Element root = TemplateIntrospector.getRootNode(template);
 * Set&lt;String&gt; vars = new VariableFinder(root).seek().getVariables();
 * </pre>
 * 
 * <p>This class also serves as an example of how to traverse the template tree
 * using the visitor pattern.
 */
public class VariableFinder implements ElementVisitor, ExprVisitor {
    private Element root;
    private Set<String> variables;

    public VariableFinder(Element root) {
        this.root = root;
        variables = new LinkedHashSet<String>();
    }

    /**
     * Scan the tree for variables. This should only be called once. 
     */
    public VariableFinder seek() {
        root.accept(this);
        return this;
    }

    /**
     * Returns the variables found with seek(). You must seek() first before
     * getting meaningful results from this method. 
     *  
     * @return set of variable names. 
     */
    public Set<String> getVariables() {
        return variables;
    }

    public void visit(Element element) {
        switch (element.getType()) {
            case ASSIGNMENT:
            case BLOCK_ASSIGNMENT:
                // TODO: handle local variables properly. For now, ignore them.
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
