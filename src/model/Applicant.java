package model;

import java.util.Objects; // equals/hashCode için

/**
 * Applicant Sınıfı
 * Bir başvuru sahibine ait temel bilgileri (ID, isim, GPA, gelir) tutan
 * basit bir veri modelidir (POJO).
 * ID (applicantID) 'final' yani değişmezdir.
 */
public class Applicant {

    // Alanlar
    private final String applicantID;  // ID değiştirilemez (immutable)
    private String name;
    private double gpa;
    private double income;

    // Sabitler
    private static final double MIN_GPA = 0.0;
    private static final double MAX_GPA = 4.0;
    private static final double MIN_INCOME = 0.0;

    /**
     * Ana Yapıcı Metot (Constructor).
     * Gelen verilerle yeni bir Applicant nesnesi oluşturur ve verileri doğrular.
     */
    public Applicant(String applicantID, String name, double gpa, double income) {
        // Validasyon (Doğrulama)
        validateApplicantID(applicantID);
        validateName(name);
        validateGpa(gpa);
        validateIncome(income);

        // Değerleri ata (trim ile temizle)
        this.applicantID = applicantID.trim();
        this.name = name.trim();
        this.gpa = gpa;
        this.income = income;
    }

    /**
     * Kopya Yapıcı Metot (Copy Constructor).
     * Varolan bir Applicant nesnesinin kopyasını oluşturur.
     */
    public Applicant(Applicant other) {
        Objects.requireNonNull(other, "Cannot copy from a null Applicant");

        // Alanlar immutable (String) veya primitif (double) olduğu için
        // direkt kopyalama (shallow copy) yeterli ve güvenlidir.
        this.applicantID = other.applicantID;
        this.name = other.name;
        this.gpa = other.gpa;
        this.income = other.income;
    }

    // --- Validasyon Metotları ---

    /**
     * Applicant ID'sinin formatını doğrular (null, boş, prefix, numeric).
     */
    private void validateApplicantID(String applicantID) {
        Objects.requireNonNull(applicantID, "Applicant ID cannot be null");
        String trimmedID = applicantID.trim();
        if (trimmedID.isEmpty()) {
            throw new IllegalArgumentException("Applicant ID cannot be empty");
        }

        // PDF'teki 3 tip (11, 22, 33) dışında prefix var mı?
        String prefix = trimmedID.substring(0, 2);
        if (!prefix.equals("11") && !prefix.equals("22") && !prefix.equals("33")) {
            throw new IllegalArgumentException("Invalid Applicant ID prefix. Must start with 11, 22, or 33");
        }

        // ID'nin tamamı sayı mı?
        try {
            // ID'ler 8 haneli, Integer'a sığar.
            Integer.parseInt(trimmedID);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Applicant ID must contain only digits");
        }
    }

    /**
     * İsim alanını doğrular (null, boş).
     */
    private void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null");
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    /**
     * GPA alanını doğrular (aralık kontrolü).
     */
    private void validateGpa(double gpa) {
        if (gpa < MIN_GPA || gpa > MAX_GPA) {
            throw new IllegalArgumentException(
                    String.format("GPA must be between %.1f and %.1f (got: %.2f)",
                            MIN_GPA, MAX_GPA, gpa)
            );
        }
    }

    /**
     * Gelir alanını doğrular (negatif olamaz).
     */
    private void validateIncome(double income) {
        if (income < MIN_INCOME) {
            throw new IllegalArgumentException(
                    String.format("Income cannot be negative (got: %.2f)", income)
            );
        }
    }

    // --- Getters ---

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

    // --- Setters ---

    public void setName(String name) {
        validateName(name);
        this.name = name.trim();
    }

    public void setGpa(double gpa) {
        validateGpa(gpa);
        this.gpa = gpa;
    }

    public void setIncome(double income) {
        validateIncome(income);
        this.income = income;
    }

    // --- GEREKLİ İŞ MANTIĞI ---

    /**
     * FileReaderService'in hangi burs objesini (Merit, Need, Research)
     * yaratacağına karar vermesi için gereken kritik metot.
     *
     * @return ID'nin ilk iki hanesi ("11", "22", veya "33")
     */
    public String getScholarshipTypeCode() {
        // applicantID'nin null veya kısa olması constructor'da engellendi.
        return applicantID.substring(0, 2);
    }

    // --- Object Metotları ---

    /**
     * Nesnenin String temsilini döndürür (hata ayıklama için).
     */
    @Override
    public String toString() {
        return "Applicant{" +
                "ID='" + applicantID + '\'' +
                ", name='" + name + '\'' +
                ", GPA=" + String.format("%.2f", gpa) +
                '}';
    }

    /**
     * İki başvuranı ID'lerine göre karşılaştırır.
     * Sadece ID'ye bakmak yeterlidir.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Applicant applicant = (Applicant) obj;
        // ID'ler unique (benzersiz) olduğu için sadece ID'yi kontrol et
        return applicantID.equals(applicant.applicantID);
    }

    /**
     * equals() ile tutarlı hashCode üretir.
     */
    @Override
    public int hashCode() {
        return Objects.hash(applicantID);
    }
}