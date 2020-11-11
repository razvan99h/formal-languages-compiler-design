package domain;

import com.sun.tools.javac.util.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyScanner {
    private String fileName;
    private List<String> reservedWords;
    private List<String> separators;
    private List<String> operators;
    private Map<String, Integer> codification;
    private List<String> reservedTokens;
    private FiniteAutomata identifierFA;
    private FiniteAutomata integerFA;
    public PIF pif;
    public SymbolTable symbolTable;


    public MyScanner(String fileName, SymbolTable symbolTable) throws FileNotFoundException {
        this.fileName = fileName;
        this.symbolTable = symbolTable;
        this.reservedWords = new ArrayList<>();
        this.separators = new ArrayList<>();
        this.operators = new ArrayList<>();
        this.codification = new HashMap<>();
        this.pif = new PIF();

        String rW = "începe_program,continuă,oprește,citește,afișează,dacă,sau_dacă,altfel,pentru,cât_timp,întreg," +
                "binar,real,caractere,șir,adevărat,fals,și,sau,negat";
        String sep = "[_]_{_}_(_)_;_:_,_ ";
        String op = "<,<=,>,>=,==,!=,!,&&,||,^,+=,-=,*=,/=,=,+,-,*,%,/";
        this.reservedWords = Arrays.stream(rW.split(",")).collect(Collectors.toList());
        this.separators = Arrays.stream(sep.split("_")).collect(Collectors.toList());
        this.operators = Arrays.stream(op.split(",")).collect(Collectors.toList());
        List<String> everything = Stream.of(this.separators, this.operators, this.reservedWords)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        this.reservedTokens = everything;

        this.codification.put("identifier", 0);
        this.codification.put("constant", 1);
        for (int i = 0; i < everything.size(); i++) {
            this.codification.put(everything.get(i), i + 2);
        }

        this.identifierFA = new FiniteAutomata("data/fa-identifiers.txt");
        this.integerFA = new FiniteAutomata("data/fa-integer-const.txt");
        if (!this.identifierFA.isDeterministic() || !this.integerFA.isDeterministic())
            throw new IllegalArgumentException("The Finite Automatons must be deterministic!");
    }

    private boolean isIdentifier(String token) {
//        return token.matches("^[a-zA-ZăîâşţĂÎÂŞŢ_]([a-zA-ZăîâşţĂÎÂŞŢ_]|[0-9])*$");
        return identifierFA.verifySequence(token);
    }

    private boolean isConstant(String token) {
        boolean isInteger = integerFA.verifySequence(token);
        boolean isString = token.matches("^([\"'])([ a-zA-ZăîâşţĂÎÂŞŢ0-9_.,:;!?<>=+/|~@#$%^&*()\\\\{}\\[\\]\\-])*[\"']$");
        boolean isFloat = token.matches("^(0|(([+\\-])?[1-9](0|[0-9])*))(,[0-9][0-9]*)?$");
        boolean isBoolean = (token.equals("adevărat") || token.equals("fals"));
        return isInteger || isString || isFloat || isBoolean;
    }

    private boolean isEscapedQuote(String line, Integer index) {
        return (index != 0) && line.charAt(index - 1) == '\\';
    }

    private Pair<String, Integer> getStringToken(String line, Integer index, char quote) {
        String token = "";
        int quoteCount = 0;

        while (index < line.length() && quoteCount < 2) {
            if (line.charAt(index) == quote && !isEscapedQuote(line, index)) {
                quoteCount += 1;
            }
            token += line.charAt(index);
            index += 1;
        }
        return new Pair<>(token, index);
    }

    private boolean isPartOfOperator(char character) {
        for (String operator : this.operators)
            if (operator.contains("" + character))
                return true;
        return false;
    }

    private Pair<String, Integer> getOperatorToken(String line, Integer index) {
        String token = "";
        while (index < line.length() && this.isPartOfOperator(line.charAt(index))) {
            token += line.charAt(index);
            index += 1;
        }
        return new Pair<>(token, index);
    }

    private List<String> getLineTokens(String line) {
        List<String> tokens = new ArrayList<>();
        String token = "";
        int index = 0;
        while (index < line.length()) {
            if (line.charAt(index) == '"' || line.charAt(index) == '\'') {
                if (!token.equals("")) {
                    tokens.add(token);
                }
                Pair<String, Integer> res = this.getStringToken(line, index, line.charAt(index));
                token = res.fst;
                index = res.snd;
                tokens.add(token);
                token = "";
            } else if (this.isPartOfOperator(line.charAt(index))) {
                if (!token.equals("")) {
                    tokens.add(token);
                }
                char character = line.charAt(index);
                boolean lastElemIdentifier = this.pif.elements.get(this.pif.elements.size() - 1).token.equals("IDENTIFIER");
                boolean lastElemConstant = this.pif.elements.get(this.pif.elements.size() - 1).token.equals("CONSTANT");
                if ((character == '+' || character == '-') && !(lastElemConstant || lastElemIdentifier)) {
                    token += character;
                    index += 1;
                }
                else {
                    Pair<String, Integer> res = this.getOperatorToken(line, index);
                    token = res.fst;
                    index = res.snd;
                    tokens.add(token);
                    token = "";
                }
            } else if (this.separators.contains("" + line.charAt(index))) {
                if (!token.equals("")) {
                    tokens.add(token);
                }
                token = "" + line.charAt(index);
                index += 1;
                tokens.add(token);
                token = "";
            } else {
                token += line.charAt(index);
                index += 1;
            }
        }
        if (!token.equals("")) {
            tokens.add(token);
        }
        return tokens;
    }

    public void startScanning() throws Exception {
        File file = new File(this.fileName);
        Scanner reader = new Scanner(file);
        int currentLine = 1;
        while (reader.hasNextLine()) {
            String line = reader.nextLine().strip();
            List<String> tokens = this.getLineTokens(line);

            for (String token : tokens) {
                if (this.reservedTokens.contains(token)) {
                    if (!token.equals(" "))
                        this.pif.addElement(token, new Position(-1, 0));
                } else if (this.isIdentifier(token)) {
                    Position position = this.symbolTable.search(token);
                    if (position.hashTableIndex == -1) {
                        position = this.symbolTable.add(token);
                    }
                    this.pif.addElement("IDENTIFIER", position);
                } else if (this.isConstant(token)) {
                    Position position = this.symbolTable.search(token);
                    if (position.hashTableIndex == -1) {
                        position = this.symbolTable.add(token);
                    }
                    this.pif.addElement("CONSTANT", position);
                } else {
                    System.err.println("Unknown token " + token + " at line " + currentLine);
                    return;
                }
            }

            System.out.println(line);

            currentLine += 1;
        }
        reader.close();
    }


    @Override
    public String toString() {
        return "MyScanner{" +
                "fileName='" + fileName + '\'' +
                ", \nreservedWords=" + reservedWords +
                ", \nseparators=" + separators +
                ", \noperators=" + operators +
                ", \ncodification=" + codification +
                ", \nreservedTokens=" + reservedTokens +
                ", \npif=" + pif +
                ", \nsymbolTable=" + symbolTable +
                '}';
    }
}
