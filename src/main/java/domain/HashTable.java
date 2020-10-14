package domain;


public class HashTable {

    private Integer dimension;
    private Node[] elements;

    public HashTable(Integer primeNumber) {
        this.dimension = primeNumber;
        elements = new Node[primeNumber];
    }

    private Integer hashFunction(String identifier) {
        int sum = 0;
        for (int i = 0; i < identifier.length(); i++) {
            sum += identifier.charAt(i);
        }
        return sum % this.dimension;
    }

    public Position addElement(String identifier) {
        Integer hashValue = this.hashFunction(identifier);
        if (this.elements[hashValue] == null) {
            this.elements[hashValue] = new Node(identifier, 0);
            return new Position(hashValue, 0);
        }
        Node currentNode = this.elements[hashValue];
        while (currentNode.nextNode != null) {
            currentNode = currentNode.nextNode;
        }
        Node newNode = new Node(identifier, currentNode.index + 1);
        currentNode.nextNode = newNode;
        return new Position(hashValue, newNode.index);
    }

    public Position search(String identifier) {
        Integer hashValue = this.hashFunction(identifier);
        Node currentNode = this.elements[hashValue];
        if (currentNode != null) {
            while (currentNode != null) {
                if (currentNode.identifier.equals(identifier)) {
                    return new Position(hashValue, currentNode.index);
                }
                currentNode = currentNode.nextNode;
            }
        }
        return new Position(-1, -1);
    }
}
