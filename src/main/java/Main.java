import domain.Position;
import domain.MyScanner;
import domain.SymbolTable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws Exception {
//        SymbolTable symbolTable = new SymbolTable(71);
//        Position a = symbolTable.search("a");
//        System.out.println("Identifier \"a\" does not exist: " + (a.hashTableIndex == -1));
//        a = symbolTable.add("a");
//        System.out.println("Identifier \"a\" now exists in the symbol table on the position: " + a.hashTableIndex + ", " + a.linkedListIndex);
//
//        symbolTable.add("ab");
//        symbolTable.add("ba");
//        Position ab = symbolTable.search("ab");
//        Position ba = symbolTable.search("ba");
//        // Show a collision resolution (because "ab" and "ba" have the same hash value)
//        System.out.println("\nIdentifier \"ab\" exists in the symbol table on the position: " + ab.hashTableIndex + ", " + ab.linkedListIndex);
//        System.out.println("Identifier \"ba\" exists in the symbol table on the position: " + ba.hashTableIndex + ", " + ba.linkedListIndex);


        System.out.println("\n\n\n");
        MyScanner scanner = new MyScanner("data/p1.txt", new SymbolTable(71));
        scanner.startScanning();
        System.out.println(scanner);

//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Input program file name: ");
//        String fileName = reader.readLine();
//        System.out.println("xxx" + fileName + "xxx");
    }
}
