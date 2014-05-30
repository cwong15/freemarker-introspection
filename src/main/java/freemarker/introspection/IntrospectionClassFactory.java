package freemarker.introspection;

import freemarker.core.TemplateElement;

class IntrospectionClassFactory {
    public static Element getIntrospectionElement(TemplateElement node) {
        return new BaseElement(ElementClassifier.getInfo(node), node);
    }
}
