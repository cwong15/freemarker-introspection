package freemarker.introspection;


/**
 * Represents the type of the expression 
 */
public enum ExprType {
    /** Add concat operator (+ operator). Parameters: left, right operands */
    ADD_CONCAT("AddConcatExpression"),

    /** And operator. Parameters: left, right operands */
    AND("AndExpression"),

    /** 
     * Arithmetic expression (-, *, /, % operations). Parameters: left operand, 
     * right operand, operator.
     */
    ARITHMETIC("ArithmeticExpression"),

    /** Boolean literal. Implements LiteralExpr interface */
    BOOLEAN_LITERAL("BooleanLiteral"),

    /** Builtin. Parameters: target, key */
    BUILTIN("BuiltIn"),

    /** Builtin variable. */
    BUILTIN_VARIABLE("BuiltinVariable"),

    /** Comparison. Parameters: left, right operands. */
    COMPARISON("ComparisonExpression"),

    /** Default to expression. Parameters: left, right operands. */
    DEFAULT_TO("DefaultToExpression"),

    /** Dot expression. Parameters: target, key. */
    DOT("Dot"),

    /** Dynamic key name. Parameters: target, name expression. */
    DYNAMIC_KEY_NAME("DynamicKeyName"),

    /** Exists expression. Parameter: exp */
    EXISTS("ExistsExpression"),

    /** Hash literal */
    HASH_LITERAL("HashLiteral"),

    /** Identifier. */
    IDENTIFIER("Identifier"),

    /** List literal. Parameters: items */
    LIST_LITERAL("ListLiteral"),

    /** Method call. Parameters: target, arguments */
    METHOD_CALL("MethodCall"),

    /** Not expression. Parameter: target expression. */
    NOT("NotExpression"),

    /** Number literal. Implements LiteralExpr interface */
    NUMBER_LITERAL("NumberLiteral"),

    /** Or expression. Parameters: left, right operands.*/
    OR("OrExpression"),

    /** Parenthetical expression. Parameter: nested expression */
    PARENTHETICAL("ParentheticalExpression"),

    /** Range. Parameters: left, right operands */
    RANGE("Range"),

    /** String literal. Implements StringExpr interface. */
    STRING_LITERAL("StringLiteral"),

    /** Unary plus/minus expression. Parameters: target expression */
    UNARY_PLUS_MINUS("UnaryPlusMinusExpression"),

    /** 
     * Expression expressing scalar value. VALUE expressions implement the 
     * ValueExpr interface.  
     */
    VALUE(""),

    /** Unidentified expression */
    GENERIC("");

    private String className;

    private ExprType(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }
}
