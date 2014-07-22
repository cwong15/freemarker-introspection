package freemarker.introspection;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class TemplateTestUtils {
    public static Element loadTemplateRoot(String templateText) {
        StringTemplateLoader tloader = new StringTemplateLoader();
        tloader.putTemplate("template", templateText);
        Configuration templateConfig = new Configuration();
        templateConfig.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
        templateConfig.setTemplateLoader(tloader);
        try {
            Template template = templateConfig.getTemplate("template");
            return TemplateIntrospector.getRootNode(template);
        } catch (IOException e) {
            throw new RuntimeException("Error loading template", e);
        }
    }

    public static void assertIdentifier(TemplateNode node, int paramIdx, String name) {
        Expr identifier = node.getParams().get(paramIdx);
        assertEquals(ExprType.IDENTIFIER, identifier.getType());
        assertEquals(name, identifier.toString());
    }

    public static void assertValue(TemplateNode node, int paramIdx, Object expectedValue) {
        Expr valueExpr = node.getParams().get(paramIdx);
        assertEquals(ExprType.VALUE, valueExpr.getType());
        assertEquals(expectedValue, ((ObjectExpr) valueExpr).getValue());
    }

    public static void assertStringExpr(TemplateNode node, int paramIdx, String expectedValue) {
        Expr strLiteral = node.getParams().get(paramIdx);
        assertEquals(ExprType.STRING_LITERAL, strLiteral.getType());
        assertEquals(expectedValue, ((StringExpr) strLiteral).getValue());
    }

    public static void assertNumberExpr(TemplateNode node, int paramIdx, int expectedValue) {
        Expr numLiteral = node.getParams().get(paramIdx);
        assertEquals(ExprType.NUMBER_LITERAL, numLiteral.getType());
        assertEquals(new BigDecimal(expectedValue),
                ((NumberLiteralExpr) numLiteral).getValue());
    }
}
