package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Application {

    // Common features (available in all scholarship types)
    protected Applicant applicant;
    protected ArrayList<Document> documents;
    protected ArrayList<Publication> publications;
    protected boolean transcriptStatus;

    // Evaluation results
    protected String status;
    protected String scholarshipType;
    protected int durationInYears;
    protected String rejectionReason;

    // Constructor
    public Application(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }
        this.applicant = applicant;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.transcriptStatus = false;
        this.status = "Pending";
        this.scholarshipType = null;
        this.durationInYears = 0;
        this.rejectionReason = null;
    }

    // Abstract Methods
    public abstract void evaluate();

    // Determine scholarship type
    protected abstract String determineScholarshipType();

    // Calculate scholarship duration
    protected abstract int calculateDuration();

    public boolean checkGeneralEligibility() {
        if (!hasDocument("ENR")) {
            this.status = "Rejected";
            this.rejectionReason = "Missing Enrollment Certificate";
            return false;
        }

        if (!transcriptStatus) {
            this.status = "Rejected";
            this.rejectionReason = "Missing Transcript";
            return false;
        }

        if (applicant.getGpa() < 2.50) {
            this.status = "Rejected";
            this.rejectionReason = "GPA below 2.5";
            return false;
        }

        return true;
    }

    public boolean hasDocument(String documentType) {
        if (documentType == null) {
            return false;
        }
        for (Document doc : documents) {
            if (doc != null && documentType.equals(doc.getDocumentType())) {
                return true;
            }
        }
        return false;
    }

    public Document getDocument(String documentType) {
        if (documentType == null) {
            return null;
        }
        for (Document doc : documents) {
            if (doc != null && documentType.equals(doc.getDocumentType())) {
                return doc;
            }
        }
        return null;
    }

    // Adds document
    public void addDocument(Document document) {
        if (document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        this.documents.add(document);
    }

    // Adds publication for ResearchGrant
    public void addPublication(Publication publication) {
        if (publication == null) {
            throw new IllegalArgumentException("Publication cannot be null");
        }
        this.publications.add(publication);
    }

    // Sets transcript status
    public void setTranscriptStatus(boolean status) {
        this.transcriptStatus = status;
    }

    // Getters
    public Applicant getApplicant() {
        return applicant;
    }

    public String getStatus() {
        return status;
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public int getDurationInYears() {
        return durationInYears;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    public List<Publication> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    public boolean getTranscriptStatus() {
        return transcriptStatus;
    }

    public abstract String getScholarshipName();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Applicant ID: ").append(applicant.getApplicantID());
        result.append(", Name: ").append(applicant.getName());
        result.append(", Scholarship: ").append(getScholarshipName());
        result.append(", Status: ").append(status);

        if ("Accepted".equals(status)) {
            result.append(", Type: ").append(scholarshipType);
            result.append(", Duration: ").append(durationInYears);
            result.append(durationInYears == 1 ? " year" : " years");
        } else if (rejectionReason != null) {
            result.append(", Reason: ").append(rejectionReason);
        }

        return result.toString();
    }

    public int compareById(Application other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null Application");
        }
        return this.applicant.getApplicantID().compareTo(other.applicant.getApplicantID());
    }
}