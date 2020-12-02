package parser;

public class LR0TableRowGoto {
    public String symbol;
    public int stateIndex;

    public LR0TableRowGoto(String symbol, int stateIndex) {
        this.symbol = symbol;
        this.stateIndex = stateIndex;
    }

    @Override
    public String toString() {
        return  "{'" + symbol + '\'' + ", " + stateIndex + "}";
    }
}
