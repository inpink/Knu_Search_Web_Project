package knusearch.clear.constants;

public enum IntegerConstants {

    ;


    private final int number;

    IntegerConstants(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
