package model;

public class NeedBasedScholarship extends Application {

    private double familyIncome;
    private int dependents;

    // Income thresholds (fixed reference values)
    private static final double BASE_FULL_THRESHOLD = 10000.0;
    private static final double BASE_HALF_THRESHOLD = 15000.0;

    public NeedBasedScholarship(Applicant applicant) {
        super(applicant);
        this.familyIncome = 0.0;
        this.dependents = 0;
    }

    public void setFamilyInfo(double familyIncome, int dependents) {
        if (familyIncome < 0) {
            throw new IllegalArgumentException("Family income cannot be negative");
        }
        if (dependents < 0) {
            throw new IllegalArgumentException("Number of dependents cannot be negative");
        }
        this.familyIncome = familyIncome;
        this.dependents = dependents;
    }

    @Override
    public void evaluate() {
        // Validate that family info has been set
        if (familyIncome == 0.0 && dependents == 0) {
            this.status = "Rejected";
            this.rejectionReason = "Family information not provided";
            return;
        }

        // General Controls
        if (!checkGeneralEligibility()) {
            return;
        }

        // Calculate Thresholds
        double currentFullThreshold = BASE_FULL_THRESHOLD;
        double currentHalfThreshold = BASE_HALF_THRESHOLD;

        double adjustment = 1.0;

        // 20% increase if you have a SAV document
        if (hasDocument("SAV")) {
            adjustment += 0.20;
        }
        // 10% increase if there are 3+ dependents
        if (dependents >= 3) {
            adjustment += 0.10;
        }

        currentFullThreshold *= adjustment;
        currentHalfThreshold *= adjustment;

        // Financial Control
        if (familyIncome > currentHalfThreshold) {
            this.status = "Rejected";
            this.rejectionReason = "Family income exceeds threshold";
            return;
        }

        // Acceptance and Type Determination
        this.status = "Accepted";

        if (familyIncome <= currentFullThreshold) {
            this.scholarshipType = "Full";
        } else {
            this.scholarshipType = "Half";
        }

        // Duration (always 1 year for need-based)
        this.durationInYears = calculateDuration();
    }

    @Override
    protected String determineScholarshipType() {
        return scholarshipType;
    }

    @Override
    protected int calculateDuration() {
        return 1;
    }

    @Override
    public String getScholarshipName() {
        return "Need-Based";
    }

    // Getters
    public double getFamilyIncome() {
        return familyIncome;
    }

    public int getDependents() {
        return dependents;
    }
}