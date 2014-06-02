package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

public enum ExprType {
    ADD_CONCAT("AddConcatExpression"),
    AND("AndExpression"),
    ARITHMETIC("ArithmeticExpression"),
    BOOLEAN_LITERAL("BooleanLiteral"),
    BUILTIN("BuiltIn"),
    BUILTIN_VARIABLE("BuiltinVariable"),
    COMPARISON("ComparisonExpression"),
    DEFAULT_TO("DefaultToExpression"),
    DOT("Dot"),
    DYNAMIC_KEY_NAME("DynamicKeyName"),
    EXISTS("ExistsExpression"),
    HASH_LITERAL("HashLiteral"),
    IDENTIFIER("Identifier"),
    LIST_LITERAL("ListLiteral"),
    METHOD_CALL("MethodCall"),
    NOT("NotExpression"),
    NUMBER_LITERAL("NumberLiteral"),
    OR("OrExpression"),
    PARENTHETICAL("ParentheticalExpression"),
    RANGE("Range"),
    STRING_LITERAL("StringLiteral"),
    UNARY_PLUS_MINUS("UnaryPlusMinusExpression"),

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
