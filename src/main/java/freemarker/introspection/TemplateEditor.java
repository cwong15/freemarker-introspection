package freemarker.introspection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * This class provides the ability to modify an existing Freemarker template. 
 * Given an existing template's text and one or more Element or Expr nodes to
 * replace, this class will return the modified template's text. Only non-VALUE
 * Expr nodes can be replaced.
 * 
 *  <p>Sample usage:
 *  <pre>
 * Element root = TemplateIntrospector.getRootNode(template);
 * Element elem = ... // find element node in tree to replace
 * Expr expr = ... // find expression node in tree to replace
 * String newTemplate = new TemplateEditor(templateText)
 *                          .replace(elem, "<#include 'something_else.ftl'>")
 *                          .replace(expr, "123")
 *                          .apply()
 *                          .getModifiedTemplate();
 *  </pre>
 */
public class TemplateEditor {
    private String templateText;
    private List<Substitution> substitutions;
    private List<Integer> lines; // location of each line in templateText
    private String newTemplateText;

    public TemplateEditor(String templateText) {
        this.templateText = templateText;
        this.substitutions = new ArrayList<TemplateEditor.Substitution>();
    }

    /**
     * Specifies a template node substitution. Substitutions must be specified 
     * with this method before calling apply().
     * 
     * @param node an Element or Expr from the parse tree.
     * @param replacementText the content to replace the node with. The 
     * replacement must be in string form, even if it is a Freemarker element
     * or expression.
     */
    public TemplateEditor replace(TemplateNode node, String replacementText) {
        if (node instanceof Expr && ((Expr) node).getType() == ExprType.VALUE) {
            throw new UnsupportedOperationException("VALUE expression replacement not supported");
        }
        substitutions.add(new Substitution(node, replacementText));
        return this;
    }

    /**
     * Carry out template replacements specified earlier by your calls to replace(). 
     */
    public TemplateEditor apply() {
        sortSubstitutions();
        checkForOverlaps();
        indexLines();
        makeSubstitutions();
        return this;
    }

    /**
     * Returns the modified template. You must call apply() first to get a
     * meaningful result.
     * 
     * @return modified template.
     */
    public String getModifiedTemplate() {
        return newTemplateText;
    }

    private void sortSubstitutions() {
        // sort substitutions in reverse physical order. We will do replacements
        // from the end back so the location numbers don't get messed up by 
        // textual insertions/deletions.
        Collections.sort(substitutions, new Comparator<Substitution>() {
            public int compare(Substitution o1, Substitution o2) {
                TemplateNode n1 = o1.node;
                TemplateNode n2 = o2.node;
                if (n1.getBeginLine() != n2.getBeginLine()) {
                    return n2.getBeginLine() - n1.getBeginLine();
                } else {
                    return n2.getBeginColumn() - n1.getBeginColumn();
                }
            }
        });
    }

    // checks if any of the nodes being replaced overlap. This assumes the 
    // substitutions list has already been sorted.
    private void checkForOverlaps() {
        for (int i = 0; i < substitutions.size() - 1; i++) {
            Substitution prev = substitutions.get(i + 1);
            Substitution next = substitutions.get(i);
            if (prev.node.getEndLine() > next.node.getBeginLine()) {
                // lines overlap
                throw new IllegalArgumentException(String.format(
                        "Substitutions overlap on lines %d - %d",
                        next.node.getBeginLine(), prev.node.getEndLine()));
            } else if (prev.node.getEndLine() == next.node.getBeginLine()) {
                // same line. Check that the columns don't overlap
                if (prev.node.getEndColumn() >= next.node.getBeginColumn()) {
                    throw new IllegalArgumentException(String.format(
                            "Substitutions overlap on line %s col %d",
                            prev.node.getEndLine(), next.node.getBeginColumn()));
                }
            }
        }
    }

    // build an index of line# vs its location in the string. This will make it
    // easier to carry out node replacements using their line/col location info.
    private void indexLines() {
        int lastLine = 0;
        if (!substitutions.isEmpty()) {
            lastLine = substitutions.get(0).node.getEndLine();
        }

        lines = new ArrayList<Integer>();
        String newlineChars = "\n\r";
        int loc = 0;
        lines.add(0); // first line location
        while (lines.size() < lastLine && loc < templateText.length()) {
            // scan to next line

            // first, find next eol char
            char curChar = templateText.charAt(loc);
            while (loc < templateText.length() && newlineChars.indexOf(curChar) < 0) {
                loc++;
                if (loc < templateText.length()) {
                    curChar = templateText.charAt(loc);
                }
            }

            // next, skip over to start of line. Skip 2 chars if we are at a \n\r pair.
            if (loc < (templateText.length() - 1) && curChar == '\r' &&
                    templateText.charAt(loc + 1) == '\n') {
                loc += 2;
            } else if (loc < templateText.length()) {
                loc++;
            }

            // record line location
            if (loc < templateText.length()) {
                lines.add(loc);
            }
        }
    }

    private void makeSubstitutions() {
        StringBuilder text = new StringBuilder(templateText);
        for (Substitution s : substitutions) {
            makeSubstitution(s, text);
        }
        newTemplateText = text.toString();
    }

    private void makeSubstitution(Substitution sub, StringBuilder text) {
        int startLoc = getLoc(sub.node.getBeginLine(), sub.node.getBeginColumn());
        int endLoc = getLoc(sub.node.getEndLine(), sub.node.getEndColumn());
        text.replace(startLoc, endLoc + 1, sub.replacementText);
    }

    private int getLoc(int line, int col) {
        int curLoc = lines.get(line - 1);
        // scan current line, counting col#, treating tabs as 8 char stops
        for (int curCol = 1; curCol < col; curCol++) {
            if (templateText.charAt(curLoc) == '\t') {
                // encountered tab character. Bump col counter to next tab stop
                while (curCol < 8 && curCol % 8 != 0) {
                    curCol++;
                }
            }
            curLoc++;
        }
        return curLoc;
    }

    private static class Substitution
    {
        private TemplateNode node;
        private String replacementText;

        Substitution(TemplateNode node, String replacementText) {
            this.node = node;
            this.replacementText = replacementText;
        }

        public boolean equals(Object otherObj) {
            Substitution other = (Substitution) otherObj;
            return node.getBeginLine() == other.node.getBeginLine() &&
                    node.getBeginColumn() == other.node.getBeginColumn();
        }
    }
}
