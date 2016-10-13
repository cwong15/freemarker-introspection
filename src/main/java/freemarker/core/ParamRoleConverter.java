package freemarker.core;

import java.util.HashMap;
import java.util.Map;

import freemarker.introspection.ParamRole;

/**
 * Utility class to convert from Freemarker's ParameterRole to our ParamRole enum.
 * Because ParameterRole is using old style static/final constants instead of an
 * enum, we cannot simply use Enum.valueOf. 
 */
public class ParamRoleConverter {
    private static Map<String, ParamRole> roleMap = null;

    static public ParamRole toRole(ParameterRole pr) {
        if (roleMap == null) {
            buildMap();
        }
        ParamRole role = roleMap.get(pr.getName());
        return role == null ? ParamRole.UNKNOWN : role;
    }

    /**
     * Builds a map of ParameterRole.name vs ParamRole.
     */
    private static void buildMap() {
        // for compatibility with FM 2.3.20, we will avoid referring to ASSIGNMENT_OPERATOR,
        // IGNORE_MISSING_PARAMETER and VALUE_PART, which are not present in that version. 
        // We'll hard-code the names from 2.3.25-incubating instead. This way, this will 
        // compile under 2.3.20.

        Map<String, ParamRole> rm = new HashMap<String, ParamRole>();
        rm.put(ParameterRole.LEFT_HAND_OPERAND.getName(), ParamRole.LEFT_HAND_OPERAND);
        rm.put(ParameterRole.RIGHT_HAND_OPERAND.getName(), ParamRole.RIGHT_HAND_OPERAND);
        rm.put(ParameterRole.ENCLOSED_OPERAND.getName(), ParamRole.ENCLOSED_OPERAND);
        rm.put(ParameterRole.ITEM_VALUE.getName(), ParamRole.ITEM_VALUE);
        rm.put(ParameterRole.ITEM_KEY.getName(), ParamRole.ITEM_KEY);
        rm.put(ParameterRole.ASSIGNMENT_TARGET.getName(), ParamRole.ASSIGNMENT_TARGET);
        // rm.put(ParameterRole.ASSIGNMENT_OPERATOR.getName(), ParamRole.ASSIGNMENT_OPERATOR);
        rm.put("assignment operator", ParamRole.ASSIGNMENT_OPERATOR);
        rm.put(ParameterRole.ASSIGNMENT_SOURCE.getName(), ParamRole.ASSIGNMENT_SOURCE);
        rm.put(ParameterRole.VARIABLE_SCOPE.getName(), ParamRole.VARIABLE_SCOPE);
        rm.put(ParameterRole.NAMESPACE.getName(), ParamRole.NAMESPACE);
        rm.put(ParameterRole.ERROR_HANDLER.getName(), ParamRole.ERROR_HANDLER);
        rm.put(ParameterRole.PASSED_VALUE.getName(), ParamRole.PASSED_VALUE);
        rm.put(ParameterRole.CONDITION.getName(), ParamRole.CONDITION);
        rm.put(ParameterRole.VALUE.getName(), ParamRole.VALUE);
        rm.put(ParameterRole.AST_NODE_SUBTYPE.getName(), ParamRole.AST_NODE_SUBTYPE);
        rm.put(ParameterRole.PLACEHOLDER_VARIABLE.getName(), ParamRole.PLACEHOLDER_VARIABLE);
        rm.put(ParameterRole.EXPRESSION_TEMPLATE.getName(), ParamRole.EXPRESSION_TEMPLATE);
        rm.put(ParameterRole.LIST_SOURCE.getName(), ParamRole.LIST_SOURCE);
        rm.put(ParameterRole.TARGET_LOOP_VARIABLE.getName(), ParamRole.TARGET_LOOP_VARIABLE);
        rm.put(ParameterRole.TEMPLATE_NAME.getName(), ParamRole.TEMPLATE_NAME);
        rm.put(ParameterRole.PARSE_PARAMETER.getName(), ParamRole.PARSE_PARAMETER);
        rm.put(ParameterRole.ENCODING_PARAMETER.getName(), ParamRole.ENCODING_PARAMETER);
        // rm.put(ParameterRole.IGNORE_MISSING_PARAMETER.getName(), ParamRole.IGNORE_MISSING_PARAMETER);
        rm.put("\"ignore_missing\" parameter", ParamRole.IGNORE_MISSING_PARAMETER);
        rm.put(ParameterRole.PARAMETER_NAME.getName(), ParamRole.PARAMETER_NAME);
        rm.put(ParameterRole.PARAMETER_DEFAULT.getName(), ParamRole.PARAMETER_DEFAULT);
        rm.put(ParameterRole.CATCH_ALL_PARAMETER_NAME.getName(), ParamRole.CATCH_ALL_PARAMETER_NAME);
        rm.put(ParameterRole.ARGUMENT_NAME.getName(), ParamRole.ARGUMENT_NAME);
        rm.put(ParameterRole.ARGUMENT_VALUE.getName(), ParamRole.ARGUMENT_VALUE);
        rm.put(ParameterRole.CONTENT.getName(), ParamRole.CONTENT);
        rm.put(ParameterRole.EMBEDDED_TEMPLATE.getName(), ParamRole.EMBEDDED_TEMPLATE);
        // rm.put(ParameterRole.VALUE_PART.getName(), ParamRole.VALUE_PART);
        rm.put("value part", ParamRole.VALUE_PART);
        rm.put(ParameterRole.MINIMUM_DECIMALS.getName(), ParamRole.MINIMUM_DECIMALS);
        rm.put(ParameterRole.MAXIMUM_DECIMALS.getName(), ParamRole.MAXIMUM_DECIMALS);
        rm.put(ParameterRole.NODE.getName(), ParamRole.NODE);
        rm.put(ParameterRole.CALLEE.getName(), ParamRole.CALLEE);
        rm.put(ParameterRole.MESSAGE.getName(), ParamRole.MESSAGE);

        roleMap = rm;
    }
}
