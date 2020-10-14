import domain.Position;
import domain.SymbolTable;

public class Main {
    public static void main(String[] args) {
        SymbolTable symbolTable = new SymbolTable(71);
        Position a = symbolTable.search("a");
        System.out.println("Identifier \"a\" does not exist: " + (a.hashTableIndex == -1));
        a = symbolTable.add("a");
        System.out.println("Identifier \"a\" now exists in the symbol table on the position: " + a.hashTableIndex + ", " + a.linkedListIndex);

        symbolTable.add("ab");
        symbolTable.add("ba");
        Position ab = symbolTable.search("ab");
        Position ba = symbolTable.search("ba");
        // Show a collision resolution (because "ab" and "ba" have the same hash value)
        System.out.println("\nIdentifier \"ab\" exists in the symbol table on the position: " + ab.hashTableIndex + ", " + ab.linkedListIndex);
        System.out.println("Identifier \"ba\" exists in the symbol table on the position: " + ba.hashTableIndex + ", " + ba.linkedListIndex);

    }
}
