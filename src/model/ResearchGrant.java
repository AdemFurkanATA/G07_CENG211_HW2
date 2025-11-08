package model;

/**
 * ResearchGrant - Araştırma Bursu (33xx ID'ler)
 *
 * Değerlendirme Kriterleri:
 * - En az 1 Publication (P) veya Grant Proposal (GRP) gerekli
 * - Ortalama Impact Factor >= 1.50 → Full Scholarship (1 yıl)
 * - 1.00 <= Impact Factor < 1.50 → Half Scholarship (6 ay)
 * - Impact Factor < 1.00 → Rejected
 *
 * Süre:
 * - Full → 1 yıl base
 * - Half → 0.5 yıl (6 ay) base
 * - RSV (Research Supervisor Approval) varsa → +1 yıl eklenir
 */
public class ResearchGrant extends Application {

    // Constructor
    public ResearchGrant(Applicant applicant) {
        super(applicant);
    }

    /**
     * Araştırma bursu başvurusunu değerlendirir
     */
    @Override
    public void evaluate() {
        // 1. GENEL KONTROLLER (ENR, Transcript, GPA >= 2.50)
        if (!checkGeneralEligibility()) {
            return;
        }

        // 2. YAYIN VEYA PROJE TEKLİFİ KONTROLÜ
        // En az 1 yayın VEYA GRP belgesi olmalı
        if (publications.isEmpty() && !hasDocument("GRP")) {
            this.status = "Rejected";
            this.rejectionReason = "Missing publication or proposal";
            return;
        }

        // 3. ORTALAMA IMPACT FACTOR HESAPLA
        double avgImpact = calculateAverageImpact();

        // 4. IMPACT FACTOR KONTROLÜ
        // Impact factor < 1.00 ise red
        if (avgImpact < 1.00) {
            this.status = "Rejected";
            this.rejectionReason = "Publication impact too low";
            return;
        }

        // 5. BAŞVURU KABUL EDİLDİ
        this.status = "Accepted";

        // 6. BURS TÜRÜNÜ BELİRLE (Full/Half)
        this.scholarshipType = determineScholarshipType();

        // 7. SÜREYİ HESAPLA
        this.durationInYears = calculateDuration();
    }

    /**
     * Ortalama impact factor'ü hesaplar
     *
     * Eğer yayın yoksa ama GRP varsa, impact factor 0 kabul edilir
     * (Bu durumda zaten "Publication impact too low" ile red olur)
     *
     * @return Ortalama impact factor
     */
    private double calculateAverageImpact() {
        // Eğer hiç yayın yoksa 0 döndür
        if (publications.isEmpty()) {
            return 0.0;
        }

        // Tüm yayınların impact factor'lerini topla
        double totalImpact = 0.0;
        for (Publication pub : publications) {
            totalImpact += pub.getImpactFactor();
        }

        // Ortalamayı hesapla
        return totalImpact / publications.size();
    }

    /**
     * Impact factor'e göre burs türünü belirler
     * @return "Full" veya "Half"
     */
    @Override
    protected String determineScholarshipType() {
        double avgImpact = calculateAverageImpact();

        if (avgImpact >= 1.50) {
            return "Full";
        } else if (avgImpact >= 1.00) {
            return "Half";
        }

        return null;  // Bu duruma gelmemeli (evaluate'de kontrol edildi)
    }

    /**
     * Burs süresini hesaplar
     *
     * Base duration:
     * - Full → 1 yıl
     * - Half → 0.5 yıl (6 ay)
     *
     * RSV (Research Supervisor Approval) varsa:
     * - +1 yıl eklenir
     *
     * NOT: Sonuç tam sayı olarak döndürülür, bu yüzden:
     * - Half + RSV yok → 0.5 yıl → 1 yıl olarak gösterilir (yuvarlanır)
     * - Half + RSV var → 1.5 yıl → 2 yıl olarak gösterilir (yuvarlanır)
     *
     * @return Süre (yıl olarak, tam sayı)
     */
    @Override
    protected int calculateDuration() {
        double baseDuration;

        // Base duration'ı belirle
        if (scholarshipType.equals("Full")) {
            baseDuration = 1.0;  // 1 yıl
        } else {
            baseDuration = 0.5;  // 6 ay = 0.5 yıl
        }

        // RSV varsa +1 yıl ekle
        if (hasDocument("RSV")) {
            baseDuration += 1.0;
        }

        // Yuvarlayarak tam sayıya çevir
        // 0.5 → 1, 1.0 → 1, 1.5 → 2, 2.0 → 2
        return (int) Math.ceil(baseDuration);
    }

    /**
     * Burs adını döndürür
     * @return "Research"
     */
    @Override
    public String getScholarshipName() {
        return "Research";
    }

    /**
     * Yayın sayısını döndürür (debug için)
     */
    public int getPublicationCount() {
        return publications.size();
    }

    /**
     * Ortalama impact factor'ü döndürür (debug için)
     */
    public double getAverageImpact() {
        return calculateAverageImpact();
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return super.toString();
    }
}