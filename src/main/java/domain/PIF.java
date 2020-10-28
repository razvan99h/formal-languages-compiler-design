package domain;

import java.util.ArrayList;
import java.util.List;

public class PIF {
    public List<PIFElement> elements;

    public PIF() {
        this.elements = new ArrayList<>();
    }

    public void addElement(String token, Position STPosition) {
        this.elements.add(new PIFElement(token, STPosition));
    }

    @Override
    public String toString() {
        return "\nPIF{\n" +
                "elements=" + elements +
                "\n}";
    }
}
