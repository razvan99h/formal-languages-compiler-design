package domain;

public class PIFElement {
    public String token;
    public Position ST_position;

    public PIFElement(String token, Position ST_position) {
        this.token = token;
        this.ST_position = ST_position;
    }

    @Override
    public String toString() {
        return "PIFElement{" +
                "token='" + token + '\'' +
                ", ST_position=" + ST_position +
                '}';
    }
}
