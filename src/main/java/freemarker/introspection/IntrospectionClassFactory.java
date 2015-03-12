package freemarker.introspection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import freemarker.core.ExprClassifier;
import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;
import freemarker.core.TemplateElement;
import freemarker.core.TemplateObject;

class IntrospectionClassFactory {
    public static Element getIntrospectionElement(TemplateElement node) {
        return new BaseElement(ElementClassifier.getType(node), node);
    }

    public static List<Expr> getParams(TemplateObject obj) {
        List<Object> paramObjs = IntrospectionAccessor.getParamValues(obj);
        if (paramObjs.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expr> params = new ArrayList<Expr>(paramObjs.size());
        for (Object paramObj : paramObjs) {
            if (paramObj instanceof Expression) {
                // wrap Expression objects as our public Expr
                Expression fmExpr = (Expression) paramObj;
                params.add(wrapExpression(fmExpr));
            } else if (paramObj != null) {
                appendObjectExprs(params, obj, paramObj);
            }
        }
        return params;
    }

    private static Expr wrapExpression(Expression fmExpr) {
        ExprType exprType = ExprClassifier.getType(fmExpr);
        Expr expr = null;
        switch (exprType) {
            case STRING_LITERAL:
                expr = new StringLiteralExpr(fmExpr);
                break;
            case NUMBER_LITERAL:
                expr = new NumberLiteralExpr(fmExpr);
                break;
            case BOOLEAN_LITERAL:
                expr = new BooleanLiteralExpr(fmExpr);
                break;
            default:
                expr = new BaseExpr(exprType, fmExpr);
        }
        return expr;
    }

    private static void appendObjectExprs(List<Expr> params, TemplateObject node,
            Object value) {
        if (value.getClass().isArray()) {
            // unpack an array into individual VALUE exprs
            int arrayLength = Array.getLength(value);
            for (int i = 0; i < arrayLength; i++) {
                params.add(new ObjectExpr(ExprType.VALUE, node, Array.get(value, i)));
            }
        } else if (value instanceof Map) {
            // unpack key/value arg map into a list, alternating key and value.
            @SuppressWarnings("unchecked")
            Map<String, Expression> m = (Map<String, Expression>) value;
            for (Map.Entry<String, Expression> entry : m.entrySet()) {
                params.add(new ObjectExpr(ExprType.VALUE, node, entry.getKey()));
                params.add(wrapExpression(entry.getValue()));
            }
        } else {
            params.add(new ObjectExpr(ExprType.VALUE, node, value));
        }
    }
}
