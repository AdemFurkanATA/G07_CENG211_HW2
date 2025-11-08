package model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public final class TranscriptInfo {

    // Immutable fields
    private final String applicantID;
    private final boolean isValid;

    private static final Set<String> VALID_STATUS_VALUES;

    static {
        Set<String> statuses = new HashSet<>();
        statuses.add("Y");
        statuses.add("YES");
        statuses.add("N");
        statuses.add("NO");
        VALID_STATUS_VALUES = Collections.unmodifiableSet(statuses);
    }

    private static final Set<String> POSITIVE_STATUS_VALUES;

    static {
        Set<String> positive = new HashSet<>();
        positive.add("Y");
        positive.add("YES");
        positive.add("TRUE");
        positive.add("1");
        POSITIVE_STATUS_VALUES = Collections.unmodifiableSet(positive);
    }

    private static final Set<String> NEGATIVE_STATUS_VALUES;

    static {
        Set<String> negative = new HashSet<>();
        negative.add("N");
        negative.add("NO");
        negative.add("FALSE");
        negative.add("0");
        NEGATIVE_STATUS_VALUES = Collections.unmodifiableSet(negative);
    }

    private static final int MIN_APPLICANT_ID_LENGTH = 4;

    // Primary Constructor
    public TranscriptInfo(String applicantID, boolean isValid) {
        // Validation
        validateApplicantID(applicantID);

        // Defensive copying and normalization
        this.applicantID = applicantID.trim();
        this.isValid = isValid;
    }

    // Secondary Constructor
    public TranscriptInfo(String applicantID, String status) {
        // Validation
        validateApplicantID(applicantID);
        validateStatus(status);

        // Defensive copying
        this.applicantID = applicantID.trim();
        this.isValid = parseStatus(status);
    }

    // Copy Constructor
    public TranscriptInfo(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot copy from null TranscriptInfo");

        this.applicantID = other.applicantID;
        this.isValid = other.isValid;
    }

    // Validation Methods

    private void validateApplicantID(String applicantID) {
        Objects.requireNonNull(applicantID, "Applicant ID cannot be null");

        String trimmed = applicantID.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Applicant ID cannot be empty");
        }

        if (trimmed.length() < MIN_APPLICANT_ID_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Applicant ID must be at least %d characters (got: %d)",
                            MIN_APPLICANT_ID_LENGTH, trimmed.length())
            );
        }

        if (!trimmed.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "Applicant ID must contain only digits"
            );
        }

        String prefix = trimmed.substring(0, 2);
        if (!prefix.equals("11") && !prefix.equals("22") && !prefix.equals("33")) {
            throw new IllegalArgumentException(
                    "Invalid Applicant ID prefix: " + prefix +
                            " (Must be 11, 22, or 33)"
            );
        }
    }

    private void validateStatus(String status) {
        Objects.requireNonNull(status, "Status cannot be null");

        String trimmed = status.trim().toUpperCase();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty");
        }

        if (!POSITIVE_STATUS_VALUES.contains(trimmed) &&
                !NEGATIVE_STATUS_VALUES.contains(trimmed)) {
            throw new IllegalArgumentException(
                    "Invalid status: '" + status + "'. " +
                            "Valid values: Y, N, Yes, No, True, False, 1, 0"
            );
        }
    }

    private boolean parseStatus(String status) {
        String normalized = status.trim().toUpperCase();

        if (POSITIVE_STATUS_VALUES.contains(normalized)) {
            return true;
        } else if (NEGATIVE_STATUS_VALUES.contains(normalized)) {
            return false;
        }

        throw new IllegalArgumentException("Invalid status: " + status);
    }

    // Getters

    public String getApplicantID() {
        return applicantID;  // String immutable, güvenli
    }

    public boolean isValid() {
        return isValid;
    }

    // Business Logic Methods

    public String getStatusString() {
        return isValid ? "Y" : "N";
    }

    public String getStatusFullString() {
        return isValid ? "Yes" : "No";
    }

    public String getStatusBooleanString() {
        return String.valueOf(isValid);
    }

    public boolean isInvalid() {
        return !isValid;
    }

    public String getStatusDescription() {
        if (isValid) {
            return "Transcript is valid - Application can proceed";
        } else {
            return "Transcript is invalid - Application will be rejected";
        }
    }

    public boolean canApplicationProceed() {
        return isValid;
    }

    public boolean shouldRejectApplication() {
        return !isValid;
    }

    public String getRiskLevel() {
        return isValid ? "LOW" : "HIGH";
    }

    public String getStatusEmoji() {
        return isValid ? "✓" : "✗";
    }

    public String getStatusColor() {
        return isValid ? "GREEN" : "RED";
    }

    // Static Utility Methods

    public static boolean isValidStatusString(String status) {
        if (status == null) {
            return false;
        }
        String normalized = status.trim().toUpperCase();
        return POSITIVE_STATUS_VALUES.contains(normalized) ||
                NEGATIVE_STATUS_VALUES.contains(normalized);
    }

    public static boolean parseStatusString(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }

        String normalized = status.trim().toUpperCase();

        if (POSITIVE_STATUS_VALUES.contains(normalized)) {
            return true;
        } else if (NEGATIVE_STATUS_VALUES.contains(normalized)) {
            return false;
        }

        throw new IllegalArgumentException(
                "Invalid status: '" + status + "'. " +
                        "Valid values: Y, N, Yes, No, True, False, 1, 0"
        );
    }

    public static Set<String> getPositiveStatusValues() {
        return POSITIVE_STATUS_VALUES;
    }

    public static Set<String> getNegativeStatusValues() {
        return NEGATIVE_STATUS_VALUES;
    }

    public static Set<String> getAllValidStatusValues() {
        Set<String> all = new HashSet<>();
        all.addAll(POSITIVE_STATUS_VALUES);
        all.addAll(NEGATIVE_STATUS_VALUES);
        return Collections.unmodifiableSet(all);
    }

    // Object Methods

    @Override
    public String toString() {
        return String.format("TranscriptInfo{ID='%s', status='%s', valid=%s}",
                applicantID,
                getStatusString(),
                isValid
        );
    }

    public String toDetailedString() {
        return String.format(
                "TranscriptInfo{" +
                        "applicantID='%s', " +
                        "status='%s' (%s), " +
                        "isValid=%s, " +
                        "description='%s', " +
                        "riskLevel='%s'" +
                        "}",
                applicantID,
                getStatusString(),
                getStatusFullString(),
                isValid,
                getStatusDescription(),
                getRiskLevel()
        );
    }

    public String toUserFriendlyString() {
        return String.format("%s %s - %s",
                getStatusEmoji(),
                applicantID,
                getStatusDescription()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        TranscriptInfo other = (TranscriptInfo) obj;
        return Objects.equals(applicantID, other.applicantID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantID);
    }

    public TranscriptInfo clone() {
        return new TranscriptInfo(this);
    }

    // Comparison Methods

    public int compareByStatus(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot compare with null TranscriptInfo");
        return Boolean.compare(this.isValid, other.isValid);
    }

    public int compareByApplicantID(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot compare with null TranscriptInfo");

        try {
            int thisID = Integer.parseInt(this.applicantID);
            int otherID = Integer.parseInt(other.applicantID);
            return Integer.compare(thisID, otherID);
        } catch (NumberFormatException e) {
            return this.applicantID.compareTo(other.applicantID);
        }
    }

    public boolean isDifferentFrom(TranscriptInfo other) {
        return !this.equals(other);
    }

    public boolean hasSameStatusAs(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot compare with null TranscriptInfo");
        return this.isValid == other.isValid;
    }

    public boolean isBetterThan(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot compare with null TranscriptInfo");
        return this.isValid && !other.isValid;
    }

    public boolean isWorseThan(TranscriptInfo other) {
        Objects.requireNonNull(other, "Cannot compare with null TranscriptInfo");
        return !this.isValid && other.isValid;
    }
}