package model;

import java.util.ArrayList;

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
        this.applicant = applicant;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.transcriptStatus = false;
        this.status = "Pending";
        this.scholarshipType = null;
        this.durationInYears = 0;
        this.rejectionReason = null;
    }

    // ============ ABSTRACT METHODS ============
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
        for (Document doc : documents) {
            if (doc.getDocumentType().equals(documentType)) {
                return true;
            }
        }
        return false;
    }

    public Document getDocument(String documentType) {
        for (Document doc : documents) {
            if (doc.getDocumentType().equals(documentType)) {
                return doc;
            }
        }
        return null;
    }

    // Adds document
    public void addDocument(Document document) {
        this.documents.add(document);
    }

    // Adds publication for ResearchGrant
    public void addPublication(Publication publication) {
        this.publications.add(publication);
    }

    // Sets transcript status
    public void setTranscriptStatus(boolean status) {
        this.transcriptStatus = status;
    }

    // ============ GETTERS ============
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

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public ArrayList<Publication> getPublications() {
        return publications;
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

        if (status.equals("Accepted")) {
            result.append(", Type: ").append(scholarshipType);
            result.append(", Duration: ").append(durationInYears);
            result.append(durationInYears == 1 ? " year" : " years");
        } else {
            result.append(", Reason: ").append(rejectionReason);
        }

        return result.toString();
    }

    public int compareById(Application other) {
        return this.applicant.getApplicantID().compareTo(other.applicant.getApplicantID());
    }
}