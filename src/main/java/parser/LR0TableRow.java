package parser;

import java.util.ArrayList;
import java.util.List;

public class LR0TableRow {
    public int stateIndex;
    public String action;
    public String reduceNonTerminal;
    public List<String> reduceProductionBody;
    public List<LR0TableRowGoto> goTo;

    public LR0TableRow() {
        this.reduceProductionBody = new ArrayList<>();
        this.goTo = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "LR0TableRow{" +
                "stateIndex=" + stateIndex +
                ", action='" + action + '\'' +
                ", goTo=" + goTo +
                '}';
    }
}
