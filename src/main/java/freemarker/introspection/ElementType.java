package freemarker.introspection;

import java.util.Arrays;
import java.util.List;

public enum ElementType {
    ASSIGNMENT("Assignment", "variableName", "value", "scope", "namespaceExp"),
    ASSIGNMENT_INSTRUCTION("AssignmentInstruction", "scope", "namespaceExp"),
    ATTEMPT_BLOCK("AttemptBlock"),
    BLOCK_ASSIGNMENT("BlockAssignment", "varName", "scope", "namespaceExp"),
    BODY_INSTRUCTION("BodyInstruction"),
    BREAK_INSTRUCTION("BreakInstruction"),
    CASE("Case", "condition", "isDefault"),
    COMMENT("Comment", "text"),
    COMPRESSED_BLOCK("CompressedBlock"),
    CONDITIONAL_BLOCK("ConditionalBlock", "condition", "type"),
    DEBUG_BREAK("DebugBreak"),
    DOLLAR_VARIABLE("DollarVariable", "expression"),
    ESCAPE_BLOCK("EscapeBlock", "variable", "expr"),
    FALLBACK_INSTRUCTION("FallbackInstruction"),
    FLUSH_INSTRUCTION("FlushInstruction"),
    IFBLOCK("IfBlock"),
    INCLUDE("Include", "templateName", "parse", "encoding"),
    ITERATOR_BLOCK("IteratorBlock", "listSource", "loopVariableName"),
    LIBRARY_LOAD("LibraryLoad", "templateName", "namespace"),
    MACRO("Macro", "name", "paramNames"),
    MIXED_CONTENT("MixedContent"),
    NO_ESCAPE_BLOCK("NoEscapeBlock"),
    NUMERICAL_OUTPUT("NumericalOutput", "expression", "minFracDigits", "maxFracDigits"),
    PROPERTY_SETTING("PropertySetting", "key", "value"),
    RECOVERY_BLOCK("RecoveryBlock"),
    RECURSE_NODE("RecurseNode", "targetNode", "namespaces"),
    RETURN_INSTRUCTION("ReturnInstruction", "exp"),
    STOP_INSTRUCTION("StopInstruction", "exp"),
    SWITCH_BLOCK("SwitchBlock", "searched"),
    TEXT_BLOCK("TextBlock", "text"),
    TRANSFORM_BLOCK("TransformBlock", "transformExpression"),
    TRIM_INSTRUCTION("TrimInstruction"),
    UNIFIED_CALL("UnifiedCall", "nameExp"),
    VISIT_NODE("VisitNode", "targetNode", "namespaces"),

    GENERIC("");

    private String className;
    private List<String> paramProps;

    private ElementType(String className, String... paramProps) {
        this.className = className;
        this.paramProps = Arrays.asList(paramProps);
    }

    String getClassName() {
        return this.className;
    }

    List<String> getParamProps() {
        return this.paramProps;
    }
}
