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