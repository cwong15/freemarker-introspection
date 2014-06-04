package freemarker.introspection;

import java.util.List;

/**
 * Represents a Freemarker element
 */
public interface Element {
    /** The type of this element */
    ElementType getType();

    /** Returns a list of this element's immediate nested elements */
    List<Element> getChildren();

    /** 
     * Returns a list of this element's parameters. The number and type of
     * parameters depends on the type of the element.
     */
    List<Expr> getParams();

    /** Returns a human-readable description of this element */
    String getDescription();

    /** The start column location of this element in the template */
    int getBeginColumn();

    /** The end column location of this element in the template */
    int getEndColumn();

    /** The start line location of this element in the template */
    int getBeginLine();

    /** The end line location of this element in the template */
    int getEndLine();

    /**
     * Visits this element. The visit() method of the visitor is called on this
     * element and its children. 
     * 
     * @param visitor an implementation of an ElementVisitor
     */
    void accept(ElementVisitor visitor);
}
