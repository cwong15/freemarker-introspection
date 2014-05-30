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
            { "AssignmentInstruction", ElementType.ASSIGNMENT_INSTRUCTION, null, l() },
            { "AttemptBlock", ElementType.ATTEMPT_BLOCK, null, l() },
            { "BlockAssignment", ElementType.BLOCK_ASSIGNMENT, null, l() },
            { "BreakInstruction", ElementType.BREAK_INSTRUCTION, null, l() },
            { "Case", ElementType.CASE, null, l() },
            { "CompressedBlock", ElementType.COMPRESSED_BLOCK, null, l() },
            { "ConditionalBlock", ElementType.CONDITIONAL_BLOCK, null, l() },
            { "DebugBreak", ElementType.DEBUG_BREAK, null, l() },
            { "DollarVariable", ElementType.DOLLAR_VARIABLE, null, l() },
            { "EscapeBlock", ElementType.ESCAPE_BLOCK, null, l() },
            { "FallbackInstruction", ElementType.FALLBACK_INSTRUCTION, null, l() },
            { "FlushInstruction", ElementType.COMMENT, null, l() },
            { "IfBlock", ElementType.IFBLOCK, null, l() },
            { "Include", ElementType.INCLUDE, null, l() },
            { "IteratorBlock", ElementType.ITERATOR_BLOCK, null, l() },
            { "LibraryLoad", ElementType.LIBRARY_LOAD, null, l() },
            { "Macro", ElementType.MACRO, null, l() },
            { "MixedContent", ElementType.MIXED_CONTENT, null, l() },
            { "NoEscapeBlock", ElementType.NO_ESCAPE_BLOCK, null, l() },
            { "NumericOutput", ElementType.NUMERIC_OUTPUT, null, l() },
            { "PropertySetting", ElementType.PROPERTY_SETTING, null, l() },
            { "RecoveryBlock", ElementType.RECOVERY_BLOCK, null, l() },
            { "RecurseNode", ElementType.RECURSE_NODE, null, l() },
            { "ReturnInstruction", ElementType.RETURN_INSTRUCTION, null, l() },
            { "StopInstruction", ElementType.STOP_INSTRUCTION, null, l() },
            { "SwitchBlock", ElementType.SWITCH_BLOCK, null, l() },
            { "TextBlock", ElementType.TEXT_BLOCK, null, l() },
            { "TransformBLock", ElementType.TRANSFORM_BLOCK, null, l() },
            { "TrimInstruction", ElementType.TRIM_INSTRUCTION, null, l() },
            { "UnifiedCall", ElementType.UNIFIED_CALL, null, l() },
            { "VisitNode", ElementType.VISIT_NODE, null, l() },
    };

    private static List<String> props(String... values) {
        return Arrays.asList(values);
    }

    private static List<String> l() {
        return Collections.emptyList();
    }
}
