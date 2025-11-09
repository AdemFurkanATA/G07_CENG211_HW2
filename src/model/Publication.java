package model;

import java.util.Objects; // equals/hashCode için

/**
 * Publication Sınıfı
 * * Bir yayının bilgilerini (ID, başlık, impact factor) saklayan
 * basit bir veri modelidir (POJO).
 */
public class Publication {

    // Alanlar (Fields)
    private String applicantID;
    private String title;
    private double impactFactor;

    /**
     * Ana Yapıcı Metot (Primary Constructor).
     * Yeni bir Publication nesnesi oluşturur.
     *
     * @param applicantID  Başvuranın ID'si
     * @param title        Yayının başlığı
     * @param impactFactor Yayının etki faktörü
     */
    public Publication(String applicantID, String title, double impactFactor) {
        // Validation (doğrulama) eklenebilir, örn: impactFactor < 0 olamaz
        this.applicantID = applicantID;
        this.title = title;
        this.impactFactor = impactFactor;
    }

    /**
     * Kopya Yapıcı Metot (Copy Constructor).
     * Varolan bir Publication nesnesinin kopyasını oluşturur.
     *
     * @param other Kopyalanacak diğer Publication nesnesi.
     */
    public Publication(Publication other) {
        if (other == null) {
            throw new IllegalArgumentException("Cannot copy from a null Publication object");
        }
        this.applicantID = other.applicantID;
        this.title = other.title;
        this.impactFactor = other.impactFactor;
    }

    // --- Getters ---

    /**
     * @return Başvuranın ID'si
     */
    public String getApplicantID() {
        return applicantID;
    }

    /**
     * @return Yayının başlığı
     */
    public String getTitle() {
        return title;
    }

    /**
     * ResearchGrant sınıfının ortalama hesaplamak için kullandığı ana metot.
     * @return Yayının etki faktörü
     */
    public double getImpactFactor() {
        return impactFactor;
    }

    // --- Yardımcı Metotlar ---

    /**
     * Etki faktörüne göre yayının kategorisini döndürür.
     *
     * @return "High Impact", "Medium Impact", veya "Low Impact"
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

    // --- Object Metotları (equals/hashCode) ---

    /**
     * İki Publication nesnesinin eşitliğini kontrol eder.
     * Eşitlik, 'applicantID' ve 'title' alanlarına göre belirlenir.
     *
     * @param object Karşılaştırılacak nesne
     * @return Eğer nesneler eşitse true, değilse false
     */
    @Override
    public boolean equals(Object object) {
        // 1. Aynı referans mı diye bak
        if (this == object) return true;
        // 2. Null mı veya farklı sınıf mı diye bak
        if (object == null || getClass() != object.getClass()) return false;

        // 3. Alanları karşılaştır
        Publication that = (Publication) object;
        return Objects.equals(applicantID, that.applicantID) &&
                Objects.equals(title, that.title);
    }

    /**
     * equals() metoduyla tutarlı bir hash code üretir.
     *
     * @return Bu nesne için üretilen hash kodu
     */
    @Override
    public int hashCode() {
        // Eşitlik kontrolünde kullanılan alanları hash'e dahil et
        return Objects.hash(applicantID, title);
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
}