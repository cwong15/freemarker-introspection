package freemarker.introspection;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.core.TemplateElement;

class ElementClassifier {
    private static Map<String, ElementInfo> elementMap = null;
    private static ElementInfo defaultInfo = null;

    static ElementInfo getInfo(TemplateElement node) {
        if (elementMap == null) {
            buildMap();
        }
        ElementInfo info = elementMap.get(node.getClass().getSimpleName());
        info = info == null ? defaultInfo : info;
        return info;
    }

    @SuppressWarnings("unchecked")
    private static void buildMap() {
        Map<String, ElementInfo> map = new HashMap<String, ElementInfo>();
        for (int i = 0; i < ELEMENT_INFO.length; i++) {
            Object[] infoArray = ELEMENT_INFO[i];
            ElementInfo info = new ElementInfo();
            info.setType((ElementType) infoArray[1]);
            info.setNameProp((String) infoArray[2]);
            info.setExprProps((List<String>) infoArray[3]);
            map.put((String) infoArray[0], info);
        }
        elementMap = map;

        defaultInfo = new ElementInfo();
        defaultInfo.setType(ElementType.GENERIC);
    }

    static Object[][] ELEMENT_INFO = {
            { "Assignment", ElementType.ASSIGNMENT, "variableName",
                    props("value", "namespaceExp") },
            { "AssignmentInstruction", ElementType.ASSIGNMENT_INSTRUCTION, null,
                    props("namespaceExpr") },
            { "AttemptBlock", ElementType.ATTEMPT_BLOCK, null, l() },
            { "BlockAssignment", ElementType.BLOCK_ASSIGNMENT, "varName",
                    props("namespaceExpr") },
            { "BreakInstruction", ElementType.BREAK_INSTRUCTION, null, l() },
            { "Case", ElementType.CASE, null, props("condition") },
            { "CompressedBlock", ElementType.COMPRESSED_BLOCK, null, l() },
            { "ConditionalBlock", ElementType.CONDITIONAL_BLOCK, null, props("condition") },
            { "DebugBreak", ElementType.DEBUG_BREAK, null, l() },
            { "DollarVariable", ElementType.DOLLAR_VARIABLE, null, props("expression") },
            { "EscapeBlock", ElementType.ESCAPE_BLOCK, "variable", props("expr") },
            { "FallbackInstruction", ElementType.FALLBACK_INSTRUCTION, null, l() },
            { "FlushInstruction", ElementType.COMMENT, null, l() },
            { "IfBlock", ElementType.IFBLOCK, null, l() },
            { "Include", ElementType.INCLUDE, null,
                    props("templateName", "encodingExpr", "parseExp") },
            { "IteratorBlock", ElementType.ITERATOR_BLOCK, "loopVariableName",
                    props("listSource") },
            { "LibraryLoad", ElementType.LIBRARY_LOAD, "namespace",
                    props("templateName") },
            { "Macro", ElementType.MACRO, "name", l() },
            { "MixedContent", ElementType.MIXED_CONTENT, null, l() },
            { "NoEscapeBlock", ElementType.NO_ESCAPE_BLOCK, null, l() },
            { "NumericalOutput", ElementType.NUMERICAL_OUTPUT, null, props("expression") },
            { "PropertySetting", ElementType.PROPERTY_SETTING, "key", props("value") },
            { "RecoveryBlock", ElementType.RECOVERY_BLOCK, null, l() },
            { "RecurseNode", ElementType.RECURSE_NODE, null,
                    props("targetNode", "namespaces") },
            { "ReturnInstruction", ElementType.RETURN_INSTRUCTION, null, props("exp") },
            { "StopInstruction", ElementType.STOP_INSTRUCTION, null, props("exp") },
            { "SwitchBlock", ElementType.SWITCH_BLOCK, null, props("searched") },
            { "TextBlock", ElementType.TEXT_BLOCK, null, l() },
            { "TransformBLock", ElementType.TRANSFORM_BLOCK, null, props("transformExpression") },
            { "TrimInstruction", ElementType.TRIM_INSTRUCTION, null, l() },
            { "UnifiedCall", ElementType.UNIFIED_CALL, null, props("nameExp") },
            { "VisitNode", ElementType.VISIT_NODE, null, l() },
    };

    private static List<String> props(String... values) {
        return Arrays.asList(values);
    }

    private static List<String> l() {
        return Collections.emptyList();
    }
}
