package freemarker.introspection;

import java.util.List;

import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;
import freemarker.core.TemplateElement;

class StringLiteralExpr extends BaseExpr implements StringExpr {
    StringLiteralExpr(Expression expr) {
        super(ExprType.STRING_LITERAL, expr);
    }

    public Object getValue() {
        return IntrospectionAccessor.getStringLiteralValue(expr);
    }

    public Element getDynamicValue() {
        List<Object> params = IntrospectionAccessor.getParamValues(expr);
        if (params.get(0) == null) {
            return null;
        }
        return IntrospectionClassFactory.getIntrospectionElement(
                (TemplateElement) params.get(0));
    }
}
