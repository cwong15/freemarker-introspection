package freemarker.introspection;

import freemarker.core.Expression;
import freemarker.core.TemplateElement;

class IntrospectionClassFactory {
    public static Element getIntrospectionElement(TemplateElement node) {
        return new BaseElement(ElementClassifier.getInfo(node), node);
    }

    public static Expr getIntrospectionExpr(Expression exprObj) {
        return new BaseExpr(ExprClassifier.getType(exprObj), exprObj);
    }
}
