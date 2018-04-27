package proof;

import parser.*;
import expression.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
        for (int i = 0; i < hypArray.length - 1;i++) {
            if(i == hypArray.length - 2) {
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
                writer.write("\n");
            }*/
            resultProof.prove(proof, proofNum, writer, parser);
            proofNum++;

        }
        //writer.write(parser.parse(sBuilder.toString()).toString() + "\n");
        reader.close();
        writer.close();
    }

    private void hyps(String[] array, ExpressionParser parser) {
        for (String hyp : array) {
            if (hyp != "" && hyp != toProveHyp) {
                //System.out.println(hyp);
                hypMap.put(parser.parse(hyp).hashCodeX(), hypMap.size());
            }
        }
    }

    private void prove(String proof, int proofNum, BufferedWriter writer, ExpressionParser parser) throws IOException {

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
            //print(proofNum, expr.toString(), formatted, writer);
            printAxOrGuess(writer,expr.toString());
            wasInProof.put(expr.hashCodeX(), proofNum);
            return;
        }
        if(toProveHyp.equals(expr.toString())) {
            wasInProof.put(expr.hashCodeX(), proofNum);
            printHyp(writer, expr.toString());
            return;
        }

        for (int i = 0; i < 10; i++) {
            HashMap<String, Integer> varM = new HashMap<>();
            if (axiom.get(i).recursivlyCompare(expr, varM)) {
                wasInProof.put(expr.hashCodeX(), proofNum);
                //String formatted = " (Сх. акс. " + (i + 1) + ")";
                //print(proofNum, expr.toString(), formatted, writer);
                printAxOrGuess(writer, expr.toString());
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
                    //print(proofNum, expr.toString(), formatted, writer);
                    //System.out.println("in");
                    printMp(writer,expr.toString(),rewrite.get(numA - 1));
                    return;
                }
            }

        }
        //}
        //wasInProof.put(expr.hashCodeX(), proofNum);
        //String formatted = " (Не доказано)";
        //print(proofNum, expr.toString(), formatted, writer);
    }

    private void printAxOrGuess(BufferedWriter writer, String input) throws IOException {
        writer.write("("+input +")");
        writer.write("\n");
        writer.write("(" + "("+input +")" + "->(" + "("+ toProveHyp + ")" + "->" + "("+input +")" + "))");
        writer.write("\n");
        writer.write("(" + "("+ toProveHyp + ")" + "->" + "("+input +")" + ")");
        writer.write("\n");
    }

    private void printHyp(BufferedWriter writer, String input) throws IOException {
        writer.write("(" + "("+ toProveHyp + ")" + "->(" + "("+ toProveHyp + ")" + "->" + "("+ toProveHyp + ")" + "))");
        writer.write("\n");
        writer.write("(" + "("+ toProveHyp + ")" +  "->((" + "("+ toProveHyp + ")" +  "->" + "("+ toProveHyp + ")" + ")->" + "("+ toProveHyp + ")" + "))");
        writer.write("\n");
        writer.write("((" + "("+ toProveHyp + ")" +  "->(" + "("+ toProveHyp + ")" + "->" + "("+ toProveHyp + ")" + "))->((" + "("+ toProveHyp + ")" + "->((" + "("+ toProveHyp + ")" +  "->" + "("+ toProveHyp + ")" + ")->" + "("+ toProveHyp + ")"+ "))" +   "->(" + "("+ toProveHyp + ")" +  "->" + "("+ toProveHyp + ")" + ")))");
        writer.write("\n");
        writer.write("((" + "("+ toProveHyp + ")" +  "->((" + "("+ toProveHyp + ")" +  "->" + "("+ toProveHyp + ")" +  ")->" + "("+ toProveHyp + ")" +  "))->(" + "("+ toProveHyp + ")" +  "->" + "("+ toProveHyp + ")" + "))" );
        writer.write("\n");
        writer.write("(" + "("+input +")" + "->" + "("+ toProveHyp + ")" + ")");
        writer.write("\n");
    }

    private void printMp(BufferedWriter writer, String input, String found) throws IOException{
        writer.write("(" + "("+ toProveHyp + ")" + "->" + "(" + found + ")" + ")->((" + "("+ toProveHyp + ")" + "->(" + "(" + found + ")" + "->" + "("+input +")" + "))->(" + "("+ toProveHyp + ")" + "->" + "("+input +")" + "))");
        writer.write("\n");
        writer.write("((" + "("+ toProveHyp + ")" + "->(" + "(" + found + ")" + "->" + "("+input +")" + "))->(" + "("+ toProveHyp + ")" + "->" + "("+input +")" + "))");
        writer.write("\n");
        writer.write("(" + "("+ toProveHyp + ")" + "->" + "("+input +")" + ")");
        writer.write("\n");
    }

}
