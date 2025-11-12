package model;

public class NeedBasedScholarship extends Application {

    // Özel alanlar
    private double familyIncome;
    private int dependents;

    // Gelir eşikleri (sabit referans değerler)
    private static final double BASE_FULL_THRESHOLD = 10000.0;
    private static final double BASE_HALF_THRESHOLD = 15000.0;

    public NeedBasedScholarship(Applicant applicant) {
        super(applicant);
        this.familyIncome = 0.0;
        this.dependents = 0;
    }

    public void setFamilyInfo(double familyIncome, int dependents) {
        this.familyIncome = familyIncome;
        this.dependents = dependents;
    }

    @Override
    public void evaluate() {
        // 1. Genel Kontroller
        if (!checkGeneralEligibility()) {
            return;
        }

        // 2. Eşikleri Hesapla
        // Her değerlendirmede temiz, sıfırdan hesaplama yapıyoruz.
        double currentFullThreshold = BASE_FULL_THRESHOLD;
        double currentHalfThreshold = BASE_HALF_THRESHOLD;

        double adjustment = 1.0;

        // SAV belgesi varsa %20 artış
        if (hasDocument("SAV")) {
            adjustment += 0.20;
        }
        // 3+ bakmakla yükümlü varsa %10 artış
        if (dependents >= 3) {
            adjustment += 0.10;
        }

        currentFullThreshold *= adjustment;
        currentHalfThreshold *= adjustment;

        // 3. Finansal Kontrol
        if (familyIncome > currentHalfThreshold) {
            this.status = "Rejected";
            this.rejectionReason = "Financial status unstable";
            return;
        }

        // 4. Kabul ve Tür Belirleme
        this.status = "Accepted";

        if (familyIncome <= currentFullThreshold) {
            this.scholarshipType = "Full";
        } else {
            this.scholarshipType = "Half";
        }

        // 5. Süre (Need-based için her zaman 1 yıl)
        this.durationInYears = calculateDuration();
    }

    @Override
    protected String determineScholarshipType() {
        return scholarshipType;
    }

    @Override
    protected int calculateDuration() {
        return 1;
    }

    @Override
    public String getScholarshipName() {
        return "Need-Based";
    }

    // Getter metodları
    public double getFamilyIncome() { return familyIncome; }
    public int getDependents() { return dependents; }
}