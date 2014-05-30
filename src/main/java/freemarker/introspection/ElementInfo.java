package freemarker.introspection;

import java.util.Collections;
import java.util.List;

class ElementInfo {
    private ElementType type;
    private String nameProp;
    private List<String> exprProps = Collections.emptyList();

    public ElementType getType() {
        return type;
    }

    public void setType(ElementType type) {
        this.type = type;
    }

    public String getNameProp() {
        return nameProp;
    }

    public void setNameProp(String nameProp) {
        this.nameProp = nameProp;
    }

    public List<String> getExprProps() {
        return exprProps;
    }

    public void setExprProps(List<String> exprProps) {
        this.exprProps = exprProps;
    }

    public String toString() {
        return type.toString();
    }
}
