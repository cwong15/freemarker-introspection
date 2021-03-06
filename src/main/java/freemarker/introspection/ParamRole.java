package freemarker.introspection;

/**
 * Represents the role of a parameter expression
 */
public enum ParamRole {
    LEFT_HAND_OPERAND,
    RIGHT_HAND_OPERAND,
    ENCLOSED_OPERAND,
    ITEM_VALUE,
    ITEM_KEY,
    ASSIGNMENT_TARGET,
    ASSIGNMENT_OPERATOR,
    ASSIGNMENT_SOURCE,
    VARIABLE_SCOPE,
    NAMESPACE,
    ERROR_HANDLER,
    PASSED_VALUE,
    CONDITION,
    VALUE,
    AST_NODE_SUBTYPE,
    PLACEHOLDER_VARIABLE,
    EXPRESSION_TEMPLATE,
    LIST_SOURCE,
    TARGET_LOOP_VARIABLE,
    TEMPLATE_NAME,
    PARSE_PARAMETER,
    ENCODING_PARAMETER,
    IGNORE_MISSING_PARAMETER,
    PARAMETER_NAME,
    PARAMETER_DEFAULT,
    CATCH_ALL_PARAMETER_NAME,
    ARGUMENT_NAME,
    ARGUMENT_VALUE,
    CONTENT,
    EMBEDDED_TEMPLATE,
    VALUE_PART,
    MINIMUM_DECIMALS,
    MAXIMUM_DECIMALS,
    NODE,
    CALLEE,
    MESSAGE,
    UNKNOWN;
}
