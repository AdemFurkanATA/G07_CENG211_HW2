package model;

/**
 * Document - Belge bilgilerini tutar
 * CSV'deki "D" ile başlayan satırlardan oluşturulur
 * Format: D, applicantID, documentType, durationInMonths
 *
 * Belge Türleri:
 * - ENR: Enrollment Certificate (kayıt belgesi) - TÜM BAŞVURULAR İÇİN ZORUNLU
 * - REC: Recommendation Letter (tavsiye mektubu) - Merit için önemli
 * - SAV: Savings Document (tasarruf belgesi) - Need-based için önemli
 * - RSV: Research Supervisor Approval (danışman onayı) - Research için önemli
 * - GRP: Grant Proposal (proje teklifi) - Research için gerekebilir
 */
public class Document {

    // Fields
    private String applicantID;       // Belgenin ait olduğu başvuran
    private String documentType;      // Belge türü (ENR, REC, SAV, RSV, GRP)
    private int durationInMonths;     // Belgenin geçerlilik süresi (ay cinsinden)

    // Constructor
    /**
     * Document nesnesi oluşturur
     * @param applicantID - Başvuran ID'si
     * @param documentType - Belge türü (ENR, REC, SAV, RSV, GRP)
     * @param durationInMonths - Belgenin süresi (ay cinsinden)
     */
    public Document(String applicantID, String documentType, int durationInMonths) {
        this.applicantID = applicantID;
        this.documentType = documentType;
        this.durationInMonths = durationInMonths;
    }

    // Getters
    public String getApplicantID() {
        return applicantID;
    }

    public String getDocumentType() {
        return documentType;
    }

    public int getDurationInMonths() {
        return durationInMonths;
    }

    // Setters (gerekirse)
    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public void setDurationInMonths(int durationInMonths) {
        this.durationInMonths = durationInMonths;
    }

    /**
     * Belgenin zorunlu olup olmadığını kontrol eder
     * ENR belgesi tüm başvurular için zorunludur
     * @return true ise zorunlu belge
     */
    public boolean isMandatory() {
        return documentType.equals("ENR");
    }

    /**
     * Belge türüne göre açıklama döndürür
     * @return Belge açıklaması
     */
    public String getDescription() {
        switch (documentType) {
            case "ENR":
                return "Enrollment Certificate";
            case "REC":
                return "Recommendation Letter";
            case "SAV":
                return "Savings Document";
            case "RSV":
                return "Research Supervisor Approval";
            case "GRP":
                return "Grant Proposal";
            default:
                return "Unknown Document";
        }
    }

    /**
     * Belgenin geçerliliğini kontrol eder
     * Süre 0'dan büyükse geçerlidir
     * @return true ise geçerli
     */
    public boolean isValid() {
        return durationInMonths > 0;
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return "Document{" +
                "applicantID='" + applicantID + '\'' +
                ", type='" + documentType + '\'' +
                ", duration=" + durationInMonths + " months" +
                '}';
    }

    /**
     * İki belgeyi karşılaştırır
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Document document = (Document) obj;
        return applicantID.equals(document.applicantID) &&
                documentType.equals(document.documentType);
    }

    /**
     * Hash code oluşturur
     */
    @Override
    public int hashCode() {
        return applicantID.hashCode() + documentType.hashCode();
    }
}