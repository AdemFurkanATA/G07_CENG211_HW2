package enums;

/**
 * ScholarshipType - Burs türlerini temsil eden enum
 *
 * Üç ana burs türü vardır:
 * - MERIT: Merit-Based Scholarship (Akademik Başarı Bursu) - 11xx ID'ler
 * - NEED_BASED: Need-Based Scholarship (İhtiyaç Bazlı Burs) - 22xx ID'ler
 * - RESEARCH: Research Grant (Araştırma Bursu) - 33xx ID'ler
 */
public enum ScholarshipType {

    /**
     * MERIT - Merit-Based Scholarship (Akademik Başarı Bursu)
     * ID Prefix: 11xx
     * Kriter: GPA
     */
    MERIT("11", "Merit", "Merit-Based Scholarship"),

    /**
     * NEED_BASED - Need-Based Scholarship (İhtiyaç Bazlı Burs)
     * ID Prefix: 22xx
     * Kriter: Aile Geliri
     */
    NEED_BASED("22", "Need-Based", "Need-Based Scholarship"),

    /**
     * RESEARCH - Research Grant (Araştırma Bursu)
     * ID Prefix: 33xx
     * Kriter: Yayın Impact Factor
     */
    RESEARCH("33", "Research", "Research Grant");

    // Enum özellikleri
    private final String idPrefix;          // ID öneki (11, 22, 33)
    private final String displayName;       // Kısa isim (Merit, Need-Based, Research)
    private final String fullName;          // Tam isim

    // Constructor
    ScholarshipType(String idPrefix, String displayName, String fullName) {
        this.idPrefix = idPrefix;
        this.displayName = displayName;
        this.fullName = fullName;
    }

    // Getters
    public String getIdPrefix() {
        return idPrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFullName() {
        return fullName;
    }

    /**
     * ID prefix'inden ScholarshipType enum'una dönüşüm
     * @param idPrefix - ID öneki (11, 22, 33)
     * @return ScholarshipType enum değeri
     * @throws IllegalArgumentException - Geçersiz prefix
     */
    public static ScholarshipType fromIdPrefix(String idPrefix) {
        for (ScholarshipType type : ScholarshipType.values()) {
            if (type.idPrefix.equals(idPrefix)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship type prefix: " + idPrefix);
    }

    /**
     * Applicant ID'sinden ScholarshipType'ı belirler
     * @param applicantID - Başvuran ID'si (örn: "1101", "2205", "3312")
     * @return ScholarshipType enum değeri
     * @throws IllegalArgumentException - Geçersiz ID formatı
     */
    public static ScholarshipType fromApplicantID(String applicantID) {
        if (applicantID == null || applicantID.length() < 2) {
            throw new IllegalArgumentException("Invalid applicant ID: " + applicantID);
        }
        String prefix = applicantID.substring(0, 2);
        return fromIdPrefix(prefix);
    }

    /**
     * Display name'den ScholarshipType enum'una dönüşüm
     * @param displayName - Display ismi (Merit, Need-Based, Research)
     * @return ScholarshipType enum değeri
     * @throws IllegalArgumentException - Geçersiz isim
     */
    public static ScholarshipType fromDisplayName(String displayName) {
        for (ScholarshipType type : ScholarshipType.values()) {
            if (type.displayName.equalsIgnoreCase(displayName.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown scholarship type: " + displayName);
    }

    /**
     * Belirli bir ID prefix'inin geçerli olup olmadığını kontrol eder
     * @param idPrefix - Kontrol edilecek prefix
     * @return true ise geçerli
     */
    public static boolean isValidPrefix(String idPrefix) {
        try {
            fromIdPrefix(idPrefix);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Belirli bir applicant ID'sinin geçerli olup olmadığını kontrol eder
     * @param applicantID - Kontrol edilecek ID
     * @return true ise geçerli
     */
    public static boolean isValidApplicantID(String applicantID) {
        try {
            fromApplicantID(applicantID);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Tüm burs türlerini listeler
     * @return Tüm ScholarshipType değerleri
     */
    public static ScholarshipType[] getAllTypes() {
        return ScholarshipType.values();
    }

    /**
     * String gösterimi
     */
    @Override
    public String toString() {
        return displayName + " (" + idPrefix + "xx)";
    }
}