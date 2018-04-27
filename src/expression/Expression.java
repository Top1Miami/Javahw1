package expression;

import java.util.HashMap;

public interface Expression {
    String toTree();
    public int hashCode();
    public boolean recursivlyCompare(Expression expr, HashMap<String, Integer> varM);
    public String getType();
    public Expression getLeft();
    public Expression getRight();
    public Integer hashCodeX();
}