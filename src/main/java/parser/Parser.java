package parser;

import domain.MyScanner;
import domain.PIFElement;
import domain.SymbolTable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Parser {
    private Grammar grammar;
    private LR0Table table;
    private List<List<LR0Item>> canonicalCollection;

    public Parser(Grammar grammar, boolean showOutput) {
        this.grammar = grammar;
        this.table = new LR0Table();
        this.buildLR0Table(showOutput);
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
                action = "reduce " + production.toProductionString();
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

    public void parse(Stack<String> inputStack) {
        Stack<LR0WorkingStackElement> workingStack = new Stack<>();
        Stack<String> outputStack = new Stack<>();
        String lastSymbol = "";
        int stateIndex = 0;
        boolean end = false;
        workingStack.push(new LR0WorkingStackElement(0, ""));
        try {
            do {
                if (stateIndex == 20) {
                    int REMOVE_this_line = 1;
                }
                LR0TableRow row = this.table.rows.get(stateIndex);
                if (row.action.equals("shift")) {
                    String character = inputStack.pop();
                    LR0TableRowGoto state = row.goTo
                            .stream()
                            .filter(e -> e.symbol.equals(character))
                            .findAny()
                            .orElse(null);
                    stateIndex = state.stateIndex;
                    lastSymbol = character;
                    workingStack.push(new LR0WorkingStackElement(state.stateIndex, character));
                } else if (row.action.equals("reduce " + row.reduceProductionString())) {
                    List<String> reduceProductionBody = new ArrayList<> (row.reduceProductionBody);
                    while (reduceProductionBody.contains(workingStack.peek().symbol) && !workingStack.empty()){
                        reduceProductionBody.remove(workingStack.peek().symbol);
                        workingStack.pop();
                    }
                    LR0TableRowGoto state = this.table.rows.get(workingStack.peek().stateIndex).goTo
                            .stream()
                            .filter(e -> e.symbol.equals(row.reduceNonTerminal))
                            .findAny()
                            .orElse(null);
                    stateIndex = state.stateIndex;
                    lastSymbol = row.reduceNonTerminal;
                    workingStack.push(new LR0WorkingStackElement(state.stateIndex, row.reduceNonTerminal));
                    outputStack.push(row.reduceProductionString());
                } else {
                    if (row.action.equals("accept")) {
                        // TODO: check for the empty stack to be empty
                        System.out.println("SUCCES " + outputStack);
                        end = true;
                    }
                    if (row.action.equals("error")) {
                        System.err.println("ERROR at state " + stateIndex);
                        end = true;
                    }

                }
            } while (!end);
        }
        catch (NullPointerException ex) {
//            ex.printStackTrace();
            System.err.println("ERROR at state " + stateIndex + " - after symbol " + lastSymbol);
        }
    }

    public void parseCharacterSequence(String sequence) {
        Stack<String> inputStack = new Stack<>();
        Arrays.stream(new StringBuilder(sequence).reverse().toString().split( "" )).forEach(inputStack::push);
        this.parse(inputStack);
    }

    public void parseFile(String fileName) throws Exception {
        Stack<String> inputStack = new Stack<>();

        MyScanner scanner = new MyScanner(fileName, new SymbolTable(71));
        scanner.startScanning();
        for(int i = scanner.pif.elements.size() - 1; i >= 0; i--) {
            PIFElement pifElement = scanner.pif.elements.get(i);
            inputStack.push(pifElement.token);
        }
        this.parse(inputStack);
    }
}
