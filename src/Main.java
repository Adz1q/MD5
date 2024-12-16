import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Podaj ciąg znaków do zakodowania: ");
        String input = sc.nextLine();

        System.out.println("Zakodowany ciąg znaków: " + MD5.hash(input));
    }
}