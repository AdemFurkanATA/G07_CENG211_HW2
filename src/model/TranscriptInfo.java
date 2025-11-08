package model;

/**
 * TranscriptInfo - Transcript (not döküm belgesi) bilgilerini tutar
 * CSV'deki "T" ile başlayan satırlardan oluşturulur
 * Format: T, applicantID, transcriptStatus (Y/N)
 *
 * Transcript Status:
 * - Y (Yes): Transcript geçerli, başvuru devam edebilir
 * - N (No): Transcript geçersiz, başvuru reddedilir
 */
public class TranscriptInfo {

    // Fields
    private String applicantID;       // Transcript'in ait olduğu başvuran
    private boolean isValid;          // Transcript geçerli mi? (Y=true, N=false)

    // Constructor
    /**
     * TranscriptInfo nesnesi oluşturur
     * @param applicantID - Başvuran ID'si
     * @param isValid - Transcript geçerli mi?
     */
    public TranscriptInfo(String applicantID, boolean isValid) {
        this.applicantID = applicantID;
        this.isValid = isValid;
    }

    /**
     * String status ile constructor (Y/N parse etmek için)
     * @param applicantID - Başvuran ID'si
     * @param status - "Y" veya "N"
     */
    public TranscriptInfo(String applicantID, String status) {
        this.applicantID = applicantID;
        this.isValid = status.trim().equalsIgnoreCase("Y");
    }

    // Getters
    public String getApplicantID() {
        return applicantID;
    }

    public boolean isValid() {
        return isValid;
    }

    // Setters
    public void setApplicantID(String applicantID) {
        this.applicantID = applicantID;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    /**
     * Status'u string olarak döndürür (Y/N)
     * @return "Y" veya "N"
     */
    public String getStatusString() {
        return isValid ? "Y" : "N";
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return "TranscriptInfo{" +
                "applicantID='" + applicantID + '\'' +
                ", status='" + getStatusString() + '\'' +
                ", isValid=" + isValid +
                '}';
    }

    /**
     * İki transcript bilgisini karşılaştırır
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TranscriptInfo that = (TranscriptInfo) obj;
        return applicantID.equals(that.applicantID);
    }

    /**
     * Hash code oluşturur
     */
    @Override
    public int hashCode() {
        return applicantID.hashCode();
    }
}