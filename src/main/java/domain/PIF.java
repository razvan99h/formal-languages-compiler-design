package domain;

import java.util.ArrayList;
import java.util.List;

public class PIF {
    public List<PIFElement> elements;

    public PIF() {
        this.elements = new ArrayList<>();
    }

    public void addElement(String token, Position ST_position) {
        this.elements.add(new PIFElement(token, ST_position));
    }

    @Override
    public String toString() {
        return "PIF{" +
                "elements=" + elements +
                '}';
    }
}
