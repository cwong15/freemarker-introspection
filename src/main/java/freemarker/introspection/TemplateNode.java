package freemarker.introspection;

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
}
