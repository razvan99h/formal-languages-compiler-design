package domain;

public class Position {
    public Integer hashTableIndex;
    public Integer linkedListIndex;

    public Position(Integer hashTableIndex, Integer linkedListIndex) {
        this.hashTableIndex = hashTableIndex;
        this.linkedListIndex = linkedListIndex;
    }

    @Override
    public String toString() {
        return "Position{" +
                "hashTableIndex=" + hashTableIndex +
                ", linkedListIndex=" + linkedListIndex +
                '}';
    }
}
