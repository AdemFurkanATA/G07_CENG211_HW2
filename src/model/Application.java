package model;

import java.util.ArrayList;

/**
 * Application - Abstract (Soyut) ana sınıf
 * Tüm burs başvuru türlerinin ortak özelliklerini ve davranışlarını tanımlar
 * Bu sınıftan direkt nesne oluşturulamaz!
 *
 * Alt sınıflar:
 * - MeritBasedScholarship (11xx)
 * - NeedBasedScholarship (22xx)
 * - ResearchGrant (33xx)
 */
public abstract class Application {

    // Ortak özellikler (tüm burs türlerinde var)
    protected Applicant applicant;                    // Başvuran bilgileri
    protected ArrayList<Document> documents;          // Belgeler listesi
    protected ArrayList<Publication> publications;    // Yayınlar listesi (research için)
    protected boolean transcriptStatus;               // Transcript geçerli mi? (Y/N)

    // Değerlendirme sonuçları
    protected String status;              // "Accepted" veya "Rejected"
    protected String scholarshipType;     // "Full", "Half", veya null
    protected int durationInYears;        // Burs süresi (yıl olarak)
    protected String rejectionReason;     // Red nedeni (varsa)

    // Constructor
    /**
     * Application nesnesi oluşturur
     * @param applicant - Başvuran bilgileri
     */
    public Application(Applicant applicant) {
        this.applicant = applicant;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.transcriptStatus = false;
        this.status = "Pending";  // Başlangıçta beklemede
        this.scholarshipType = null;
        this.durationInYears = 0;
        this.rejectionReason = null;
    }

    // ============ ABSTRACT METODLAR ============
    // Bu metodlar her alt sınıfta farklı şekilde implement edilmeli

    /**
     * Başvuruyu değerlendirir (her burs türü kendi kurallarına göre)
     * Bu metod alt sınıflarda mutlaka yazılmalı!
     */
    public abstract void evaluate();

    /**
     * Burs türünü belirler (Full/Half)
     * Her burs türünün kendine özgü kriterleri var
     */
    protected abstract String determineScholarshipType();

    /**
     * Burs süresini hesaplar
     * Her burs türünün farklı süre hesaplama mantığı var
     */
    protected abstract int calculateDuration();

    // ============ ORTAK KONTROL METODLARı ============
    // Tüm burs türlerinde aynı şekilde çalışan metodlar

    /**
     * Genel uygunluk kontrolü (tüm burs türleri için aynı)
     * 1. ENR belgesi var mı?
     * 2. Transcript geçerli mi?
     * 3. GPA >= 2.50 mi?
     * @return true ise genel şartlar sağlanıyor
     */
    public boolean checkGeneralEligibility() {
        // 1. Kayıt belgesi kontrolü (ENR)
        if (!hasDocument("ENR")) {
            this.status = "Rejected";
            this.rejectionReason = "Missing Enrollment Certificate";
            return false;
        }

        // 2. Transcript kontrolü
        if (!transcriptStatus) {
            this.status = "Rejected";
            this.rejectionReason = "Missing Transcript";
            return false;
        }

        // 3. Minimum GPA kontrolü (2.50)
        if (applicant.getGpa() < 2.50) {
            this.status = "Rejected";
            this.rejectionReason = "GPA below 2.5";
            return false;
        }

        // Tüm genel şartlar sağlanıyor
        return true;
    }

    /**
     * Belirli bir belge türünün var olup olmadığını kontrol eder
     * @param documentType - Belge kodu (ENR, REC, SAV, RSV, GRP)
     * @return true ise belge mevcut
     */
    public boolean hasDocument(String documentType) {
        for (Document doc : documents) {
            if (doc.getDocumentType().equals(documentType)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Belirli bir belgeyi döndürür (varsa)
     * @param documentType - Belge kodu
     * @return Document nesnesi veya null
     */
    public Document getDocument(String documentType) {
        for (Document doc : documents) {
            if (doc.getDocumentType().equals(documentType)) {
                return doc;
            }
        }
        return null;
    }

    // ============ EKLEME METODLARı ============

    /**
     * Belge ekler
     */
    public void addDocument(Document document) {
        this.documents.add(document);
    }

    /**
     * Yayın ekler (Research grant için)
     */
    public void addPublication(Publication publication) {
        this.publications.add(publication);
    }

    /**
     * Transcript durumunu ayarlar
     */
    public void setTranscriptStatus(boolean status) {
        this.transcriptStatus = status;
    }

    // ============ GETTER METODLARı ============

    public Applicant getApplicant() {
        return applicant;
    }

    public String getStatus() {
        return status;
    }

    public String getScholarshipType() {
        return scholarshipType;
    }

    public int getDurationInYears() {
        return durationInYears;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public ArrayList<Document> getDocuments() {
        return documents;
    }

    public ArrayList<Publication> getPublications() {
        return publications;
    }

    public boolean getTranscriptStatus() {
        return transcriptStatus;
    }

    // ============ SETTER METODLARı ============

    protected void setStatus(String status) {
        this.status = status;
    }

    protected void setScholarshipType(String scholarshipType) {
        this.scholarshipType = scholarshipType;
    }

    protected void setDurationInYears(int duration) {
        this.durationInYears = duration;
    }

    protected void setRejectionReason(String reason) {
        this.rejectionReason = reason;
    }

    /**
     * Başvurunun hangi burs türü olduğunu döndürür
     * Alt sınıflarda override edilebilir
     */
    public abstract String getScholarshipName();

    /**
     * Çıktı formatında sonucu döndürür
     * Format: Applicant ID: 1101, Name: Liam Smith, Scholarship: Merit, Status: Accepted, Type: Full, Duration: 2 years
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Applicant ID: ").append(applicant.getApplicantID());
        result.append(", Name: ").append(applicant.getName());
        result.append(", Scholarship: ").append(getScholarshipName());
        result.append(", Status: ").append(status);

        if (status.equals("Accepted")) {
            result.append(", Type: ").append(scholarshipType);
            result.append(", Duration: ").append(durationInYears);
            result.append(durationInYears == 1 ? " year" : " years");
        } else {
            result.append(", Reason: ").append(rejectionReason);
        }

        return result.toString();
    }

    /**
     * ID'ye göre karşılaştırma yapar (sıralama için)
     */
    public int compareById(Application other) {
        return this.applicant.getApplicantID().compareTo(other.applicant.getApplicantID());
    }
}