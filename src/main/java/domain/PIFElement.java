package domain;

public class PIFElement {
    public String token;
    public Position STPosition;

    public PIFElement(String token, Position ST_position) {
        this.token = token;
        this.STPosition = ST_position;
    }

    @Override
    public String toString() {
        return "\nPIFElement{" +
                "token='" + token + '\'' +
                ", ST_position=" + STPosition +
                "}";
    }
}
