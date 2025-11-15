package model;

public class NeedBasedScholarship extends Application {

    private double familyIncome;
    private int dependents;
    private boolean familyInfoProvided;

    private static final double BASE_FULL_THRESHOLD = 10000.0;
    private static final double BASE_HALF_THRESHOLD = 15000.0;

    public NeedBasedScholarship(Applicant applicant) {
        super(applicant);
        this.familyIncome = 0.0;
        this.dependents = 0;
        this.familyInfoProvided = false;
    }

    public void setFamilyInfo(double familyIncome, int dependents) {
        this.familyIncome = familyIncome;
        this.dependents = dependents;
        this.familyInfoProvided = true;
    }

    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) {
            return;
        }

        if (!familyInfoProvided) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("Missing mandatory document");
            return;
        }

        ScholarshipType type = determineScholarshipType();
        if (type == null) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("Financial status unstable");
            return;
        }

        setStatus(ApplicationStatus.ACCEPTED);
        setRejectionReason(null);
        setScholarshipType(type);
        this.durationInYears = calculateDuration();
    }

    @Override
    protected ScholarshipType determineScholarshipType() {
        double currentFullThreshold = BASE_FULL_THRESHOLD;
        double currentHalfThreshold = BASE_HALF_THRESHOLD;

        if (hasDocument("SAV")) {
            currentFullThreshold *= 1.20;
            currentHalfThreshold *= 1.20;
        }
        if (dependents >= 3) {
            currentFullThreshold *= 1.10;
            currentHalfThreshold *= 1.10;
        }

        if (familyIncome > currentHalfThreshold) {
            return null;
        }

        if (familyIncome <= currentFullThreshold) {
            return ScholarshipType.FULL;
        }
        return ScholarshipType.HALF;
    }

    @Override
    protected double calculateDuration() {
        return 1.0;
    }

    @Override
    public String getScholarshipName() {
        return "Need-Based";
    }

    public double getFamilyIncome() {
        return familyIncome;
    }

    public int getDependents() {
        return dependents;
    }
}
