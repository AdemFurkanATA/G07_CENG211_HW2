package service;

import model.Application;
import java.util.List;
import java.util.stream.Collectors;

/**
 * EvaluationService - Başvuruları değerlendirir, sıralar ve raporlar.
 *
 * Bu sınıf, başvuruların iş mantığını (değerlendirme, sıralama) uygular
 * ve sonuçları String formatında sunar. Sunum (System.out) bu sınıfta yapılmaz.
 */
public class EvaluationService {

    /**
     * Verilen listedeki tüm başvuruları değerlendirir.
     * Her Application nesnesinin kendi evaluate() metodunu çağırır.
     *
     * @param applications Değerlendirilecek başvuruların listesi.
     */
    public void evaluateAll(List<Application> applications) {
        for (Application application : applications) {
            application.evaluate();
        }
    }

    /**
     * Başvuruları Applicant ID'ye göre artan sırada sıralar.
     * (Yan etki: Metoda verilen orijinal listeyi değiştirir.)
     *
     * @param applications Sıralanacak başvurular listesi.
     */
    public void sortByApplicantID(List<Application> applications) {
        // ID'leri sayısal olarak karşılaştır, geçersiz ID varsa metin olarak karşılaştır.
        applications.sort((app1, app2) -> {
            String id1 = app1.getApplicant().getApplicantID();
            String id2 = app2.getApplicant().getApplicantID();

            try {
                // ID'leri integer'a çevirerek sayısal sıralama yap
                int numId1 = Integer.parseInt(id1);
                int numId2 = Integer.parseInt(id2);
                return Integer.compare(numId1, numId2);
            } catch (NumberFormatException e) {
                // Eğer ID'ler sayı değilse, alfasayısal (string) sıralama yap
                return id1.compareTo(id2);
            }
        });
    }

    /**
     * Tüm başvuru sonuçlarını (Application.toString()) birleştirerek
     * yeni satırla ayrılmış tek bir String olarak döndürür.
     *
     * @param applications Sonuçları alınacak başvurular listesi.
     * @return Formatlanmış sonuç String'i.
     */
    public String getResultsAsString(List<Application> applications) {
        StringBuilder sb = new StringBuilder();
        for (Application application : applications) {
            sb.append(application.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Başvuru listesi için detaylı istatistikler oluşturur ve
     * bunları formatlanmış bir String olarak döndürür.
     *
     * @param applications İstatistikleri hesaplanacak başvurular listesi.
     * @return İstatistik raporunu içeren String.
     */
    public String getStatisticsAsString(List<Application> applications) {
        if (applications == null || applications.isEmpty()) {
            return "No application data to generate statistics.";
        }

        int total = applications.size();
        int accepted = 0;
        int fullScholarships = 0;
        int halfScholarships = 0;

        int meritCount = 0;
        int needBasedCount = 0;
        int researchCount = 0;

        // Başvuruları tek tek analiz et
        for (Application app : applications) {
            if (app.getStatus().equals("Accepted")) {
                accepted++;
                if (app.getScholarshipType() != null) {
                    if (app.getScholarshipType().equals("Full")) {
                        fullScholarships++;
                    } else if (app.getScholarshipType().equals("Half")) {
                        halfScholarships++;
                    }
                }
            }

            // Başvuru türünü say
            String scholarshipName = app.getScholarshipName();
            if (scholarshipName.equals("Merit")) {
                meritCount++;
            } else if (scholarshipName.equals("Need-Based")) {
                needBasedCount++;
            } else if (scholarshipName.equals("Research")) {
                researchCount++;
            }
        }

        int rejected = total - accepted;

        // İstatistik raporunu StringBuilder ile oluştur
        StringBuilder sb = new StringBuilder();
        sb.append("\n=== STATISTICS ===\n");
        sb.append("Total Applications: ").append(total).append("\n");
        sb.append("Accepted: ").append(accepted).append(" (").append(String.format("%.1f", (accepted * 100.0 / total))).append("%)\n");
        sb.append("Rejected: ").append(rejected).append(" (").append(String.format("%.1f", (rejected * 100.0 / total))).append("%)\n");
        sb.append("\nScholarship Types:\n");
        sb.append("  Full Scholarships: ").append(fullScholarships).append("\n");
        sb.append("  Half Scholarships: ").append(halfScholarships).append("\n");
        sb.append("\nApplication Distribution:\n");
        sb.append("  Merit-Based: ").append(meritCount).append("\n");
        sb.append("  Need-Based: ").append(needBasedCount).append("\n");
        sb.append("  Research Grant: ").append(researchCount).append("\n");

        return sb.toString();
    }

    /**
     * Başvuruları burs türüne göre (Merit, Need-Based, Research) filtreler.
     * Orijinal listeyi değiştirmez, yeni bir liste döndürür.
     *
     * @param applications    Tüm başvuruların listesi.
     * @param scholarshipName Filtrelenecek burs adı (örn: "Merit").
     * @return Sadece belirtilen burs türüne ait başvuruları içeren yeni bir liste.
     */
    public List<Application> filterByScholarshipType(
            List<Application> applications, String scholarshipName) {

        // Stream API kullanarak listeyi filtrele
        return applications.stream()
                .filter(app -> app.getScholarshipName().equals(scholarshipName))
                .collect(Collectors.toList());
    }

    /**
     * Başvuruları durumuna göre (Accepted, Rejected) filtreler.
     * Orijinal listeyi değiştirmez, yeni bir liste döndürür.
     *
     * @param applications Tüm başvuruların listesi.
     * @param status       Filtrelenecek durum (örn: "Accepted").
     * @return Sadece belirtilen duruma sahip başvuruları içeren yeni bir liste.
     */
    public List<Application> filterByStatus(
            List<Application> applications, String status) {

        // Stream API kullanarak listeyi filtrele
        return applications.stream()
                .filter(app -> app.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}