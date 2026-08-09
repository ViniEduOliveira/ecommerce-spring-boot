package util;

import java.util.Scanner;

// Classe que representa um scanner para usar em toda a aplicação
public class ScannerUtil {
    private static final Scanner scanner = new Scanner(System.in);

    public static Scanner getScanner() {
        return scanner;
    }
}