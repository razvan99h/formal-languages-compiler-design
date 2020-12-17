package parser;


import com.sun.tools.javac.util.Pair;

import java.util.List;
import java.util.Stack;

public class TreeOutput {
    Node root;

    public Node addSibling(Node node, String value, Integer level, Integer index) {
        if (node == null) {
            return null;
        }
        while (node.getRightSibling() != null) {
            node = node.getRightSibling();
        }

        Node sibling = new Node(value);
        sibling.setParent(node.getParent());
        sibling.setLevel(level);
        sibling.setIndex(index);
        node.setRightSibling(sibling);

        return node.getRightSibling();
    }

    public Node addChild(Node node, String value, Integer level, Integer index) {
        if (node == null) {
            return null;
        }

        if (node.getLeftChild() != null) {
            return addSibling(node.getLeftChild(), value, level, index);
        } else {
            Node child = new Node(value);
            child.setParent(node);
            child.setLevel(level);
            child.setIndex(index);
            node.setLeftChild(child);
            return node.getLeftChild();
        }
    }

    public void addParsedSequence(Stack<Integer> parsedSequence, Grammar grammar) {
        Node lastParent = null;
        Integer level = 0;
        Integer index = 0;

        while (!parsedSequence.empty()) {
            int productionIndex = parsedSequence.pop();
            Pair<String, List<String>> production = grammar.getOrderedProductions().get(productionIndex);

            if (productionIndex == 1) {
                root = new Node(production.fst);
                root.setLevel(level);
                root.setIndex(index);
                level++;
                index++;
                lastParent = root;
            }

            for (String symbol : production.snd) {
                Node child = addChild(lastParent, symbol, level, index);
                if (!parsedSequence.empty()) {
                    int nextProductionIndex = parsedSequence.peek();
                    Pair<String, List<String>> nextProduction = grammar.getOrderedProductions().get(nextProductionIndex);
                    if (symbol.equals(nextProduction.fst)) {
                        lastParent = child;
                    }
                }
                index++;
            }
            level++;
        }
    }

    public void traverseTree(Node root) {
        if (root == null) {
            return;
        }

        while (root != null) {
            System.out.println(root);
            if (root.getLeftChild() != null) {
                traverseTree(root.getLeftChild());
            }
            root = root.getRightSibling();
        }
    }

    public Node getRoot() {
        return root;
    }
}
