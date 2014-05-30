package freemarker.introspection;

import java.util.List;

public interface Element {
    ElementType getType();

    List<Element> getChildren();

    String getDescription();

    int getBeginColumn();

    int getEndColumn();

    int getBeginLine();

    int getEndLine();
}
