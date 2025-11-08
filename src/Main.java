import service.FileReaderService;
import service.EvaluationService;
import model.Application;
import java.util.ArrayList;

/**
 * Main - Program giriş noktası
 *
 * Bu sınıf Scholarship Evaluation System'i başlatır ve çalıştırır.
 *
 * İş Akışı:
 * 1. CSV dosyasını okur
 * 2. Application nesnelerini oluşturur
 * 3. Tüm başvuruları değerlendirir
 * 4. Sonuçları ID'ye göre sıralar
 * 5. Formatlanmış çıktı üretir
 *
 * @author [Your Name]
 * @version 1.0
 * @since 2025-11-16
 */
public class Main {

    // CSV dosyasının yolu (göreceli yol kullanılmalı!)
    private static final String CSV_FILE_PATH = "Files/ScholarshipApplications.csv";

    // Debug modu (detaylı çıktı için)
    private static final boolean DEBUG_MODE = false;

    /**
     * Program giriş noktası
     * @param args - Komut satırı argümanları (kullanılmıyor)
     */
    public static void main(String[] args) {

        // Başlangıç mesajı
        printWelcomeMessage();

        try {
            // ============ ADIM 1: CSV DOSYASINI OKU ============
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Reading CSV file: " + CSV_FILE_PATH);
            }

            FileReaderService fileReader = new FileReaderService();
            ArrayList<Application> applications =
                    fileReader.readAndParseApplications(CSV_FILE_PATH);

            if (DEBUG_MODE) {
                System.out.println("[DEBUG] Total applications loaded: " + applications.size());
            }

            // Başvuru yok kontrolü
            if (applications.isEmpty()) {
                System.out.println("\nNo applications found in the CSV file.");
                return;
            }

            // ============ ADIM 2: BAŞVURULARI DEĞERLENDİR ============
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Evaluating applications...");
            }

            EvaluationService evaluator = new EvaluationService();
            evaluator.evaluateAll(applications);

            if (DEBUG_MODE) {
                System.out.println("[DEBUG] Evaluation completed.");
            }

            // ============ ADIM 3: ID'YE GÖRE SIRALA ============
            if (DEBUG_MODE) {
                System.out.println("[DEBUG] Sorting applications by Applicant ID...");
            }

            evaluator.sortByApplicantID(applications);

            // ============ ADIM 4: SONUÇLARI YAZDIR ============
            System.out.println(); // Boş satır
            evaluator.printResults(applications);

            // ============ ADIM 5: İSTATİSTİKLER (Opsiyonel) ============
            if (DEBUG_MODE) {
                evaluator.printStatistics(applications);
            }

            // Başarı mesajı
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Program completed successfully.");
            }

        } catch (java.io.FileNotFoundException e) {
            // Dosya bulunamadı hatası
            System.err.println("\nERROR: CSV file not found!");
            System.err.println("Expected path: " + CSV_FILE_PATH);
            System.err.println("Please make sure the file exists and the path is correct.");
            System.err.println("\nDetails: " + e.getMessage());

            if (DEBUG_MODE) {
                e.printStackTrace();
            }

        } catch (java.io.IOException e) {
            // Dosya okuma hatası
            System.err.println("\nERROR: Failed to read the CSV file!");
            System.err.println("Details: " + e.getMessage());

            if (DEBUG_MODE) {
                e.printStackTrace();
            }

        } catch (NumberFormatException e) {
            // Sayı parse hatası
            System.err.println("\nERROR: Invalid number format in CSV file!");
            System.err.println("Please check that all numeric values are valid.");
            System.err.println("Details: " + e.getMessage());

            if (DEBUG_MODE) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            // Genel hata
            System.err.println("\nERROR: An unexpected error occurred!");
            System.err.println("Details: " + e.getMessage());

            if (DEBUG_MODE) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Hoş geldiniz mesajı yazdırır
     */
    private static void printWelcomeMessage() {
        if (DEBUG_MODE) {
            System.out.println("========================================");
            System.out.println("  SCHOLARSHIP EVALUATION SYSTEM");
            System.out.println("========================================");
            System.out.println("Version: 1.0");
            System.out.println("Course: CENG211 - Programming Fundamentals");
            System.out.println("Homework: #2");
            System.out.println("========================================");
        }
    }

    /**
     * Komut satırı argümanlarını işler (gelecekteki geliştirmeler için)
     *
     * Örnek kullanım:
     * java Main --file="Files/test.csv" --debug
     *
     * @param args - Komut satırı argümanları
     * @return CSV dosya yolu
     */
    private static String processCommandLineArguments(String[] args) {
        String filePath = CSV_FILE_PATH;  // Default

        for (String arg : args) {
            if (arg.startsWith("--file=")) {
                filePath = arg.substring(7);
            } else if (arg.equals("--debug")) {
                // DEBUG_MODE'u aktif et (final olduğu için şu an çalışmaz)
                System.out.println("Debug mode enabled via command line.");
            } else if (arg.equals("--help")) {
                printHelp();
                System.exit(0);
            }
        }

        return filePath;
    }

    /**
     * Yardım mesajı yazdırır
     */
    private static void printHelp() {
        System.out.println("Scholarship Evaluation System - Help");
        System.out.println("\nUsage:");
        System.out.println("  java Main [options]");
        System.out.println("\nOptions:");
        System.out.println("  --file=<path>    Specify CSV file path");
        System.out.println("  --debug          Enable debug mode");
        System.out.println("  --help           Show this help message");
        System.out.println("\nExample:");
        System.out.println("  java Main --file=\"Files/ScholarshipApplications.csv\" --debug");
    }

    /**
     * Programın çalışma süresini ölçer (performans testi için)
     *
     * @param applications - Başvurular listesi
     */
    private static void measurePerformance(ArrayList<Application> applications) {
        long startTime = System.currentTimeMillis();

        EvaluationService evaluator = new EvaluationService();
        evaluator.evaluateAll(applications);
        evaluator.sortByApplicantID(applications);

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("\n[PERFORMANCE] Evaluation completed in " + duration + " ms");
        System.out.println("[PERFORMANCE] Average time per application: "
                + (duration / (double) applications.size()) + " ms");
    }
}