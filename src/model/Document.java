package model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

public final class Document {

    private final String applicantID;
    private final String documentType;
    private final int durationInMonths;

    private static final Set<String> VALID_DOCUMENT_TYPES;
    static {
        Set<String> types = new HashSet<>();
        Collections.addAll(types, "ENR", "REC", "SAV", "RSV", "GRP");
        VALID_DOCUMENT_TYPES = Collections.unmodifiableSet(types);
    }

    public Document(String applicantID, String documentType, int durationInMonths) {
        validateApplicantID(applicantID);
        validateDocumentType(documentType);

        this.applicantID = applicantID.trim();
        this.documentType = documentType.trim().toUpperCase();
        this.durationInMonths = durationInMonths;
    }

    // Copy Constructor
    public Document(Document other) {
        this.applicantID = other.applicantID;
        this.documentType = other.documentType;
        this.durationInMonths = other.durationInMonths;
    }

    private void validateApplicantID(String applicantID) {
        Objects.requireNonNull(applicantID);
        if (applicantID.trim().length() < 4) throw new IllegalArgumentException("Invalid ID");
    }

    private void validateDocumentType(String documentType) {
        if (!VALID_DOCUMENT_TYPES.contains(documentType.trim().toUpperCase())) {
            throw new IllegalArgumentException("Invalid document type: " + documentType);
        }
    }

    public String getApplicantID() { return applicantID; }
    public String getDocumentType() { return documentType; }

    @Override
    public String toString() {
        return String.format("Document{ID='%s', type='%s', duration=%d}", applicantID, documentType, durationInMonths);
    }
}