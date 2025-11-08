package enums;

/**
 * DocumentType - Belge türlerini temsil eden enum
 *
 * Belge Türleri:
 * - ENR: Enrollment Certificate (Kayıt Belgesi) - TÜM BAŞVURULAR İÇİN ZORUNLU
 * - REC: Recommendation Letter (Tavsiye Mektubu) - Merit için önemli
 * - SAV: Savings Document (Tasarruf Belgesi) - Need-Based için önemli
 * - RSV: Research Supervisor Approval (Danışman Onayı) - Research için önemli
 * - GRP: Grant Proposal (Proje Teklifi) - Research için gerekli olabilir
 */
public enum DocumentType {

    /**
     * ENR - Enrollment Certificate (Kayıt Belgesi)
     * Tüm başvurular için zorunlu
     */
    ENR("ENR", "Enrollment Certificate", true),

    /**
     * REC - Recommendation Letter (Tavsiye Mektubu)
     * Merit-based scholarship için önemli (süreyi 2 yıla çıkarır)
     */
    REC("REC", "Recommendation Letter", false),

    /**
     * SAV - Savings Document (Tasarruf Belgesi)
     * Need-based scholarship için önemli (gelir eşiklerini %20 artırır)
     */
    SAV("SAV", "Savings Document", false),

    /**
     * RSV - Research Supervisor Approval (Danışman Onayı)
     * Research grant için önemli (süreye +1 yıl ekler)
     */
    RSV("RSV", "Research Supervisor Approval", false),

    /**
     * GRP - Grant Proposal (Proje Teklifi)
     * Research grant için yayın yoksa gerekli olabilir
     */
    GRP("GRP", "Grant Proposal", false);

    // Enum özellikleri
    private final String code;              // Kısa kod (ENR, REC, vb.)
    private final String description;       // Açıklama
    private final boolean mandatory;        // Zorunlu mu?

    // Constructor
    DocumentType(String code, String description, boolean mandatory) {
        this.code = code;
        this.description = description;
        this.mandatory = mandatory;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * String kod'dan DocumentType enum'una dönüşüm
     * @param code - Belge kodu (örn: "ENR", "REC")
     * @return DocumentType enum değeri
     * @throws IllegalArgumentException - Geçersiz kod
     */
    public static DocumentType fromCode(String code) {
        for (DocumentType type : DocumentType.values()) {
            if (type.code.equalsIgnoreCase(code.trim())) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown document type: " + code);
    }

    /**
     * Belirli bir kod'un geçerli olup olmadığını kontrol eder
     * @param code - Kontrol edilecek kod
     * @return true ise geçerli
     */
    public static boolean isValidCode(String code) {
        try {
            fromCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Tüm belge türlerini listeler
     * @return Tüm DocumentType değerleri
     */
    public static DocumentType[] getAllTypes() {
        return DocumentType.values();
    }

    /**
     * Sadece zorunlu belgeleri döndürür
     * @return Zorunlu belge türleri array'i
     */
    public static DocumentType[] getMandatoryTypes() {
        return new DocumentType[]{ENR};
    }

    /**
     * String gösterimi
     */
    @Override
    public String toString() {
        return code + " (" + description + ")";
    }
}