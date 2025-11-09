package service;

import model.Application;
import java.util.List;
import java.util.Collections;

/**
 * EvaluationService - Başvuruları değerlendirir, sıralar ve raporlar.
 *
 * Bu servis sınıfı, başvurular listesi üzerinde ana iş mantığını
 * (değerlendirme, sıralama) yürüten metotları içerir.
 */
public class EvaluationService {

    /**
     * Verilen listedeki tüm başvuruları değerlendirir.
     * Her Application nesnesinin kendi (override edilmiş) evaluate() metodunu çağırır.
     * (Bu, Polymorphism'in uygulandığı yerdir).
     *
     * @param applications Değerlendirilecek başvuruların listesi (Bu liste değiştirilir).
     */
    public void evaluateAll(List<Application> applications) {
        for (Application application : applications) {
            application.evaluate();
        }
    }

    /**
     * Başvuruları Applicant ID'ye göre artan sırada sıralar.
     * 'Application' sınıfı 'Comparable' arayüzünü uyguladığı için
     * Collections.sort() metodu doğal sıralamayı (ID'ye göre) kullanır.
     *
     * @param applications Sıralanacak başvurular listesi (Bu liste değiştirilir).
     */
    public void sortByApplicantID(List<Application> applications) {
        // Application.compareTo() metodu kullanılır
        Collections.sort(applications);
    }

    /**
     * Tüm başvuru sonuçlarını (Application.toString() kullanarak) birleştirerek
     * yeni satırla ('\n') ayrılmış tek bir String olarak döndürür.
     *
     * @param applications Sonuçları alınacak başvurular listesi.
     * @return PDF'te istenen formata uygun, tüm sonuçları içeren String.
     */
    public String getResultsAsString(List<Application> applications) {
        StringBuilder sb = new StringBuilder();

        for (Application application : applications) {
            sb.append(application.toString()).append("\n");
        }

        // Çıktının sonunda fazladan bir boş satır olmasını engelle
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }

        return sb.toString();
    }
}