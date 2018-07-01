package fullness;

import expression.*;
import parser.*;
import proof.ProofCheck;
import proof.TaskOne;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FullnessProof {
    private String formula;
    private String formulaWithHyp;
    private HashMap<String, Boolean> variable = new HashMap<>();
    private ArrayList<ArrayList<String>> proofList = new ArrayList<>();
    private HashMap<String, ArrayList<String>> detectPrimitives = new HashMap<>();
    private ArrayList<String> vars = new ArrayList<>();
    private ArrayList<String> hypList = new ArrayList<>();
    private String newHeader = "";
    public static void main(String argv[]) throws IOException {

        FullnessProof fullnessProof = new FullnessProof();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"));
        String header = reader.readLine();
        String[] headerSp = header.split("\\|=");
        String[] hypArray = headerSp[0].split(",");
        fullnessProof.newHeader = header.replace("|=", "|-");
        //System.out.println(fullnessProof.hypList.size());
        //fullnessProof.hypList.addAll(Arrays.asList(hypArray));
        for(int i =0; i < hypArray.length;i++) {
            if(!hypArray[i].isEmpty()) {
                fullnessProof.hypList.add(hypArray[i]);
            }
        }
        fullnessProof.formula = headerSp[1];
        StringBuilder temp = new StringBuilder();
        if (!hypArray[0].equals("")) {
            for (int i = hypArray.length - 1; i >= 0; i--) {
                temp.append("(" + hypArray[i] + ")->");
            }
        }
        temp.append("(" + fullnessProof.formula + ")");
        fullnessProof.formulaWithHyp = temp.toString();
        ExpressionParser parser = new ExpressionParser();
        Expression expression = parser.parse(fullnessProof.formulaWithHyp);
        for (int i = 0; i < fullnessProof.formulaWithHyp.length(); i++) {
            if (fullnessProof.formulaWithHyp.charAt(i) >= 'A' && fullnessProof.formulaWithHyp.charAt(i) <= 'Z') {
                if (!fullnessProof.vars.contains(String.valueOf(fullnessProof.formulaWithHyp.charAt(i)))) {
                    fullnessProof.vars.add(String.valueOf(fullnessProof.formulaWithHyp.charAt(i)));
                    fullnessProof.variable.put(String.valueOf(fullnessProof.formulaWithHyp.charAt(i)), true);
                }
            }
        }
        fullnessProof.calc(expression, writer);

        /*try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        writer.flush();
        writer.close();
        TaskOne taskOne = new TaskOne();
        taskOne.check();
    }


    private void calc(Expression expression, BufferedWriter writer) throws IOException {
        for (int i = 0; i < 1 << vars.size(); i++) {
            for (int j = 0; j < vars.size(); j++) {
                variable.put(vars.get(j), (i & (1 << j)) != 0);
            }
            if (!expression.canBeProved(variable)) {
                StringBuilder temp = new StringBuilder();
                temp.append("Высказывание ложно при ");
                for (int j = 0; j < vars.size(); j++) {
                    temp.append((vars.get(j)));
                    temp.append('=');
                    temp.append((((i >> j) & 1) == 1) ? "И" : "Л");
                    if (j != vars.size() - 1)
                        temp.append(", ");
                }
                writer.write(temp.toString());
                writer.flush();
                return;
            }
        }
        proofOfFormula(expression, writer);
    }


    private void getPrimitives() throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("src/primitiveProves.txt"));
        //BufferedReader reader = new BufferedReader(new FileReader(new File("src\\primitiveProves.txt")));
        String inp = reader.readLine();
        while (inp != null) {
            String header = inp;
            ArrayList<String> prime = new ArrayList<>();
            //prime.add(header);
            inp = reader.readLine();
            while (!inp.equals("---")) {
                prime.add(inp);
                inp = reader.readLine();
            }
            detectPrimitives.put(header, prime);
            inp = reader.readLine();
        }
    }

    private void makeProof(Expression expression, ArrayList<String> singleProof) {
        if (expression instanceof Negation) {
            makeProof(((Negation) expression).getNegated(), singleProof);
            String key = (((Negation) expression).getNegated().canBeProved(variable) ? 1 : 0) + expression.getType();
            ArrayList<String> template = new ArrayList<>(detectPrimitives.get(key));
            for (int i = 0; i < template.size(); i++) {
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < template.get(i).length(); j++) {
                    if (template.get(i).charAt(j) == 'A') {
                        result.append("(" + ((Negation) expression).getNegated().toString() + ")");
                    } else {
                        result.append(template.get(i).charAt(j));
                    }
                }
                singleProof.add(result.toString());
            }
        } else if (expression instanceof Variable) {
            if (variable.get(((Variable) expression).getName())) {
                singleProof.add(expression.toString());
            } else {
                singleProof.add("!" + expression.toString());
            }
        } else {
            //System.out.println(detectPrimitives);
            makeProof(expression.getLeft(), singleProof);
            //System.out.println(expression.getRight().toString());
            makeProof(expression.getRight(), singleProof);
            String key = ((expression.getLeft().canBeProved(variable)) ? 1 : 0) + expression.getType() + ((expression.getRight().canBeProved(variable)) ? 1 : 0);
            //System.out.println(detectPrimitives.get(key));
            ArrayList<String> template = new ArrayList<>(detectPrimitives.get(key));

            for (int i = 0; i < template.size(); i++) {
                StringBuilder result = new StringBuilder();
                for (int j = 0; j < template.get(i).length(); j++) {
                    if (template.get(i).charAt(j) == 'A') {
                        result.append(expression.getLeft().toString());
                    } else if (template.get(i).charAt(j) == 'B') {
                        result.append(expression.getRight().toString());
                    } else {
                        result.append(template.get(i).charAt(j));
                    }
                }
                singleProof.add(result.toString());
            }
        }
    }

    private String proofOfFormula(Expression expression, BufferedWriter writer) throws IOException {
        writer.write(newHeader + "\n");
        String result = "";
        getPrimitives();
        for (int i = 0; i < 1 << vars.size(); i++) {
            ArrayList<String> singleProof = new ArrayList<>();
            for (int j = 0; j < vars.size(); j++) {
                variable.put(vars.get(j), (i & (1 << j)) != 0);
            }
            ArrayList<String> header = new ArrayList<>();
            StringBuilder toBuildHeader = new StringBuilder();
            for (int j = 0; j < vars.size(); j++) {
                toBuildHeader.append(((variable.get(vars.get(j)) ? "" : "!") + vars.get(j)));
                if (j != vars.size() - 1) {
                    toBuildHeader.append(',');
                }
            }
            toBuildHeader.append("|-");
            toBuildHeader.append(expression.toString());
            singleProof.add(toBuildHeader.toString());
            makeProof(expression, singleProof);
            proofList.add(singleProof);
        }

        for (int i = 0; i < proofList.size(); i++) {
            for (int j = 0; j < vars.size(); j++) {
                ProofCheck checker = new ProofCheck();
                proofList.set(i, checker.rebuildProof(proofList.get(i)));
            }
            proofList.get(i).remove(0);
            for (int j = 0; j < proofList.get(i).size(); j++) {
                writer.write(proofList.get(i).get(j) + '\n');
                writer.flush();
            }
        }
        for (int i = 0; i < vars.size(); i++) {
            String singleVar = vars.get(i);
            String key = "special";
            ArrayList<String> template = detectPrimitives.get(key);

            for (int j = 0; j < 1 << (vars.size() - i - 1); j++) {for(int k = 0; k < template.size();k++) {
                StringBuilder singleString = new StringBuilder();
                for(int z = 0; z < template.get(k).length();z++) {
                    if(template.get(k).charAt(z) == 'A') {
                        singleString.append("(").append(singleVar).append(")");
                    } else {
                        singleString.append(template.get(k).charAt(z));
                    }
                }
                writer.write(singleString.toString() + '\n');
                //System.out.println(k);
                //System.out.println(singleString.toString());
            }
                StringBuilder formula = new StringBuilder("(");
                for(int k = i + 1; k < vars.size();k++) {
                    Boolean bit = (j & (1 << (k - i - 1))) != 0;
                    formula.append(bit ? "" : "!");
                    formula.append(vars.get(k));
                    formula.append("->");
                }
                formula.append("(" + formulaWithHyp + ")");
                formula.append(")");
                //System.out.println(formula.toString());
                //ArrayList<String> special = new ArrayList<>();

                writer.write("(" + singleVar + "->" + formula.toString() + ")->(!" + singleVar + "->" + formula.toString() + ")->((" + singleVar + "|!" + singleVar + ")->" + formula + ")");
                writer.write("\n");
                writer.write("(!" + singleVar + "->" + formula + ")->((" + singleVar + "|!" + singleVar + ")->" + formula + ")");
                writer.write("\n");
                writer.write("((" + singleVar + "|!" + singleVar + ")->" + formula + ")");
                writer.write("\n");
                writer.write(formula.toString());
                writer.write("\n");
            }
        }
        for(String hyp : hypList) {
            writer.write(hyp + "\n");
        }
        ExpressionParser parser = new ExpressionParser();
        Expression formExpr = parser.parse(formulaWithHyp);
        //System.out.println(formulaWithHyp);
        //System.out.println(formExpr.toString() + " " + hypList.get(0));
        for(int i = 0; i < hypList.size();i++) {
            formExpr = formExpr.getRight();
            writer.write(formExpr.toString() + '\n');
        }
        return result;
    }
}
