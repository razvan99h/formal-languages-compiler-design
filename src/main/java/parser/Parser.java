package parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Parser {
    private Grammar grammar;
    private LR0Table table;
    private List<List<LR0Item>> canonicalCollection;

    public Parser(Grammar grammar) {
        this.grammar = grammar;
        this.table = new LR0Table();
    }

    private List<LR0Item> closure(List<LR0Item> items) {
        List<LR0Item> closure = new ArrayList<>(items);

        int index = 0;
        while (index < closure.size()) {
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
            index++;
        }

        return closure;
    }

    private List<LR0Item> goTo(List<LR0Item> state, String symbol) {
        List<LR0Item> items = new ArrayList<>();
        for (LR0Item item : state)
            if (item.getDotPosition() < item.getContent().size() && symbol.equals(item.getContent().get(item.getDotPosition())))
                items.add(item);

        for (int i = 0; i < items.size(); i++) {
            LR0Item item = items.get(i);
            items.set(i, new LR0Item(item.getNonTerminal(), item.getContent(), item.getDotPosition() + 1));
        }

        return closure(items);
    }

    public List<List<LR0Item>> canonicalCollection(boolean showOutput) {
        List<List<LR0Item>> collection = new ArrayList<>();

        LR0Item startItem = new LR0Item(grammar.getStartSymbol(), grammar.getProductionsOfNonTerminal(grammar.getStartSymbol()).get(0));
        List<LR0Item> startingState = new ArrayList<>();
        startingState.add(startItem);
        List<LR0Item> s0 = closure(startingState);
        collection.add(s0);

        if (showOutput)
            System.out.println("s0 = " + s0);

        int index = 0;
        while (index < collection.size()) {
            List<LR0Item> state = collection.get(index);
            for (String symbol : grammar.getTerminalsAndNonTerminals()) {
                List<LR0Item> goToResult = goTo(state, symbol);
                if (!goToResult.isEmpty() && !collection.contains(goToResult)) {
                    if (showOutput)
                        System.out.println("s" + collection.size() + " = goTo(s" + collection.indexOf(state) + ", " + symbol + ") = " + goToResult);
                    collection.add(goToResult);
                }
            }
            index++;
        }

        return collection;
    }

    public void buildLR0Table(boolean showOutput) {
        this.canonicalCollection = this.canonicalCollection(showOutput);
        for (int i = 0; i < canonicalCollection.size(); i++) {
            List<LR0Item> state = canonicalCollection.get(i);
            LR0TableRow row = new LR0TableRow();
            row.stateIndex = i;

            String action = "";
            if (state.stream().anyMatch(item -> !item.dotInTheEnd()))
                action = "shift";
            else if (state.stream().anyMatch(item -> grammar.getStartSymbol().equals(item.getNonTerminal()) && item.dotInTheEnd()))
                action = "accept";
            else if (state.stream().anyMatch(LR0Item::dotInTheEnd)) {
                LR0Item production = state.stream().filter(LR0Item::dotInTheEnd).findAny().orElse(null);
                row.reduceNonTerminal = production.getNonTerminal();
                row.reduceProductionBody = production.getContent();
                action = "reduce " + production.getNonTerminal() + " -> " + production.getContent();
            }
            if (action.equals(""))
                action = "error";

            row.action = action;

            List<LR0TableRowGoto> goTos = new ArrayList<>();
            for (String symbol : grammar.getTerminalsAndNonTerminals()) {
                List<LR0Item> goToResult = goTo(state, symbol);
                if (!goToResult.isEmpty()) {
                    int stateIndex = canonicalCollection.indexOf(goToResult);
                    goTos.add(new LR0TableRowGoto(symbol, stateIndex));
                }
            }
            row.goTo = goTos;
            table.rows.add(row);

            System.out.println(table.rows.get(i));
        }
    }

    public void parseSequence(String sequence, boolean showOutput) {
        this.buildLR0Table(showOutput);
        int stateIndex = 0;
    }
}
