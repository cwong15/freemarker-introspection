package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

/**
 * Represents the type of the element
 */
public enum ElementType {
    /** 
     * Variable assignment. Parameters: variable name, scope, value
     * and namespace 
     */
    ASSIGNMENT("Assignment", "variableName", "value", "scope", "namespaceExp"),

    /**
     * Assignment instruction. Parameters: scope, namespace.
     */
    ASSIGNMENT_INSTRUCTION("AssignmentInstruction", "scope", "namespaceExp"),

    /** Attempt block */
    ATTEMPT_BLOCK("AttemptBlock"),

    /**
     * Block assignment. Parameters: variable name, scope, namespace.
     */
    BLOCK_ASSIGNMENT("BlockAssignment", "varName", "scope", "namespaceExp"),

    /** Body instruction */
    BODY_INSTRUCTION("BodyInstruction"),

    /** Break instruction */
    BREAK_INSTRUCTION("BreakInstruction"),

    /**
     * A case in a switch statement. Parameters: condition (may be null),
     * default.
     */
    CASE("Case", l("condition"), l("expression")),

    /**
     * Comment. Parameter: text. 
     */
    COMMENT("Comment", "text"),

    /** Compressed block */
    COMPRESSED_BLOCK("CompressedBlock"),

    /**
     * Conditional block in an if statement. Parameters: condition
     * (may be null), type.
     */
    CONDITIONAL_BLOCK("ConditionalBlock", "condition"),

    /** Debug break */
    DEBUG_BREAK("DebugBreak"),

    /** 
     * A ${} expression. Parameter: the expression in parenthesis  
     */
    DOLLAR_VARIABLE("DollarVariable", "expression"),

    /**
     * Escape block. Parameter: variable name, expression
     */
    ESCAPE_BLOCK("EscapeBlock", "variable", "expr"),

    /** Fallback instruction */
    FALLBACK_INSTRUCTION("FallbackInstruction"),

    /** Flush instruction */
    FLUSH_INSTRUCTION("FlushInstruction"),

    /** If block*/
    IFBLOCK("IfBlock"),

    /**
     * Represents an include. Parameters: template name, parse, encoding.
     */
    INCLUDE("Include",
            l("templateName", "parse", "encoding"),
            l("includedTemplateName", "parse", "encoding"),
            l("includedTemplateNameExp", "parse", "encoding")),

    /**
     * Iterator block. Parameters: list source, loop variable name
     */
    ITERATOR_BLOCK("IteratorBlock",
            l("listSource", "loopVariableName"),
            l("listExpression", "indexName"),
            l("listExpression", "loopVariableName")),

    /**
     * Library load. Parameters: template name, namespace.
     */
    LIBRARY_LOAD("LibraryLoad",
            l("templateName", "namespace"),
            l("importedTemplateNameExp", "namespace")),

    /** Macro. Parameters: name, parameter names... */
    MACRO("Macro", l("name", "paramNames"), l("name", "argumentNames")),

    /** Mixed content. */
    MIXED_CONTENT("MixedContent"),

    /** No escape block */
    NO_ESCAPE_BLOCK("NoEscapeBlock"),

    /** Numerical output. Parameters: expression, min and max fractional digits */
    NUMERICAL_OUTPUT("NumericalOutput", "expression", "minFracDigits", "maxFracDigits"),

    /** Property setting. Parameters: key, value */
    PROPERTY_SETTING("PropertySetting", "key", "value"),

    /** Recovery block */
    RECOVERY_BLOCK("RecoveryBlock"),

    /** Recurse node. Parameters: target node, namespaces */
    RECURSE_NODE("RecurseNode", "targetNode", "namespaces"),

    /** Return instruction. Parameter: expression. */
    RETURN_INSTRUCTION("ReturnInstruction", "exp"),

    /** Stop instruction. Parameter: expression. */
    STOP_INSTRUCTION("StopInstruction", "exp"),

    /** Switch block. Parameter: search expression. */
    SWITCH_BLOCK("SwitchBlock", l("searched"), l("testExpression")),

    /** Text block. Parameter: text */
    TEXT_BLOCK("TextBlock", "text"),

    /** Transform block. Parameter: transform expression */
    TRANSFORM_BLOCK("TransformBlock", "transformExpression"),

    /** Trim instruction */
    TRIM_INSTRUCTION("TrimInstruction"),

    /** Unified macro/transform call. Parameter: name expression. */
    UNIFIED_CALL("UnifiedCall", "nameExp"),

    /** Visit node. Parameter: target node, namespaces */
    VISIT_NODE("VisitNode", "targetNode", "namespaces"),

    /** Unidentified Freemarker element. */
    GENERIC("");

    private String className;
    private List<List<String>> paramFields;

    private ElementType(String className) {
        this(className, l());
    }

    private ElementType(String className, String... paramFields) {
        this(className, l(l(paramFields)));
    }

    private ElementType(String className, List<String>... paramProps) {
        this(className, l(paramProps));
    }

    private ElementType(String className, List<List<String>> paramProps) {
        this.className = className;
        this.paramFields = paramProps;
    }

    public String getClassName() {
        return this.className;
    }

    public List<List<String>> getParamFields() {
        return this.paramFields;
    }

    private static List<String> l() {
        return Arrays.asList();
    }

    private static <T> List<T> l(T... fields) {
        return Arrays.asList(fields);
    }
}
