package formulaIIV;
import expression.*;

import java.util.HashSet;
import java.util.Set;

public class OneTree {
    int number = -1;
    HashSet<Expression> treeforce = new HashSet<>();
    Set<OneTree> children = new HashSet<>();
    OneTree() {}
    OneTree(int number) {
        this.number = number;
    }
}
