package knusearch.clear.jpa.domain;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import lombok.Getter;

public enum Classification {

    ACADEMIC_NOTIFICATION("학사", 0),
    SCHOLARSHIP("장학", 1),
    LEARNING_KNOWHOW("학습/상담", 2),
    EMPLOYMENT_STARTUP("취창업", 3);

    @Getter
    private final String description;

    private final int index;

    Classification(String description, int index) {
        this.description = description;
        this.index = index;
    }

    public static int calcMinIndex() {
        return getIndexes()
                .min()
                .orElseThrow(() -> new NoSuchElementException("No enum constants in Classification"));
    }

    public static int calcMaxIndex() {
        return getIndexes()
                .max()
                .orElseThrow(() -> new NoSuchElementException("No enum constants in Classification"));
    }

    private static IntStream getIndexes() {
        return Arrays.stream(Classification.values())
                .mapToInt(Classification::getIndex);
    }

    public String getDescription() {
        return description;
    }

    public int getIndex() {
        return index;
    }
}
