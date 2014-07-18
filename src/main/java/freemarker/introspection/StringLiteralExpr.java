package freemarker.introspection;

import org.apache.commons.lang3.reflect.FieldUtils;

import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;
import freemarker.core.TemplateElement;

class StringLiteralExpr extends BaseExpr implements StringExpr {
    private static final String DYN_FIELD = "dynamicValue";
    private static final String ALT_DYN_FIELD = "interpolatedOutput";

    StringLiteralExpr(Expression expr) {
        super(ExprType.STRING_LITERAL, expr);
    }

    public Object getValue() {
        return IntrospectionAccessor.getStringLiteralValue(expr);
    }

    public Element getDynamicValue() {
        Object dynObj = getExprProp(DYN_FIELD);
        dynObj = dynObj == null ? getExprProp(ALT_DYN_FIELD) : dynObj;
        if (dynObj != null) {
            return IntrospectionClassFactory.getIntrospectionElement(
                    (TemplateElement) dynObj);
        }
        return null;
    }

    private Object getExprProp(String prop) {
        try {
            return (Object) FieldUtils.readField(expr, prop, true);
        } catch (IllegalAccessException e) {
            return null;
        } catch (IllegalArgumentException iae) {
            return null;
        }
    }
}
