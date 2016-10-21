package freemarker.introspection;

/**
 * Represents the type of the element
 */
public enum ElementType {
    /** 
     * Variable assignment. Parameters: variable name, value, scope
     * and namespace 
     */
    ASSIGNMENT("Assignment"),

    /**
     * Assignment instruction. Parameters: scope, namespace.
     */
    ASSIGNMENT_INSTRUCTION("AssignmentInstruction"),

    /** Attempt block */
    ATTEMPT_BLOCK("AttemptBlock"),

    /** Auto escape block */
    AUTO_ESC_BLOCK("AutoEscBlock"),

    /**
     * Block assignment. Parameters: variable name, scope, namespace.
     */
    BLOCK_ASSIGNMENT("BlockAssignment"),

    /** Body instruction. Parameters: loop variables */
    BODY_INSTRUCTION("BodyInstruction"),

    /** Break instruction */
    BREAK_INSTRUCTION("BreakInstruction"),

    /**
     * A case in a switch statement. Parameters: condition (none for #default),
     * case type.
     */
    CASE("Case"),

    /**
     * Comment. Parameter: text. 
     */
    COMMENT("Comment"),

    /** Compressed block */
    COMPRESSED_BLOCK("CompressedBlock"),

    /**
     * Conditional block in an if statement. Parameters: condition
     * (none for #else), type.
     */
    CONDITIONAL_BLOCK("ConditionalBlock"),

    /** Debug break */
    DEBUG_BREAK("DebugBreak"),

    /** 
     * A ${} expression. Parameter: the expression in parenthesis  
     */
    DOLLAR_VARIABLE("DollarVariable"),

    /**
     * Escape block. Parameter: variable name, expression
     */
    ESCAPE_BLOCK("EscapeBlock"),

    /** Fallback instruction */
    FALLBACK_INSTRUCTION("FallbackInstruction"),

    /** Flush instruction */
    FLUSH_INSTRUCTION("FlushInstruction"),

    /** If block*/
    IFBLOCK("IfBlock"),

    /**
     * Represents an include. Parameters: template name, parse, encoding.
     */
    INCLUDE("Include"),

    /** #items directive in a #list */
    ITEMS("Items"),

    /**
     * Iterator block (#list). Parameters: list source, loop variable name
     */
    ITERATOR_BLOCK("IteratorBlock"),

    /**
     * Library load (#import). Parameters: template name, namespace.
     */
    LIBRARY_LOAD("LibraryLoad"),

    /** #else clause of a #list */
    LIST_ELSE_CONTAINER("ListElseContainer"),

    /** Macro. Parameters: name, parameter names..., type (macro/function) */
    MACRO("Macro"),

    /** Mixed content. */
    MIXED_CONTENT("MixedContent"),

    /** No auto escape block*/
    NO_AUTO_ESC_BLOCK("NoAutoEscBlock"),

    /** No escape block */
    NO_ESCAPE_BLOCK("NoEscapeBlock"),

    /** Numerical output. Parameters: expression, min and max fractional digits */
    NUMERICAL_OUTPUT("NumericalOutput"),

    /** #outputformat block */
    OUTPUT_FORMAT_BLOCK("OutputFormatBlock"),

    /** Property setting. Parameters: key, value */
    PROPERTY_SETTING("PropertySetting"),

    /** Recovery block */
    RECOVERY_BLOCK("RecoveryBlock"),

    /** Recurse node. Parameters: target node, namespace */
    RECURSE_NODE("RecurseNode"),

    /** Return instruction. Parameter: expression, if any. */
    RETURN_INSTRUCTION("ReturnInstruction"),

    /** 
     * #sep block in a #list
     */
    SEP("Sep"),

    /** Stop instruction. Parameter: expression. */
    STOP_INSTRUCTION("StopInstruction"),

    /** Switch block. Parameter: switch expression. */
    SWITCH_BLOCK("SwitchBlock"),

    /** Text block. Parameter: text */
    TEXT_BLOCK("TextBlock"),

    /** 
     * Transform block. Parameters: transform expression, followed by param name 
     * and value pairs, in order
     */
    TRANSFORM_BLOCK("TransformBlock"),

    /** Trim instruction */
    TRIM_INSTRUCTION("TrimInstruction"),

    /** 
     * Unified macro/transform call. Parameter: name expression, followed by 
     * param name and default value pairs, in order. 
     */
    UNIFIED_CALL("UnifiedCall"),

    /** Visit node. Parameters: target node, namespace */
    VISIT_NODE("VisitNode"),

    /** Unidentified Freemarker element. */
    GENERIC("");

    private String className;

    private ElementType(String className) {
        this.className = className;
    }

    String getClassName() {
        return this.className;
    }
}
