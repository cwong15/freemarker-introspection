package freemarker.introspection;

import static freemarker.introspection.TemplateTestUtils.assertIdentifier;
import static freemarker.introspection.TemplateTestUtils.assertStringExpr;
import static freemarker.introspection.TemplateTestUtils.assertValue;
import static freemarker.introspection.TemplateTestUtils.loadTemplateRoot;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests that each type of Element returns the expected Element.params values
 */
public class DirectiveParamsTests {
    @Test
    public void testIfDirective() {
        Element root = loadTemplateRoot("[#if boolVar1][#elseif boolVar2][#else][/#if]");
        assertEquals(ElementType.IFBLOCK, root.getType());

        // [#if boolVar1]
        Element cond = root.getChildren().get(0);
        assertEquals(ElementType.CONDITIONAL_BLOCK, cond.getType());
        assertEquals(2, cond.getParams().size());
        assertIdentifier(cond, 0, "boolVar1");
        assertValue(cond, 1, 0); // cond block type

        // [#elseif boolVar2]
        cond = root.getChildren().get(1);
        assertEquals(ElementType.CONDITIONAL_BLOCK, cond.getType());
        assertEquals(2, cond.getParams().size());
        assertIdentifier(cond, 0, "boolVar2");
        assertValue(cond, 1, 2);

        // [#else]
        cond = root.getChildren().get(2);
        assertEquals(ElementType.CONDITIONAL_BLOCK, cond.getType());
        assertEquals(1, cond.getParams().size());
        assertValue(cond, 0, 1);
    }

    @Test
    public void testSwitchDirective() {
        Element root = loadTemplateRoot(
                "[#switch var1][#case 'a'][#break][#default][/#switch]");

        assertEquals(ElementType.SWITCH_BLOCK, root.getType());
        assertEquals(2, root.getChildren().size());
        assertEquals(1, root.getParams().size());
        assertIdentifier(root, 0, "var1");

        // [#case 'a'][#break]
        Element c = root.getChildren().get(0);
        assertEquals(ElementType.CASE, c.getType());
        assertEquals(2, c.getParams().size());
        assertStringExpr(c, 0, "a");
        assertValue(c, 1, 0);

        // [#default]
        c = root.getChildren().get(1);
        assertEquals(ElementType.CASE, c.getType());
        assertEquals(1, c.getParams().size());
        assertValue(c, 0, 1);
    }

    @Test
    public void testListAndBreakDirectives() {
        Element root = loadTemplateRoot("[#list [1,2,3] as x]something[#break][/#list]");
        assertEquals(ElementType.ITERATOR_BLOCK, root.getType());
        assertEquals(2, root.getChildren().size());
        assertEquals(2, root.getParams().size());

        assertEquals(ExprType.LIST_LITERAL, root.getParams().get(0).getType());
        assertValue(root, 1, "x");

        Element breakElem = root.getChildren().get(1);
        assertEquals(ElementType.BREAK_INSTRUCTION, breakElem.getType());
        assertTrue(breakElem.getParams().isEmpty());
    }

    @Test
    public void testIncludeDirective() {
        Element root = loadTemplateRoot(
                "[#include 'foo/bar' parse=true encoding='UTF-16']");
        assertEquals(ElementType.INCLUDE, root.getType());
        assertEquals(3, root.getParams().size());

        assertStringExpr(root, 0, "foo/bar");
        assertValue(root, 1, true);
        assertValue(root, 2, "UTF-16");
    }

    @Test
    public void testImportDirective() {
        Element root = loadTemplateRoot("[#import 'foo/bar' as h]");
        assertEquals(ElementType.LIBRARY_LOAD, root.getType());
        assertEquals(2, root.getParams().size());
        assertStringExpr(root, 0, "foo/bar");
        assertValue(root, 1, "h");
    }

    @Test
    public void testEscapeDirective() {
        Element root = loadTemplateRoot("[#escape x as x?html][/#escape]");
        assertEquals(ElementType.ESCAPE_BLOCK, root.getType());
        assertEquals(2, root.getParams().size());
        assertValue(root, 0, "x");
        assertEquals(ExprType.BUILTIN, root.getParams().get(1).getType());
    }

    @Test
    public void testAssignmentDirectives() {
        Element root = loadTemplateRoot("[#assign foo = 'a' in n]");
        assertEquals(ElementType.ASSIGNMENT, root.getType());
        assertEquals(4, root.getParams().size());
        assertValue(root, 0, "foo");
        assertStringExpr(root, 1, "a");
        assertValue(root, 2, 1);
        assertIdentifier(root, 3, "n");

        root = loadTemplateRoot("[#assign foo=1 bar=2 in n]");
        assertEquals(ElementType.ASSIGNMENT_INSTRUCTION, root.getType());
        assertEquals(2, root.getParams().size());
        assertValue(root, 0, 1); // scope
        assertIdentifier(root, 1, "n"); // namespace
        // assignment elements as children
        assertEquals(2, root.getChildren().size());
        for (Element e : root.getChildren()) {
            assertEquals(ElementType.ASSIGNMENT, e.getType());
        }

        root = loadTemplateRoot("[#assign foooooo in n]oops[/#assign]");
        assertEquals(ElementType.BLOCK_ASSIGNMENT, root.getType());
        assertEquals(3, root.getParams().size());
        assertValue(root, 0, "foooooo");
        assertValue(root, 1, 1);
        assertIdentifier(root, 2, "n");
    }

    @Test
    public void testGlobalDirectives() {
        Element root = loadTemplateRoot("[#global foo = 'a']");
        assertEquals(ElementType.ASSIGNMENT, root.getType());
        assertEquals(3, root.getParams().size());
        assertValue(root, 0, "foo");
        assertStringExpr(root, 1, "a");
        assertValue(root, 2, 3);

        root = loadTemplateRoot("[#global foo=1 bar=2]");
        assertEquals(ElementType.ASSIGNMENT_INSTRUCTION, root.getType());
        assertEquals(1, root.getParams().size());
        assertValue(root, 0, 3); // scope
        // assignment elements as children
        assertEquals(2, root.getChildren().size());
        for (Element e : root.getChildren()) {
            assertEquals(ElementType.ASSIGNMENT, e.getType());
        }

        root = loadTemplateRoot("[#global foo]oops[/#global]");
        assertEquals(ElementType.BLOCK_ASSIGNMENT, root.getType());
        assertEquals(2, root.getParams().size());
        assertValue(root, 0, "foo");
        assertValue(root, 1, 3);
    }

    @Test
    public void testLocalDirectives() {
        Element root = loadTemplateRoot("[#macro m][#local foo = 'a'][/#macro]")
                .getChildren().get(0);
        assertEquals(ElementType.ASSIGNMENT, root.getType());
        assertEquals(3, root.getParams().size());
        assertValue(root, 0, "foo");
        assertStringExpr(root, 1, "a");
        assertValue(root, 2, 2); // scope

        root = loadTemplateRoot("[#macro m][#local foo=1 bar=2][/#macro]")
                .getChildren().get(0);
        assertEquals(ElementType.ASSIGNMENT_INSTRUCTION, root.getType());
        assertEquals(1, root.getParams().size());
        assertValue(root, 0, 2);
        // assignment elements as children
        assertEquals(2, root.getChildren().size());
        for (Element e : root.getChildren()) {
            assertEquals(ElementType.ASSIGNMENT, e.getType());
        }

        root = loadTemplateRoot("[#macro m][#local foo]oops[/#local][/#macro]")
                .getChildren().get(0);
        assertEquals(ElementType.BLOCK_ASSIGNMENT, root.getType());
        assertEquals(2, root.getParams().size());
        assertValue(root, 0, "foo");
        assertValue(root, 1, 2);
    }

    @Test
    public void testSettingDirective() {
        Element root = loadTemplateRoot("[#setting locale='fr_FR']");
        assertEquals(ElementType.PROPERTY_SETTING, root.getType());
        assertEquals(2, root.getParams().size());
        assertValue(root, 0, "locale");
        assertStringExpr(root, 1, "fr_FR");
    }

    @Test
    public void testUserDefinedDirective() {
        Element root = loadTemplateRoot(
                "[@foo param1='1' param2='2'/][#macro foo param1 param2][/#macro]");
        Element elem = root.getChildren().get(0);
        assertEquals(ElementType.UNIFIED_CALL, elem.getType());
        assertIdentifier(elem, 0, "foo");
        assertValue(elem, 1, "param1");
        assertStringExpr(elem, 2, "1");
        assertValue(elem, 3, "param2");
        assertStringExpr(elem, 4, "2");
    }

    @Test
    public void testMacroDirective() {
        Element root = loadTemplateRoot(
                "[#macro m param1 param2][#nested loopv1 loopv2][#return][/#macro]");
        assertEquals(ElementType.MACRO, root.getType());
        assertEquals(2, root.getChildren().size());
        assertEquals(4, root.getParams().size());
        assertValue(root, 0, "m");
        assertValue(root, 1, "param1");
        assertValue(root, 2, "param2");
        assertValue(root, 3, 0);

        Element nested = root.getChildren().get(0);
        assertEquals(ElementType.BODY_INSTRUCTION, nested.getType());
        assertEquals(2, nested.getParams().size());
        assertIdentifier(nested, 0, "loopv1");
        assertIdentifier(nested, 1, "loopv2");

        Element retElem = root.getChildren().get(1);
        assertEquals(ElementType.RETURN_INSTRUCTION, retElem.getType());
        assertTrue(retElem.getParams().isEmpty());
    }

    @Test
    public void testFunctionDirective() {
        Element root = loadTemplateRoot(
                "[#function foo param1 param2][#return 'rval'][/#function]");
        assertEquals(ElementType.MACRO, root.getType());
        assertEquals(1, root.getChildren().size());
        root.getParams();
        assertEquals(4, root.getParams().size());
        assertValue(root, 0, "foo");
        assertValue(root, 1, "param1");
        assertValue(root, 2, "param2");
        assertValue(root, 3, 1);

        Element retElem = root.getChildren().get(0);
        assertEquals(ElementType.RETURN_INSTRUCTION, retElem.getType());
        assertStringExpr(retElem, 0, "rval");
    }

    @Test
    public void testStopDirective() {
        Element root = loadTemplateRoot("[#stop 'stopreason']");
        assertEquals(ElementType.STOP_INSTRUCTION, root.getType());
        assertEquals(1, root.getParams().size());
        assertStringExpr(root, 0, "stopreason");
    }

    @Test
    public void testVisitRecurseDirectives() {
        Element root = loadTemplateRoot("[#visit node using n]");
        assertEquals(ElementType.VISIT_NODE, root.getType());
        assertEquals(2, root.getParams().size());
        assertIdentifier(root, 0, "node");
        assertIdentifier(root, 1, "n");

        root = loadTemplateRoot("[#recurse node using n]");
        assertEquals(ElementType.RECURSE_NODE, root.getType());
        assertEquals(2, root.getParams().size());
        assertIdentifier(root, 0, "node");
        assertIdentifier(root, 1, "n");
    }

    @Test
    public void testDollarVariable() {
        Element root = loadTemplateRoot("${foo}");
        assertEquals(ElementType.DOLLAR_VARIABLE, root.getType());
        assertEquals(1, root.getParams().size());
        assertIdentifier(root, 0, "foo");
    }

    @Test
    public void testNumericalOutput() {
        Element root = loadTemplateRoot("#{foo; m2M5}");
        assertEquals(ElementType.NUMERICAL_OUTPUT, root.getType());
        assertEquals(3, root.getParams().size());
        assertIdentifier(root, 0, "foo");
        assertValue(root, 1, 2);
        assertValue(root, 2, 5);
    }
}
