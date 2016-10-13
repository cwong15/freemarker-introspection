package freemarker.introspection;

import java.util.List;

/**
 * Represents a Freemarker node in a template 
 */
public interface TemplateNode {
    /** The start column location of this node in the template */
    int getBeginColumn();

    /** The end column location of this node in the template */
    int getEndColumn();

    /** The start line location of this node in the template */
    int getBeginLine();

    /** The end line location of this node in the template */
    int getEndLine();

    /** 
     * Returns a list of this node's parameters. The number and type of
     * parameters depends on the type of the node.
     */
    List<Expr> getParams();

    /**
     * Returns the first parameter expression with the given role. Returns
     * null if no matching parameter is found.
     * 
     * @param role role of parameter to match on
     */
    Expr paramByRole(ParamRole role);
}
