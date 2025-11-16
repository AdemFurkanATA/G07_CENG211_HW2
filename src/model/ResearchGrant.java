package model;

public class ResearchGrant extends Application {

    public ResearchGrant(Applicant applicant) {
        super(applicant);
    }

    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) return;

        if (publications == null || (publications.isEmpty() && !hasDocument("GRP"))) {
            this.status = "Rejected";
            this.rejectionReason = "Missing publication or proposal";
            return;
        }

        double avgImpact = calculateAverageImpact();

        if (avgImpact < 1.00) {
            this.status = "Rejected";
            this.rejectionReason = "Publication impact too low";
            return;
        }

        this.status = "Accepted";
        this.scholarshipType = determineScholarshipType();
        this.durationInYears = calculateDuration();
    }

    private double calculateAverageImpact() {
        if (publications == null || publications.isEmpty()) return 0.0;
        double totalImpact = 0.0;
        for (Publication pub : publications) {
            if (pub != null) totalImpact += pub.getImpactFactor();
        }
        return totalImpact / publications.size();
    }

    @Override
    protected String determineScholarshipType() {
        double avgImpact = calculateAverageImpact();
        if (avgImpact >= 1.50) return "Full";
        else if (avgImpact >= 1.00) return "Half";
        return null;
    }

    @Override
    protected int calculateDuration() {
        double baseDuration = "Full".equals(scholarshipType) ? 1.0 : 0.5;
        if (hasDocument("RSV")) baseDuration += 1.0;
        return (int) Math.ceil(baseDuration);
    }

    @Override
    public String getScholarshipName() {
        return "Research";
    }
}