package freemarker.introspection;

import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;

class BooleanLiteralExpr extends BaseExpr implements LiteralExpr {
    BooleanLiteralExpr(Expression expr) {
        super(ExprType.BOOLEAN_LITERAL, expr);
    }

    public Object getValue() {
        return IntrospectionAccessor.getBooleanLiteralValue(expr);
    }
}
