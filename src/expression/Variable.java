package expression;

import java.util.HashMap;
import java.util.Objects;

public class Variable implements Expression {

    private String name;
    private Integer hash = null;
    private Boolean evaluation = null;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String toTree() {
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return Objects.equals(name, variable.name);
    }

    public Integer hashCodeX() {
        if (hash != null) {
            return hash;
        }
        hash = Objects.hash("var", name);
        return hash;
    }

    @Override
    public Boolean canBeProved(HashMap<String, Boolean> variable) {
        evaluation = variable.get(name);
        return evaluation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public boolean recursivlyCompare(Expression expr, HashMap<String, Integer> varM) {
        if (varM.get(name) == null) {
            varM.put(name, expr.hashCodeX());
            return true;
        } else if (varM.get(name).equals(expr.hashCodeX())) {
            return true;
        }
        return false;
    }

    @Override
    public String getType() {
        return "val";
    }

    @Override
    public Expression getLeft() {
        return null;
    }

    @Override
    public Expression getRight() {
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}