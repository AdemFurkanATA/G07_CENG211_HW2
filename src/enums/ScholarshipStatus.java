package enums;

/**
 * ScholarshipStatus - Burs durumunu temsil eden enum
 *
 * İki ana durum vardır:
 * - FULL: Full Scholarship (Tam Burs)
 * - HALF: Half Scholarship (Yarım Burs)
 *
 * Not: "Rejected" durumu için ayrı bir ApplicationStatus enum'u kullanılabilir
 * veya status String olarak "Accepted"/"Rejected" şeklinde tutulabilir
 */
public enum ScholarshipStatus {

    /**
     * FULL - Full Scholarship (Tam Burs)
     *
     * Kriterler:
     * - Merit: GPA >= 3.20
     * - Need-Based: Gelir <= 10,000 (adjusted)
     * - Research: Avg Impact Factor >= 1.50
     */
    FULL("Full", "Full Scholarship", 100),

    /**
     * HALF - Half Scholarship (Yarım Burs)
     *
     * Kriterler:
     * - Merit: 3.00 <= GPA < 3.20
     * - Need-Based: 10,000 < Gelir <= 15,000 (adjusted)
     * - Research: 1.00 <= Avg Impact Factor < 1.50
     */
    HALF("Half", "Half Scholarship", 50);

    // Enum özellikleri
    private final String displayName;       // Kısa isim (Full, Half)
    private final String fullName;          // Tam isim
    private final int percentage;           // Burs yüzdesi (100%, 50%)

    // Constructor
    ScholarshipStatus(String displayName, String fullName, int percentage) {
        this.displayName = displayName;
        this.fullName = fullName;
        this.percentage = percentage;
    }

    // Getters
    public String getDisplayName() {
        return displayName;
    }

    public String getFullName() {
        return fullName;
    }

    public int getPercentage() {
        return percentage;
    }

    /**
     * Display name'den ScholarshipStatus enum'una dönüşüm
     * @param displayName - Display ismi (Full, Half)
     * @return ScholarshipStatus enum değeri
     * @throws IllegalArgumentException - Geçersiz isim
     */
    public static ScholarshipStatus fromDisplayName(String displayName) {
        for (ScholarshipStatus status : ScholarshipStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship status: " + displayName);
    }

    /**
     * Yüzde değerinden ScholarshipStatus enum'una dönüşüm
     * @param percentage - Burs yüzdesi (100 veya 50)
     * @return ScholarshipStatus enum değeri
     * @throws IllegalArgumentException - Geçersiz yüzde
     */
    public static ScholarshipStatus fromPercentage(int percentage) {
        for (ScholarshipStatus status : ScholarshipStatus.values()) {
            if (status.percentage == percentage) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship percentage: " + percentage);
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
     * Full scholarship olup olmadığını kontrol eder
     * @return true ise Full scholarship
     */
    public boolean isFull() {
        return this == FULL;
    }

    /**
     * Half scholarship olup olmadığını kontrol eder
     * @return true ise Half scholarship
     */
    public boolean isHalf() {
        return this == HALF;
    }

    /**
     * Burs miktarını hesaplar (varsayılan burs miktarına göre)
     * @param baseAmount - Tam burs miktarı
     * @return Hesaplanmış burs miktarı
     */
    public double calculateAmount(double baseAmount) {
        return baseAmount * (percentage / 100.0);
    }

    /**
     * Tüm burs durumlarını listeler
     * @return Tüm ScholarshipStatus değerleri
     */
    public static ScholarshipStatus[] getAllStatuses() {
        return ScholarshipStatus.values();
    }

    /**
     * String gösterimi
     */
    @Override
    public String toString() {
        return displayName + " (" + percentage + "%)";
    }
}

/**
 * ApplicationStatus - Başvuru durumunu temsil eden enum (opsiyonel)
 *
 * Ana durumlar:
 * - ACCEPTED: Başvuru kabul edildi
 * - REJECTED: Başvuru reddedildi
 * - PENDING: Başvuru değerlendirme aşamasında
 */
enum ApplicationStatus {

    /**
     * ACCEPTED - Başvuru kabul edildi
     */
    ACCEPTED("Accepted", "Application Accepted"),

    /**
     * REJECTED - Başvuru reddedildi
     */
    REJECTED("Rejected", "Application Rejected"),

    /**
     * PENDING - Başvuru değerlendirme aşamasında
     */
    PENDING("Pending", "Under Evaluation");

    private final String displayName;
    private final String description;

    ApplicationStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public static ApplicationStatus fromDisplayName(String displayName) {
        for (ApplicationStatus status : ApplicationStatus.values()) {
            if (status.displayName.equalsIgnoreCase(displayName.trim())) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown application status: " + displayName);
    }

    public boolean isAccepted() {
        return this == ACCEPTED;
    }

    public boolean isRejected() {
        return this == REJECTED;
    }

    public boolean isPending() {
        return this == PENDING;
    }

    @Override
    public String toString() {
        return displayName;
    }
}