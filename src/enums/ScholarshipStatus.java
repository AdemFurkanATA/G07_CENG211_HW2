package enums;

public enum ScholarshipStatus {

    FULL("Full", "Full Scholarship", 100),

    HALF("Half", "Half Scholarship", 50);

    // Enum properties
    private final String displayName;
    private final String fullName;
    private final int percentage;

    // Constructor
    ScholarshipStatus(String displayName, String fullName, int percentage) {
        this.displayName = displayName;
        this.fullName = fullName;
        this.percentage = percentage;
    }

    // Getters
    public String getDisplayName() {
        return displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getPercentage() {
        return percentage;
    }

    /**
     Conversion from display name to ScholarshipStatus enum
     @param displayName - Display name (Full, Half)
     @return ScholarshipStatus enum value
     @throws IllegalArgumentException - Invalid name
     */
    public static ScholarshipStatus fromDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            throw new IllegalArgumentException("Display name cannot be null or empty");
        }

        for (ScholarshipStatus status : ScholarshipStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship status: " + displayName);
    }

    /**
     Conversion from percentage value to Scholarship Status enum
     @param percentage - Scholarship percentage (100 or 50)
     @return ScholarshipStatus enum Value
     @throws IllegalArgumentException - Invalid percentage
     */
    public static ScholarshipStatus fromPercentage(int percentage) {
        for (ScholarshipStatus status : ScholarshipStatus.values()) {
            if (status.percentage == percentage) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship percentage: " + percentage);
    }

    /**
     Checks if a given display name is valid
     @param displayName - Name to check
     @return valid if true
     */
    public static boolean isValidDisplayName(String displayName) {
        if (displayName == null || displayName.trim().isEmpty()) {
            return false;
        }
        try {
            fromDisplayName(displayName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    //Checks whether there is a full scholarship
    public boolean isFull() {
        return this == FULL;
    }

    //Checks whether there is a half scholarship
    public boolean isHalf() {
        return this == HALF;
    }

    /**
     Calculates the scholarship amount (based on the default scholarship amount)
     @param baseAmount - Full scholarship amount
     @return Calculated scholarship amount
     */
    public double calculateAmount(double baseAmount) {
        return baseAmount * (percentage / 100.0);
    }

    /**
     Lists all scholarship statuses
     @return All Scholarship Status values
     */
    public static ScholarshipStatus[] getAllStatuses() {
        return ScholarshipStatus.values();
    }

    @Override
    public String toString() {
        return displayName + " (" + percentage + "%)";
    }
}

