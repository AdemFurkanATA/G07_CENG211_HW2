import service.FileReaderService;
import service.EvaluationService;
import model.Application;
import java.util.List; // Import 'List' arayüzü
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Ana program sınıfı.
 * Burs başvuru sistemini başlatır, verileri okur, değerlendirir ve sonuçları yazdırır.
 */
public class Main {

    // CSV dosyasının yolu
    private static final String CSV_FILE_PATH = "Files/ScholarshipApplications.csv";

    // Debug modu, ek çıktıların (istatistikler vb.) gösterilip gösterilmeyeceğini belirler.
    private static final boolean DEBUG_MODE = true;

    /**
     * Programın ana giriş noktası.
     * @param args Komut satırı argümanları (kullanılmıyor)
     */
    public static void main(String[] args) {

        // 0. Başlangıç mesajını yazdır
        printWelcomeMessage();

        try {
            // 1. CSV dosyasını oku ve Application nesnelerine dönüştür
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Reading CSV file: " + CSV_FILE_PATH);
            }

            FileReaderService fileReader = new FileReaderService();
            List<Application> applications =
                    fileReader.readAndParseApplications(CSV_FILE_PATH);

            if (DEBUG_MODE) {
                System.out.println("[DEBUG] Total applications loaded: " + applications.size());
            }

            // Başvuru listesi boşsa programı sonlandır
            if (applications.isEmpty()) {
                System.out.println("\nNo applications found in the CSV file.");
                return;
            }

            // 2. Başvuruları değerlendir
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Evaluating applications...");
            }

            EvaluationService evaluator = new EvaluationService();
            evaluator.evaluateAll(applications); // Listenin içindeki nesneleri günceller

            if (DEBUG_MODE) {
                System.out.println("[DEBUG] Evaluation completed.");
            }

            // 3. Başvuruları ID'ye göre sırala
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Sorting applications by Applicant ID...");
            }

            evaluator.sortByApplicantID(applications); // Listenin sırasını değiştirir

            // 4. Değerlendirme sonuçlarını yazdır
            System.out.println(); // Okunabilirlik için boş satır

            // Servisten formatlanmış sonuç String'ini al ve ekrana yazdır
            String results = evaluator.getResultsAsString(applications);
            System.out.println(results);

            // 5. İstatistikleri yazdır (debug modu aktifse)
            if (DEBUG_MODE) {
                // Servisten formatlanmış istatistik String'ini al ve ekrana yazdır
                String statistics = evaluator.getStatisticsAsString(applications);
                System.out.println(statistics);
            }

            // Program başarıyla tamamlandı
            if (DEBUG_MODE) {
                System.out.println("\n[DEBUG] Program completed successfully.");
            }

        } catch (FileNotFoundException e) {
            // Hata: CSV dosyası bulunamadı
            System.err.println("\nERROR: CSV file not found!");
            System.err.println("Expected path: " + CSV_FILE_PATH);
            System.err.println("Details: " + e.getMessage());

        } catch (IOException e) {
            // Hata: Dosya okunurken G/Ç hatası
            System.err.println("\nERROR: Failed to read the CSV file!");
            System.err.println("Details: " + e.getMessage());

        } catch (NumberFormatException e) {
            // Hata: CSV içinde geçersiz sayı formatı
            System.err.println("\nERROR: Invalid number format in CSV file!");
            System.err.println("Please check that all numeric values are valid.");
            System.err.println("Details: " + e.getMessage());

        } catch (Exception e) {
            // Hata: Beklenmeyen bir hata oluştu
            System.err.println("\nERROR: An unexpected error occurred!");
            System.err.println("Details: " + e.getMessage());
            e.printStackTrace(); // Hatanın detayını konsola yaz
        }
    }

    /**
     * Hoş geldiniz mesajı yazdırır (debug modu için).
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
     * Komut satırı argümanlarını işler (gelecekteki geliştirmeler için).
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
                // TODO: DEBUG_MODE'u dinamik olarak ayarla (final olduğu için şu an çalışmaz)
                System.out.println("Debug mode enabled via command line.");
            } else if (arg.equals("--help")) {
                printHelp();
                System.exit(0);
            }
        }

        return filePath;
    }

    /**
     * Yardım mesajı yazdırır.
     */
    private static void printHelp() {
        System.out.println("Scholarship Evaluation system - Help");
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
     * Programın çalışma süresini ölçer (performans testi için).
     *
     * @param applications - Başvurular listesi
     */
    private static void measurePerformance(List<Application> applications) {
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