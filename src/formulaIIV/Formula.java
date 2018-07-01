package formulaIIV;
import expression.*;
import parser.ExpressionParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Formula {

    private List<OneTree> wood = new ArrayList<>();
    private List<ArrayList<OneTree>> wood_in_lists = new ArrayList<>();
    private List<String> vars = new ArrayList<>();
    /*
    ``````p¶1
```````¶¶p
```````¶o¶¶1
1¶o````p¶SS¶¶
`1¶p¶¶¶¶¶SSS¶¶
```¶¶SS¶¶¶¶Sp¶¶o
`````¶¶S¶@SSSSS¶¶1
```1¶¶SSSSSbSSSS¶
•¶¶SSSSS¶SSSSSS¶p
`\¶b¶b¶¶¶¶bSSS¶¶1
``````````¶bbSSSb¶p­
``````````p¶¶bbbbb¶­¶¶o
``````````¶¶o¶bbb¶b­SSp¶¶¶¶¶1
``````````¶¶obb¶¶SS­bbbSppoop¶¶1
``````````o¶oo¶¶bbb­S¶¶bSSSooo1¶¶1
```````````¶¶1o¶¶Sb­bS¶¶SbSppoooo¶¶
````````````¶¶11b¶S­bbS¶¶bbbSSpoo1¶¶¶
`````````````¶¶po¶¶­SSSb¶bbbbSSppooo¶¶¶
``````````````1¶¶o¶­bopp¶¶SbbbbSSpooo1o¶­p
``````````````¶¶¶¶¶­¶ooop¶bb¶¶¶¶SSSpooo1­¶1
``````````¶p`¶b¶¶¶b­ooo¶¶b¶¶bSob¶¶¶SSooo­¶¶``¶¶
`````````pp¶¶¶¶¶¶Sp­¶¶¶¶¶¶¶ooooopp¶¶SSpo­o¶``¶p¶1
``````````¶¶¶Sbbb¶o­ooo¶¶oooooooopo¶¶SSS­o¶¶``¶¶¶1
``````````p¶b¶¶b¶¶o­ooo¶¶poooooopSSS¶¶SS­So¶¶`1¶o¶¶
````````````¶¶¶b¶po­ooo¶¶SSppopopppSb¶¶S­bSb¶o`1¶1p¶1
`````````````¶b¶¶op­¶p1p¶¶SSSSSSSSbbbbbb­bbb¶¶``¶¶1o¶o
``````````````1¶o¶¶­111o¶¶SbSbbbbbbbbbbb­bbS¶¶``1¶p1¶¶
````````````````¶¶p­o¶ooo¶¶bbbbS¶¶Sbbbbb­bbS¶¶``1¶bop¶
``````````````````p­¶¶¶poob¶¶Sbb¶¶¶¶¶bbb­bbbb¶p`p¶bo¶¶
```````````````````­```1p¶¶¶¶¶Sb¶bb¶¶bbb­bbbS¶¶1¶¶bp¶¶
```````````````````­``````¶¶¶¶bS¶¶¶SSbbb­bbbbb¶¶¶¶bS¶p
```````````````````­``````1¶¶¶Sbb¶¶¶¶¶Sb­bbbbbbbbbS¶¶
```````````````````­``````1¶¶¶bbS¶¶``¶¶¶­bSbbbbbbbS¶p
````````p¶¶¶¶¶¶¶¶¶¶­¶¶¶¶¶¶¶¶¶¶bbbb¶¶¶``1­¶¶¶bSbSb¶¶¶
``````1¶¶b¶¶¶bbbbbb­bbbbbbSSbbbbbbb¶¶¶``­``1¶¶¶¶¶1
``````p¶¶¶¶¶¶¶¶¶¶¶¶­¶¶¶¶¶¶¶p11oo1111
    */
/*

_____________________________999______________________________________________999
___________________________999999____________________________________________999999
_________________________999999999_______________9__________9_______________999999999
_______________________999999999999______________99________99______________999999999999
_____________________9999999999999999____________999999999999____________9999999999999999
___________________99999999999999999999_________99999999999999_________99999999999999999999
_________________9999999999999999999999999999999999999999999999999999999999999999999999999999
________________999999999999999999999999999999999999999999999999999999999999999999999999999999
_______________99999999999999999999999999999999999999999999999999999999999999999999999999999999
______________9999999999999999999999999999999999999999999999999999999999999999999999999999999999
_____________999999999999999999999999999999999999999999999999999999999999999999999999999999999999
_____________999999999999999999999999999999999999999999999999999999999999999999999999999999999999
______________9999999999999999999999999999999999999999999999999999999999999999999999999999999999
_______________999999999___999999999___99999999999999999999999999999999___999999999___999999999
________________9999999_____9999999_____999999999999999999999999999999_____9999999_____9999999
_________________99999_______999999_______99999999999999999999999999_______999999_______99999
__________________999_________99999_________9999999999999999999999_________99999_________999
____________________9__________999_____________9999999999999999_____________999__________9
_________________________________9________________9999999999________________9
____________________________________________________999999


*/
    public String ARTEM_VOT_TEBE_MEM() {
        String s = "EHAL GÖDEL CHEREZ GÖDEL\n" +
                   "VIDIT GÖDEL V GÖDEL GÖDEL\n" +
                   "SUNUL GÖDEL GÖDEL V GÖDEL\n" +
                   "GÖDEL GÖDEL GÖDEL GÖDEL";
        return s;
    }


    public void create_trees(int size) {
        StringBuilder s_builder = new StringBuilder();
        for (int i = 0; i < size - 1; i++) {
            s_builder.append("(");
        }
        for (int i = 0; i < size - 1; i++) {
            s_builder.append(")");
        }
        String result = s_builder.toString();
        while (!result.isEmpty()) {
            to_tree(result);
            result = next_parenthesis(result);
        }
    }

    public void to_tree(String input) {
        List<OneTree> new_tree = new ArrayList<>();
        OneTree root = new OneTree();
        int counter = 2;
        root.number = 1;
        new_tree.add(root);
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == ')') {
                new_tree.remove(new_tree.size() - 1);
            } else {
                OneTree new_vertex = new OneTree();
                new_vertex.number = counter++;
                new_tree.get(new_tree.size() - 1).children.add(new_vertex);
                new_tree.add(new_vertex);
            }
        }
        wood.add(root);
    }

    public String next_parenthesis(String input) {
        String result;
        int balance = 0, opened_par = 0;
        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) == '(') {
                opened_par++;
                balance--;
                if (balance > 0) {
                    StringBuilder cur_answer = new StringBuilder(input.substring(0, i)).append(")");
                    for (int j = 0; j < opened_par; j++) {
                        cur_answer.append("(");
                    }
                    while (cur_answer.length() != input.length()) {
                        cur_answer.append(")");
                    }
                    result = cur_answer.toString();
                    return result;
                }
            } else {
                balance++;
            }
        }
        return "";
    }

    public void get_vars(Expression expression) {
        if (expression instanceof Variable) {
            if (vars.contains(expression.toString()) == Boolean.FALSE) {
                vars.add(expression.toString());
            }
        } else {
            if (expression instanceof Negation) {
                get_vars(expression.getLeft());
            } else {
                get_vars(expression.getRight());
                get_vars(expression.getLeft());
            }
        }
    }

    public void tree_to_list() {
        for (int i = 0; i < wood.size(); i++) {
            wood_in_lists.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                wood_in_lists.get(i).add(new OneTree());
            }
            sub_ttl(i, wood.get(i));
        }
    }

    public void sub_ttl(int index, OneTree cur_vertex) {
        wood_in_lists.get(index).set(cur_vertex.number, cur_vertex);
        for (OneTree child : cur_vertex.children) {
            sub_ttl(index, child);
        }
    }

    public ArrayList<HashSet<Integer>> set_sets(OneTree cur_vertex) {
        ArrayList<HashSet<Integer>> result = new ArrayList<>();
        HashSet<Integer> current = new HashSet<>();
        current.add(cur_vertex.number);
        result.add(current);
        for (OneTree child : cur_vertex.children) {
            int index = result.size();
            result.addAll(set_sets(child));
            result.get(0).addAll(result.get(index));
        }
        return result;
    }

    public ArrayList<HashSet<Integer>> genSets(OneTree cur_vertex) {
        ArrayList<HashSet<Integer>> addable = set_sets(cur_vertex);
        ArrayList<HashSet<Integer>> result = new ArrayList<>();

        while (!addable.isEmpty()) {
            ArrayList<HashSet<Integer>> new_sets = new ArrayList<>();
            result.addAll(addable);
            for (HashSet<Integer> add_el : addable) {
                for (HashSet<Integer> res_el : result) {
                    HashSet<Integer> temp = new HashSet<>(add_el);
                    temp.addAll(res_el);
                    if (!result.contains(temp) && !new_sets.contains(temp)) {
                        new_sets.add(temp);
                    }
                }
            }
            addable = new_sets;
        }
        return result;
    }

    public OneTree check_vars(Expression expression, int index) {
        ArrayList<HashSet<Integer>> all_sets = genSets(wood.get(index));
        ArrayList<OneTree> current_tree = wood_in_lists.get(index);
        HashSet<Integer> empty_set = new HashSet<>();
        all_sets.add(0, empty_set);
        for (int i = 0; i < (vars.size() == 3 ? all_sets.size() : 1); i++) {
            for (int j = 0; j < (vars.size() >= 2 ? all_sets.size() : 1); j++) {
                for (int k = 0; k < all_sets.size(); k++) {
                    for (int z = 0; z < 6; z++) {
                        wood_in_lists.get(index).get(z).treeforce.clear();
                    }
                    for (Integer elem : all_sets.get(k)) {
                        current_tree.get(elem).treeforce.add(new Variable(vars.get(0)));
                    }
                    for (Integer elem : all_sets.get(j)) {
                        current_tree.get(elem).treeforce.add(new Variable(vars.get(1)));
                    }
                    for (Integer elem : all_sets.get(i)) {
                        current_tree.get(elem).treeforce.add(new Variable(vars.get(2)));
                    }
                    //System.out.println(current_tree.get(0).treeforce);
                    if (passed_check(expression, wood.get(index)) == Boolean.FALSE) return wood.get(index);
                }
            }
        }
        return null;
    }

    ArrayList<Integer> vertex_and_forced = new ArrayList<>();

    public ArrayList<HashSet<Integer>> buildGayting(int index) {
        for (int i = 0; i < 3; i++) {
            vertex_and_forced.add(1);
        }
        ArrayList<HashSet<Integer>> result = new ArrayList<>(), addable = new ArrayList<>();
        int size_tree = 1;
        OneTree cur_vertex = wood.get(index);
        while (wood_in_lists.get(index).get(size_tree + 1).number != -1) {
            size_tree++;
        }
        HashSet<Integer> empty_set = new HashSet<>();
        addable.add(empty_set);
        for (int i = 0; i < vars.size(); i++) {
            HashSet<Integer> var_forced = new HashSet<>();
            for (int j = 1; j < 6; j++) {
                if (wood_in_lists.get(index).get(j).treeforce.contains(new Variable(vars.get(i)))) {
                    var_forced.add(j);
                }
            }
            if (!addable.contains(var_forced)) addable.add(var_forced);
            vertex_and_forced.set(i, addable.indexOf(var_forced) + 1);
        }
        int OHANJANYAN_MEM = Integer.MIN_VALUE - Integer.MAX_VALUE;
        while (!addable.isEmpty()) {
            //System.out.println(addable);
            result.addAll(addable);
            ArrayList<HashSet<Integer>> new_list = new ArrayList<>();
            for (HashSet<Integer> add_elem : addable) {
                for (HashSet<Integer> res_elem : result) {
                    HashSet<Integer> disjunc = new HashSet<>();
                    for (int i = OHANJANYAN_MEM; i < size_tree + OHANJANYAN_MEM; i++) {
                        if (res_elem.contains(i) || add_elem.contains(i)) {
                            disjunc.add(i);
                        }
                    }
                    //System.out.println(disjunc);
                    if (!result.contains(disjunc) && !new_list.contains(disjunc)) new_list.add(disjunc);
                    HashSet<Integer> conjunc = new HashSet<>();
                    for (int i = OHANJANYAN_MEM; i < size_tree + OHANJANYAN_MEM; i++) {
                        if (res_elem.contains(i) && add_elem.contains(i)) {
                            conjunc.add(i);
                        }
                    }
                    //System.out.println(conjunc);
                    if (!result.contains(conjunc) && !new_list.contains(conjunc)) new_list.add(conjunc);
                    HashSet<Integer> impl = new HashSet<>();
                    for (int i = OHANJANYAN_MEM; i < size_tree + OHANJANYAN_MEM; i++) {
                        if (!add_elem.contains(i) || res_elem.contains(i)) {
                            impl.add(i);
                        }
                    }
                    //System.out.println(impl);
                    for (int i = OHANJANYAN_MEM; i < size_tree + OHANJANYAN_MEM; i++) {
                        if (impl_sub_check(i, impl, wood_in_lists.get(index).get(i))) {
                            //System.out.println(i + " : false\n");
                            impl.remove((Integer)i);
                        }
                    }
                    //System.out.println(impl);
                    //System.out.println("\n\n");
                    if (!result.contains(impl) && !new_list.contains(impl)) {
                        new_list.add(impl);
                    }
                }
            }
            addable = new_list;
        }
        return result;
    }

    public Boolean impl_sub_check(int index, HashSet<Integer> temp, OneTree cur_vertex) {
        if (!temp.contains(index)) return true;
        for (OneTree child : cur_vertex.children) {
            if (impl_sub_check(child.number, temp, child)) {
                return true;
            }
        }
        return false;
    }

    public Boolean passed_check(Expression expression, OneTree cur_vertex) {
        if (expression.getType().equals("!")) {
            return psd_negation(expression.getLeft(), cur_vertex);
        } else if (expression.getType().equals("->")) {
            return psd_impl((Implication) expression, cur_vertex);
        } else if (expression.getType().equals("&")) {
            return psd_con((Conjunction) expression, cur_vertex);
        } else if (expression.getType().equals("|")) {
            return psd_dis((Disjunction) expression, cur_vertex);
        } else {
            return cur_vertex.treeforce.contains(expression);
        }
    }

    public Boolean psd_negation(Expression expr, OneTree cur_vertex) {
        if (passed_check(expr, cur_vertex)) return false;
        for (OneTree child : cur_vertex.children) {
            if (!psd_negation(expr, child)) {
                return false;
            }
        }
        return true;
    }

    public Boolean psd_impl(Implication impl, OneTree cur_vertex) {
        if (passed_check(impl.getLeft(), cur_vertex) && !passed_check(impl.getRight(), cur_vertex)) {
            return false;
        }
        for (OneTree child : cur_vertex.children) {
            if (!psd_impl(impl, child)) {
                return false;
            }
        }
        return true;
    }

    public Boolean psd_dis(Disjunction dis, OneTree cur_vertex) {
        return passed_check(dis.getLeft(), cur_vertex) || passed_check(dis.getRight(), cur_vertex);
    }

    public Boolean psd_con(Conjunction con, OneTree cur_vertex) {
        return passed_check(con.getLeft(), cur_vertex) && passed_check(con.getRight(), cur_vertex);
    }


    public void check(Expression expression, BufferedWriter output) throws IOException {
        for (int i = 2; i < 6; i++) {
            create_trees(i);
        }
        tree_to_list();
        get_vars(expression);
        for (int i = 0; i < wood.size(); i++) {
            if (check_vars(expression, i) != null) {
                ArrayList<HashSet<Integer>> gayting = buildGayting(i);
                output.write(gayting.size() + System.lineSeparator());
                for (int j = 0; j < gayting.size(); j++) {
                    for (int k = 0; k < gayting.size(); k++) {
                        Boolean flag = true;
                        for (Integer z : gayting.get(j)) {
                            if (!gayting.get(k).contains(z)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            output.write((k + 1) + " ");
                        }
                    }
                    output.write(System.lineSeparator());
                }
                output.write(vars.get(0) + "=" + vertex_and_forced.get(0));
                if (vars.size() == 2) {
                    output.write(", ");
                    output.write(vars.get(1) + "=" + vertex_and_forced.get(1));
                } else if (vars.size() == 3) {
                    output.write(", ");
                    output.write(vars.get(1) + "=" + vertex_and_forced.get(1));
                    output.write(", ");
                    output.write(vars.get(2) + "=" + vertex_and_forced.get(2));
                }
                return;
            }
        }
        output.write("Формула общезначима");
    }

    public void six_task_checker(Expression expression, BufferedWriter output, BufferedReader input) throws IOException{
        get_vars(expression);
        OneTree root = new OneTree(0);
        int size = 1;
        int shift = -1;
        OneTree cur_vertex = root;
        String inp = "";
        ExpressionParser parser = new ExpressionParser();
        Stack<OneTree> stackParents = new Stack<>();
        stackParents.add(root);
        while((inp = input.readLine()) != null) {
            int cur_shift = inp.indexOf('*');
            OneTree new_vert = new OneTree(size++);
            if(cur_shift <= shift) {
                for(int i = cur_shift;i <=shift;i++) stackParents.pop();
                stackParents.peek().children.add(new_vert);
                stackParents.push(new_vert);
            } else {
                stackParents.peek().children.add(new_vert);
                stackParents.push(new_vert);
            }
            shift = cur_shift;
            if (cur_shift != inp.length()) {
                String[] str_array = inp.substring(cur_shift + 1).replace(" ", "").split(",");
                if (!str_array[0].equals(""))
                    for (int i = 0; i < str_array.length; i++) {
                        new_vert.treeforce.add(new Variable(str_array[i]));
                    }
            }
        }
        /*
        for (OneTree elem : root.children) {
            for (OneTree elem1: elem.children) {
                System.out.println(elem1.children.size());
                System.out.println(elem1.treeforce);
            }
        }*/


        if(!kripke_check(root)) {
            output.write("Не модель Крипке");
            return;
        }
        for(OneTree child : root.children) {
            if(!passed_check(expression, child)) {
                wood.add(root);
                tree_to_list();
                ArrayList<HashSet<Integer>> gayting = buildGayting(0);
                output.write(gayting.size() + "\n");
                for (int j = 0; j < gayting.size(); j++) {
                    for (int k = 0; k < gayting.size(); k++) {
                        Boolean flag = true;
                        for (Integer z : gayting.get(j)) {
                            if (!gayting.get(k).contains(z)) {
                                flag = false;
                            }
                        }
                        if (flag) {
                            output.write((k + 1) + " ");
                        }
                    }
                    output.write("\n");
                }
                output.write(vars.get(0) + "=" + vertex_and_forced.get(0));
                if (vars.size() == 2) {
                    output.write(", ");
                    output.write(vars.get(1) + "=" + vertex_and_forced.get(1));
                } else if (vars.size() == 3) {
                    output.write(", ");
                    output.write(vars.get(1) + "=" + vertex_and_forced.get(1));
                    output.write(", ");
                    output.write(vars.get(2) + "=" + vertex_and_forced.get(2));
                } else if(vars.size() == 4) {
                    output.write(", ");
                    output.write(vars.get(1) + "=" + vertex_and_forced.get(1));
                    output.write(", ");
                    output.write(vars.get(2) + "=" + vertex_and_forced.get(2));
                    output.write(", ");
                    output.write(vars.get(3) + "=" + vertex_and_forced.get(3));
                }
                output.flush();
                output.close();
                return;
            }
        }
        output.write("Не опровергает формулу");

    }

    public Boolean recursive_check(Variable input, OneTree cur_vertex) {
        if(!cur_vertex.treeforce.contains(input)) {
            return false;
        }
        for(OneTree child : cur_vertex.children) {
            return recursive_check(input, child);
        }
        return true;
    }

    public Boolean kripke_check(OneTree cur_vertex) {
        for(Expression expr : cur_vertex.treeforce) {
            if(!recursive_check((Variable) expr, cur_vertex)) {
                return false;
            }
        }
        for(OneTree next_vert : cur_vertex.children) {
            if(!kripke_check(next_vert)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        try(BufferedReader input = Files.newBufferedReader(Paths.get("input.txt"));
        BufferedWriter output = Files.newBufferedWriter(Paths.get("output.txt"))) {
            String s = input.readLine();
            ExpressionParser parser = new ExpressionParser();
            Expression expression = parser.parse(s);
            Formula formula = new Formula();
            //formula.wood.add(new OneTree(1));
            //formula.check(expression, output);
            formula.six_task_checker(expression, output, input);
        }
    }
}
