package freemarker.core;

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

    public static boolean getBooleanLiteralValue(Expression expr) {
        return ((BooleanLiteral) expr).evalToBoolean(null);
    }
}
