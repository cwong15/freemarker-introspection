package freemarker.introspection;

import java.util.List;

public interface Element {
    ElementType getType();

    List<Element> getChildren();

    List<Expr> getParams();

    String getDescription();

    int getBeginColumn();

    int getEndColumn();

    int getBeginLine();

    int getEndLine();

    void accept(ElementVisitor visitor);
}
