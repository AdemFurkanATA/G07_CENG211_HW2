package enums;

/**
 * ApplicationStatus - Başvuru durumunu temsil eden enum
 *
 * Ana durumlar:
 * - ACCEPTED: Başvuru kabul edildi
 * - REJECTED: Başvuru reddedildi
 * - PENDING: Başvuru değerlendirme aşamasında (henüz değerlendirilmedi)
 */
public enum ApplicationStatus {

    /**
     * ACCEPTED - Başvuru kabul edildi
     * Başvuran burs almaya hak kazandı
     */
    ACCEPTED("Accepted", "Application Accepted", "Başvuru kabul edildi"),

    /**
     * REJECTED - Başvuru reddedildi
     * Başvuran kriterleri karşılayamadı
     */
    REJECTED("Rejected", "Application Rejected", "Başvuru reddedildi"),

    /**
     * PENDING - Başvuru değerlendirme aşamasında
     * Henüz evaluate() metodu çağrılmadı
     */
    PENDING("Pending", "Under Evaluation", "Değerlendirme aşamasında");

    // Enum özellikleri
    private final String displayName;       // Kısa isim (Accepted, Rejected, Pending)
    private final String description;       // İngilizce açıklama
    private final String descriptionTR;     // Türkçe açıklama

    // Constructor
    ApplicationStatus(String displayName, String description, String descriptionTR) {
        this.displayName = displayName;
        this.description = description;
        this.descriptionTR = descriptionTR;
    }

    // Getters
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionTR() {
        return descriptionTR;
    }

    /**
     * Display name'den ApplicationStatus enum'una dönüşüm
     * @param displayName - Display ismi (Accepted, Rejected, Pending)
     * @return ApplicationStatus enum değeri
     * @throws IllegalArgumentException - Geçersiz isim
     */
    public static ApplicationStatus fromDisplayName(String displayName) {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown application status: " + displayName);
    }

    /**
     * Belirli bir display name'in geçerli olup olmadığını kontrol eder
     * @param displayName - Kontrol edilecek isim
     * @return true ise geçerli
     */
    public static boolean isValidDisplayName(String displayName) {
        try {
            fromDisplayName(displayName);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Başvurunun kabul edilip edilmediğini kontrol eder
     * @return true ise kabul edildi
     */
    public boolean isAccepted() {
        return this == ACCEPTED;
    }

    /**
     * Başvurunun reddedilip edilmediğini kontrol eder
     * @return true ise reddedildi
     */
    public boolean isRejected() {
        return this == REJECTED;
    }

    /**
     * Başvurunun beklemede olup olmadığını kontrol eder
     * @return true ise beklemede
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * Başvurunun sonuçlandırılıp sonuçlandırılmadığını kontrol eder
     * @return true ise sonuçlandırıldı (Accepted veya Rejected)
     */
    public boolean isFinalized() {
        return this == ACCEPTED || this == REJECTED;
    }

    /**
     * Tüm durumları listeler
     * @return Tüm ApplicationStatus değerleri
     */
    public static ApplicationStatus[] getAllStatuses() {
        return ApplicationStatus.values();
    }

    /**
     * String gösterimi
     */
    @Override
    public String toString() {
        return displayName;
    }
}