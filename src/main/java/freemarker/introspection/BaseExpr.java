package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import freemarker.core.Expression;

class BaseExpr implements Expr {
    private ExprType type;
    private Expression expr;

    BaseExpr(ExprType type, Expression expr) {
        this.type = type;
        this.expr = expr;
    }

    public ExprType getType() {
        return type;
    }

    public List<Expr> getParams() {
        List<String> props = type.getSubExprProps();
        if (props.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expr> params = new ArrayList<Expr>();
        for (String prop : props) {
            Expression e;
            try {
                e = (Expression) FieldUtils.readDeclaredField(expr, prop, true);
            } catch (IllegalAccessException iae) {
                throw new RuntimeException("Could not access field " + prop, iae);
            }
            params.add(IntrospectionClassFactory.getIntrospectionExpr(e));
        }
        return params;
    }

    public int getBeginColumn() {
        return expr.getBeginColumn();
    }

    public int getBeginLine() {
        return expr.getBeginLine();
    }

    public int getEndColumn() {
        return expr.getEndColumn();
    }

    public int getEndLine() {
        return expr.getEndLine();
    }
}
