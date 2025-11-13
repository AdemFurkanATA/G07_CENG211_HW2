package model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public final class TranscriptInfo {

    private final String applicantID;
    private final boolean isValid;

    private static final Set<String> POSITIVE_STATUS_VALUES;
    private static final Set<String> NEGATIVE_STATUS_VALUES;

    static {
        Set<String> positive = new HashSet<>();
        Collections.addAll(positive, "Y", "YES", "TRUE", "1");
        POSITIVE_STATUS_VALUES = Collections.unmodifiableSet(positive);

        Set<String> negative = new HashSet<>();
        Collections.addAll(negative, "N", "NO", "FALSE", "0");
        NEGATIVE_STATUS_VALUES = Collections.unmodifiableSet(negative);
    }

    // Constructors
    public TranscriptInfo(String applicantID, boolean isValid) {
        validateApplicantID(applicantID);
        this.applicantID = applicantID.trim();
        this.isValid = isValid;
    }

    public TranscriptInfo(String applicantID, String status) {
        validateApplicantID(applicantID);
        this.applicantID = applicantID.trim();
        this.isValid = parseStatus(status);
    }

    // Validation
    private void validateApplicantID(String applicantID) {
        Objects.requireNonNull(applicantID, "Applicant ID cannot be null");
        if (applicantID.trim().length() < 4) throw new IllegalArgumentException("Invalid ID length");
    }

    private boolean parseStatus(String status) {
        String normalized = status.trim().toUpperCase();
        if (POSITIVE_STATUS_VALUES.contains(normalized)) return true;
        if (NEGATIVE_STATUS_VALUES.contains(normalized)) return false;
        throw new IllegalArgumentException("Invalid status: " + status);
    }

    public String getApplicantID() {
        return applicantID;
    }

    public boolean isValid() {
        return isValid;
    }

    @Override
    public String toString() {
        return "TranscriptInfo{ID='" + applicantID + "', valid=" + isValid + "}";
    }
}