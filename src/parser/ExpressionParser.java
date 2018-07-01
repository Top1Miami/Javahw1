package parser;

import expression.*;
import expression.Expression;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Dima on 08.04.2018.
 */
public class ExpressionParser {
    private int pos;
    private static String expression;

    private enum Token {VAR, CON, DIS, NEG, IMP, LBR, RBR}

    private Token curToken;
    private String var;

    private char get() {
        if (pos < expression.length()) {
            char c = expression.charAt(pos);
            pos++;
            return c;
        } else {
            return '#';
        }
    }

    private void getToken() {
        char c = get();
        if (c == '&') {
            curToken = Token.CON;
        } else if (c == '|') {
            curToken = Token.DIS;
        } else if (c == '!') {
            curToken = Token.NEG;
        } else if (c == '-') {
            pos++;
            curToken = Token.IMP;
        } else if (c == '(') {
            curToken = Token.LBR;
        } else if (c == ')') {
            curToken = Token.RBR;
        } else {
            StringBuilder new_var = new StringBuilder();
            new_var.append(c);
            while (pos < expression.length() && (Character.isDigit(expression.charAt(pos)) || Character.isLetter(expression.charAt(pos)))) {
                new_var.append(expression.charAt(pos));
                pos++;
            }
            var = new_var.toString();
            curToken = Token.VAR;
        }
    }

    private Expression nvp() {
        getToken();
        Expression res;
        if (curToken == Token.NEG) {
            res = new Negation(nvp());

        } else if (curToken == Token.VAR) {
            res = new Variable(var);
            getToken();
        } else if (curToken == Token.LBR) {
            res = impl();
            getToken();
        } else {
            res = null;
        }
        return res;
    }

    private Expression con() {
        Expression res = nvp();
        while (true) {
            if (curToken == Token.CON) {
                res = new Conjunction(res, nvp());
            } else {
                return res;
            }
        }
    }

    private Expression dis() {
        Expression res = con();
        while (true) {
            if (curToken == Token.DIS) {
                res = new Disjunction(res, con());
            } else {
                return res;
            }
        }
    }

    private Expression impl() {
        Expression res = dis();
        while (true) {
            if (curToken == Token.IMP) {
                res = new Implication(res, impl());
            } else {
                return res;
            }
        }
    }

    public Expression parse(String input) {
        expression = "";
        StringBuilder temp = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) != '\t' && input.charAt(i) != '\n' && !Character.isWhitespace(input.charAt(i))) {
                temp.append(input.charAt(i));
            }
        }
        pos = 0;
        expression = temp.toString();
        return impl();
    }

    public static void main(String[] argv) throws IOException {
        BufferedReader reader = Files.newBufferedReader(Paths.get("input.txt"), StandardCharsets.UTF_8);
        BufferedWriter writer = Files.newBufferedWriter(Paths.get("output.txt"));
        StringBuilder input = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            input.append(line);
        }
        ExpressionParser ePar = new ExpressionParser();
        writer.write(ePar.parse(input.toString()).toTree());
        writer.close();
        reader.close();
    }


}
