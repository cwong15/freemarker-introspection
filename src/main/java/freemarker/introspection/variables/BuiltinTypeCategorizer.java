package freemarker.introspection.variables;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import freemarker.introspection.Expr;

/**
 * Figures out the type of the builtin's target expression based on the builtin
 * name.
 */
class BuiltinTypeCategorizer {
    static VariableType inferType(Expr builtin) {
        String builtinKey = builtin.getParams().get(1).toString();
        if (STRING_BUILTINS.contains(builtinKey)) {
            return VariableType.STRING;
        } else if (NUMBER_BUILTINS.contains(builtinKey)) {
            return VariableType.NUMBER;
        } else if (isDate(builtinKey)) {
            return VariableType.DATE;
        } else {
            return VariableType.UNKNOWN;
        }
    }

    static boolean isDate(String key) {
        return key.startsWith("iso");
    }

    private static final Set<String> NUMBER_BUILTINS = new HashSet<String>(
            Arrays.asList("abs", "is_infinite", "is_nan", "round", "floor", "ceiling"));

    private static final Set<String> STRING_BUILTINS = new HashSet<String>(
            Arrays.asList("substring", "cap_first", "uncap_first", "capitalize",
                    "chop_linebreak", "ends_with", "html", "groups", "index_of",
                    "j_string", "js_string", "json_string", "last_index_of",
                    "length", "lower_case", "left_pad", "right_pad", "contains",
                    "matches", "number", "replace", "rtf", "url", "split",
                    "starts_with", "trim", "upper_case", "word_list", "xhtml",
                    "xml"));
}
