package proof;

import parser.*;
import expression.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by Dima on 08.04.2018.
 */
public class ProofCheck {
    private static ArrayList<String> str_axiom = new ArrayList<>(Arrays.asList("A->(B->A)",
            "(A->B)->(A->B->C)->(A->C)",
            "A->B->A&B",
            "(A&B)->A",
            "(A&B)->B",
            "A->(A|B)",
            "A->(B|A)",
            "(A->C)->(B->C)->((A|B)->C)",
            "(A->B)->(A->(!B))->(!A)",
            "(!!A)->A"));
    private String toProveHyp;
    private ArrayList<String> rewrite = new ArrayList<>();
    private ArrayList<Expression> axiom = new ArrayList<>();
    private HashMap<Integer, Integer> hypMap = new HashMap<>();
    private HashMap<Integer, Integer> wasInProof = new HashMap<>();
    private HashMap<Integer, ArrayList<ArrayList<Integer>>> mpP = new HashMap<>();

    public static void main(String[] argv) throws IOException {
        ProofCheck resultProof = new ProofCheck();
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"));
        String header = reader.readLine();
        while (header.equals("")) {
            header = reader.readLine();
        }
        ExpressionParser parser = new ExpressionParser();
        //String dick = "A->B";


        for (int i = 0; i < 10; i++) {
            //System.out.println(expros.toString());
            resultProof.axiom.add(parser.parse(str_axiom.get(i)));
        }
        String[] headerSp = header.split("\\|-");
        String[] hypArray;
        hypArray = headerSp[0].split(",");
        resultProof.toProveHyp = hypArray[hypArray.length - 1];
        resultProof.hyps(hypArray, parser);
        String proof;
        for (int i = 0; i < hypArray.length - 1; i++) {
            if (i == hypArray.length - 2) {
                writer.write(hypArray[i]);
                continue;
            }
            writer.write(hypArray[i] + ",");
        }
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("((" + parser.parse(resultProof.toProveHyp).toString() + ")->(" + parser.parse(headerSp[1]).toString() + "))");
        //System.out.println(sBuilder.toString());
        //ExpressionParser parserOfConclusion = new ExpressionParser();
        writer.write("|-" + sBuilder.toString() + "\n");
        resultProof.toProveHyp = parser.parse(resultProof.toProveHyp).toString();
        int proofNum = 1;
        while ((proof = reader.readLine()) != null) {

            if (proof.equals("")) {
                continue;
            }
            /*
            if (proofNum != 1) {
                result.add()("\n");
            }*/
            //resultProof.prove(proof, proofNum, result, parser);
            proofNum++;

        }
        //result.add()(parser.parse(sBuilder.toString()).toString() + "\n");
        reader.close();
        writer.close();
    }

    public ArrayList<String> rebuildProof(ArrayList<String> currentProofList) {
        ExpressionParser parser = new ExpressionParser();
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            axiom.add(parser.parse(str_axiom.get(i)));
        }
        String header = currentProofList.get(0);
        StringBuilder headerResult = new StringBuilder();
        String[] headerSp = header.split("\\|-");
        String[] hypArray;
        hypArray = headerSp[0].split(",");
        toProveHyp = hypArray[hypArray.length - 1];
        hyps(hypArray, parser);
        for (int i = 0; i < hypArray.length - 1; i++) {
            if (i == hypArray.length - 2) {
                headerResult.append(hypArray[i]);
                continue;
            }
            headerResult.append(hypArray[i] + ",");
        }
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append("((" + parser.parse(toProveHyp).toString() + ")->(" + parser.parse(headerSp[1]).toString() + "))");
        //System.out.println(sBuilder.toString());
        //ExpressionParser parserOfConclusion = new ExpressionParser();
        headerResult.append("|-" + sBuilder.toString());
        result.add(headerResult.toString());
        toProveHyp = parser.parse(toProveHyp).toString();
        int proofNum = 1;
        for (int i = 1; i < currentProofList.size(); i++) {
            prove(currentProofList.get(i), proofNum, result, parser);
            proofNum++;
        }
        return result;
    }

    private void hyps(String[] array, ExpressionParser parser) {
        for (String hyp : array) {
            if (!Objects.equals(hyp, "") && !Objects.equals(hyp, toProveHyp)) {
                //System.out.println(hyp);
                hypMap.put(parser.parse(hyp).hashCodeX(), hypMap.size());
            }
        }
    }

    private void prove(String proof, int proofNum, ArrayList<String> result, ExpressionParser parser) {

        Expression expr = parser.parse(proof);
        rewrite.add(expr.toString());
        if (expr.getType().equals("->")) {
            //System.out.println(expr.toString());
            ArrayList<ArrayList<Integer>> temp;
            if (mpP.get(expr.getRight().hashCodeX()) != null)
                temp = mpP.get(expr.getRight().hashCodeX());
            else temp = new ArrayList<>();
            ArrayList<Integer> ttemp = new ArrayList<>();
            ttemp.add(expr.hashCodeX());
            ttemp.add(expr.getLeft().hashCodeX());
            temp.add(ttemp);
            mpP.remove(expr.getRight().hashCodeX());
            mpP.put(expr.getRight().hashCodeX(), temp);
            //System.out.println(expr.toString());
            //System.out.println(expr.getRight().hashCodeX());
        }
        if (hypMap.get(expr.hashCodeX()) != null) {
            //String formatted = " (Предп. " + (hypMap.get(expr.hashCodeX()) + 1) + ")";
            //print(proofNum, expr.toString(), formatted, result);
            printAxOrGuess(result, expr.toString());
            wasInProof.put(expr.hashCodeX(), proofNum);
            return;
        }
        if (toProveHyp.equals(expr.toString())) {
            wasInProof.put(expr.hashCodeX(), proofNum);
            printHyp(result, expr.toString());
            return;
        }

        for (int i = 0; i < 10; i++) {
            HashMap<String, Integer> varM = new HashMap<>();
            if (axiom.get(i).recursivlyCompare(expr, varM)) {
                wasInProof.put(expr.hashCodeX(), proofNum);
                //String formatted = " (Сх. акс. " + (i + 1) + ")";
                //print(proofNum, expr.toString(), formatted, result);
                printAxOrGuess(result, expr.toString());
                return;
            }
        }
        if (mpP.get(expr.hashCodeX()) != null) {
            //System.out.println(1);
            ArrayList<ArrayList<Integer>> temp = mpP.get(expr.hashCodeX());
            for (ArrayList<Integer> pointer : temp) {
                //System.out.println(expr.toString());
                //System.out.println(poin  ter.get(1));
                if (wasInProof.get(pointer.get(1)) != null) {
                    wasInProof.put(expr.hashCodeX(), proofNum);
                    Integer numAimplB = wasInProof.get(pointer.get(0));
                    Integer numA = wasInProof.get(pointer.get(1));
                    //String formatted = " (M.P. " + numAimplB + ", " + numA + ")";
                    //print(proofNum, expr.toString(), formatted, result);
                    //System.out.println("in");
                    printMp(result, expr.toString(), rewrite.get(numA - 1));
                    return;
                }
            }

        }
        //}
        //wasInProof.put(expr.hashCodeX(), proofNum);
        //String formatted = " (Не доказано)";
        //print(proofNum, expr.toString(), formatted, result);
    }

    private void printAxOrGuess(ArrayList<String> result, String input)  {
        result.add("(" + input + ")");
        result.add("(" + "(" + input + ")" + "->(" + "(" + toProveHyp + ")" + "->" + "(" + input + ")" + "))");
        result.add("(" + "(" + toProveHyp + ")" + "->" + "(" + input + ")" + ")");
    }

    private void printHyp(ArrayList<String> result, String input) {
        result.add("(" + "(" + toProveHyp + ")" + "->(" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + "))");
        result.add("(" + "(" + toProveHyp + ")" + "->((" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + ")->" + "(" + toProveHyp + ")" + "))");
        result.add("((" + "(" + toProveHyp + ")" + "->(" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + "))->((" + "(" + toProveHyp + ")" + "->((" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + ")->" + "(" + toProveHyp + ")" + "))" + "->(" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + ")))");
        result.add("((" + "(" + toProveHyp + ")" + "->((" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + ")->" + "(" + toProveHyp + ")" + "))->(" + "(" + toProveHyp + ")" + "->" + "(" + toProveHyp + ")" + "))");
        result.add("(" + "(" + input + ")" + "->" + "(" + toProveHyp + ")" + ")");
    }

    private void printMp(ArrayList<String> result, String input, String found) {
        result.add("(" + "(" + toProveHyp + ")" + "->" + "(" + found + ")" + ")->((" + "(" + toProveHyp + ")" + "->(" + "(" + found + ")" + "->" + "(" + input + ")" + "))->(" + "(" + toProveHyp + ")" + "->" + "(" + input + ")" + "))");
        result.add("((" + "(" + toProveHyp + ")" + "->(" + "(" + found + ")" + "->" + "(" + input + ")" + "))->(" + "(" + toProveHyp + ")" + "->" + "(" + input + ")" + "))");
        result.add("(" + "(" + toProveHyp + ")" + "->" + "(" + input + ")" + ")");
    }

}
