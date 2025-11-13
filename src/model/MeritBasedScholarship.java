package model;

public final class MeritBasedScholarship extends Application {

    private static final double FULL_SCHOLARSHIP_GPA_THRESHOLD = 3.20;
    private static final double HALF_SCHOLARSHIP_GPA_THRESHOLD = 3.00;
    private static final double MERIT_MINIMUM_GPA = 3.00;

    private static final int DURATION_WITH_REC = 2;
    private static final int DURATION_WITHOUT_REC = 1;

    private static final String RECOMMENDATION_LETTER = "REC";
    private static final String SCHOLARSHIP_NAME = "Merit";

    public MeritBasedScholarship(Applicant applicant) {
        super(applicant);
        validateScholarshipType(applicant);
    }

    // Validation
    private void validateScholarshipType(Applicant applicant) {
        if (applicant == null) throw new IllegalArgumentException("Applicant cannot be null");
        String typeCode = applicant.getScholarshipTypeCode();
        if (!typeCode.equals("11")) {
            throw new IllegalArgumentException("Invalid type for Merit. Expected 11, got: " + typeCode);
        }
    }

    private boolean meetsMinimumMeritGPA(double gpa) {
        return gpa >= MERIT_MINIMUM_GPA;
    }

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
        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) return "Full";
        else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) return "Half";
        return null;
    }

    @Override
    protected int calculateDuration() {
        return hasDocument(RECOMMENDATION_LETTER) ? DURATION_WITH_REC : DURATION_WITHOUT_REC;
    }

    @Override
    public String getScholarshipName() {
        return SCHOLARSHIP_NAME;
    }
}