package service;

import model.Application;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * EvaluationService - Başvuruları değerlendirir, sıralar ve sonuçları yazdırır
 *
 * Görevler:
 * 1. Tüm başvuruları değerlendirir (evaluate())
 * 2. Başvuruları ID'ye göre sıralar
 * 3. Sonuçları formatlanmış şekilde yazdırır
 */
public class EvaluationService {

    /**
     * Tüm başvuruları değerlendirir
     * Her Application'ın evaluate() metodunu çağırır
     *
     * @param applications - Değerlendirilecek başvurular listesi
     */
    public void evaluateAll(ArrayList<Application> applications) {
        for (Application application : applications) {
            application.evaluate();
        }
    }

    /**
     * Başvuruları Applicant ID'ye göre sıralar (küçükten büyüğe)
     * Örnek: 1101, 1102, 1114, 2201, 2205, 3301, 3312
     *
     * @param applications - Sıralanacak başvurular listesi
     */
    public void sortByApplicantID(ArrayList<Application> applications) {
        Collections.sort(applications, new Comparator<Application>() {
            @Override
            public int compare(Application app1, Application app2) {
                // String karşılaştırma yapar
                String id1 = app1.getApplicant().getApplicantID();
                String id2 = app2.getApplicant().getApplicantID();

                // Sayısal karşılaştırma için integer'a çevir
                try {
                    int numId1 = Integer.parseInt(id1);
                    int numId2 = Integer.parseInt(id2);
                    return Integer.compare(numId1, numId2);
                } catch (NumberFormatException e) {
                    // Eğer integer'a çevrilemezse string olarak karşılaştır
                    return id1.compareTo(id2);
                }
            }
        });
    }

    /**
     * Başvuruları Applicant ID'ye göre sıralar (alternatif yöntem)
     * Lambda expression kullanır (Java 8+)
     *
     * @param applications - Sıralanacak başvurular listesi
     */
    public void sortByApplicantIDLambda(ArrayList<Application> applications) {
        applications.sort((app1, app2) -> {
            String id1 = app1.getApplicant().getApplicantID();
            String id2 = app2.getApplicant().getApplicantID();

            try {
                int numId1 = Integer.parseInt(id1);
                int numId2 = Integer.parseInt(id2);
                return Integer.compare(numId1, numId2);
            } catch (NumberFormatException e) {
                return id1.compareTo(id2);
            }
        });
    }

    /**
     * Tüm başvuru sonuçlarını formatlanmış şekilde yazdırır
     *
     * Format:
     * - Accepted: Applicant ID: 1101, Name: Liam Smith, Scholarship: Merit, Status: Accepted, Type: Full, Duration: 2 years
     * - Rejected: Applicant ID: 1120, Name: Oliver Brown, Scholarship: Merit, Status: Rejected, Reason: GPA below 3.0
     *
     * @param applications - Yazdırılacak başvurular listesi
     */
    public void printResults(ArrayList<Application> applications) {
        for (Application application : applications) {
            System.out.println(application.toString());
        }
    }

    /**
     * Kabul edilen başvuruları yazdırır
     * @param applications - Başvurular listesi
     */
    public void printAcceptedApplications(ArrayList<Application> applications) {
        System.out.println("=== ACCEPTED APPLICATIONS ===");
        for (Application application : applications) {
            if (application.getStatus().equals("Accepted")) {
                System.out.println(application.toString());
            }
        }
    }

    /**
     * Reddedilen başvuruları yazdırır
     * @param applications - Başvurular listesi
     */
    public void printRejectedApplications(ArrayList<Application> applications) {
        System.out.println("=== REJECTED APPLICATIONS ===");
        for (Application application : applications) {
            if (application.getStatus().equals("Rejected")) {
                System.out.println(application.toString());
            }
        }
    }

    /**
     * İstatistikleri yazdırır
     * - Toplam başvuru sayısı
     * - Kabul edilen sayısı
     * - Reddedilen sayısı
     * - Burs türlerine göre dağılım
     *
     * @param applications - Başvurular listesi
     */
    public void printStatistics(ArrayList<Application> applications) {
        int total = applications.size();
        int accepted = 0;
        int rejected = 0;
        int fullScholarships = 0;
        int halfScholarships = 0;

        // Burs türlerine göre sayaçlar
        int meritCount = 0;
        int needBasedCount = 0;
        int researchCount = 0;

        for (Application app : applications) {
            if (app.getStatus().equals("Accepted")) {
                accepted++;
                if (app.getScholarshipType().equals("Full")) {
                    fullScholarships++;
                } else {
                    halfScholarships++;
                }
            } else {
                rejected++;
            }

            // Burs türünü say
            String scholarshipName = app.getScholarshipName();
            if (scholarshipName.equals("Merit")) {
                meritCount++;
            } else if (scholarshipName.equals("Need-Based")) {
                needBasedCount++;
            } else if (scholarshipName.equals("Research")) {
                researchCount++;
            }
        }

        System.out.println("\n=== STATISTICS ===");
        System.out.println("Total Applications: " + total);
        System.out.println("Accepted: " + accepted + " (" + String.format("%.1f", (accepted * 100.0 / total)) + "%)");
        System.out.println("Rejected: " + rejected + " (" + String.format("%.1f", (rejected * 100.0 / total)) + "%)");
        System.out.println("\nScholarship Types:");
        System.out.println("  Full Scholarships: " + fullScholarships);
        System.out.println("  Half Scholarships: " + halfScholarships);
        System.out.println("\nApplication Distribution:");
        System.out.println("  Merit-Based: " + meritCount);
        System.out.println("  Need-Based: " + needBasedCount);
        System.out.println("  Research Grant: " + researchCount);
    }

    /**
     * Belirli bir burs türünün başvurularını filtreler
     * @param applications - Tüm başvurular
     * @param scholarshipName - Filtre ("Merit", "Need-Based", "Research")
     * @return Filtrelenmiş liste
     */
    public ArrayList<Application> filterByScholarshipType(
            ArrayList<Application> applications, String scholarshipName) {
        ArrayList<Application> filtered = new ArrayList<>();
        for (Application app : applications) {
            if (app.getScholarshipName().equals(scholarshipName)) {
                filtered.add(app);
            }
        }
        return filtered;
    }

    /**
     * Belirli bir duruma göre başvuruları filtreler
     * @param applications - Tüm başvurular
     * @param status - Filtre ("Accepted" veya "Rejected")
     * @return Filtrelenmiş liste
     */
    public ArrayList<Application> filterByStatus(
            ArrayList<Application> applications, String status) {
        ArrayList<Application> filtered = new ArrayList<>();
        for (Application app : applications) {
            if (app.getStatus().equals(status)) {
                filtered.add(app);
            }
        }
        return filtered;
    }
}