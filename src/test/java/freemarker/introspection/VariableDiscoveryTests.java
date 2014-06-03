package freemarker.introspection;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

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
        Element root = TemplateIntrospector.getRootNode(template);
        for (Element child : root.getChildren()) {
            if (child.getType() == ElementType.IFBLOCK) {
                for (Element condBlk : child.getChildren()) {
                    Object expr = condBlk.getParams().get(0);
                }
            }
        }
        assertNotNull(root);
    }

}
