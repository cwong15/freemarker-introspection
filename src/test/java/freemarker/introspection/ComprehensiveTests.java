package freemarker.introspection;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ComprehensiveTests {
    private Template template;

    @Before
    public void setup() throws IOException {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(this.getClass(), "");
        template = config.getTemplate("everything.ftl");
    }

    /**
     * Tests that we can wrap all supported element and expression types. 
     * We check that each node can load its children and/or params without
     * throwing an expression. We check that the test template results in all
     * expected types being recognized and loaded into our tree.  
     */
    @Test
    public void testLoadEverything() {
        Element root = TemplateIntrospector.getRootNode(template);
        ComprehensiveVisitor visitor = new ComprehensiveVisitor();
        root.accept(visitor);

        for (ElementType etype : testedElementTypes()) {
            Element element = visitor.elements.get(etype);
            assertNotNull("Missing element of type " + etype, element);
            element.getParams();
            element.getChildren();
        }

        for (ExprType etype : testedExprTypes()) {
            Expr expr = visitor.expressions.get(etype);
            assertNotNull("Missing expr of type " + etype, expr);
            expr.getParams();
        }
    }

    private Set<ElementType> testedElementTypes() {
        Set<ElementType> all = new HashSet<ElementType>(EnumSet.allOf(ElementType.class));
        all.removeAll(EnumSet.of(ElementType.DEBUG_BREAK, ElementType.GENERIC,
                ElementType.TRIM_INSTRUCTION));
        return all;
    }

    private Set<ExprType> testedExprTypes() {
        Set<ExprType> all = new HashSet<ExprType>(EnumSet.allOf(ExprType.class));
        all.removeAll(EnumSet.of(ExprType.GENERIC));
        return all;
    }

    private class ComprehensiveVisitor implements ElementVisitor, ExprVisitor {
        Map<ElementType, Element> elements = new HashMap<ElementType, Element>();
        Map<ExprType, Expr> expressions = new HashMap<ExprType, Expr>();

        public void visit(Expr expr) {
            expressions.put(expr.getType(), expr);
            for (Expr e : expr.getParams()) {
                e.accept(this);
            }
        }

        public boolean visit(Element element) {
            elements.put(element.getType(), element);
            for (Expr e : element.getParams()) {
                e.accept(this);
            }
            return true;
        }
    }
}
