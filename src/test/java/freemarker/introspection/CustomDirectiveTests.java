package freemarker.introspection;

import static freemarker.introspection.TemplateTestUtils.assertIdentifier;
import static freemarker.introspection.TemplateTestUtils.assertNumberExpr;
import static freemarker.introspection.TemplateTestUtils.assertStringExpr;
import static freemarker.introspection.TemplateTestUtils.assertValue;
import static freemarker.introspection.TemplateTestUtils.loadTemplateRoot;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CustomDirectiveTests {
    @Test
    public void testUserDefinedDirective() {
        Element root = loadTemplateRoot(
                "[@foo param1='1' param2=2/][#macro foo param1 param2][/#macro]");
        Element elem = root.getChildren().get(0);
        assertEquals(ElementType.UNIFIED_CALL, elem.getType());
        assertIdentifier(elem, 0, "foo");
        assertValue(elem, 1, "param1");
        assertStringExpr(elem, 2, "1");
        assertValue(elem, 3, "param2");
        assertNumberExpr(elem, 4, 2);
    }
}
