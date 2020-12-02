package parser;

import java.util.ArrayList;
import java.util.List;

public class LR0Table {
    public List<LR0TableRow> rows;

    public LR0Table() {
        this.rows = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "LR0Table{" +
                "rows=" + rows +
                '}';
    }
}
