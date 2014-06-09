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

    public static List<Expr> getParams(TemplateObject obj, List<String> fields,
            List<String> altFields) {
        if (fields.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expr> params = null;
        try {
            params = tryProps(obj, fields);
        } catch (InaccessibleFieldException e) {
            // error accessing a field. This can be due to the fact that FM 
            // internal field names can change across versions. Try the 
            // alternate set of field names, if available.
            if (altFields != null) {
                params = tryProps(obj, altFields);
            } else {
                throw e;
            }
        }

        return Collections.unmodifiableList(params);
    }

    private static List<Expr> tryProps(TemplateObject obj, List<String> fields) {
        List<Expr> params = new ArrayList<Expr>();
        for (String field : fields) {
            Object p;
            try {
                p = (Object) FieldUtils.readField(obj, field, true);
            } catch (IllegalAccessException iae) {
                throw new InaccessibleFieldException(field, iae);
            } catch (IllegalArgumentException iae) {
                throw new InaccessibleFieldException(field, iae);
            }
            if (p instanceof Expression) {
                // wrap Expression objects as our public Expr
                Expression fmExpr = (Expression) p;
                params.add(new BaseExpr(ExprClassifier.getType(fmExpr), fmExpr));
            } else {
                params.add(new ObjectExpr(ExprType.VALUE, obj, p));
            }
        }
        return params;
    }
}
