package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freemarker.core.TemplateElement;

public class BaseElement implements Element {
    private TemplateElement node;
    private ElementType type;

    // populated lazily as needed
    private List<Element> children = null;
    private List<Expr> params = null;

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
            params = IntrospectionClassFactory.getParams(node, type.getParamProps());
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
}
