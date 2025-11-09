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

    /**
     * Programın ana giriş noktası.
     * @param args Komut satırı argümanları (kullanılmıyor)
     */
    public static void main(String[] args) {

        try {
            // 1. CSV dosyasını oku ve Application nesnelerine dönüştür
            FileReaderService fileReader = new FileReaderService();
            List<Application> applications =
                    fileReader.readAndParseApplications(CSV_FILE_PATH);

            // Başvuru listesi boşsa programı sonlandır
            if (applications.isEmpty()) {
                System.out.println("\nNo applications found in the CSV file.");
                return;
            }

            // 2. Başvuruları değerlendir
            EvaluationService evaluator = new EvaluationService();
            evaluator.evaluateAll(applications);

            // 3. Başvuruları ID'ye göre sırala
            evaluator.sortByApplicantID(applications);

            // 4. Değerlendirme sonuçlarını yazdır
            System.out.println(); // İstenen çıktı öncesi bir boşluk

            // Servisten formatlanmış sonuç String'ini al ve ekrana yazdır
            String results = evaluator.getResultsAsString(applications);
            System.out.println(results);


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
}