package freemarker.introspection;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import freemarker.core.TemplateElement;

class ElementClassifier {
    private static Map<String, ElementType> elementMap = null;

    static ElementType getType(TemplateElement node) {
        if (elementMap == null) {
            buildMap();
        }
        ElementType type = elementMap.get(node.getClass().getSimpleName());
        type = type == null ? ElementType.GENERIC : type;
        return type;
    }

    private static void buildMap() {
        Map<String, ElementType> map = new HashMap<String, ElementType>();
        for (ElementType t : EnumSet.allOf(ElementType.class)) {
            if (t != ElementType.GENERIC) {
                map.put(t.getClassName(), t);
            }
        }
        elementMap = map;
    }
}
