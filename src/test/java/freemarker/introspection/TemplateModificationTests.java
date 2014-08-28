package freemarker.introspection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import freemarker.cache.StringTemplateLoader;
import freemarker.introspection.variables.VariableFinder;
import freemarker.introspection.variables.VariableInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateModificationTests {
    private static String TEMPLATE_FILE = "templateWithVariables.ftl";
    private static String TEMPLATE_FILE2 = "varRenameWithTabs.ftl";

    private Configuration templateConfig;
    private Template template;
    private String templateText;

    @Before
    public void setup() throws IOException, URISyntaxException {
        loadTemplate(TEMPLATE_FILE);
    }

    private void loadTemplate(String templateFile) throws IOException, URISyntaxException {
        templateText = FileUtils.readFileToString(new File(this.getClass()
                .getResource(templateFile).toURI()));

        StringTemplateLoader tloader = new StringTemplateLoader();
        tloader.putTemplate(templateFile, templateText);
        templateConfig = new Configuration();
        templateConfig.setTemplateLoader(tloader);
        template = templateConfig.getTemplate(templateFile);
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

    @Test
    public void testFindAndRenameMultipleVariables() {
        // use the VariableFinder class to find a variable called "somevar" to 
        // rename. This tests renaming multiple occurrences of the same variable

        List<VariableInfo> results = new VariableFinder(
                TemplateIntrospector.getRootNode(template))
                .seek()
                .getVariableInfo();

        TemplateEditor editor = new TemplateEditor(templateText);
        for (VariableInfo vi : results) {
            if (vi.getName().equals("somevar")) {
                for (Expr var : vi.getVariables()) {
                    editor.replace(var, "othervar");
                }
            }
        }

        String origText = "[#if 0 < somevar && somevar < 10]";
        String updatedText = "[#if 0 < othervar && othervar < 10]";
        String newTemplate = editor.apply().getModifiedTemplate();
        assertTrue(templateText.contains(origText));
        assertFalse(newTemplate.contains(origText));
        assertFalse(templateText.contains(updatedText));
        assertTrue(newTemplate.contains(updatedText));
    }

    @Test
    public void testRenameWithTabs() throws IOException, URISyntaxException {
        loadTemplate(TEMPLATE_FILE2);

        List<VariableInfo> results = new VariableFinder(
                TemplateIntrospector.getRootNode(template))
                .seek()
                .getVariableInfo();

        TemplateEditor editor = new TemplateEditor(templateText);
        for (VariableInfo vi : results) {
            if (vi.getName().equals("somevariable")) {
                for (Expr var : vi.getVariables()) {
                    editor.replace(var, "othervariable");
                }
            }
        }

        String modifiedTemplate = editor.apply().getModifiedTemplate();
        StringTemplateLoader tloader = new StringTemplateLoader();
        tloader.putTemplate("t", modifiedTemplate);
        templateConfig = new Configuration();
        templateConfig.setTemplateLoader(tloader);
        template = templateConfig.getTemplate("t");
    }

    /**
     * Looks for variables that end with "thing" and include elements.
     */
    private static class NodeFinder implements ElementVisitor, ExprVisitor {
        private Element root;
        private List<Expr> thingVariables;
        private List<Element> includeElements;
        private Set<String> variables;

        NodeFinder(Element root) {
            this.root = root;
            thingVariables = new ArrayList<Expr>();
            includeElements = new ArrayList<Element>();
            variables = new HashSet<String>();
        }

        public NodeFinder seek() {
            root.accept(this);
            return this;
        }

        public List<Expr> getThingVariables() {
            return thingVariables;
        }

        public List<Element> getIncludeElements() {
            return includeElements;
        }

        public boolean visit(Element element) {
            if (element.getType() == ElementType.INCLUDE) {
                includeElements.add(element);
            } else {
                scanParams(element);
            }
            return true;
        }

        public void visit(Expr expr) {
            switch (expr.getType()) {
                case IDENTIFIER:
                case DOT:
                    String name = expr.toString();
                    variables.add(name);
                    if (name.endsWith("thing")) {
                        thingVariables.add(expr);
                    }
                    break;
                default:
                    scanParams(expr);
            }
        }

        private void scanParams(TemplateNode node) {
            for (Expr e : node.getParams()) {
                if (e != null) {
                    e.accept(this);
                }
            }
        }

        public Set<String> getVariables() {
            return variables;
        }
    }
}
