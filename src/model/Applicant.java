package model;

public class Applicant {

    private final String applicantID;
    private String name;
    private double gpa;
    private double income;

    public Applicant(String applicantID, String name, double gpa, double income) {
        validateApplicantID(applicantID);
        this.applicantID = applicantID.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;
    }

    // Copy Constructor
    public Applicant(Applicant other) {
        this(other.applicantID, other.name, other.gpa, other.income);
    }

    private void validateApplicantID(String applicantID) {
        if (applicantID == null || applicantID.trim().length() < 4) {
            throw new IllegalArgumentException("Invalid Applicant ID");
        }
    }

    public String getApplicantID() { return applicantID; }
    public String getName() { return name; }
    public double getGpa() { return gpa; }
    public double getIncome() { return income; }

    public String getScholarshipTypeCode() {
        return (applicantID != null && applicantID.length() >= 2) ? applicantID.substring(0, 2) : "";
    }

    @Override
    public String toString() {
        return "Applicant{ID='" + applicantID + "', name='" + name + "', GPA=" + gpa + "}";
    }
}