package model;

import java.util.Objects;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public final class Document {  // final class - extend edilemez (güvenlik)

    // Immutable Fields
    private final String applicantID;       // Belgenin sahibi (immutable)
    private final String documentType;      // Belge türü (immutable)
    private final int durationInMonths;     // Geçerlilik süresi (immutable)

    private static final Set<String> VALID_DOCUMENT_TYPES;

    static {
        Set<String> types = new HashSet<>();
        types.add("ENR");
        types.add("REC");
        types.add("SAV");
        types.add("RSV");
        types.add("GRP");
        VALID_DOCUMENT_TYPES = Collections.unmodifiableSet(types);
    }

    private static final Set<String> MANDATORY_TYPES;

    static {
        Set<String> mandatory = new HashSet<>();
        mandatory.add("ENR");
        MANDATORY_TYPES = Collections.unmodifiableSet(mandatory);
    }

    private static final int MIN_DURATION = 0;
    private static final int MAX_DURATION = 120;  // Max 10 yıl (120 ay)
    private static final int MIN_APPLICANT_ID_LENGTH = 4;

    // Primary Constructor
    public Document(String applicantID, String documentType, int durationInMonths) {
        // Validation
        validateApplicantID(applicantID);
        validateDocumentType(documentType);
        validateDuration(durationInMonths);

        // Defensive copying and normalization
        this.applicantID = applicantID.trim();
        this.documentType = documentType.trim().toUpperCase();  // Büyük harfe çevir
        this.durationInMonths = durationInMonths;
    }

    // Copy Constructor
    public Document(Document other) {
        Objects.requireNonNull(other, "Cannot copy from null Document");

        // Immutable copy
        this.applicantID = other.applicantID;
        this.documentType = other.documentType;
        this.durationInMonths = other.durationInMonths;
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

    private void validateDocumentType(String documentType) {
        Objects.requireNonNull(documentType, "Document type cannot be null");

        String trimmed = documentType.trim().toUpperCase();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("Document type cannot be empty");
        }

        if (!VALID_DOCUMENT_TYPES.contains(trimmed)) {
            throw new IllegalArgumentException(
                    "Invalid document type: " + trimmed +
                            ". Valid types: " + VALID_DOCUMENT_TYPES
            );
        }
    }

    private void validateDuration(int duration) {
        if (duration < MIN_DURATION) {
            throw new IllegalArgumentException(
                    String.format("Duration cannot be negative (got: %d)", duration)
            );
        }

        if (duration > MAX_DURATION) {
            throw new IllegalArgumentException(
                    String.format("Duration cannot exceed %d months (got: %d)",
                            MAX_DURATION, duration)
            );
        }
    }

    // Getters

    public String getApplicantID() {
        return applicantID;  // String immutable, güvenli
    }

    public String getDocumentType() {
        return documentType;  // String immutable, güvenli
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }


    // Business Logic Methods

    public boolean isMandatory() {
        return MANDATORY_TYPES.contains(documentType);
    }

    public String getDescription() {
        switch (documentType) {
            case "ENR":
                return "Enrollment Certificate (Mandatory)";
            case "REC":
                return "Recommendation Letter";
            case "SAV":
                return "Savings Document";
            case "RSV":
                return "Research Supervisor Approval";
            case "GRP":
                return "Grant Proposal";
            default:
                return "Unknown Document Type";
        }
    }

    public boolean isValid() {
        return durationInMonths > 0;
    }

    public boolean isExpired() {
        return durationInMonths == 0;
    }

    public double getDurationInYears() {
        return durationInMonths / 12.0;
    }

    public String getRelevantScholarshipType() {
        switch (documentType) {
            case "ENR":
                return "ALL";
            case "REC":
                return "11";
            case "SAV":
                return "22";
            case "RSV":
            case "GRP":
                return "33";
            default:
                return "UNKNOWN";
        }
    }

    public boolean isRelevantFor(String scholarshipTypeCode) {
        if (scholarshipTypeCode == null || scholarshipTypeCode.trim().isEmpty()) {
            return false;
        }

        String relevantType = getRelevantScholarshipType();
        return relevantType.equals("ALL") || relevantType.equals(scholarshipTypeCode.trim());
    }

    public int getImportanceLevel() {
        switch (documentType) {
            case "ENR":
                return 5;
            case "REC":
            case "RSV":
                return 4;
            case "SAV":
            case "GRP":
                return 3;
            default:
                return 1;
        }
    }

    // Static Utility Methods

    public static boolean isValidDocumentType(String documentType) {
        if (documentType == null) {
            return false;
        }
        return VALID_DOCUMENT_TYPES.contains(documentType.trim().toUpperCase());
    }

    public static Set<String> getValidDocumentTypes() {
        return VALID_DOCUMENT_TYPES;
    }

    public static Set<String> getMandatoryDocumentTypes() {
        return MANDATORY_TYPES;
    }

    // Object Methods

    @Override
    public String toString() {
        return String.format("Document{ID='%s', type='%s', duration=%d months%s}",
                applicantID,
                documentType,
                durationInMonths,
                isMandatory() ? " (MANDATORY)" : ""
        );
    }

    public String toDetailedString() {
        return String.format(
                "Document{" +
                        "applicantID='%s', " +
                        "type='%s', " +
                        "description='%s', " +
                        "duration=%d months (%.1f years), " +
                        "valid=%s, " +
                        "mandatory=%s, " +
                        "importance=%d/5" +
                        "}",
                applicantID,
                documentType,
                getDescription(),
                durationInMonths,
                getDurationInYears(),
                isValid(),
                isMandatory(),
                getImportanceLevel()
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

        Document other = (Document) obj;
        return Objects.equals(applicantID, other.applicantID) &&
                Objects.equals(documentType, other.documentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(applicantID, documentType);
    }

    public Document clone() {
        return new Document(this);
    }

    // Comparison Methods

    public int compareByDuration(Document other) {
        Objects.requireNonNull(other, "Cannot compare with null Document");
        return Integer.compare(this.durationInMonths, other.durationInMonths);
    }

    public int compareByImportance(Document other) {
        Objects.requireNonNull(other, "Cannot compare with null Document");
        return Integer.compare(this.getImportanceLevel(), other.getImportanceLevel());
    }

    public int compareByType(Document other) {
        Objects.requireNonNull(other, "Cannot compare with null Document");
        return this.documentType.compareTo(other.documentType);
    }

    public boolean isDifferentFrom(Document other) {
        return !this.equals(other);
    }
}