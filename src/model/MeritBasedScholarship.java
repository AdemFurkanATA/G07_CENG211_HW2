package model;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List; // ArrayList yerine List kullanmak daha doğru

/**
 * MeritBasedScholarship - Başarı Bazlı Burs (11xx ID'ler)
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
public final class MeritBasedScholarship extends Application {

    // --- Sınıf Sabitleri (Constants) ---
    private static final double FULL_SCHOLARSHIP_GPA_THRESHOLD = 3.20;
    private static final double HALF_SCHOLARSHIP_GPA_THRESHOLD = 3.00;
    private static final double MERIT_MINIMUM_GPA = 3.00; // Merit-özel minimumu

    private static final int DURATION_WITH_REC = 2;    // yıl
    private static final int DURATION_WITHOUT_REC = 1; // yıl

    private static final String RECOMMENDATION_LETTER = "REC";
    private static final String SCHOLARSHIP_NAME = "Merit";

    /**
     * Ana Yapıcı Metot (Constructor).
     * Yeni bir MeritBasedScholarship nesnesi oluşturur.
     *
     * @param applicant Başvuru sahibi Applicant nesnesi.
     */
    public MeritBasedScholarship(Applicant applicant) {
        super(applicant);
    }

    /**
     * Kopya Yapıcı Metot (Copy Constructor).
     * Bu metot, varolan bir MeritBasedScholarship nesnesinin 'deep copy'sini (derin kopyasını) oluşturur.
     *
     * @param other Kopyalanacak diğer MeritBasedScholarship nesnesi.
     */
    public MeritBasedScholarship(MeritBasedScholarship other) {
        // 1. Üst sınıfın yapıcısını çağır.
        // 'other.applicant' protected olduğu için direkt erişebiliriz.
        // Kopyanın kopyasını almamak için getApplicant() KULLANMIYORUZ.
        super(new Applicant(other.applicant));

        Objects.requireNonNull(other, "Cannot copy from a null MeritBasedScholarship");

        // 2. Kapsanan listelerin deep copy'si (Defensive Copying)
        // other.getDocuments() artık 'List<Document>' döndürüyor (ana sınıftan)
        List<Document> docsToCopy = other.getDocuments();
        for (Document doc : docsToCopy) {
            this.addDocument(new Document(doc)); // Document'in copy constructor'ı
        }

        // other.getPublications() artık 'List<Publication>' döndürüyor (ana sınıftan)
        List<Publication> pubsToCopy = other.getPublications();
        for (Publication pub : pubsToCopy) {
            this.addPublication(new Publication(pub)); // Publication'ın copy constructor'ı
        }

        // 3. Diğer primitif ve immutable alanları kopyala
        this.setTranscriptStatus(other.getTranscriptStatus());
        this.status = other.status;
        this.scholarshipType = other.scholarshipType;
        this.durationInYears = other.durationInYears;
        this.rejectionReason = other.rejectionReason;
    }

    /**
     * Başarı bazlı burs başvurusunu değerlendirir.
     * Ödevde istenen ana iş mantığını (business logic) uygular.
     */
    @Override
    public void evaluate() {
        // 1. GENEL KONTROLLER (ENR, Transcript, GPA >= 2.50)
        if (!checkGeneralEligibility()) {
            return; // checkGeneralEligibility zaten rejectionReason'ı ayarlar
        }

        // 2. MERIT-ÖZEL KONTROLÜ (GPA < 3.00)
        if (applicant.getGpa() < MERIT_MINIMUM_GPA) {
            this.status = "Rejected";
            this.rejectionReason = "GPA below 3.0";
            return;
        }

        // 3. BAŞVURU KABUL EDİLDİ
        this.status = "Accepted";
        this.rejectionReason = null; // Red nedenini temizle

        // 4. BURS TÜRÜNÜ BELİRLE (Full/Half)
        this.scholarshipType = determineScholarshipType();

        // 5. SÜREYİ HESAPLA (REC'e göre)
        this.durationInYears = calculateDuration();
    }

    /**
     * GPA'ya göre burs türünü ("Full" veya "Half") belirler.
     * Bu metot, 'evaluate' tarafından çağrılır.
     *
     * @return "Full" veya "Half", ya da null (hata durumu)
     */
    @Override
    protected String determineScholarshipType() {
        double gpa = applicant.getGpa();

        if (gpa >= FULL_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Full";
        } else if (gpa >= HALF_SCHOLARSHIP_GPA_THRESHOLD) {
            return "Half";
        }
        return null; // evaluate() içindeki kontrollerden dolayı buraya düşmemeli
    }

    /**
     * Burs süresini REC belgesine göre belirler (1 veya 2 yıl).
     * Bu metot, 'evaluate' tarafından çağrılır.
     *
     * @return Süre (yıl olarak)
     */
    @Override
    protected int calculateDuration() {
        if (hasDocument(RECOMMENDATION_LETTER)) {
            return DURATION_WITH_REC;  // 2 yıl
        } else {
            return DURATION_WITHOUT_REC;  // 1 yıl
        }
    }

    /**
     * Bursun adını döndürür.
     *
     * @return "Merit"
     */
    @Override
    public String getScholarshipName() {
        return SCHOLARSHIP_NAME;
    }

    /**
     * Ana sınıfın toString() metodunu kullanır (hata ayıklama için).
     *
     * @return Başvurunun formatlanmış String hali
     */
    @Override
    public String toString() {
        return super.toString();
    }
}