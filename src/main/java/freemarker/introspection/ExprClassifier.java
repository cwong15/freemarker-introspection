package freemarker.introspection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import freemarker.core.Expression;

class ExprClassifier {
    private static Map<String, ExprType> exprMap = null;

    static ExprType getType(Expression exprObj) {
        if (exprMap == null) {
            buildMap();
        }
        ExprType type = exprMap.get(exprObj.getClass().getSimpleName());
        type = type == null ? ExprType.GENERIC : type;
        return type;
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
