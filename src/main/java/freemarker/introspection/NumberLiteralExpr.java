package freemarker.introspection;

import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;

class NumberLiteralExpr extends BaseExpr implements LiteralExpr {
    NumberLiteralExpr(Expression expr) {
        super(ExprType.NUMBER_LITERAL, expr);
    }

    public Object getValue() {
        return IntrospectionAccessor.getNumberLiteralValue(expr);
    }
}
