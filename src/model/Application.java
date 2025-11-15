package model;

import java.util.ArrayList;

/**
 * Abstract base for every scholarship application. It keeps the shared data
 * (applicant, documents, publications, evaluation results) and provides helper
 * operations for subclasses. Concrete scholarship types must extend this class
 * and implement their own evaluation logic.
 */
public abstract class Application {

    protected Applicant applicant;
    protected ArrayList<Document> documents;
    protected ArrayList<Publication> publications;
    protected boolean transcriptStatus;

    protected ApplicationStatus status;
    protected ScholarshipType scholarshipType;
    protected double durationInYears;
    protected String rejectionReason;

    public Application(Applicant applicant) {
        this.applicant = applicant;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.transcriptStatus = false;
        this.status = ApplicationStatus.PENDING;
        this.scholarshipType = null;
        this.durationInYears = 0;
        this.rejectionReason = null;
    }

    public abstract void evaluate();

    protected abstract ScholarshipType determineScholarshipType();

    protected abstract double calculateDuration();

    public boolean checkGeneralEligibility() {
        if (!hasDocument("ENR")) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("Missing Enrollment Certificate");
            return false;
        }

        if (!transcriptStatus) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("Missing Transcript");
            return false;
        }

        if (applicant.getGpa() < 2.50) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("GPA below 2.5");
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

    public void addDocument(Document document) {
        documents.add(document);
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
    }

    public void setTranscriptStatus(boolean status) {
        this.transcriptStatus = status;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public ScholarshipType getScholarshipType() {
        return scholarshipType;
    }

    public double getDurationInYears() {
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

    protected void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    protected void setScholarshipType(ScholarshipType scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    protected void setDurationInYears(double duration) {
        this.durationInYears = duration;
    }

    protected void setRejectionReason(String reason) {
        this.rejectionReason = reason;
    }

    public abstract String getScholarshipName();

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Applicant ID: ").append(applicant.getApplicantID());
        result.append(", Name: ").append(applicant.getName());
        result.append(", Scholarship: ").append(getScholarshipName());
        result.append(", Status: ").append(status);

        if (status == ApplicationStatus.ACCEPTED) {
            result.append(", Type: ").append(scholarshipType);
            result.append(", Duration: ");
            if (durationInYears == Math.floor(durationInYears)) {
                int years = (int) durationInYears;
                result.append(years);
                result.append(years == 1 ? " year" : " years");
            } else {
                result.append(durationInYears).append(" years");
            }
        } else {
            result.append(", Reason: ").append(rejectionReason);
        }

        return result.toString();
    }

    public int compareById(Application other) {
        return this.applicant.getApplicantID().compareTo(other.applicant.getApplicantID());
    }
}
