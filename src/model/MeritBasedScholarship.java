package model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MeritBasedScholarship extends Application {

    private static final double FULL_SCHOLARSHIP_GPA_THRESHOLD = 3.20;
    private static final double HALF_SCHOLARSHIP_GPA_THRESHOLD = 3.00;
    private static final double MERIT_MINIMUM_GPA = 3.00;  // Merit-specific minimum

    private static final int DURATION_WITH_REC = 2;    // years
    private static final int DURATION_WITHOUT_REC = 1; // years

    private static final String RECOMMENDATION_LETTER = "REC";

    private static final String SCHOLARSHIP_NAME = "Merit";

    // Constructor
    public MeritBasedScholarship(Applicant applicant) {
        super(applicant);
        validateScholarshipType(applicant);
    }

    // Copy Constructor
    public MeritBasedScholarship(MeritBasedScholarship other) {
        super(new Applicant(other.getApplicant()));  // Deep copy applicant

        Objects.requireNonNull(other, "Cannot copy from null MeritBasedScholarship");

        for (Document doc : other.getDocuments()) {
            this.addDocument(new Document(doc));
        }

        for (Publication pub : other.getPublications()) {
            this.addPublication(new Publication(pub));
        }

        this.setTranscriptStatus(other.getTranscriptStatus());

        this.status = other.status;
        this.scholarshipType = other.scholarshipType;
        this.durationInYears = other.durationInYears;
        this.rejectionReason = other.rejectionReason;
    }

    // Validation Methods

    private void validateScholarshipType(Applicant applicant) {
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant cannot be null");
        }

        String typeCode = applicant.getScholarshipTypeCode();
        if (!typeCode.equals("11")) {
            throw new IllegalArgumentException(
                    "Invalid scholarship type for MeritBasedScholarship. " +
                            "Expected ID starting with '11', got: '" + typeCode + "'"
            );
        }
    }

    private boolean meetsMinimumMeritGPA(double gpa) {
        return gpa >= MERIT_MINIMUM_GPA;
    }

    // Evaluation Method
    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) {
            return;
        }

        if (!meetsMinimumMeritGPA(applicant.getGpa())) {
            this.status = "Rejected";
            this.rejectionReason = "GPA below 3.0";
            return;
        }

        this.status = "Accepted";
        this.rejectionReason = null;

        this.scholarshipType = determineScholarshipType();

        this.durationInYears = calculateDuration();
    }

    @Override
    protected String determineScholarshipType() {
        double gpa = applicant.getGpa();

        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Full";
        } else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Half";
        }

        return null;
    }

    @Override
    protected int calculateDuration() {
        if (hasDocument(RECOMMENDATION_LETTER)) {
            return DURATION_WITH_REC;  // 2 yıl
        } else {
            return DURATION_WITHOUT_REC;  // 1 yıl
        }
    }

    @Override
    public String getScholarshipName() {
        return SCHOLARSHIP_NAME;
    }

    // Business Logic Methods
    public boolean qualifiesForFullScholarship() {
        return applicant.getGpa() >= FULL_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public boolean qualifiesForHalfScholarship() {
        return applicant.getGpa() >= HALF_SCHOLARSHIP_GPA_THRESHOLD &&
                applicant.getGpa() < FULL_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public boolean meetsMinimumRequirements() {
        return meetsMinimumMeritGPA(applicant.getGpa());
    }

    public boolean hasRecommendationLetter() {
        return hasDocument(RECOMMENDATION_LETTER);
    }

    public double getGpaDistanceFromFullThreshold() {
        return applicant.getGpa() - FULL_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public double getGpaDistanceFromHalfThreshold() {
        return applicant.getGpa() - HALF_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public String getGpaCategory() {
        double gpa = applicant.getGpa();

        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Excellent";
        } else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Good";
        } else {
            return "Below Merit Minimum";
        }
    }

    public List<String> getStrengths() {
        List<String> strengths = new ArrayList<>();

        if (qualifiesForFullScholarship()) {
            strengths.add("Excellent GPA (>= 3.20)");
        } else if (qualifiesForHalfScholarship()) {
            strengths.add("Good GPA (3.00-3.19)");
        }

        if (hasRecommendationLetter()) {
            strengths.add("Has Recommendation Letter");
        }

        if (hasDocument("ENR")) {
            strengths.add("Valid Enrollment Certificate");
        }

        if (transcriptStatus) {
            strengths.add("Valid Transcript");
        }

        return Collections.unmodifiableList(strengths);
    }

    public List<String> getWeaknesses() {
        List<String> weaknesses = new ArrayList<>();

        if (!meetsMinimumMeritGPA(applicant.getGpa())) {
            weaknesses.add("GPA below 3.0 (Merit minimum)");
        } else if (!qualifiesForFullScholarship()) {
            weaknesses.add("GPA below 3.20 (Full scholarship threshold)");
        }

        if (!hasRecommendationLetter()) {
            weaknesses.add("Missing Recommendation Letter (affects duration)");
        }

        if (!transcriptStatus) {
            weaknesses.add("Invalid or Missing Transcript");
        }

        if (!hasDocument("ENR")) {
            weaknesses.add("Missing Enrollment Certificate");
        }

        return Collections.unmodifiableList(weaknesses);
    }

    public double calculateApplicationScore() {
        double score = 0.0;

        double gpa = applicant.getGpa();
        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) {
            score += 60.0;
        } else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) {
            score += 40.0;
        } else if (gpa >= 2.50) {
            score += 20.0;
        }
        if (transcriptStatus) {
            score += 20.0;
        }

        if (hasDocument("ENR")) {
            score += 10.0;
        }

        if (hasRecommendationLetter()) {
            score += 10.0;
        }

        return score;
    }

    public String getApplicationQuality() {
        double score = calculateApplicationScore();

        if (score >= 90.0) {
            return "Excellent";
        } else if (score >= 70.0) {
            return "Good";
        } else if (score >= 50.0) {
            return "Average";
        } else {
            return "Poor";
        }
    }

    // Getters
    @Override
    public ArrayList<Document> getDocuments() {
        ArrayList<Document> documentsCopy = new ArrayList<>();
        for (Document doc : documents) {
            documentsCopy.add(new Document(doc));
        }
        return documentsCopy;
    }

    @Override
    public Applicant getApplicant() {
        return new Applicant(applicant);  // Deep copy
    }

    // Object Methods
    @Override
    public String toString() {
        return super.toString();
    }

    public String toDetailedString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Merit-Based Scholarship Application ===\n");
        sb.append("Applicant: ").append(applicant.getName()).append(" (").append(applicant.getApplicantID()).append(")\n");
        sb.append("GPA: ").append(String.format("%.2f", applicant.getGpa())).append(" (").append(getGpaCategory()).append(")\n");
        sb.append("Income: $").append(String.format("%.2f", applicant.getIncome())).append("\n");
        sb.append("Transcript Status: ").append(transcriptStatus ? "Valid" : "Invalid").append("\n");
        sb.append("Documents: ").append(documents.size()).append(" (");
        if (hasRecommendationLetter()) {
            sb.append("includes REC");
        } else {
            sb.append("no REC");
        }
        sb.append(")\n");
        sb.append("Status: ").append(status).append("\n");
        if (status.equals("Accepted")) {
            sb.append("Scholarship Type: ").append(scholarshipType).append("\n");
            sb.append("Duration: ").append(durationInYears).append(" year").append(durationInYears > 1 ? "s" : "").append("\n");
        } else if (status.equals("Rejected")) {
            sb.append("Rejection Reason: ").append(rejectionReason).append("\n");
        }
        sb.append("Application Score: ").append(String.format("%.1f", calculateApplicationScore())).append("/100 (").append(getApplicationQuality()).append(")\n");
        sb.append("Strengths: ").append(getStrengths()).append("\n");
        sb.append("Weaknesses: ").append(getWeaknesses()).append("\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public MeritBasedScholarship clone() {
        return new MeritBasedScholarship(this);
    }

    // Static Utility Methods
    public static double getFullScholarshipGpaThreshold() {
        return FULL_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public static double getHalfScholarshipGpaThreshold() {
        return HALF_SCHOLARSHIP_GPA_THRESHOLD;
    }

    public static double getMeritMinimumGpa() {
        return MERIT_MINIMUM_GPA;
    }

    public static int getDurationWithRecommendation() {
        return DURATION_WITH_REC;
    }

    public static int getDurationWithoutRecommendation() {
        return DURATION_WITHOUT_REC;
    }

    public static String getScholarshipTypeName() {
        return SCHOLARSHIP_NAME;
    }

    public static String predictScholarshipType(double gpa) {
        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Full";
        } else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Half";
        } else {
            return "Ineligible";
        }
    }

    public static String getScholarshipInfo() {
        return String.format(
                "Merit-Based Scholarship (ID: 11xx)\n" +
                        "- Full Scholarship: GPA >= %.2f (Duration: %d years with REC, %d year without)\n" +
                        "- Half Scholarship: %.2f <= GPA < %.2f (Duration: %d years with REC, %d year without)\n" +
                        "- Minimum GPA: %.2f\n" +
                        "- Required: ENR certificate, Valid transcript\n" +
                        "- Optional but beneficial: Recommendation Letter (REC)",
                FULL_SCHOLARSHIP_GPA_THRESHOLD, DURATION_WITH_REC, DURATION_WITHOUT_REC,
                HALF_SCHOLARSHIP_GPA_THRESHOLD, FULL_SCHOLARSHIP_GPA_THRESHOLD, DURATION_WITH_REC, DURATION_WITHOUT_REC,
                MERIT_MINIMUM_GPA
        );
    }
}