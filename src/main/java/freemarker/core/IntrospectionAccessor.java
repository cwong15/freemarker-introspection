package freemarker.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Helper class to provide access to package-level fields or methods for classes
 * outside of freemarker.core.  
 */
public class IntrospectionAccessor {
    public static String getStringLiteralValue(Expression expr) {
        return ((StringLiteral) expr).getAsString();
    }

    public static Number getNumberLiteralValue(Expression expr) {
        return ((NumberLiteral) expr).getAsNumber();
    }

    public static List<Param> getParams(TemplateObject obj) {
        if (obj.getParameterCount() == 0) {
            return Collections.emptyList();
        }

        List<Param> params = new ArrayList<Param>(obj.getParameterCount());
        for (int i = 0; i < obj.getParameterCount(); i++) {
            params.add(new Param(obj.getParameterRole(i), obj.getParameterValue(i)));
        }
        return params;
    }
}
