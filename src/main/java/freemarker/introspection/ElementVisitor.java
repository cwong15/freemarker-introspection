package freemarker.introspection;

/**
 * Visitor interface for traversing a Freemarker element tree. Each element and
 * its nested elements will be recursively visited.
 */
public interface ElementVisitor {
    /** method to be called on the element. */
    void visit(Element element);
}
