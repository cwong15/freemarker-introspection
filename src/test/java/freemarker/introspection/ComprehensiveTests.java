package freemarker.introspection;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ComprehensiveTests {
    /**
     * Tests that we can wrap all supported element and expression types. 
     * We check that each node can load its children and/or params without
     * throwing an expression. We check that the test template results in all
     * expected types being recognized and loaded into our tree.  
     */
    @Test
    public void testLoadEverything() {
        ComprehensiveVisitor visitor = visitTemplate("everything.ftl");

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

    /**
     * Separate set of tests for autoescape directive. This cannot coexist with the 
     * legacy escape directive in everything.ftl. 
     */
    @Test
    public void testLoadAutoescape() throws Exception {
        ComprehensiveVisitor visitor = visitTemplate("autoescaping.ftl");
        List<ElementType> aaTypes = Arrays.asList(ElementType.AUTO_ESC_BLOCK,
                ElementType.NO_AUTO_ESC_BLOCK);
        for (ElementType etype : aaTypes) {
            Element element = visitor.elements.get(etype);
            assertNotNull("Missing element of type " + etype, element);
            element.getParams();
            element.getChildren();
        }
    }

    private ComprehensiveVisitor visitTemplate(String path) {
        Configuration config = TemplateTestUtils.templateConfig();
        config.setClassForTemplateLoading(this.getClass(), "");
        Template template = null;
        try {
            template = config.getTemplate(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Freemarker template", e);
        }
        Element root = TemplateIntrospector.getRootNode(template);
        ComprehensiveVisitor visitor = new ComprehensiveVisitor();
        root.accept(visitor);
        return visitor;
    }

    private Set<ElementType> testedElementTypes() {
        Set<ElementType> all = new HashSet<ElementType>(EnumSet.allOf(ElementType.class));
        all.removeAll(EnumSet.of(ElementType.AUTO_ESC_BLOCK, ElementType.DEBUG_BREAK,
                ElementType.GENERIC, ElementType.NO_AUTO_ESC_BLOCK,
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
