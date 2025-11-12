import service.FileReaderService;
import service.EvaluationService;
import model.Application;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {

    private static final String CSV_FILE_PATH = "Files/ScholarshipApplications.csv";

    public static void main(String[] args) {
        try {
            // 1. Dosya Okuma
            FileReaderService fileReader = new FileReaderService();
            List<Application> applications = fileReader.readAndParseApplications(CSV_FILE_PATH);

            if (applications.isEmpty()) {
                System.out.println("No applications found.");
                return;
            }

            // 2. Değerlendirme
            EvaluationService evaluator = new EvaluationService();
            evaluator.evaluateAll(applications);

            // 3. Sıralama
            evaluator.sortByApplicantID(applications);

            // 4. Sonuç Yazdırma
            System.out.println();
            System.out.println(evaluator.getResultsAsString(applications));

        } catch (FileNotFoundException e) {
            System.err.println("Error: CSV file not found at " + CSV_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Error: Unable to read the file. " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error: Data format issue in CSV. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}