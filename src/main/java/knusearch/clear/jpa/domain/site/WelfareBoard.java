package knusearch.clear.jpa.domain.site;

public enum WelfareBoard implements Board {
    NOTICE("공지사항", "22dd7f703ec676ffdecdd6b4e4fe1b1b.do");

    private final String name;
    private final String encryptedName;

    WelfareBoard(String name, String encryptedName) {
        this.name = name;
        this.encryptedName = encryptedName;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEncryptedName() {
        return null;
    }
}
