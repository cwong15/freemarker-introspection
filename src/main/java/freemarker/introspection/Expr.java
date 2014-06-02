package freemarker.introspection;

import java.util.List;

public interface Expr {
    ExprType getType();

    List<Expr> getParams();

    int getBeginColumn();

    int getBeginLine();

    int getEndColumn();

    int getEndLine();
}
