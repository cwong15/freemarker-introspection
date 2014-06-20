package freemarker.introspection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import freemarker.introspection.variables.VariableFinder;
import freemarker.introspection.variables.VariableInfo;
import freemarker.introspection.variables.VariableType;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class VariableDiscoveryTests {
    private Configuration config;

    @Before
    public void setup() {
        config = new Configuration();
        config.setClassForTemplateLoading(this.getClass(), "");
    }

    @Test
    public void testFindVariables() throws IOException {
        Template template = config.getTemplate("templateWithVariables.ftl");
        Set<String> expectedVariables = new LinkedHashSet<String>(
                Arrays.asList("foo", "bar", "thing", "this.thing"));

        Element root = TemplateIntrospector.getRootNode(template);
        Set<String> variables = new VariableFinder(root).seek().getVariables();
        assertEquals(expectedVariables, variables);
    }

    @Test
    public void testVariableTypeInference() throws IOException {
        Template template = config.getTemplate("typedVariables.ftl");
        Element root = TemplateIntrospector.getRootNode(template);

        List<VariableInfo> vars = new VariableFinder(root).seek().getVariableInfo();
        assertEquals(22, vars.size());
        for (VariableInfo vi : vars) {
            if (vi.getName().startsWith("num")) {
                assertEquals(vi.getName() + " should be numeric",
                        VariableType.NUMBER, vi.getType());
            } else if (vi.getName().startsWith("bool")) {
                assertEquals(vi.getName() + " should be boolean",
                        VariableType.BOOLEAN, vi.getType());
            } else if (vi.getName().startsWith("str")) {
                assertEquals(vi.getName() + " should be string",
                        VariableType.STRING, vi.getType());
            } else if (vi.getName().startsWith("unknown")) {
                assertEquals(vi.getName() + " should be unknown type",
                        VariableType.UNKNOWN, vi.getType());
            }
            assertEquals(vi.getName(), vi.getVariable().toString());
        }
    }

    @Test
    public void testVariableDefaultValueExtraction() throws IOException {
        Template template = config.getTemplate("typedVariables.ftl");
        Element root = TemplateIntrospector.getRootNode(template);

        List<VariableInfo> vars = new VariableFinder(root).seek().getVariableInfo();
        VariableInfo varWithDefaults = getVariable(vars, "strvar2");
        assertEquals(1, varWithDefaults.getValues().size());
        assertEquals("oops", varWithDefaults.getValues().get(0));

        varWithDefaults = getVariable(vars, "numvar4");
        assertEquals(2, varWithDefaults.getValues().size());
        for (Object val : varWithDefaults.getValues()) {
            assertTrue(val.toString().equals("88") || val.toString().equals("99"));
        }

        varWithDefaults = getVariable(vars, "numvar5.key.subkey");
        assertEquals(1, varWithDefaults.getValues().size());
        assertEquals("123", varWithDefaults.getValues().get(0).toString());

        varWithDefaults = getVariable(vars, "strvar3.key");
        assertEquals(1, varWithDefaults.getValues().size());
        assertEquals("xyz", varWithDefaults.getValues().get(0));
    }

    private VariableInfo getVariable(List<VariableInfo> vars, String name) {
        for (VariableInfo vi : vars) {
            if (vi.getName().equals(name)) {
                return vi;
            }
        }
        return null;
    }
}
