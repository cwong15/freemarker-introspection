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
        Configuration templateConfig = templateConfig();
        templateConfig.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
        templateConfig.setTemplateLoader(tloader);
        try {
            Template template = templateConfig.getTemplate("template");
            return TemplateIntrospector.getRootNode(template);
        } catch (IOException e) {
            throw new RuntimeException("Error loading template", e);
        }
    }

    public static Configuration templateConfig() {
        Configuration templateConfig = new Configuration(Configuration.VERSION_2_3_25);
        return templateConfig;
    }

    public static void assertIdentifier(TemplateNode node, int paramIdx, String name) {
        Expr identifier = node.getParams().get(paramIdx);
        assertEquals(ExprType.IDENTIFIER, identifier.getType());
        assertEquals(name, identifier.toString());
    }

    public static void assertIdentifier(TemplateNode node, ParamRole role, String name) {
        Expr identifier = node.paramByRole(role);
        assertEquals(ExprType.IDENTIFIER, identifier.getType());
        assertEquals(name, identifier.toString());
    }

    public static void assertValue(TemplateNode node, int paramIdx, Object expectedValue) {
        // a value may be a literal expression or a value expression. We'll accept both kinds. 
        Expr valueExpr = node.getParams().get(paramIdx);
        assertExprValue(valueExpr, expectedValue);
    }

    public static void assertValue(TemplateNode node, ParamRole role, Object expectedValue) {
        Expr valueExpr = node.paramByRole(role);
        assertExprValue(valueExpr, expectedValue);
    }

    private static void assertExprValue(Expr valueExpr, Object expectedValue) {
        Object value = null;
        if (valueExpr instanceof ValueExpr)
            value = ((ValueExpr) valueExpr).getValue();
        else if (valueExpr instanceof LiteralExpr)
            value = ((LiteralExpr) valueExpr).getValue();
        else
            value = valueExpr.toString();
        assertEquals(expectedValue, value);
    }

    public static void assertStringExpr(TemplateNode node, int paramIdx, String expectedValue) {
        Expr strLiteral = node.getParams().get(paramIdx);
        assertEquals(ExprType.STRING_LITERAL, strLiteral.getType());
        assertEquals(expectedValue, ((StringExpr) strLiteral).getValue());
    }

    public static void assertStringExpr(TemplateNode node, ParamRole role, String expectedValue) {
        Expr strLiteral = node.paramByRole(role);
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
