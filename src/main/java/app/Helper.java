package app;

import java.util.Scanner;

public class Helper {

    static public String scanString(String msg){
        System.out.print(msg);
        return new Scanner(System.in).nextLine();
    }

    static public int scanInt(String msg) {
        while (true) {
            try {
                return Integer.parseInt(scanString(msg));
            } catch (NumberFormatException e) {
                System.out.println("Er wordt een getal verwacht.");
            }
        }
    }

}


