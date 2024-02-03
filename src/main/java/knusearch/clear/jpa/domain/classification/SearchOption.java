package knusearch.clear.jpa.domain.classification;

public enum SearchOption {

    TITLE(0), TEXT(1), TITLE_AND_TEXT(2);

    private final int index;

    SearchOption(final int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
