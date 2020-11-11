package domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class FiniteAutomata {
    private Set<String> states;
    private Set<String> alphabet;
    private String initialState;
    private Set<String> finalStates;
    private List<Transition> transitions;

    public FiniteAutomata() {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new ArrayList<>();
    }

    public FiniteAutomata(String fileName) throws FileNotFoundException {
        this.states = new HashSet<>();
        this.alphabet = new HashSet<>();
        this.finalStates = new HashSet<>();
        this.transitions = new ArrayList<>();
        this.readFromFile(fileName);
    }

    public void readFromFile(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner reader = new Scanner(file);
        this.parseLine(this.states, reader.nextLine().strip(), "states");
        this.parseLine(this.alphabet, reader.nextLine().strip(), "alphabet");
        this.initialState = reader.nextLine().strip();
        // TODO: check finalStates to appear in this.states
        this.parseLine(this.finalStates, reader.nextLine().strip(), "alphabet");

        while (reader.hasNextLine()) {
            Transition newTransition = this.parseTransitionLine(reader.nextLine().strip());
            // TODO: check if transition exists (i.e. same initial state and symbol) and if it exists, add the states
            //  not a new transition
            for (Transition transition : this.transitions) {
                if (transition.fromState.equals(newTransition.fromState) && transition.symbol.equals(newTransition.symbol)) {
                    transition.toStates.add(newTransition.fromState);
                }
            }
            this.transitions.add(newTransition);
        }
    }

    public boolean isDeterministic() {
        for (Transition transition : this.transitions) {
            if (transition.toStates.size() > 1)
                return false;
        }
        return true;
    }

    public boolean verifySequence(String sequence) {
        String state = initialState;
        int i;
        for (i = 0; i < sequence.length(); i++) {
            String symbol = "" + sequence.charAt(i);
            boolean foundTransition = false;
            for (Transition transition : this.transitions) {
                if (transition.fromState.equals(state) && transition.symbol.equals(symbol)) {
                    foundTransition = true;
                    state = transition.toStates.get(0);
                }
            }
            if (!foundTransition)
                return false;
        }
        return i == sequence.length() && this.finalStates.contains(state);
    }

    private void parseLine(Set<String> set, String line, String element) {
        List<String> items = Arrays.asList(line.split(","));
        items.forEach(item -> {
            if (set.contains(item))
                throw new IllegalArgumentException("FA " + element + "  must be unique!");
            set.add(item);
        });
    }

    private Transition parseTransitionLine(String line) {
        List<String> items = Arrays.asList(line.split(","));
        // TODO: do some checks (elements lenght >= 2) + states to appear in this.states + elements[1] in alphabet
        return new Transition(items.get(0), items.get(1), items.subList(2, items.size()));
    }

    @Override
    public String toString() {
        return "FiniteAutomata {" +
                "\n\tstates=" + states +
                ", \n\talphabet=" + alphabet +
                ", \n\tinitialState='" + initialState + '\'' +
                ", \n\tfinalStates=" + finalStates +
                ", \n\ttransitions=" + transitions +
                "\n}";
    }
}
