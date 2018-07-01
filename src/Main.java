import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Dima on 08.04.2018.
 */
public class Main {
    public static void main(String[] argv) {

        Scanner reader = new Scanner(System.in);
        String header =reader.nextLine();
        String[] temp = header.split("\\|-" );
        String[] hypArray = temp[0].split(",");
        for(String s : hypArray) {
            System.out.println(s);
        }
    }
}
