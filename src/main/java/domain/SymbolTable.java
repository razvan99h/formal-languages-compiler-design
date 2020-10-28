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

    public boolean exists(String identifier) {
        Position pos = this.search(identifier);
        return pos.hashTableIndex == -1 && pos.linkedListIndex == -1;
    }

    @Override
    public String toString() {
        return "SymbolTable{" +
                "hashTable=" + hashTable +
                '}';
    }
}
