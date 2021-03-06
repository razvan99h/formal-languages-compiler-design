import domain.FiniteAutomata;
import domain.MyScanner;
import domain.SymbolTable;
import parser.Grammar;
import parser.Parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

//        // Lab 4 - final
//        System.out.println("Input program file name: ");
//        String fileName = reader.readLine();
//
//        MyScanner scanner = new MyScanner("data/"+ fileName, new SymbolTable(71));
//        scanner.startScanning();
//        System.out.println(scanner);
//
//        FileWriter myWriter = new FileWriter("data/" + fileName + ".out");
//        myWriter.write(scanner.toString());

//        // Lab 4
//        FiniteAutomata finiteAutomata = new FiniteAutomata();
//        finiteAutomata.readFromFile("data/fa-integer-const.txt");
//        System.out.println(finiteAutomata);
//
//        while(true) {
//            if (finiteAutomata.isDeterministic()) {
//                System.out.println("\nThe given FA is deterministic!");
//                System.out.println("\nInput sequence for FA: ");
//                String sequence = reader.readLine();
//                if (finiteAutomata.verifySequence(sequence))
//                    System.out.println("\nThe given sequence is accepted by the FA!");
//                else
//                    System.out.println("\nThe given sequence is not accepted by the FA!");
//            }
//            else
//                System.out.println("\nThe given FA is nondeterministic!");
//        }

//        // Lab 5 - part 1
//        Grammar grammar = new Grammar("data/g1.txt");
//        System.out.println(grammar.toString());


//        // Lab 5 - part 2
//        Grammar grammar = new Grammar("data/g4.txt");
//        Parser parser = new Parser(grammar);
//        parser.canonicalCollection(false);

//        // Lab 6
//        Grammar grammar = new Grammar("data/g4.txt");
//        System.out.println(grammar.toString());
//        Parser parser = new Parser(grammar, true);
//        parser.parseCharacterSequence("a");

        // Lab 7
        Grammar grammar = new Grammar("data/gLanguage.txt", true);
        System.out.println(grammar.toString());
        Parser parser = new Parser(grammar, true);

        parser.parseFile("data/p5.txt");

    }

}
