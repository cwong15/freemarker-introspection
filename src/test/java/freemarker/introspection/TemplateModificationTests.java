package freemarker.introspection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateModificationTests {
    private static String TEMPLATE_FILE = "templateWithVariables.ftl";

    private Configuration templateConfig;
    private Template template;
    private String templateText;

    @Before
    public void setup() throws IOException, URISyntaxException {
        templateText = FileUtils.readFileToString(new File(this.getClass()
                .getResource(TEMPLATE_FILE).toURI()));

        StringTemplateLoader tloader = new StringTemplateLoader();
        tloader.putTemplate(TEMPLATE_FILE, templateText);
        templateConfig = new Configuration();
        templateConfig.setTemplateLoader(tloader);
        template = templateConfig.getTemplate(TEMPLATE_FILE);
    }

    @Test
    public void testReplaceInclude() {
        // in this test, we replace the [#include] directive in the template with
        // some text. This is a test of element replacement.

        String REPLACEMENT_TEXT = "<p>No includes!</p>";

        List<Element> includes = ((NodeFinder) new NodeFinder(
                TemplateIntrospector.getRootNode(template)).seek())
                .getIncludeElements();
        assertEquals(1, includes.size());

        String newTemplateText = new TemplateEditor(templateText)
                .replace(includes.get(0), REPLACEMENT_TEXT)
                .apply()
                .getModifiedTemplate();

        assertTrue(newTemplateText.contains(REPLACEMENT_TEXT));
        assertEquals(templateText.indexOf("[#include"),
                newTemplateText.indexOf(REPLACEMENT_TEXT));
    }

    @Test
    public void testRenameVariables() {
        // in this test, we rename the two "thing" variables, "thing" and 
        // "this.thing", to "other" and "this.other" respectively.
        // This is a test of expression replacement.

        NodeFinder nf = new NodeFinder(TemplateIntrospector.getRootNode(template));
        Set<String> variableNames = nf.seek().getVariables();
        assertTrue(variableNames.contains("thing"));
        assertTrue(variableNames.contains("this.thing"));

        List<Expr> thingVariables = nf.getThingVariables();
        assertEquals(2, thingVariables.size());
        TemplateEditor editor = new TemplateEditor(templateText);
        for (Expr thingVar : thingVariables) {
            editor.replace(thingVar, thingVar.toString().replace("thing", "other"));
        }
        String newTemplateText = editor.apply().getModifiedTemplate();

        assertTrue(templateText.contains("${thing"));
        assertTrue(templateText.contains("${this.thing"));
        assertFalse(newTemplateText.contains("${thing"));
        assertFalse(newTemplateText.contains("${this.thing"));
        assertEquals(templateText.indexOf("${thing"),
                newTemplateText.indexOf("${other"));
    }

    private static class NodeFinder extends VariableFinder {
        private List<Expr> thingVariables;
        private List<Element> includeElements;

        NodeFinder(Element root) {
            super(root);
            thingVariables = new ArrayList<Expr>();
            includeElements = new ArrayList<Element>();
        }

        public List<Expr> getThingVariables() {
            return thingVariables;
        }

        public List<Element> getIncludeElements() {
            return includeElements;
        }

        public void visit(Element element) {
            if (element.getType() == ElementType.INCLUDE) {
                includeElements.add(element);
            }
            super.visit(element);
        }

        public void visit(Expr expr) {
            switch (expr.getType()) {
                case IDENTIFIER:
                case DOT:
                    if (expr.toString().endsWith("thing")) {
                        thingVariables.add(expr);
                    }
                    super.visit(expr);
                    break;
                default:
                    super.visit(expr);
            }
        }
    }
}