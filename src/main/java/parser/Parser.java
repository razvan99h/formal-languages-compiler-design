package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private Grammar grammar;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
    }

    private List<LR0Item> closure(List<LR0Item> items) {
        List<LR0Item> closure = new ArrayList<>(items);

        int index = 0;
        while(index < closure.size()) {
            LR0Item item = closure.get(index);
            int dotPosition = item.getDotPosition();
            if (dotPosition < item.getContent().size()) {
                String symbol = item.getContent().get(dotPosition);
                if (grammar.getNonTerminals().contains(symbol)) {
                    for (List<String> production : grammar.getProductionsOfNonTerminal(symbol)) {
                        LR0Item newItem = new LR0Item(symbol, production);
                        if (!closure.contains(newItem))
                            closure.add(newItem);
                    }
                }
            }
            index ++;
        }

        return closure;
    }

    private List<LR0Item> goTo(List<LR0Item> state, String symbol) {
        List<LR0Item> items = new ArrayList<>();
        for(LR0Item item: state)
            if(item.getDotPosition() < item.getContent().size() && symbol.equals(item.getContent().get(item.getDotPosition())))
                items.add(item);

        for(int i = 0; i < items.size(); i++) {
            LR0Item item = items.get(i);
            items.set(i, new LR0Item(item.getNonTerminal(), item.getContent(), item.getDotPosition() + 1));
        }

        return closure(items);
    }

    public List<List<LR0Item>> canonicalCollection() {
        List<List<LR0Item>> collection = new ArrayList<>();

        LR0Item startItem = new LR0Item(grammar.getStartSymbol(), grammar.getProductionsOfNonTerminal(grammar.getStartSymbol()).get(0));
        List<LR0Item> startingState = new ArrayList<>();
        startingState.add(startItem);
        List<LR0Item> s0 = closure(startingState);
        collection.add(s0);

        System.out.println("s0 = " + s0 + "\n");

        int index = 0;
        while(index < collection.size()){
            List<LR0Item> state = collection.get(index);
            for (String symbol : grammar.getTerminalsAndNonTerminals()) {
                List<LR0Item> goToResult = goTo(state, symbol);
                if (!goToResult.isEmpty() && !collection.contains(goToResult)) {
                    System.out.println("goTo(s" + collection.indexOf(state) + ", " + symbol + ") = " + goToResult);
                    collection.add(goToResult);
                }
            }
            index++;
        }

        return collection;
    }
}
