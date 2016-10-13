package freemarker.introspection;

import java.util.List;

import freemarker.core.IntrospectionAccessor;
import freemarker.core.Param;
import freemarker.core.ParamRoleConverter;
import freemarker.core.TemplateElement;

class StringLiteralExpr extends BaseExpr implements StringExpr {
    StringLiteralExpr(Param exprParam) {
        super(ExprType.STRING_LITERAL, ParamRoleConverter.toRole(exprParam.getRole()),
                exprParam.asExpression());
    }

    public Object getValue() {
        return IntrospectionAccessor.getStringLiteralValue(expr);
    }

    public Element getDynamicValue() {
        List<Param> params = IntrospectionAccessor.getParams(expr);
        if (params.isEmpty() || params.get(0).getValue() == null) {
            return null;
        }
        return IntrospectionClassFactory.getIntrospectionElement(
                (TemplateElement) params.get(0).getValue());
    }
}
