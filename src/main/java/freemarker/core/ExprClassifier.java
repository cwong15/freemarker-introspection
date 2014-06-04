package freemarker.core;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import freemarker.introspection.ExprType;

public class ExprClassifier {
    private static Map<String, ExprType> exprMap = null;

    public static ExprType getType(Expression exprObj) {
        if (exprMap == null) {
            buildMap();
        }
        if (exprObj instanceof BuiltIn) {
            return ExprType.BUILTIN;
        } else {
            ExprType type = exprMap.get(exprObj.getClass().getSimpleName());
            type = type == null ? ExprType.GENERIC : type;
            return type;
        }
    }

    private static void buildMap() {
        Map<String, ExprType> map = new HashMap<String, ExprType>();
        for (ExprType t : EnumSet.allOf(ExprType.class)) {
            if (t != ExprType.GENERIC) {
                map.put(t.getClassName(), t);
            }
        }
        exprMap = map;
    }
}
