package model;

public class ResearchGrant extends Application {

    public ResearchGrant(Applicant applicant) {
        super(applicant);
    }

    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) {
            return;
        }

        // Check if there's at least one publication or a proposal document
        if ((publications == null || publications.isEmpty()) && !hasDocument("GRP")) {
            this.status = "Rejected";
            this.rejectionReason = "Missing publication or proposal";
            return;
        }

        // If there are publications, check their impact
        if (publications != null && !publications.isEmpty()) {
            double avgImpact = calculateAverageImpact();

            if (avgImpact < 1.00) {
                this.status = "Rejected";
                this.rejectionReason = "Publication impact too low";
                return;
            }
        }

        this.status = "Accepted";
        this.scholarshipType = determineScholarshipType();
        this.durationInYears = calculateDuration();
    }

    private double calculateAverageImpact() {
        if (publications == null || publications.isEmpty()) {
            return 0.0;
        }

        double totalImpact = 0.0;
        int validCount = 0;

        for (Publication pub : publications) {
            if (pub != null) {
                double impact = pub.getImpactFactor();
                if (impact < 0) {
                    throw new IllegalStateException("Publication with negative impact factor detected");
                }
                totalImpact += impact;
                validCount++;
            }
        }

        // Prevent division by zero
        if (validCount == 0) {
            return 0.0;
        }

        return totalImpact / validCount;
    }

    @Override
    protected String determineScholarshipType() {
        // If only proposal document exists (no publications)
        if (publications == null || publications.isEmpty()) {
            if (hasDocument("GRP")) {
                return "Half";
            }
            return null;
        }

        double avgImpact = calculateAverageImpact();

        if (avgImpact >= 1.50) {
            return "Full";
        } else if (avgImpact >= 1.00) {
            return "Half";
        }

        return null;
    }

    @Override
    protected int calculateDuration() {
        if (scholarshipType == null) {
            return 0;
        }

        double baseDuration = "Full".equals(scholarshipType) ? 1.0 : 0.5;

        if (hasDocument("RSV")) {
            baseDuration += 1.0;
        }

        return (int) Math.ceil(baseDuration);
    }

    @Override
    public String getScholarshipName() {
        return "Research";
    }
}