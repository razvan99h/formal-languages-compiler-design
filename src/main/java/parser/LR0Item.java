package parser;

import java.util.List;
import java.util.Objects;

public class LR0Item {
    private String nonTerminal;
    private List<String> content;  
    private int dotPosition;

    public LR0Item(String nonTerminal, List<String> content, int dotPosition) {
        this.nonTerminal = nonTerminal;
        this.content = content;
        this.dotPosition = dotPosition;
    }

    public LR0Item(String nonTerminal, List<String> content) {
        this.nonTerminal = nonTerminal;
        this.content = content;
        this.dotPosition = 0;
    }

    public String getNonTerminal() {
        return nonTerminal;
    }

    public List<String> getContent() {
        return content;
    }

    public int getDotPosition() {
        return dotPosition;
    }

    public boolean dotInTheEnd() {
        return dotPosition == content.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LR0Item lr0Item = (LR0Item) o;
        return dotPosition == lr0Item.dotPosition &&
                Objects.equals(nonTerminal, lr0Item.nonTerminal) &&
                Objects.equals(content, lr0Item.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nonTerminal, content, dotPosition);
    }

    public String toProductionString() {
        return this.nonTerminal + " -> " + this.getContent();
    }

    @Override
    public String toString() {
        String result = "[ " + nonTerminal + " -> ";
        for (int i = 0; i <= content.size(); i++) {
            if (dotPosition == i) 
                result += ".";
            if(i < content.size())
                result += content.get(i);
        }
        result += " ]";
        return result;
    }
}