package freemarker.introspection;

/**
 * Visitor interface for traversing a Freemarker element tree. Each element and
 * its nested elements will be recursively visited if desired.
 */
public interface ElementVisitor {
    /** 
     * method to be called on the element. Return true iff you want to 
     * recursive into the element's children.
     */
    boolean visit(Element element);
}
