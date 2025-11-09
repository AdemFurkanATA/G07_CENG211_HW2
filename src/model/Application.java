package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Application - Abstract (Soyut) ana sınıf
 * Tüm burs başvuru türlerinin ortak özelliklerini ve davranışlarını tanımlar
 * Bu sınıf, ID'ye göre sıralanabilmesi için Comparable arayüzünü uygular.
 */
public abstract class Application implements Comparable<Application> { // Comparable ekle

    // Ortak özellikler
    protected Applicant applicant;
    protected ArrayList<Document> documents;
    protected ArrayList<Publication> publications;
    protected boolean transcriptStatus;

    // Değerlendirme sonuçları
    protected String status;
    protected String scholarshipType;
    protected int durationInYears;
    protected String rejectionReason;

    /**
     * Application nesnesi oluşturur
     * @param applicant - Başvuran bilgileri
     */
    public Application(Applicant applicant) {
        this.applicant = applicant;
        this.documents = new ArrayList<>();
        this.publications = new ArrayList<>();
        this.transcriptStatus = false;
        this.status = "Pending";
        this.rejectionReason = null;
    }

    // ============ ABSTRACT METODLAR ============

    /**
     * Başvuruyu değerlendirir (her burs türü kendi kurallarına göre)
     */
    public abstract void evaluate();

    /**
     * Burs türünü belirler (Full/Half)
     */
    protected abstract String determineScholarshipType();

    /**
     * Burs süresini hesaplar
     */
    protected abstract int calculateDuration();

    /**
     * Başvurunun hangi burs türü olduğunu döndürür
     */
    public abstract String getScholarshipName();

    // ============ ORTAK KONTROL METODLARı ============

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

    public void addDocument(Document document) {
        this.documents.add(document);
    }

    public void addPublication(Publication publication) {
        this.publications.add(publication);
    }

    public void setTranscriptStatus(boolean status) {
        this.transcriptStatus = status;
    }

    // ============ GETTER METODLARı ============

    public Applicant getApplicant() {
        // Madem deep copy gördünüz, bunu da yap
        return (this.applicant == null) ? null : new Applicant(this.applicant);
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

    public boolean getTranscriptStatus() {
        return transcriptStatus;
    }

    /**
     * Kapsüllemeyi korumak için, listenin "değiştirilemez" (unmodifiable)
     * bir kopyasını döndürür. Bu, 'deep copy'den daha verimlidir.
     * @return Belgelerin 'read-only' (salt okunur) listesi
     */
    public List<Document> getDocuments() {
        return Collections.unmodifiableList(documents);
    }

    /**
     * Yayınların 'read-only' (salt okunur) listesini döndürür.
     * @return Yayınların 'read-only' listesi
     */
    public List<Publication> getPublications() {
        return Collections.unmodifiableList(publications);
    }

    // (Protected setter'lar kalabilir, onlar subclass'lar için gerekli)

    // ============ ÇIKTI VE SIRALAMA ============

    /**
     * Çıktı formatında sonucu döndürür (BU METOT KUSURSUZDU)
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
     * Comparable arayüzü için ID'ye göre karşılaştırma metodu.
     * Bu metot, Collections.sort() tarafından otomatik kullanılır.
     * O aptal 'compareById' yerine bunu yazdık.
     */
    @Override
    public int compareTo(Application other) {
        // ID'leri String olarak değil, Sayısal olarak karşılaştırmak DAHA DOĞRU
        try {
            int id1 = Integer.parseInt(this.applicant.getApplicantID());
            int id2 = Integer.parseInt(other.applicant.getApplicantID());
            return Integer.compare(id1, id2);
        } catch (NumberFormatException e) {
            // Eğer ID sayı değilse (hatalı veri), string karşılaştırması yap
            return this.applicant.getApplicantID().compareTo(other.applicant.getApplicantID());
        }
    }
}