package service;

import model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * FileReaderService - CSV dosyasını okur ve Application nesneleri oluşturur
 *
 * İşlem Adımları:
 * 1. CSV dosyasını satır satır okur
 * 2. Her satırı parse eder (A, T, I, D, P)
 * 3. Verileri applicant ID'ye göre gruplar
 * 4. Her başvuran için uygun Application nesnesini oluşturur
 * 5. Belgeleri, yayınları ve diğer bilgileri ekler
 */
public class FileReaderService {

    /**
     * CSV dosyasını okur ve Application listesi döndürür
     * @param filePath - CSV dosyasının yolu (örn: "Files/ScholarshipApplications.csv")
     * @return Application nesnelerinin listesi
     * @throws IOException - Dosya okuma hatası
     */
    public ArrayList<Application> readAndParseApplications(String filePath) throws IOException {
        // 1. Verileri saklamak için map'ler oluştur
        Map<String, Applicant> applicants = new HashMap<>();
        Map<String, TranscriptInfo> transcripts = new HashMap<>();
        Map<String, ArrayList<Document>> documents = new HashMap<>();
        Map<String, ArrayList<Publication>> publications = new HashMap<>();
        Map<String, FamilyInfo> familyInfos = new HashMap<>();

        // 2. CSV dosyasını oku
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            // Boş satırları atla
            if (line.trim().isEmpty()) {
                continue;
            }

            // Satırı parse et
            String[] data = line.split(",");

            // Prefix'e göre işle
            String prefix = data[0].trim();

            switch (prefix) {
                case "A":
                    // Applicant bilgisi
                    parseApplicantInfo(data, applicants);
                    break;

                case "T":
                    // Transcript bilgisi
                    parseTranscriptInfo(data, transcripts);
                    break;

                case "I":
                    // Family Info
                    parseFamilyInfo(data, familyInfos);
                    break;

                case "D":
                    // Document
                    parseDocument(data, documents);
                    break;

                case "P":
                    // Publication
                    parsePublication(data, publications);
                    break;
            }
        }

        reader.close();

        // 3. Application nesnelerini oluştur
        ArrayList<Application> applications = createApplications(
                applicants, transcripts, documents, publications, familyInfos
        );

        return applications;
    }

    /**
     * Applicant satırını parse eder
     * Format: A, applicantID, name, GPA, income
     */
    private void parseApplicantInfo(String[] data, Map<String, Applicant> applicants) {
        String applicantID = data[1].trim();
        String name = data[2].trim();
        double gpa = Double.parseDouble(data[3].trim());
        double income = Double.parseDouble(data[4].trim());

        Applicant applicant = new Applicant(applicantID, name, gpa, income);
        applicants.put(applicantID, applicant);
    }

    /**
     * Transcript satırını parse eder
     * Format: T, applicantID, transcriptStatus (Y/N)
     */
    private void parseTranscriptInfo(String[] data, Map<String, TranscriptInfo> transcripts) {
        String applicantID = data[1].trim();
        String status = data[2].trim();

        TranscriptInfo transcript = new TranscriptInfo(applicantID, status);
        transcripts.put(applicantID, transcript);
    }

    /**
     * Family Info satırını parse eder
     * Format: I, applicantID, familyIncome, dependents
     */
    private void parseFamilyInfo(String[] data, Map<String, FamilyInfo> familyInfos) {
        String applicantID = data[1].trim();
        double familyIncome = Double.parseDouble(data[2].trim());
        int dependents = Integer.parseInt(data[3].trim());

        FamilyInfo familyInfo = new FamilyInfo(familyIncome, dependents);
        familyInfos.put(applicantID, familyInfo);
    }

    /**
     * Document satırını parse eder
     * Format: D, applicantID, documentType, durationInMonths
     */
    private void parseDocument(String[] data, Map<String, ArrayList<Document>> documents) {
        String applicantID = data[1].trim();
        String documentType = data[2].trim();
        int duration = Integer.parseInt(data[3].trim());

        Document document = new Document(applicantID, documentType, duration);

        // ArrayList'e ekle (varsa ekle, yoksa yeni oluştur)
        documents.computeIfAbsent(applicantID, k -> new ArrayList<>()).add(document);
    }

    /**
     * Publication satırını parse eder
     * Format: P, applicantID, title, impactFactor
     */
    private void parsePublication(String[] data, Map<String, ArrayList<Publication>> publications) {
        String applicantID = data[1].trim();
        String title = data[2].trim();
        double impactFactor = Double.parseDouble(data[3].trim());

        Publication publication = new Publication(applicantID, title, impactFactor);

        // ArrayList'e ekle
        publications.computeIfAbsent(applicantID, k -> new ArrayList<>()).add(publication);
    }

    /**
     * Tüm verileri birleştirerek Application nesneleri oluşturur
     */
    private ArrayList<Application> createApplications(
            Map<String, Applicant> applicants,
            Map<String, TranscriptInfo> transcripts,
            Map<String, ArrayList<Document>> documents,
            Map<String, ArrayList<Publication>> publications,
            Map<String, FamilyInfo> familyInfos) {

        ArrayList<Application> applications = new ArrayList<>();

        // Her başvuran için Application oluştur
        for (Map.Entry<String, Applicant> entry : applicants.entrySet()) {
            String applicantID = entry.getKey();
            Applicant applicant = entry.getValue();

            // ID'ye göre burs türünü belirle
            Application application = createApplicationByType(applicant);

            // Transcript bilgisini ekle
            TranscriptInfo transcript = transcripts.get(applicantID);
            if (transcript != null) {
                application.setTranscriptStatus(transcript.isValid());
            }

            // Belgeleri ekle
            ArrayList<Document> docs = documents.get(applicantID);
            if (docs != null) {
                for (Document doc : docs) {
                    application.addDocument(doc);
                }
            }

            // Yayınları ekle (Research grant için)
            ArrayList<Publication> pubs = publications.get(applicantID);
            if (pubs != null) {
                for (Publication pub : pubs) {
                    application.addPublication(pub);
                }
            }

            // Aile bilgilerini ekle (Need-based için)
            if (application instanceof NeedBasedScholarship) {
                FamilyInfo familyInfo = familyInfos.get(applicantID);
                if (familyInfo != null) {
                    ((NeedBasedScholarship) application).setFamilyInfo(
                            familyInfo.getFamilyIncome(),
                            familyInfo.getDependents()
                    );
                }
            }

            applications.add(application);
        }

        return applications;
    }

    /**
     * Applicant ID'sine göre uygun Application türünü oluşturur
     * 11xx → MeritBasedScholarship
     * 22xx → NeedBasedScholarship
     * 33xx → ResearchGrant
     */
    private Application createApplicationByType(Applicant applicant) {
        String typeCode = applicant.getScholarshipTypeCode();

        switch (typeCode) {
            case "11":
                return new MeritBasedScholarship(applicant);
            case "22":
                return new NeedBasedScholarship(applicant);
            case "33":
                return new ResearchGrant(applicant);
            default:
                throw new IllegalArgumentException("Unknown scholarship type: " + typeCode);
        }
    }

    /**
     * FamilyInfo - İç sınıf, aile bilgilerini geçici olarak tutar
     */
    private static class FamilyInfo {
        private double familyIncome;
        private int dependents;

        public FamilyInfo(double familyIncome, int dependents) {
            this.familyIncome = familyIncome;
            this.dependents = dependents;
        }

        public double getFamilyIncome() {
            return familyIncome;
        }

        public int getDependents() {
            return dependents;
        }
    }
}