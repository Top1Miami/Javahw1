package expression;

import java.util.HashMap;
import java.util.Objects;

public class Negation implements Expression{

    private Boolean evaluation = null;
    private Expression negated;
    private Integer hash = null;
    public Integer hashCodeX() {
        if(hash != null) {
            return hash;
        }
        hash = Objects.hash("!", negated.hashCodeX().toString());
        return hash;
    }

    @Override
    public Boolean canBeProved(HashMap<String, Boolean> variable) {
        evaluation = !negated.canBeProved(variable);
        return evaluation;
    }

    public Negation(Expression negated) {
        this.negated = negated;
    }

    public Expression getNegated() {
        return negated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Negation negation = (Negation) o;
        return Objects.equals(negated, negation.negated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(negated);
    }

    @Override
    public boolean recursivlyCompare(Expression expr, HashMap<String, Integer> varM) {
        if(expr.getType() != "!") {
            return false;
        }
        return negated.recursivlyCompare(expr.getLeft(), varM);
    }

    @Override
    public String getType() {
        return "!";
    }

    @Override
    public Expression getLeft() {
        return this.negated;
    }

    @Override
    public Expression getRight() {
        return null;
    }

    @Override
    public String toTree() {
        return "(!" + negated.toTree() + ")";
    }

    @Override
    public String toString() {
        return "!" + negated.toString();
    }
}