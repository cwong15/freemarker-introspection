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
    AND("AndExpression",
            l("lho", "rho"),
            l("left", "right")),

    /** 
     * Arithmetic expression (-, *, /, % operations). Parameters: left operand, 
     * right operand, operator.
     */
    ARITHMETIC("ArithmeticExpression",
            l("lho", "rho", "operator"),
            l("left", "right", "operation")),

    /** Boolean literal. Implements LiteralExpr interface */
    BOOLEAN_LITERAL("BooleanLiteral"),

    /** Builtin. Parameters: target, key */
    BUILTIN("BuiltIn", "target", "key"),

    /** Builtin variable. */
    BUILTIN_VARIABLE("BuiltinVariable"),

    /** Comparison. Parameters: left, right operands. */
    COMPARISON("ComparisonExpression", "left", "right"),

    /** Default to expression. Parameters: left, right operands. */
    DEFAULT_TO("DefaultToExpression",
            l("lho", "rho"),
            l("lhs", "rhs")),

    /** Dot expression. Parameters: target, key. */
    DOT("Dot", "target", "key"),

    /** Dynamic key name. Parameters: target, name expression. */
    DYNAMIC_KEY_NAME("DynamicKeyName",
            l("target", "nameExpression"),
            l("target", "keyExpression")),

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

    /** Number literal. Implements LiteralExpr interface */
    NUMBER_LITERAL("NumberLiteral"),

    /** Or expression. Parameters: left, right operands.*/
    OR("OrExpression",
            l("lho", "rho"),
            l("left", "right")),

    /** Parenthetical expression. Parameter: nested expression */
    PARENTHETICAL("ParentheticalExpression", "nested"),

    /** Range. Parameters: left, right operands */
    RANGE("Range",
            l("lho", "rho"),
            l("left", "right")),

    /** String literal. Implements StringExpr interface. */
    STRING_LITERAL("StringLiteral"),

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
    private List<List<String>> exprFields;

    private ExprType(String className) {
        this(className, l());
    }

    private ExprType(String className, String... exprFields) {
        this(className, l(l(exprFields)));
    }

    private ExprType(String className, List<String>... exprFields) {
        this(className, l(exprFields));
    }

    private ExprType(String className, List<List<String>> exprFields) {
        this.className = className;
        this.exprFields = exprFields;
    }

    public String getClassName() {
        return this.className;
    }

    public List<List<String>> getExprFields() {
        return exprFields;
    }

    private static List<String> l() {
        return Arrays.asList();
    }

    private static <T> List<T> l(T... fields) {
        return Arrays.asList(fields);
    }
}
