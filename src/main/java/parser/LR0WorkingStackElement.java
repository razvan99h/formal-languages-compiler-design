package parser;

public class LR0WorkingStackElement {
    public int stateIndex;
    public String symbol;

    public LR0WorkingStackElement(int stateIndex, String symbol) {
        this.stateIndex = stateIndex;
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "LR0WorkingStackElement{" +
                "stateIndex=" + stateIndex +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
