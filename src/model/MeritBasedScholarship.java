package model;

/**
 * MeritBasedScholarship - Akademik Başarı Bursu (11xx ID'ler)
 *
 * Değerlendirme Kriterleri:
 * - GPA >= 3.20 → Full Scholarship
 * - 3.00 <= GPA < 3.20 → Half Scholarship
 * - GPA < 3.00 → Rejected
 *
 * Süre:
 * - REC (Recommendation Letter) varsa → 2 yıl
 * - REC yoksa → 1 yıl
 */
public class MeritBasedScholarship extends Application {

    // Constructor
    public MeritBasedScholarship(Applicant applicant) {
        super(applicant);  // Application'ın constructor'ını çağır
    }

    /**
     * Akademik burs başvurusunu değerlendirir
     */
    @Override
    public void evaluate() {
        // 1. GENEL KONTROLLER (ENR, Transcript, GPA >= 2.50)
        if (!checkGeneralEligibility()) {
            return;  // Genel şartlar sağlanmazsa, rejectionReason zaten set edildi
        }

        // 2. MERİT-SPECIFIC GPA KONTROLÜ
        // GPA < 3.00 ise red
        if (applicant.getGpa() < 3.00) {
            this.status = "Rejected";
            this.rejectionReason = "GPA below 3.0";
            return;
        }

        // 3. BAŞVURU KABUL EDİLDİ
        this.status = "Accepted";

        // 4. BURS TÜRÜNÜ BELİRLE (Full/Half)
        this.scholarshipType = determineScholarshipType();

        // 5. SÜREYİ HESAPLA (1 veya 2 yıl)
        this.durationInYears = calculateDuration();
    }

    /**
     * GPA'ya göre burs türünü belirler
     * @return "Full" veya "Half"
     */
    @Override
    protected String determineScholarshipType() {
        if (applicant.getGpa() >= 3.20) {
            return "Full";
        } else if (applicant.getGpa() >= 3.00) {
            return "Half";
        }
        return null;  // Bu duruma gelmemeli (evaluate'de kontrol edildi)
    }

    /**
     * Burs süresini hesaplar
     * REC belgesi varsa 2 yıl, yoksa 1 yıl
     * @return Süre (yıl olarak)
     */
    @Override
    protected int calculateDuration() {
        if (hasDocument("REC")) {
            return 2;  // Recommendation Letter varsa 2 yıl
        } else {
            return 1;  // Yoksa 1 yıl
        }
    }

    /**
     * Burs adını döndürür
     * @return "Merit"
     */
    @Override
    public String getScholarshipName() {
        return "Merit";
    }

    /**
     * Debug için bilgi döndürür
     */
    @Override
    public String toString() {
        return super.toString();
    }
}