package domain;

public class Node {
    public String identifier; // identifiers and constants always stored as a string
    public Integer index;
    public Node nextNode;

    public Node(String identifier, Integer index) {
        this.identifier = identifier;
        this.index = index;
        this.nextNode = null;
    }

}
