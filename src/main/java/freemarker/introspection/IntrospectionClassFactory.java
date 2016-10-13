package freemarker.introspection;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import freemarker.core.ExprClassifier;
import freemarker.core.Expression;
import freemarker.core.IntrospectionAccessor;
import freemarker.core.Param;
import freemarker.core.ParamRoleConverter;
import freemarker.core.TemplateElement;
import freemarker.core.TemplateObject;

class IntrospectionClassFactory {
    public static Element getIntrospectionElement(TemplateElement node) {
        return new BaseElement(ElementClassifier.getType(node), node);
    }

    public static List<Expr> getParams(TemplateObject obj) {
        List<Param> paramObjs = IntrospectionAccessor.getParams(obj);
        if (paramObjs.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expr> params = new ArrayList<Expr>(paramObjs.size());
        for (Param param : paramObjs) {
            if (param != null && param.getValue() instanceof Expression) {
                // wrap Expression objects as our public Expr
                params.add(wrapExpression(param));
            } else if (param != null && param.getValue() != null) {
                appendObjectExprs(params, obj, param);
            }
        }
        return params;
    }

    private static Expr wrapExpression(Param param) {
        ExprType exprType = ExprClassifier.getType(param.asExpression());
        Expr expr = null;
        switch (exprType) {
            case STRING_LITERAL:
                expr = new StringLiteralExpr(param);
                break;
            case NUMBER_LITERAL:
                expr = new NumberLiteralExpr(param);
                break;
            case BOOLEAN_LITERAL:
                expr = new BooleanLiteralExpr(param);
                break;
            default:
                expr = new BaseExpr(exprType, ParamRoleConverter.toRole(param.getRole()),
                        param.asExpression());
        }
        return expr;
    }

    private static void appendObjectExprs(List<Expr> params, TemplateObject node,
            Param param) {
        if (param.getValue().getClass().isArray()) {
            // unpack an array into individual VALUE exprs
            int arrayLength = Array.getLength(param.getValue());
            for (int i = 0; i < arrayLength; i++) {
                params.add(new ObjectExpr(ExprType.VALUE, ParamRoleConverter.toRole(param.getRole()),
                        node, Array.get(param.getValue(), i)));
            }
        } else if (param.getValue() instanceof Map) {
            // unpack key/value arg map into a list, alternating key and value.
            @SuppressWarnings("unchecked")
            Map<String, Expression> m = (Map<String, Expression>) param.getValue();
            for (Map.Entry<String, Expression> entry : m.entrySet()) {
                params.add(new ObjectExpr(ExprType.VALUE, ParamRoleConverter.toRole(param.getRole()),
                        node, entry.getKey()));
                params.add(wrapExpression(new Param(param, entry.getValue())));
            }
        } else {
            params.add(new ObjectExpr(ExprType.VALUE, ParamRoleConverter.toRole(param.getRole()),
                    node, param.getValue()));
        }
    }
}
