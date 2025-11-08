package model;

/**
 * Publication - Yayın bilgilerini tutar
 * CSV'deki "P" ile başlayan satırlardan oluşturulur
 * Format: P, applicantID, title, impactFactor
 *
 * Impact Factor:
 * - Yayının bilimsel etkisini gösteren sayısal değer
 * - Research Grant değerlendirmesinde kullanılır
 * - >= 1.50 → Full Scholarship
 * - >= 1.00 → Half Scholarship
 * - < 1.00 → Rejected
 */
public class Publication {

    // Fields
    private String applicantID;       // Yayının ait olduğu başvuran
    private String title;             // Yayın başlığı
    private double impactFactor;      // Etki faktörü (impact factor)

    // Constructor
    /**
     * Publication nesnesi oluşturur
     * @param applicantID - Başvuran ID'si
     * @param title - Yayın başlığı
     * @param impactFactor - Etki faktörü
     */
    public Publication(String applicantID, String title, double impactFactor) {
        this.applicantID = applicantID;
        this.title = title;
        this.impactFactor = impactFactor;
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

    // Setters (gerekirse)
    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImpactFactor(double impactFactor) {
        this.impactFactor = impactFactor;
    }

    /**
     * Yayının kalitesini kategorize eder
     * @return "High Impact" (>=1.50), "Medium Impact" (>=1.00), veya "Low Impact" (<1.00)
     */
    public String getImpactCategory() {
        if (impactFactor >= 1.50) {
            return "High Impact";
        } else if (impactFactor >= 1.00) {
            return "Medium Impact";
        } else {
            return "Low Impact";
        }
    }

    /**
     * Yayının Full Scholarship için yeterli olup olmadığını kontrol eder
     * @return true ise impact factor >= 1.50
     */
    public boolean qualifiesForFullScholarship() {
        return impactFactor >= 1.50;
    }

    /**
     * Yayının Half Scholarship için yeterli olup olmadığını kontrol eder
     * @return true ise impact factor >= 1.00
     */
    public boolean qualifiesForHalfScholarship() {
        return impactFactor >= 1.00;
    }

    /**
     * Yayının kabul edilebilir olup olmadığını kontrol eder
     * @return true ise impact factor >= 1.00
     */
    public boolean isAcceptable() {
        return impactFactor >= 1.00;
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return "Publication{" +
                "applicantID='" + applicantID + '\'' +
                ", title='" + title + '\'' +
                ", impactFactor=" + impactFactor +
                " (" + getImpactCategory() + ")" +
                '}';
    }

    /**
     * İki yayını karşılaştırır
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Publication that = (Publication) obj;
        return applicantID.equals(that.applicantID) &&
                title.equals(that.title);
    }

    /**
     * Hash code oluşturur
     */
    @Override
    public int hashCode() {
        return applicantID.hashCode() + title.hashCode();
    }

    /**
     * Impact factor'e göre yayınları karşılaştırır (sıralama için)
     * @param other - Karşılaştırılacak yayın
     * @return Negatif ise bu yayın daha düşük, pozitif ise daha yüksek impact'e sahip
     */
    public int compareByImpact(Publication other) {
        return Double.compare(this.impactFactor, other.impactFactor);
    }
}