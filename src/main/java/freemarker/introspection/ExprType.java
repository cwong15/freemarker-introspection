package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the type of the expression 
 */
public enum ExprType {
    /** Add concat operator (+ operator). Parameters: left, right operands */
    ADD_CONCAT("AddConcatExpression", "left", "right"),

    /** And operator. Parameters: left, right operands */
    AND("AndExpression", l("lho", "rho"), l("left", "right")),

    /** 
     * Arithmetic expression (-, *, /, % operations). Parameters: left operand, 
     * right operand, operator.
     */
    ARITHMETIC("ArithmeticExpression", l("lho", "rho", "operator"),
            l("left", "right", "operation")),

    /** Boolean literal. Parameter: value */
    BOOLEAN_LITERAL("BooleanLiteral", "val"),

    /** Builtin. Parameters: target, key */
    BUILTIN("BuiltIn", "target", "key"),

    /** Builtin variable. */
    BUILTIN_VARIABLE("BuiltinVariable"),

    /** Comparison. Parameters: left, right operands. */
    COMPARISON("ComparisonExpression", "left", "right"),

    /** Default to expression. Parameters: left, right operands. */
    DEFAULT_TO("DefaultToExpression", l("lho", "rho"), l("lhs", "rhs")),

    /** Dot expression. Parameters: target, key. */
    DOT("Dot", "target", "key"),

    /** Dynamic key name. Parameters: target, name expression. */
    DYNAMIC_KEY_NAME("DynamicKeyName", "target", "nameExpression"),

    /** Exists expression. Parameter: exp */
    EXISTS("ExistsExpression", "exp"),

    /** Hash literal */
    HASH_LITERAL("HashLiteral"),

    /** Identifier. Parameter: name. */
    IDENTIFIER("Identifier", "name"),

    /** List literal. Parameter: items */
    LIST_LITERAL("ListLiteral", l("items"), l("values")),

    /** Method call. Parameters: target, arguments */
    METHOD_CALL("MethodCall", "target", "arguments"),

    /** Not expression. Parameter: target expression. */
    NOT("NotExpression", "target"),

    /** Number literal. Parameter: value */
    NUMBER_LITERAL("NumberLiteral", "value"),

    /** Or expression. Parameters: left, right operands.*/
    OR("OrExpression", l("lho", "rho"), l("left", "right")),

    /** Parenthetical expression. Parameter: nested expression */
    PARENTHETICAL("ParentheticalExpression", "nested"),

    /** Range. Parameters: left, right operands */
    RANGE("Range", l("lho", "rho"), l("left", "right")),

    /** String literal. Parameter: value */
    STRING_LITERAL("StringLiteral", "value"),

    /** Unary plus/minus expression */
    UNARY_PLUS_MINUS("UnaryPlusMinusExpression"),

    /** 
     * Expression expressing scalar value. VALUE expressions implement the 
     * ValueExpr interface.  
     */
    VALUE(""),

    /** Unidentified expression */
    GENERIC("");

    private String className;
    private List<String> subExprFields;
    private List<String> altExprFields;

    private ExprType(String className, String... exprFields) {
        this(className, l(exprFields), null);
    }

    private ExprType(String className, List<String> exprFields, List<String> altFields) {
        this.className = className;
        this.subExprFields = exprFields;
        this.altExprFields = altFields;
    }

    public String getClassName() {
        return this.className;
    }

    List<String> getSubExprFields() {
        return this.subExprFields;
    }

    List<String> getAltExprFields() {
        return this.altExprFields;
    }

    private static List<String> l(String... fields) {
        return Arrays.asList(fields);
    }
}
