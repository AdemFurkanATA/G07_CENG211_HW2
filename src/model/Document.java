package model;

import java.util.Objects;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Document Sınıfı
 * Bir belgeye ait bilgileri (ID, tür, süre) saklayan basit bir veri modelidir (POJO).
 * Bu sınıf immutable (değişmez) olarak tasarlanmıştır.
 */
public final class Document { // final - extend edilemez

    // --- Alanlar (Fields) ---
    // final - bu alanlar constructorda ayarlandıktan sonra değiştirilemez
    private final String applicantID;
    private final String documentType;
    private final int durationInMonths;

    /**
     * Ana Yapıcı Metot (Primary Constructor).
     * Basitçe değerleri atar. Validasyon (doğrulama) bu sınıfın değil,
     * onu çağıran servisin (FileReaderService) sorumluluğundadır.
     *
     * @param applicantID      Başvuranın ID'si
     * @param documentType     Belge türü (ENR, REC, vb.)
     * @param durationInMonths Belgenin geçerlilik süresi (ay olarak)
     */
    public Document(String applicantID, String documentType, int durationInMonths) {
        // Bu bir "data class", validasyon yapmaz, sadece veriyi tutar.
        this.applicantID = applicantID;
        this.documentType = documentType;
        this.durationInMonths = durationInMonths;
    }

    /**
     * Kopya Yapıcı Metot (Copy Constructor).
     * Varolan bir Document nesnesinin kopyasını oluşturur.
     * Alanlar immutable olduğu için bu bir 'shallow copy'dir, ancak etkilidir.
     *
     * @param other Kopyalanacak diğer Document nesnesi.
     */
    public Document(Document other) {
        Objects.requireNonNull(other, "Cannot copy from a null Document object");
        this.applicantID = other.applicantID;
        this.documentType = other.documentType;
        this.durationInMonths = other.durationInMonths;
    }

    // --- Getters ---
    // (Setters yok, çünkü alanlar 'final' ve 'immutable')

    /**
     * @return Başvuranın ID'si
     */
    public String getApplicantID() {
        return applicantID;
    }

    /**
     * @return Belge türü (ENR, REC, vb.)
     */
    public String getDocumentType() {
        return documentType;
    }

    /**
     * @return Belgenin geçerlilik süresi (ay olarak)
     */
    public int getDurationInMonths() {
        return durationInMonths;
    }

    // --- İş Mantığı (Business Logic) ---

    /**
     * Bu belgenin zorunlu (mandatory) olup olmadığını kontrol eder.
     * Ödev kurallarına göre sadece 'ENR' zorunludur.
     *
     * @return Eğer belge "ENR" ise true, değilse false
     */
    public boolean isMandatory() {
        // 'documentType' null olamaz (constructor'dan dolayı varsayıyoruz)
        return "ENR".equals(this.documentType);
    }

    // --- Object Metotları (equals/hashCode) ---

    /**
     * İki Document nesnesinin eşitliğini kontrol eder.
     * Eşitlik, 'applicantID' ve 'documentType' alanlarına göre belirlenir.
     * (Bir başvuranın aynı türden iki belgesi olamaz varsayımı)
     *
     * @param object Karşılaştırılacak nesne
     * @return Eğer nesneler eşitse true, değilse false
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        Document document = (Document) object;
        return Objects.equals(applicantID, document.applicantID) &&
                Objects.equals(documentType, document.documentType);
    }

    /**
     * equals() metoduyla tutarlı bir hash code üretir.
     *
     * @return Bu nesne için üretilen hash kodu
     */
    @Override
    public int hashCode() {
        // Eşitlik kontrolünde kullanılan alanları hash'e dahil et
        return Objects.hash(applicantID, documentType);
    }

    /**
     * Nesnenin String temsilini döndürür (hata ayıklama için).
     *
     * @return Nesnenin formatlanmış String hali
     */
    @Override
    public String toString() {
        return "Document{" +
                "applicantID='" + applicantID + '\'' +
                ", documentType='" + documentType + '\'' +
                ", durationInMonths=" + durationInMonths +
                '}';
    }
}