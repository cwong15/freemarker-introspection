package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import freemarker.core.TemplateElement;

public class BaseElement implements Element {
    private TemplateElement node;
    private ElementInfo info;

    public BaseElement(ElementInfo info, TemplateElement node) {
        this.info = info;
        this.node = node;
    }

    public List<Element> getChildren() {
        if (node.isLeaf()) {
            return Collections.emptyList();
        } else {
            int numChildren = node.getChildCount();
            List<Element> elements = new ArrayList<Element>(numChildren);
            for (int i = 0; i < numChildren; i++) {
                elements.add(IntrospectionClassFactory.getIntrospectionElement(
                        (TemplateElement) node.getChildAt(i)));
            }
            return elements;
        }
    }

    public ElementType getType() {
        return info.getType();
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
