package orm.models;

public enum DbType
{
    CHAR("CHAR"),
    VARCHAR("VARCHAR"),
    TEXT("Text"),
    MEDIUM_TEXT("MEDIUMTEXT"),
    LONG_TEXT("LONGTEXT"),
    BLOB("BLOB"),
    INT("INT"),
    DECIMAL("DECIMAL"),
    DATE("DATE"),
    DATE_TIME("DATETIME"),
    TIME("TIME"),
    TIMESTAMP("TIMESTAMP"),
    YEAR("YEAR"),
    JSON("JSON");

    private String name;
    DbType(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getName() { return name; }

    /**
     *
     * @return
     */
    public boolean requiresLength()
    {
        switch(name)
        {
            case "CHAR":
            case "VARCHAR":
                return true;
            default:
                return false;
        }
    }
}
