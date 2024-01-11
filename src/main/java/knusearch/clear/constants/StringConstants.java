package knusearch.clear.constants;

public enum StringConstants {

    UNDETERMINED("미정");

    private final String description;

    StringConstants(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
