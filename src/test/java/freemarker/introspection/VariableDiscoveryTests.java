package freemarker.introspection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class VariableDiscoveryTests {
    private Template template;

    @Before
    public void setup() throws IOException {
        Configuration config = new Configuration();
        config.setClassForTemplateLoading(this.getClass(), "");
        template = config.getTemplate("templateWithVariables.ftl");
    }

    @Test
    public void testFindVariables() {
        Set<String> expectedVariables = new LinkedHashSet<String>(
                Arrays.asList("foo", "bar", "thing", "this.thing"));

        Element root = TemplateIntrospector.getRootNode(template);
        Set<String> variables = new VariableFinder(root).seek().getVariables();
        assertEquals(expectedVariables, variables);
    }
}
