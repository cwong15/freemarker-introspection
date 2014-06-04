package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import freemarker.core.ExprClassifier;
import freemarker.core.Expression;
import freemarker.core.TemplateElement;
import freemarker.core.TemplateObject;

class IntrospectionClassFactory {
    public static Element getIntrospectionElement(TemplateElement node) {
        return new BaseElement(ElementClassifier.getType(node), node);
    }

    public static List<Expr> getParams(TemplateObject obj, List<String> props) {
        if (props.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expr> params = new ArrayList<Expr>();
        for (String prop : props) {
            Object p;
            try {
                p = (Object) FieldUtils.readField(obj, prop, true);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException("Could not access field " + prop, iae);
            }
            if (p instanceof Expression) {
                // wrap Expression objects as our public Expr
                Expression fmExpr = (Expression) p;
                params.add(new BaseExpr(ExprClassifier.getType(fmExpr), fmExpr));
            } else {
                params.add(new ObjectExpr(ExprType.VALUE, obj, p));
            }
        }

        return Collections.unmodifiableList(params);
    }
}
