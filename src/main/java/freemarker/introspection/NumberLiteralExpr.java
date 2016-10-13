package freemarker.introspection;

import freemarker.core.IntrospectionAccessor;
import freemarker.core.Param;
import freemarker.core.ParamRoleConverter;

class NumberLiteralExpr extends BaseExpr implements LiteralExpr {
    NumberLiteralExpr(Param exprParam) {
        super(ExprType.NUMBER_LITERAL, ParamRoleConverter.toRole(exprParam.getRole()),
                exprParam.asExpression());
    }

    public Object getValue() {
        return IntrospectionAccessor.getNumberLiteralValue(expr);
    }
}
