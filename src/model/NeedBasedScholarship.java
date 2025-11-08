package model;

/**
 * NeedBasedScholarship - İhtiyaç Bazlı Burs (22xx ID'ler)
 *
 * Değerlendirme Kriterleri:
 * - Aylık Gelir <= 10,000 → Full Scholarship
 * - 10,000 < Gelir <= 15,000 → Half Scholarship
 * - Gelir > 15,000 → Rejected
 *
 * Özel Durumlar:
 * - SAV (Savings Document) varsa → eşikler %20 artar
 * - 3+ bakmakla yükümlü varsa → eşikler %10 daha artar
 *
 * Süre: 1 yıl (sabit)
 */
public class NeedBasedScholarship extends Application {

    // Özel alanlar (sadece need-based için)
    private double familyIncome;      // Aile geliri
    private int dependents;           // Bakmakla yükümlü sayısı

    // Gelir eşikleri (base values)
    private double fullScholarshipThreshold = 10000.0;
    private double halfScholarshipThreshold = 15000.0;

    // Constructor
    public NeedBasedScholarship(Applicant applicant) {
        super(applicant);
        this.familyIncome = 0.0;
        this.dependents = 0;
    }

    /**
     * Aile bilgilerini ayarlar (CSV'den I satırından gelir)
     * @param familyIncome - Aile geliri
     * @param dependents - Bakmakla yükümlü sayısı
     */
    public void setFamilyInfo(double familyIncome, int dependents) {
        this.familyIncome = familyIncome;
        this.dependents = dependents;
    }

    /**
     * İhtiyaç bazlı burs başvurusunu değerlendirir
     */
    @Override
    public void evaluate() {
        // 1. GENEL KONTROLLER (ENR, Transcript, GPA >= 2.50)
        if (!checkGeneralEligibility()) {
            return;
        }

        // 2. GELİR EŞİKLERİNİ AYARLA (SAV ve dependents'e göre)
        calculateAdjustedThresholds();

        // 3. FİNANSAL DURUM KONTROLÜ
        // Gelir, adjusted threshold'lardan yüksekse red
        if (familyIncome > halfScholarshipThreshold) {
            this.status = "Rejected";
            this.rejectionReason = "Financial status unstable";
            return;
        }

        // 4. BAŞVURU KABUL EDİLDİ
        this.status = "Accepted";

        // 5. BURS TÜRÜNÜ BELİRLE (Full/Half)
        this.scholarshipType = determineScholarshipType();

        // 6. SÜREYİ HESAPLA (1 yıl sabit)
        this.durationInYears = calculateDuration();
    }

    /**
     * SAV belgesi ve bakmakla yükümlü sayısına göre gelir eşiklerini ayarlar
     *
     * Mantık:
     * 1. Eğer SAV belgesi varsa, eşikler %20 artar
     * 2. Eğer 3+ bakmakla yükümlü varsa, eşikler %10 daha artar
     *
     * Örnek:
     * - Base: Full=10000, Half=15000
     * - SAV varsa: Full=12000, Half=18000
     * - SAV + 3 dependents: Full=13200, Half=19800
     */
    private void calculateAdjustedThresholds() {
        double adjustment = 1.0;  // Başlangıç çarpanı (değişiklik yok)

        // SAV belgesi kontrolü
        if (hasDocument("SAV")) {
            adjustment += 0.20;  // %20 artır (1.0 → 1.20)
        }

        // Bakmakla yükümlü kontrolü (3 veya daha fazla)
        if (dependents >= 3) {
            adjustment += 0.10;  // %10 daha artır (1.20 → 1.30 veya 1.0 → 1.10)
        }

        // Eşikleri güncelle
        fullScholarshipThreshold = 10000.0 * adjustment;
        halfScholarshipThreshold = 15000.0 * adjustment;
    }

    /**
     * Gelire göre burs türünü belirler
     * @return "Full" veya "Half"
     */
    @Override
    protected String determineScholarshipType() {
        if (familyIncome <= fullScholarshipThreshold) {
            return "Full";
        } else if (familyIncome <= halfScholarshipThreshold) {
            return "Half";
        }
        return null;  // Bu duruma gelmemeli (evaluate'de kontrol edildi)
    }

    /**
     * Burs süresini hesaplar
     * Need-based scholarship için süre her zaman 1 yıl
     * @return 1 (yıl)
     */
    @Override
    protected int calculateDuration() {
        return 1;  // Need-based için süre sabit 1 yıl
    }

    /**
     * Burs adını döndürür
     * @return "Need-Based"
     */
    @Override
    public String getScholarshipName() {
        return "Need-Based";
    }

    // Getter metodları
    public double getFamilyIncome() {
        return familyIncome;
    }

    public int getDependents() {
        return dependents;
    }

    public double getFullScholarshipThreshold() {
        return fullScholarshipThreshold;
    }

    public double getHalfScholarshipThreshold() {
        return halfScholarshipThreshold;
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return super.toString();
    }
}