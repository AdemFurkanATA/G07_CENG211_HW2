package model;

public class Applicant {

    private final String applicantID;  // ID değiştirilemez (immutable)
    private String name;
    private double gpa;
    private double income;

    private static final double MIN_GPA = 0.0;
    private static final double MAX_GPA = 4.0;
    private static final double MIN_INCOME = 0.0;
    private static final double MINIMUM_ELIGIBILITY_GPA = 2.50;


    public Applicant(String applicantID, String name, double gpa, double income) {
        validateApplicantID(applicantID);
        validateName(name);
        validateGpa(gpa);
        validateIncome(income);

        this.applicantID = applicantID.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;
    }

    public Applicant(Applicant other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from null Applicant");
        }

        this.applicantID = other.applicantID;
        this.name = other.name;
        this.gpa = other.gpa;
        this.income = other.income;
    }

    private void validateApplicantID(String applicantID) {
        if (applicantID == null) {
            throw new IllegalArgumentException("Applicant ID cannot be null");
        }

        String trimmedID = applicantID.trim();

        if (trimmedID.isEmpty()) {
            throw new IllegalArgumentException("Applicant ID cannot be empty");
        }

        if (trimmedID.length() < 4) {
            throw new IllegalArgumentException("Applicant ID must be at least 4 characters");
        }

        String prefix = trimmedID.substring(0, 2);
        if (!prefix.equals("11") && !prefix.equals("22") && !prefix.equals("33")) {
            throw new IllegalArgumentException("Invalid Applicant ID prefix. Must start with 11, 22, or 33");
        }

        try {
            Integer.parseInt(trimmedID);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Applicant ID must contain only digits");
        }
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be null");
        }

        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    private void validateGpa(double gpa) {
        if (Double.isNaN(gpa)) {
            throw new IllegalArgumentException("GPA cannot be NaN");
        }

        if (Double.isInfinite(gpa)) {
            throw new IllegalArgumentException("GPA cannot be Infinite");
        }

        if (gpa < MIN_GPA || gpa > MAX_GPA) {
            throw new IllegalArgumentException(
                    String.format("GPA must be between %.2f and %.2f (got: %.2f)",
                            MIN_GPA, MAX_GPA, gpa)
            );
        }
    }

    private void validateIncome(double income) {
        if (Double.isNaN(income)) {
            throw new IllegalArgumentException("Income cannot be NaN");
        }

        if (Double.isInfinite(income)) {
            throw new IllegalArgumentException("Income cannot be Infinite");
        }

        if (income < MIN_INCOME) {
            throw new IllegalArgumentException(
                    String.format("Income cannot be negative (got: %.2f)", income)
            );
        }
    }

    // Getters

    public String getApplicantID() {
        return applicantID;
    }

    public String getName() {
        return name;
    }

    public double getGpa() {
        return gpa;
    }

    public double getIncome() {
        return income;
    }

    // Setters
    public void setName(String name) {
        validateName(name);
        this.name = name.trim();  // Defensive: trim ile ekstra boşlukları temizle
    }

    public void setGpa(double gpa) {
        validateGpa(gpa);
        this.gpa = gpa;
    }

    public void setIncome(double income) {
        validateIncome(income);
        this.income = income;
    }

    // Business Logic Methods
    public String getScholarshipTypeCode() {
        if (applicantID != null && applicantID.length() >= 2) {
            return applicantID.substring(0, 2);
        }
        return "";
    }

    public boolean meetsMinimumGPA() {
        return gpa >= MINIMUM_ELIGIBILITY_GPA;
    }

    public String getGpaCategory() {
        if (gpa >= 3.50) {
            return "Excellent";
        } else if (gpa >= 3.00) {
            return "Good";
        } else if (gpa >= 2.50) {
            return "Average";
        } else {
            return "Below Minimum";
        }
    }

    public String getIncomeCategory() {
        if (income <= 10000) {
            return "Low";
        } else if (income <= 15000) {
            return "Medium";
        } else {
            return "High";
        }
    }

    // Object Methods

    @Override
    public String toString() {
        return "Applicant{" +
                "ID='" + applicantID + '\'' +
                ", name='" + name + '\'' +
                ", GPA=" + String.format("%.2f", gpa) +
                ", incomeCategory='" + getIncomeCategory() + '\'' +
                '}';
    }

    public String toDetailedString() {
        return "Applicant{" +
                "ID='" + applicantID + '\'' +
                ", name='" + name + '\'' +
                ", GPA=" + String.format("%.2f", gpa) +
                " (" + getGpaCategory() + ")" +
                ", income=" + String.format("%.2f", income) +
                " (" + getIncomeCategory() + ")" +
                ", meetsMinGPA=" + meetsMinimumGPA() +
                ", scholarshipType=" + getScholarshipTypeCode() +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        Applicant other = (Applicant) obj;
        return applicantID.equals(other.applicantID);
    }

    @Override
    public int hashCode() {
        return applicantID.hashCode();
    }

    @Override
    public Applicant clone() {
        return new Applicant(this);
    }

    // Utility Methods

    public boolean isDifferentFrom(Applicant other) {
        return !this.equals(other);
    }

    public double getGpaAsPercentage() {
        return (gpa / 4.0) * 100.0;
    }

    public boolean isValid() {
        try {
            validateApplicantID(applicantID);
            validateName(name);
            validateGpa(gpa);
            validateIncome(income);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public int compareByGpa(Applicant other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null Applicant");
        }
        return Double.compare(this.gpa, other.gpa);
    }

    public int compareByIncome(Applicant other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot compare with null Applicant");
        }
        return Double.compare(this.income, other.income);
    }
}