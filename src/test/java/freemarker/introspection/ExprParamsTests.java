package freemarker.introspection;

import static freemarker.introspection.TemplateTestUtils.assertIdentifier;
import static freemarker.introspection.TemplateTestUtils.assertNumberExpr;
import static freemarker.introspection.TemplateTestUtils.assertStringExpr;
import static freemarker.introspection.TemplateTestUtils.assertValue;
import static freemarker.introspection.TemplateTestUtils.loadTemplateRoot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests that each type of Expr returns the expected params. Expressions without
 * params are not tested here. 
 */
public class ExprParamsTests {
    @Test
    public void testAddConcat() {
        Expr expr = loadExpr("foo + bar");
        assertEquals(ExprType.ADD_CONCAT, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
    }

    @Test
    public void testAnd() {
        Expr expr = loadExpr("foo && bar");
        assertEquals(ExprType.AND, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
    }

    @Test
    public void testArithmetic() {
        Expr expr = loadExpr("foo * bar");
        assertEquals(ExprType.ARITHMETIC, expr.getType());
        assertEquals(3, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
        assertValue(expr, 2, 1);
    }

    @Test
    public void testBuiltIn() {
        Expr expr = loadExpr("foo?abs");
        assertEquals(ExprType.BUILTIN, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertValue(expr, 1, "abs");
    }

    @Test
    public void testComparison() {
        Expr expr = loadExpr("foo < bar");
        assertEquals(ExprType.COMPARISON, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
    }

    @Test
    public void testDefaultTo() {
        Expr expr = loadExpr("foo!'bar'");
        assertEquals(ExprType.DEFAULT_TO, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertStringExpr(expr, 1, "bar");
    }

    @Test
    public void testDot() {
        Expr expr = loadExpr("foo.bar");
        assertEquals(ExprType.DOT, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertValue(expr, 1, "bar");
    }

    @Test
    public void testDynamicKeyName() {
        Expr expr = loadExpr("foo[bar]");
        assertEquals(ExprType.DYNAMIC_KEY_NAME, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
    }

    @Test
    public void testExists() {
        Expr expr = loadExpr("foo??");
        assertEquals(ExprType.EXISTS, expr.getType());
        assertEquals(1, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
    }

    @Test
    public void testHashLiteral() {
        // wrap then unpack hash literal, because FM expects a scalar in this 
        // context.
        Expr expr = loadExpr("{'foo':'abc', bar:123}[x]");
        assertEquals(ExprType.DYNAMIC_KEY_NAME, expr.getType());
        expr = expr.getParams().get(0);

        assertEquals(ExprType.HASH_LITERAL, expr.getType());
        assertEquals(4, expr.getParams().size());
        assertStringExpr(expr, 0, "foo");
        assertStringExpr(expr, 1, "abc");
        assertIdentifier(expr, 2, "bar");
        assertNumberExpr(expr, 3, 123);
    }

    @Test
    public void testListLiteral() {
        // wrap then unpack list literal
        Expr expr = loadExpr("['foo', 'bar', 123][x]");
        assertEquals(ExprType.DYNAMIC_KEY_NAME, expr.getType());
        expr = expr.getParams().get(0);

        assertEquals(ExprType.LIST_LITERAL, expr.getType());
        assertEquals(3, expr.getParams().size());
        assertStringExpr(expr, 0, "foo");
        assertStringExpr(expr, 1, "bar");
        assertNumberExpr(expr, 2, 123);
    }

    @Test
    public void testMethodCall() {
        Expr expr = loadExpr("foo('bar', 123)");
        assertEquals(ExprType.METHOD_CALL, expr.getType());
        assertEquals(3, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertStringExpr(expr, 1, "bar");
        assertNumberExpr(expr, 2, 123);
    }

    @Test
    public void testNot() {
        Expr expr = loadExpr("!foo");
        assertEquals(ExprType.NOT, expr.getType());
        assertEquals(1, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
    }

    @Test
    public void testOr() {
        Expr expr = loadExpr("foo || bar");
        assertEquals(ExprType.OR, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertIdentifier(expr, 1, "bar");
    }

    @Test
    public void testParenthetical() {
        Expr expr = loadExpr("(foo)");
        assertEquals(ExprType.PARENTHETICAL, expr.getType());
        assertEquals(1, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
    }

    @Test
    public void testRange() {
        Expr expr = loadExpr("foo .. 123");
        assertEquals(ExprType.RANGE, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "foo");
        assertNumberExpr(expr, 1, 123);
    }

    @Test
    public void testStringLiteral() {
        Expr expr = loadExpr("'foo'");
        assertEquals(ExprType.STRING_LITERAL, expr.getType());
        assertNull(((StringLiteralExpr) expr).getDynamicValue());

        expr = loadExpr("'${foo}'");
        assertEquals(ExprType.STRING_LITERAL, expr.getType());
        Element dynValue = ((StringLiteralExpr) expr).getDynamicValue();
        assertNotNull(dynValue);

        // the AST got simplified in FM 2.3.24. In many cases, there 
        // is no longer any MixedContent node between the real parent and
        // children. 
        if (dynValue.getType() == ElementType.MIXED_CONTENT) {
            // older FM model. Bypass MixedContent layer and get real child.
            assertEquals(1, dynValue.getChildren().size());
            dynValue = dynValue.getChildren().get(0);
        }
        assertEquals(ElementType.DOLLAR_VARIABLE, dynValue.getType());
        assertEquals(1, dynValue.getParams().size());
        assertIdentifier(dynValue, 0, "foo");
    }

    @Test
    public void testUnaryPlusMinus() {
        Expr expr = loadExpr("-whatever");
        assertEquals(ExprType.UNARY_PLUS_MINUS, expr.getType());
        assertEquals(2, expr.getParams().size());
        assertIdentifier(expr, 0, "whatever");
        assertValue(expr, 1, 0);
    }

    private Expr loadExpr(String expr) {
        String template = String.format("${%s}", expr);
        Element root = loadTemplateRoot(template);
        assertEquals(ElementType.DOLLAR_VARIABLE, root.getType());
        assertEquals(1, root.getParams().size());
        return root.getParams().get(0);
    }
}
