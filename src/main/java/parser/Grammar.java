package parser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Grammar {
    private List<String> terminals;
    private List<String> nonTerminals;
    private HashMap<String, List<List<String>>> productions;
    private String startSymbol;
    private String fileName;

    public Grammar(String fileName) throws IOException {
        this.terminals = new ArrayList<>();
        this.nonTerminals = new ArrayList<>();
        this.productions = new HashMap<>();
        this.fileName = fileName;
        this.readFromFile();
    }

    public List<String> getTerminals() {
        return terminals;
    }

    public List<String> getNonTerminals() {
        return nonTerminals;
    }

    public HashMap<String, List<List<String>>> getProductions() {
        return productions;
    }

    public List<List<String>> getProductionsOfNonTerminal(String nonTerminal) {
        return this.productions.get(nonTerminal);
    }

    private void validateElement(String element) {
        if (!this.nonTerminals.contains(element) && !this.terminals.contains(element))
            throw new RuntimeException("The element '" + element + "' is neither a terminal nor non terminal!");
    }

    private void validateProductionElement(List<String> productionElements) {
        for (String element : productionElements) {
            this.validateElement(element);
        }
    }

    private void readProductions(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null || line.equals("")) {
                break;
            }
            List<String> productionLine = Arrays.asList(line.strip().split("->"));
            String nonTerminal = productionLine.get(0).strip();

            List<String> rightSide = Arrays.asList(productionLine.get(1).strip().split("\\|"));
            for (String element : rightSide) {
                List<String> productionElements =  Arrays.asList(element.strip().split(" "));
                this.validateProductionElement(productionElements);

                List<List<String>> existingProductions = this.productions.get(nonTerminal);
                if (existingProductions == null) {
                    existingProductions = new ArrayList<>();
                    existingProductions.add(productionElements);
                    this.productions.put(nonTerminal, existingProductions);
                } else {
                    existingProductions.add(productionElements);
                }
            }
        }
    }

    private void readFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
        this.nonTerminals = Arrays.asList(reader.readLine().split(","));
        this.terminals = Arrays.asList(reader.readLine().split(","));
        this.startSymbol = reader.readLine();

        this.validateElement(this.startSymbol);

        this.readProductions(reader);
        reader.close();
    }

    @Override
    public String toString() {
        return "Grammar {" +
                "\n\tnonTerminals = " + nonTerminals +
                ",\n\tterminals = " + terminals +
                ",\n\tstartSymbol = '" + startSymbol + '\'' +
                ",\n\tproductions = " + productions +
                "\n}";
    }
}
