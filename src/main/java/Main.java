import domain.MyScanner;
import domain.SymbolTable;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input program file name: ");
        String fileName = reader.readLine();

        MyScanner scanner = new MyScanner("data/"+ fileName, new SymbolTable(71));
        scanner.startScanning();
        System.out.println(scanner);

        FileWriter myWriter = new FileWriter("data/" + fileName + ".out");
        myWriter.write(scanner.toString());
    }
}
