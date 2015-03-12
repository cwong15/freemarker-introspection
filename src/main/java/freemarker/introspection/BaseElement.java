package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freemarker.core.TemplateElement;

public class BaseElement implements Element {
    TemplateElement node;
    private ElementType type;

    // populated lazily as needed
    private List<Element> children = null;
    List<Expr> params = null;

    public BaseElement(ElementType type, TemplateElement node) {
        this.type = type;
        this.node = node;
    }

    public List<Element> getChildren() {
        if (children != null) {
            return children;
        }

        if (node.isLeaf()) {
            children = Collections.emptyList();
        } else {
            int numChildren = node.getChildCount();
            List<Element> elements = new ArrayList<Element>(numChildren);
            for (int i = 0; i < numChildren; i++) {
                elements.add(IntrospectionClassFactory.getIntrospectionElement(
                        (TemplateElement) node.getChildAt(i)));
            }
            children = Collections.unmodifiableList(elements);
        }

        return children;
    }

    public List<Expr> getParams() {
        if (params == null) {
            params = IntrospectionClassFactory.getParams(node);
        }
        return params;
    }

    public ElementType getType() {
        return type;
    }

    public String getDescription() {
        return node.getDescription();
    }

    public int getBeginColumn() {
        return node.getBeginColumn();
    }

    public int getBeginLine() {
        return node.getBeginLine();
    }

    public int getEndColumn() {
        return node.getEndColumn();
    }

    public int getEndLine() {
        return node.getEndLine();
    }

    public void accept(ElementVisitor visitor) {
        boolean recurse = visitor.visit(this);
        if (recurse) {
            for (Element e : getChildren()) {
                e.accept(visitor);
            }
        }
    }
}
