package parser;

public class Node {
    private String value;
    private Node parent;
    private Node leftChild;
    private Node rightSibling;
    private Integer level;
    private Integer index;

    public Node(String value, Node parent, Node leftChild, Node rightSibling) {
        this.value = value;
        this.parent = parent;
        this.leftChild = leftChild;
        this.rightSibling = rightSibling;
    }

    public Node(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Node getParent() {
        return parent;
    }

    public Node getLeftChild() {
        return leftChild;
    }

    public Node getRightSibling() {
        return rightSibling;
    }

    public Integer getLevel() {
        return level;
    }

    public Integer getIndex() {
        return index;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightSibling(Node rightSibling) {
        this.rightSibling = rightSibling;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "Node{" +
                "value='" + value + '\'' +
                ", parent=" + (parent != null ? parent.getIndex() : -1) +
                ", leftChild=" + (leftChild != null ? leftChild.getIndex() : -1) +
                ", rightSibling=" + (rightSibling != null ? rightSibling.getIndex() : -1) +
                ", level=" + level +
                ", index=" + index +
                '}';
    }
}
