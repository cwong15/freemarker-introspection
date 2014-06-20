package freemarker.introspection;

/**
 * Represents a special case of an Expr: a string literal. Because a string 
 * literal can contain ${} or #{} dynamic expressions, this interface provides
 * methods to expose these values.  
 */
public interface StringExpr extends Expr {
    /**
     * Returns the raw string expression.
     */
    String getValue();

    /**
     * Returns the element representing the string's dynamic value, if any. 
     * Otherwise, returns null; 
     */
    Element getDynamicValue();
}
