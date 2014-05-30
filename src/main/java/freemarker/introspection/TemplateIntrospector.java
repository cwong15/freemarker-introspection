package freemarker.introspection;

import freemarker.template.Template;

public class TemplateIntrospector {
    public static Element getRootNode(Template template) {
        return IntrospectionClassFactory.getIntrospectionElement(
                template.getRootTreeNode());
    }
}
