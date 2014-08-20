package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

import freemarker.core.TemplateElement;

/**
 * represents "UnifiedCall" FM element. Special behavior needed to build a param
 * list from call arg map. Not needed with packagelocal branch.  
 */
public class UnifiedCallElement extends BaseElement {

    public UnifiedCallElement(TemplateElement node) {
        super(ElementType.UNIFIED_CALL, node);
    }

    public List<Expr> getParams() {
        if (params == null) {
            buildParameterList();
        }
        return params;
    }

    private void buildParameterList() {
        params = IntrospectionClassFactory.getParams(node,
                Arrays.asList("nameExp", "namedArgs"), null);
    }
}
