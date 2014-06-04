package freemarker.introspection;

import freemarker.template.Template;

/**
 * This class provides the entry point to Freemarker template introspection. 
 * Given a template, it will provide a DOM-like object tree representation of 
 * the template that can be traversed either manually or using the visitor facility.  
 */
public class TemplateIntrospector {
    /** 
     * Returns the root node of the AST representation of the parsed template.
     *  
     * @param template Freemarker template
     * @return root element of parse tree
     */
    public static Element getRootNode(Template template) {
        return IntrospectionClassFactory.getIntrospectionElement(
                template.getRootTreeNode());
    }
}
