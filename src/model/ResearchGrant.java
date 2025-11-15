package model;

public class ResearchGrant extends Application {

    public ResearchGrant(Applicant applicant) {
        super(applicant);
    }

    @Override
    public void evaluate() {
        if (!checkGeneralEligibility()) return;

        boolean hasPublications = publications != null && !publications.isEmpty();
        boolean hasGrantProposal = hasDocument("GRP");

        if (!hasPublications && !hasGrantProposal) {
            setStatus(ApplicationStatus.REJECTED);
            setRejectionReason("Missing publication or proposal");
            return;
        }

        if (hasPublications) {
            double avgImpact = calculateAverageImpact();

            if (avgImpact < 1.00) {
                setStatus(ApplicationStatus.REJECTED);
                setRejectionReason("Publication impact too low");
                return;
            }

            setScholarshipType(determineScholarshipType());
        } else {
            setScholarshipType(ScholarshipType.HALF);
        }

        setStatus(ApplicationStatus.ACCEPTED);
        setRejectionReason(null);
        setDurationInYears(calculateDuration());
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
    protected ScholarshipType determineScholarshipType() {
        double avgImpact = calculateAverageImpact();
        if (avgImpact >= 1.50) return ScholarshipType.FULL;
        else if (avgImpact >= 1.00) return ScholarshipType.HALF;
        return null;
    }

    @Override
    protected double calculateDuration() {
        double baseDuration = scholarshipType == ScholarshipType.FULL ? 1.0 : 0.5;
        if (hasDocument("RSV")) baseDuration += 1.0;
        return baseDuration;
    }

    @Override
    public String getScholarshipName() {
        return "Research";
    }
}
