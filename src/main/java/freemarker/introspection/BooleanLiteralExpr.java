package freemarker.introspection;

import freemarker.core.Expression;

class BooleanLiteralExpr extends BaseExpr implements LiteralExpr {
    BooleanLiteralExpr(Expression expr) {
        super(ExprType.BOOLEAN_LITERAL, expr);
    }

    public Object getValue() {
        String boolStr = expr.toString();
        return "true".equals(boolStr);
    }
}
