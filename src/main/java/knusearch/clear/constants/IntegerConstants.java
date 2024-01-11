package knusearch.clear.constants;

public enum IntegerConstants {

    MIN_CLAS(0),
    MAX_CLAS(4),
    ACADEMIC_NOTIFICATION(0),
    SCHOLARSHIP(1),
    LEARNING_KNOWHOW(2),
    EMPLOYMENT_STARTUP(3);


    private final int number;

    IntegerConstants(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return String.valueOf(number);
    }
}
