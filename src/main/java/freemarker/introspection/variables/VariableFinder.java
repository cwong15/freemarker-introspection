package freemarker.introspection.variables;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import freemarker.core.VarScope;
import freemarker.introspection.Element;
import freemarker.introspection.ElementType;
import freemarker.introspection.ElementVisitor;
import freemarker.introspection.Expr;
import freemarker.introspection.ExprType;
import freemarker.introspection.ExprVisitor;
import freemarker.introspection.LiteralExpr;
import freemarker.introspection.StringExpr;
import freemarker.introspection.TemplateNode;
import freemarker.introspection.ValueExpr;

/**
 * This class provides the ability to scan a Freemarker template for variables
 * used. This helps you to figure out what variables it will request from the
 * model during processing. In addition to the variable name, this will also 
 * try to discern the scalar data type of the variable based on its context. 
 * 
 * <p>Sample usage:
 * <pre>
 * Element root = TemplateIntrospector.getRootNode(template);
 * List&lt;VariableInfo&gt; vars = new VariableFinder(root).seek().getVariableInfo();
 * </pre>
 */
public class VariableFinder implements ElementVisitor, ExprVisitor {
    private Element root;

    // stack of local scopes. A scope is a set of assigned variables (loop, local) 
    // local to current local scope. These shadow other variables in the local scope
    private Deque<Set<String>> scopeStack;
    // assigned variables. These shadow other variables.
    private Set<String> shadowedVariables;
    // variable name vs var info
    private Map<String, VariableInfo> vars;
    // tracks current/enclosing inferred type
    private Deque<VariableType> typeStack;

    public VariableFinder(Element root) {
        this.root = root;
        scopeStack = new LinkedList<Set<String>>();
        shadowedVariables = new HashSet<String>();
        vars = new LinkedHashMap<String, VariableInfo>();
        typeStack = new LinkedList<VariableType>();
    }

    /**
     * Scan the tree for variables. This should only be called once. 
     */
    public VariableFinder seek() {
        root.accept(this);
        return this;
    }

    /**
     * Returns the variables found with seek(). You must seek() first before
     * getting meaningful results from this method. 
     *  
     * @return set of variable names. 
     */
    public Set<String> getVariables() {
        return vars.keySet();
    }

    /**
     * Returns the variable info found with seek(). You must seek() first before
     * getting meaningful results from this method. 
     *  
     * @return list of variable info objects. 
     */
    public List<VariableInfo> getVariableInfo() {
        return new ArrayList<VariableInfo>(vars.values());
    }

    public boolean visit(Element element) {
        switch (element.getType()) {
            case ASSIGNMENT:
            case BLOCK_ASSIGNMENT:
                return processAssignment(element);
            case INCLUDE:
                // TODO: optionally chase includes
                break;
            case CONDITIONAL_BLOCK:
                typeStack.addFirst(VariableType.BOOLEAN);
                scanParams(element);
                typeStack.removeFirst();
                break;
            case ITERATOR_BLOCK:
                scanParams(element);
                processNewScope(element, element.getParams().get(1));
                return false;
            case MACRO:
                Expr[] macroArgs = new Expr[element.getParams().size() - 1];
                for (int i = 0; i < macroArgs.length; i++) {
                    macroArgs[i] = element.getParams().get(i + 1);
                }
                processNewScope(element, macroArgs);
                return false;
            case UNIFIED_CALL:
                scanUnifiedCallParams(element);
                break;
            default:
                scanParams(element);
                break;
        }
        return true;
    }

    /**
     * Processes a variable assignment element. This records the variable name
     * in the current or namespace scope so we can tell if a variable that
     * we might encounter later is shadowed by this assigned variable.  
     */
    private boolean processAssignment(Element element) {
        boolean recurse = true;
        if (element.getType() == ElementType.BLOCK_ASSIGNMENT) {
            // block assignment: visit children first, then process var
            for (Element child : element.getChildren()) {
                child.accept(this);
            }
            recurse = false;
        }

        // visit all expressions apart from the assigned var name (first param).
        List<Expr> params = element.getParams();
        for (int i = 1; i < params.size(); i++) {
            params.get(i).accept(this);
        }

        int scopeIdx = element.getType() == ElementType.BLOCK_ASSIGNMENT ? 1 : 2;
        int scope = (Integer) ((ValueExpr) element.getParams().get(scopeIdx)).getValue();
        String varName = params.get(0).toString();

        switch (scope) {
            case VarScope.GLOBAL:
                // can ignore, because global vars don't shadow namespace/model
                // variables
                break;
            case VarScope.LOCAL:
                scopeStack.peekFirst().add(varName);
                break;
            case VarScope.NAMESPACE:
                shadowedVariables.add(varName);
                break;
        }

        return recurse;
    }

    /**
     * Processes the element's children in a new, inherited scope. A macro or
     * iteration block introduces a new scope where local variables may be 
     * introduced. 
     * 
     * @param element macro or iteration element to be processed
     * @param newShadowedVars array of variable Exprs that need to be introduced
     * into this scope. These might be parameter names or iteration variables.
     */
    private void processNewScope(Element element, Expr... newShadowedVars) {
        Set<String> newScope = new HashSet<String>();
        for (Expr e : newShadowedVars) {
            ValueExpr val = (ValueExpr) e;
            if (val.getValue() != null) {
                newScope.add((String) val.getValue());
            }
        }

        // push a new variable scope and type context before visiting the 
        // element's children. Pop them once we're done. 
        scopeStack.addFirst(newScope);
        typeStack.addFirst(VariableType.UNKNOWN);
        for (Element child : element.getChildren()) {
            child.accept(this);
        }
        typeStack.removeFirst();
        scopeStack.removeFirst();
    }

    private void scanParams(TemplateNode node) {
        for (Expr e : node.getParams()) {
            if (e != null) {
                e.accept(this);
            }
        }
    }

    private void scanUnifiedCallParams(TemplateNode unifiedCall) {
        List<Expr> params = unifiedCall.getParams();
        // scan the RHS of each param=value pair. First param is call name, so
        // start at 3rd param.
        for (int i = 2; i < params.size(); i += 2) {
            Expr paramVal = params.get(i);
            if (paramVal != null) {
                paramVal.accept(this);
            }
        }
    }

    public void visit(Expr expr) {
        switch (expr.getType()) {
            case DOT:
                processDotExpression(expr);
                break;
            case IDENTIFIER:
                recordVariable(expr, typeStack.isEmpty() ?
                        VariableType.UNKNOWN : typeStack.peekFirst());
                break;
            case ADD_CONCAT:
            case ARITHMETIC:
            case COMPARISON:
                List<Expr> params = expr.getParams();
                scanLeftRightExpr(params.get(0), params.get(1));
                break;
            case DEFAULT_TO:
                processDefaults(expr);
                break;
            case AND:
            case OR:
            case NOT:
                // evaluate subexpressions within a boolean context
                typeStack.addFirst(VariableType.BOOLEAN);
                scanParams(expr);
                typeStack.removeFirst();
                break;
            case STRING_LITERAL:
                StringExpr strExpr = (StringExpr) expr;
                Element dynElement = strExpr.getDynamicValue();
                if (dynElement != null) {
                    dynElement.accept(this);
                }
                break;
            case RANGE:
                typeStack.addFirst(VariableType.NUMBER);
                scanParams(expr);
                typeStack.removeFirst();
                break;
            case EXISTS:
                typeStack.addFirst(VariableType.UNKNOWN);
                scanParams(expr);
                typeStack.removeFirst();
                break;
            case BUILTIN:
                typeStack.addFirst(BuiltinTypeCategorizer.inferType(expr));
                scanParams(expr);
                typeStack.removeFirst();
                break;
            default:
                scanParams(expr);
        }
    }

    private void processDotExpression(Expr dotExpr) {
        // only record dot expressions consisting of identifiers, like a.b.c. 
        // Stuff like {'a':1}.a isn't really a variable and won't be recorded.
        Expr target = dotExpr.getParams().get(0);
        while (target.getType() == ExprType.DOT) {
            target = target.getParams().get(0);
        }
        if (target.getType() == ExprType.IDENTIFIER &&
                !shadowedVariables.contains(target.toString())) {
            recordVariable(dotExpr, typeStack.isEmpty() ?
                    VariableType.UNKNOWN : typeStack.peekFirst());
        } else {
            // target is not an identifier. Scan it further for variables. It
            // could be something like datevar?string.short, so we don't want to
            // skip the "datevar?string" part. 
            target.accept(this);
        }
    }

    /**
     * Record an Expr object of type DEFAULT_TO. If it's LHS param is a variable,
     * we'll add the default value (if a literal) to that variable's values.
     *  
     * @param defExpr Expr object of type DEFAULT_TO
     */
    private void processDefaults(Expr defExpr) {
        List<Expr> params = defExpr.getParams();
        VariableInfo vi = scanLeftRightExpr(params.get(0), params.get(1));
        if (isVariable(params.get(0)) && vi != null) {
            Expr rhs = params.get(1);
            if (rhs instanceof LiteralExpr) {
                vi.getValues().add(((LiteralExpr) rhs).getValue());
            }
        }
    }

    // handles binary expressions like a+b, a-b, a<b, a!b. If one operand is a 
    // variable, we can infer the type from the other.
    private VariableInfo scanLeftRightExpr(Expr left, Expr right) {
        if (isVariable(left) && !isVariable(right)) {
            // var * expr
            return recordVariable(left, inferType(right));
        } else if (!isVariable(left) && isVariable(right)) {
            // expr * var
            return recordVariable(right, inferType(left));
        } else if (isVariable(left) && isVariable(right)) {
            // var * var
            recordVariable(left, VariableType.UNKNOWN);
            recordVariable(right, VariableType.UNKNOWN);
            return null;
        } else {
            // expr * expr. No immediate variables. Continue scanning operands.
            VariableType vt = inferType(left);
            vt = vt == VariableType.UNKNOWN ? inferType(right) : vt;
            typeStack.addFirst(vt);
            left.accept(this);
            right.accept(this);
            typeStack.removeFirst();
            return null;
        }
    }

    private VariableType inferType(Expr expr) {
        switch (expr.getType()) {
            case AND:
            case OR:
            case NOT:
            case BOOLEAN_LITERAL:
            case EXISTS:
                return VariableType.BOOLEAN;
            case STRING_LITERAL:
                return VariableType.STRING;
            case NUMBER_LITERAL:
                return VariableType.NUMBER;
            case PARENTHETICAL:
                return inferType(expr.getParams().get(0));
            default:
                return VariableType.UNKNOWN;
        }
    }

    private boolean isVariable(Expr expr) {
        return expr.getType() == ExprType.IDENTIFIER ||
                expr.getType() == ExprType.DOT;
    }

    private VariableInfo recordVariable(Expr variable, VariableType type) {
        // check the namespace variable scope and then walk the scope chain to
        // see if this variable is shadowed.
        String variableName = variable.toString();
        boolean shadowed = shadowedVariables.contains(variableName);
        for (Set<String> scope : scopeStack) {
            shadowed = shadowed || scope.contains(variableName);
        }

        if (!shadowed) {
            VariableInfo vi = vars.get(variableName);
            if (vi == null) {
                vi = new VariableInfo();
                vi.setName(variableName);
                vi.setType(type);
                vars.put(variableName, vi);
            } else if (vi.getType() == VariableType.UNKNOWN) {
                vi.setType(type);
            }
            vi.getVariables().add(variable);
            return vi;
        } else {
            return null;
        }
    }
}
