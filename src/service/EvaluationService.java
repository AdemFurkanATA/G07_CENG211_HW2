package service;

import model.Application;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EvaluationService {

    public void evaluateAll(List<Application> applications) {
        for (Application application : applications) {
            application.evaluate();
        }
    }

    public void sortByApplicantID(List<Application> applications) {
        Collections.sort(applications, new Comparator<Application>() {
            @Override
            public int compare(Application app1, Application app2) {
                String id1 = app1.getApplicant().getApplicantID();
                String id2 = app2.getApplicant().getApplicantID();

                try {
                    int numId1 = Integer.parseInt(id1);
                    int numId2 = Integer.parseInt(id2);
                    return Integer.compare(numId1, numId2);
                } catch (NumberFormatException e) {
                    return id1.compareTo(id2);
                }
            }
        });
    }

    public String getResultsAsString(List<Application> applications) {
        StringBuilder sb = new StringBuilder();
        for (Application app : applications) {
            sb.append(app.toString()).append("\n");
        }
        return sb.toString();
    }
}