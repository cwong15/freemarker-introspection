package freemarker.introspection;

public enum ElementType {
    ASSIGNMENT, // name, expr
    ASSIGNMENT_INSTRUCTION, // expr
    ATTEMPT_BLOCK,
    BLOCK_ASSIGNMENT, // name, expr, scope
    BODY_INSTRUCTION, // list of expr
    BREAK_INSTRUCTION,
    CASE, // expr, isDefault (bool)
    COMMENT, // text
    COMPRESSED_BLOCK,
    CONDITIONAL_BLOCK, // expr, isFirst, isSimple
    DEBUG_BREAK,
    DOLLAR_VARIABLE, // expr
    ESCAPE_BLOCK, // name, expr
    FALLBACK_INSTRUCTION,
    FLUSH_INSTRUCTION,
    IFBLOCK,
    INCLUDE, // expr(s), encoding, parse
    ITERATOR_BLOCK, // expr, name, isForeach
    LIBRARY_LOAD, // expr, namespace, path
    MACRO, // name, argNames, map
    MIXED_CONTENT,
    NO_ESCAPE_BLOCK,
    NUMERICAL_OUTPUT, // expr, min/maxFracDigits
    PROPERTY_SETTING, // name, expr
    RECOVERY_BLOCK,
    RECURSE_NODE, // expr, expr
    RETURN_INSTRUCTION, // expr
    STOP_INSTRUCTION,
    SWITCH_BLOCK, // expr
    TEXT_BLOCK, // text, unparsed (bool)
    TRANSFORM_BLOCK, // expr, map
    TRIM_INSTRUCTION, // left/right (bools)
    UNIFIED_CALL, // expr, map, args (lists)
    VISIT_NODE, // expr, expr

    GENERIC
}
