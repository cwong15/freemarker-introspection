package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

public enum ExprType {
    ADD_CONCAT("AddConcatExpression", "left", "right"),
    AND("AndExpression", "lho", "rho"),
    ARITHMETIC("ArithmeticExpression", "lho", "rho", "operator"),
    BOOLEAN_LITERAL("BooleanLiteral", "val"),
    BUILTIN("BuiltIn"),
    BUILTIN_VARIABLE("BuiltinVariable"),
    COMPARISON("ComparisonExpression", "left", "right"),
    DEFAULT_TO("DefaultToExpression", "lho", "rho"),
    DOT("Dot", "target", "key"),
    DYNAMIC_KEY_NAME("DynamicKeyName", "target", "nameExpression"),
    EXISTS("ExistsExpression", "exp"),
    HASH_LITERAL("HashLiteral"),
    IDENTIFIER("Identifier", "name"),
    LIST_LITERAL("ListLiteral", "items"),
    METHOD_CALL("MethodCall", "target", "arguments"),
    NOT("NotExpression", "target"),
    NUMBER_LITERAL("NumberLiteral", "value"),
    OR("OrExpression", "lho", "rho"),
    PARENTHETICAL("ParentheticalExpression", "nested"),
    RANGE("Range", "lho", "rho"),
    STRING_LITERAL("StringLiteral", "value"),
    UNARY_PLUS_MINUS("UnaryPlusMinusExpression"),

    VALUE(""),

    GENERIC("");

    private String className;
    private List<String> subExprProps;

    private ExprType(String className, String... exprProps) {
        this.className = className;
        this.subExprProps = Arrays.asList(exprProps);
    }

    String getClassName() {
        return this.className;
    }

    List<String> getSubExprProps() {
        return this.subExprProps;
    }
}
