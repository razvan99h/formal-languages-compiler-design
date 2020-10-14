package domain;

public class SymbolTable {
    private HashTable hashTable;

    public SymbolTable(Integer primeNumber) {
        hashTable = new HashTable(primeNumber);
    }

    public Position add(String identifier) {
        return hashTable.addElement(identifier);
    }

    public Position search(String identifier) {
        return hashTable.search(identifier);
    }

}
