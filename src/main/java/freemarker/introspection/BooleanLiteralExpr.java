package freemarker.introspection;

import freemarker.core.Param;
import freemarker.core.ParamRoleConverter;

class BooleanLiteralExpr extends BaseExpr implements LiteralExpr {
    BooleanLiteralExpr(Param exprParam) {
        super(ExprType.BOOLEAN_LITERAL, ParamRoleConverter.toRole(exprParam.getRole()),
                exprParam.asExpression());
    }

    public Object getValue() {
        String boolStr = expr.toString();
        return "true".equals(boolStr);
    }
}
