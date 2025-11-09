package model;

public class ResearchGrant extends Application {

    // Constructor
    public ResearchGrant(Applicant applicant) {
        super(applicant);
    }

    //Evaluates research scholarship applications
    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) {
            return;
        }

        if (publications == null) {
            this.status = "Rejected";
            this.rejectionReason = "Publication data unavailable";
            return;
        }

        if (applicant == null) {
            this.status = "Rejected";
            this.rejectionReason = "Applicant information missing";
            return;
        }

        if (publications.isEmpty() && !hasDocument("GRP")) {
            this.status = "Rejected";
            this.rejectionReason = "Missing publication or proposal";
            return;
        }


        // Calculate average Impact Factor
        double avgImpact = calculateAverageImpact();

        // Impact Factor control
        if (avgImpact < 1.00) {
            this.status = "Rejected";
            this.rejectionReason = "Publication impact too low";
            return;
        }

        this.status = "Accepted";

        // Determine Scholarship Type (Full/Half)
        this.scholarshipType = determineScholarshipType();

        // Calculate Duration
        this.durationInYears = calculateDuration();
    }

    /**
     * Calculates average Impact Factor
     * If there is no publication but there is GRP, the Impact Factor is considered 0
     *
     * @return Average Impact Factor
     */
    private double calculateAverageImpact() {
        // If there are no publication returns 0
        if (publications == null || publications.isEmpty()) {
            return 0.0;
        }

        // Add Impact Factors of all publications
        double totalImpact = 0.0;
        for (Publication pub : publications) {
            if (pub != null) {
                totalImpact += pub.getImpactFactor();
            }
        }

        // Calculate average
        return totalImpact / publications.size();
    }

    //Determines the type of scholarship according to Impact Factor
    @Override
    protected String determineScholarshipType() {
        double avgImpact = calculateAverageImpact();

        if (avgImpact >= 1.50) {
            return "Full";
        } else if (avgImpact >= 1.00) {
            return "Half";
        }
        return null;  // Shouldn't happen to this state (checked in evaluate)
    }

    /**
     * Calculates the scholarship duration
     *
     * Base duration:
     * - Full → 1 year
     * - Half → 0.5 year (6 months)
     *
     *If there is RSV (Research Supervisor Approval):
     * Add +1 year
     *
     * NOTE: The result is returned as an integer, so:
     * Half + RSV is displayed as no → 0.5 years → 1 year (rounded up)
     * Half + RSV present → shown as 1.5 years → 2 years (rounded up)
     *
     * @return Duration (in years, integer)
     */
    @Override
    protected int calculateDuration() {
        double baseDuration = 0.0;

        // Determine Base Duration
        if ("Full".equals(scholarshipType)) {
            baseDuration = 1.0;
        } else if ("Half".equals(scholarshipType)) {
            baseDuration = 0.5;
        }

        // Add 1 more year if there is RSV
        if (hasDocument("RSV")) {
            baseDuration += 1.0;
        }

        // Convert to integer by rounding
        // 0.5 → 1, 1.0 → 1, 1.5 → 2, 2.0 → 2
        return (int) Math.ceil(baseDuration);
    }

    //Returns Scholarship name
    @Override
    public String getScholarshipName() {
        return "Research";
    }

    //Returns publication count for debugging
    public int getPublicationCount() {
        return publications.size();
    }

    //Returns average Impact Factor for debugging
    public double getAverageImpact() {
        return calculateAverageImpact();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}