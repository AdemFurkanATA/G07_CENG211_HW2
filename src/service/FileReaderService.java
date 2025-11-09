package service;

import model.*; // model paketindeki tüm sınıfları import et
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List; // ArrayList yerine List arayüzünü kullanmak daha iyidir

/**
 * FileReaderService - CSV dosyasını okur ve Application nesneleri oluşturur.
 *
 * Bu servis, CSV dosyasını iki aşamalı (two-pass) bir okuma stratejisi
 * kullanarak işler:
 * 1. Geçiş (Pass 1): Sadece 'A' (Applicant) satırlarını okur,
 * Application nesnelerini (Merit, Need, Research) oluşturur ve
 * bir HashMap'e ID'leriyle birlikte depolar.
 * 2. Geçiş (Pass 2): Diğer tüm satırları (T, I, D, P) okur ve
 * HashMap'te bulunan ilgili Application nesnelerini günceller.
 */
public class FileReaderService {

    /**
     * CSV dosyasını okur, parse eder ve Application nesnelerinden oluşan
     * bir ArrayList döndürür.
     *
     * @param filePath Okunacak CSV dosyasının yolu (örn: "Files/ScholarshipApplications.csv")
     * @return Doldurulmuş ve değerlendirilmeye hazır Application nesnelerinin listesi
     * @throws IOException Dosya okuma sırasında bir hata oluşursa
     */
    public ArrayList<Application> readAndParseApplications(String filePath) throws IOException {

        // Tüm başvuruları ID'ye göre hızlı erişim için saklayan ana Map
        Map<String, Application> applicationMap = new HashMap<>();

        // --- 1. GEÇİŞ: Başvuranları (Applicant) Oluştur ---
        // try-with-resources bloğu, 'reader' nesnesinin otomatik kapanmasını sağlar
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Boş satırları atla
                }

                String[] data = line.split(",");
                String prefix = data[0].trim();

                // Sadece 'A' satırlarıyla ilgilen
                if (prefix.equals("A")) {
                    parseAndCreateApplicant(data, applicationMap);
                }
            }
        } // 'reader' burada otomatik olarak reader.close() çağrılır

        // --- 2. GEÇİŞ: Başvuruları Detaylarla (T, I, D, P) Güncelle ---
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue; // Boş satırları atla
                }

                String[] data = line.split(",");
                String prefix = data[0].trim();

                // 'A' satırı olmayanlar için de ID'yi al (data[1])
                String applicantID = data[1].trim();

                // Map'ten ilgili başvuruyu al
                Application app = applicationMap.get(applicantID);

                // Eğer map'te bu ID'ye ait bir 'A' kaydı yoksa (hatalı veri),
                // bu satırı işlemeden atla.
                if (app == null) {
                    continue;
                }

                // Satır tipine göre ilgili güncelleme metodunu çağır
                switch (prefix) {
                    case "A":
                        // 'A' satırları 1. geçişte halledildi, tekrar işleme
                        break;
                    case "T":
                        parseTranscriptInfo(data, app);
                        break;
                    case "I":
                        parseFamilyInfo(data, app);
                        break;
                    case "D":
                        parseDocument(data, app);
                        break;
                    case "P":
                        parsePublication(data, app);
                        break;
                }
            }
        } // 'reader' burada da otomatik olarak reader.close() çağrılır

        // Map'teki tüm Application objelerini (values) yeni bir ArrayList'e
        // kopyala ve bu listeyi döndür.
        return new ArrayList<>(applicationMap.values());
    }

    /**
     * 'A' (Applicant) satırını parse eder, bir Applicant ve bir Application
     * nesnesi oluşturur ve bunları map'e ekler.
     * Format: A, applicantID, name, GPA, income
     */
    private void parseAndCreateApplicant(String[] data, Map<String, Application> map) {
        String applicantID = data[1].trim();
        String name = data[2].trim();
        double gpa = Double.parseDouble(data[3].trim());
        double income = Double.parseDouble(data[4].trim());

        // 1. Applicant (veri) nesnesini oluştur
        Applicant applicant = new Applicant(applicantID, name, gpa, income);

        // 2. ID'ye göre doğru Application (iş mantığı) nesnesini oluştur (Factory metodu)
        Application application = createApplicationByType(applicant);

        // 3. Oluşturulan başvuruyu map'e ekle
        map.put(applicantID, application);
    }

    /**
     * 'T' (Transcript) satırını parse eder ve varolan Application nesnesini günceller.
     * Format: T, applicantID, transcriptStatus (Y/N)
     */
    private void parseTranscriptInfo(String[] data, Application app) {
        String status = data[2].trim();
        app.setTranscriptStatus(status.equalsIgnoreCase("Y")); // "Y" veya "y" ise true
    }

    /**
     * 'I' (Family Info) satırını parse eder ve varolan Application nesnesini günceller.
     * Format: I, applicantID, familyIncome, dependents
     */
    private void parseFamilyInfo(String[] data, Application app) {
        // Bu bilgi sadece NeedBasedScholarship ile ilgilidir
        if (app instanceof NeedBasedScholarship) {
            double familyIncome = Double.parseDouble(data[2].trim());
            int dependents = Integer.parseInt(data[3].trim());

            // 'app' referansını NeedBasedScholarship'e cast et (tür dönüştür)
            // ve ilgili metodu çağır
            ((NeedBasedScholarship) app).setFamilyInfo(familyIncome, dependents);
        }
    }

    /**
     * 'D' (Document) satırını parse eder, bir Document nesnesi oluşturur
     * ve bunu varolan Application nesnesine ekler.
     * Format: D, applicantID, documentType, durationInMonths
     */
    private void parseDocument(String[] data, Application app) {
        String documentType = data[2].trim();
        int duration = Integer.parseInt(data[3].trim());

        // ID'yi Application'ın içindeki Applicant'ten al (veri tutarlılığı)
        String applicantID = app.getApplicant().getApplicantID();

        Document document = new Document(applicantID, documentType, duration);
        app.addDocument(document); // Application'daki listeye ekle
    }

    /**
     * 'P' (Publication) satırını parse eder, bir Publication nesnesi oluşturur
     * ve bunu varolan Application nesnesine ekler.
     * Format: P, applicantID, title, impactFactor
     */
    private void parsePublication(String[] data, Application app) {
        String title = data[2].trim();
        double impactFactor = Double.parseDouble(data[3].trim());

        String applicantID = app.getApplicant().getApplicantID();

        Publication publication = new Publication(applicantID, title, impactFactor);
        app.addPublication(publication); // Application'daki listeye ekle
    }

    /**
     * Applicant ID'sine göre uygun Application alt sınıfını (Merit, Need, Research)
     * oluşturan bir factory metodu.
     *
     * @param applicant Applicant (veri) nesnesi
     * @return Uygun Application (iş mantığı) nesnesi
     */
    private Application createApplicationByType(Applicant applicant) {
        String typeCode = applicant.getScholarshipTypeCode(); // "11", "22", veya "33"

        switch (typeCode) {
            case "11":
                return new MeritBasedScholarship(applicant);
            case "22":
                return new NeedBasedScholarship(applicant);
            case "33":
                return new ResearchGrant(applicant);
            default:
                // CSV'de "11", "22", "33" dışında bir prefix varsa
                // programın durması için bir exception fırlat.
                throw new IllegalArgumentException("Unknown scholarship type code: " + typeCode);
        }
    }
}