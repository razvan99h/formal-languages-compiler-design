package domain;

import java.util.List;

public class Transition {
    public String fromState;
    public String symbol;
    public List<String> toStates;

    public Transition(String fromState, String symbol, List<String> toStates) {
        this.fromState = fromState;
        this.symbol = symbol;
        this.toStates = toStates;
    }

    @Override
    public String toString() {
        return "\n\t\tTransition{" +
                "fromState='" + fromState + '\'' +
                ", symbol='" + symbol + '\'' +
                ", toStates=" + toStates +
                '}';
    }
}
