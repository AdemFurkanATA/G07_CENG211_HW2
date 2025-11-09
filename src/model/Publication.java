package model;

public class Publication {

    // Fields
    private String applicantID;
    private String title;
    private double impactFactor;

    // Creates Publication object
    public Publication(String applicantID, String title, double impactFactor) {
        this.applicantID = applicantID;
        this.title = title;
        this.impactFactor = impactFactor;
    }

    // Copy Constructor
    public Publication(Publication other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from null Publication");
        }
        this.applicantID = other.applicantID;
        this.title = other.title;
        this.impactFactor = other.impactFactor;
    }

    // Getters
    public String getApplicantID() {
        return applicantID;
    }

    public String getTitle() {
        return title;
    }

    public double getImpactFactor() {
        return impactFactor;
    }

    // Setters
    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImpactFactor(double impactFactor) {
        this.impactFactor = impactFactor;
    }

    // Returns Impact Category based on Impact Factor
    public String getImpactCategory() {
        if (impactFactor >= 1.50) {
            return "High Impact";
        } else if (impactFactor >= 1.00) {
            return "Medium Impact";
        } else {
            return "Low Impact";
        }
    }

    // Checks if publication is qualified for full scholarship
    public boolean qualifiesForFullScholarship() {
        return impactFactor >= 1.50;
    }

    // Checks if publication is qualified for half scholarship
    public boolean qualifiesForHalfScholarship() {
        return impactFactor >= 1.00;
    }

    // Checks if publication is acceptable
    public boolean isAcceptable() {
        return impactFactor >= 1.00;
    }

    @Override
    public String toString() {
        return "Publication{" +
                "applicantID='" + applicantID + '\'' +
                ", title='" + title + '\'' +
                ", impactFactor=" + impactFactor +
                " (" + getImpactCategory() + ")" +
                '}';
    }

    // Compares 2 publication based on Applicant ID and Title
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Publication that = (Publication) object;
        return applicantID.equals(that.applicantID) &&
                title.equals(that.title);
    }

    // Creates Hash code
    @Override
    public int hashCode() {
        return applicantID.hashCode() + title.hashCode();
    }

    // Compares 2 publication based on Impact Factor
    public int compareByImpact(Publication other) {
        return Double.compare(this.impactFactor, other.impactFactor);
    }
}
