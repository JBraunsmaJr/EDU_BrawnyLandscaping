package models;

/**
 * Helps determine the frequency of service
 */
public enum ServiceInterval
{
    NOT_SPECIFIED("Not specified"),
    WEEKLY("Weekly"),
    BIWEEKLY("Biweekly"),
    BIMONTHLY("Bimonthly"),
    MONTHLY("Monthly");

    private final String displayName;
    ServiceInterval(String name) { displayName = name; }
    public String getDisplayName() { return displayName; }
}
