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
public class TaskOne {
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

    private ArrayList<Expression> axiom = new ArrayList<>();
    private HashMap<Integer, Integer> hypMap = new HashMap<>();
    private HashMap<Integer, Integer> wasInProof = new HashMap<>();
    private HashMap<Integer, ArrayList<ArrayList<Integer>>> mpP = new HashMap<>();

    public static void main(String[] argv) throws IOException {
        TaskOne resultProof = new TaskOne();
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
        String[] headerSp = header.split("\\|-" );
        String[] hypArray;
        if(header.charAt(0) != '|') {
            hypArray = headerSp[0].split(",");
            resultProof.hyps(hypArray, parser);
        }
        String proof;
        int proofNum = 1;
        while ((proof = reader.readLine()) != null) {

            if (proof.equals("")) {
                continue;
            }
            if (proofNum != 1) {
                writer.write("\n");
            }
            resultProof.prove(proof, proofNum, writer, parser);
            proofNum++;

        }
        reader.close();
        writer.close();
    }
    public void check() throws IOException{
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("outputd.txt"));
        BufferedReader reader = Files.newBufferedReader(Paths.get("output.txt"));
        String header = reader.readLine();
        while (header.equals("")) {
            header = reader.readLine();
        }
        ExpressionParser parser = new ExpressionParser();
        //String dick = "A->B";


        for (int i = 0; i < 10; i++) {
            //System.out.println(expros.toString());
            axiom.add(parser.parse(str_axiom.get(i)));
        }
        String[] headerSp = header.split("\\|-" );
        String[] hypArray;
        if(header.charAt(0) != '|') {
            hypArray = headerSp[0].split(",");
            hyps(hypArray, parser);
        }
        String proof;
        int proofNum = 1;
        while ((proof = reader.readLine()) != null) {

            if (proof.equals("")) {
                continue;
            }
            if (proofNum != 1) {
                writer.write("\n");
            }
            prove(proof, proofNum, writer, parser);
            proofNum++;

        }
        reader.close();
        writer.close();
    }
    private void hyps(String[] array, ExpressionParser parser) {
        for(String hyp: array) {
            if(hyp != "") {
                hypMap.put(parser.parse(hyp).hashCodeX(), hypMap.size());
            }
        }
    }

    private void prove(String proof, int proofNum, BufferedWriter writer, ExpressionParser parser) throws IOException{

        Expression expr = parser.parse(proof);
        //System.out.println(expr.toString());
        //System.out.println(hypMap.get(expr.hashCodeX()()));
        if(expr.getType().equals("->")) {
            if(mpP.get(expr.getRight().hashCodeX()) == null) {
                ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
                ArrayList<Integer> ttemp = new ArrayList<>();
                ttemp.add(expr.hashCodeX());
                ttemp.add(expr.getLeft().hashCodeX());
                temp.add(ttemp);
                mpP.put(expr.getRight().hashCodeX(), temp);
            } else {
                ArrayList<Integer> ttemp = new ArrayList<>();
                ttemp.add(expr.hashCodeX());
                ttemp.add(expr.getLeft().hashCodeX());
                mpP.get(expr.getRight().hashCodeX()).add(ttemp);
            }
            /*ArrayList<ArrayList<Integer>> temp;
            if( mpP.get(expr.getRight().hashCodeX()()) != null)
            temp = mpP.get(expr.getRight().hashCodeX()());
            else temp = new ArrayList<>();
            ArrayList<Integer> ttemp = new ArrayList<>();
            ttemp.add(expr.hashCodeX()());
            ttemp.add(expr.getLeft().hashCodeX()());
            temp.add(ttemp);
            mpP.remove(expr.getRight().hashCodeX()());
            mpP.put(expr.getRight().hashCodeX()(), temp);*/
        }
        if(hypMap.get(expr.hashCodeX()) != null) {
            String formatted = " (Предп. " + (hypMap.get(expr.hashCodeX()) + 1) + ")";
            print(proofNum, expr.toString(), formatted, writer);
            wasInProof.put(expr.hashCodeX(), proofNum);
            return;
        } //else if(mpP.get(expr.hashCodeX()()) != null && wasInProof.get(mpP.get(expr.hashCodeX()()).get(1)) != null) {
        for(int i = 0; i < 10;i++) {
            HashMap<String, Integer> varM = new HashMap<>();
            if(axiom.get(i).recursivlyCompare(expr,varM)) {
                wasInProof.put(expr.hashCodeX(), proofNum);
                String formatted = " (Сх. акс. " + (i + 1) + ")";
                print(proofNum, expr.toString(), formatted, writer);
                return;
            }
        }
        if(mpP.get(expr.hashCodeX()) != null) {
            ArrayList<ArrayList<Integer>> temp = mpP.get(expr.hashCodeX());
            for(ArrayList<Integer> pointer : temp) {
                /*
                Integer numAimplB = wasInProof.get(mpP.get(expr.hashCodeX()()).get(0));
                Integer numA = wasInProof.get(mpP.get(expr.hashCodeX()()).get(1));
                String formatted = " (M.P. " + numAimplB + ", " + numA + ")";
                print(proofNum, expr.toString(), formatted, writer);*/
                if(wasInProof.get(pointer.get(1)) != null) {
                    wasInProof.put(expr.hashCodeX(), proofNum);
                    Integer numAimplB = wasInProof.get(pointer.get(0));
                    Integer numA = wasInProof.get(pointer.get(1));
                    String formatted = " (M.P. " + numAimplB + ", " + numA + ")";
                    print(proofNum, expr.toString(), formatted, writer);

                    return;
                }
            }

        }
        //}

        wasInProof.put(expr.hashCodeX(), proofNum);
        String formatted = " (Не доказано)";
        System.out.println("not proved" + proofNum);
        print(proofNum, expr.toString(), formatted, writer);
    }
    private void print(int number, String value, String formatted, BufferedWriter writer) throws IOException {
        writer.write("(" + number + ") "+ value + formatted);
    }
}
