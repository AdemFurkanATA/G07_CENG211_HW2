package service;

import model.*;
import util.CSVParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileReaderService {

    public ArrayList<Application> readAndParseApplications(String filePath) throws IOException {
        Map<String, Applicant> applicants = new HashMap<>();
        Map<String, TranscriptInfo> transcripts = new HashMap<>();
        Map<String, ArrayList<Document>> documents = new HashMap<>();
        Map<String, ArrayList<Publication>> publications = new HashMap<>();
        Map<String, FamilyInfo> familyInfos = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] data = CSVParser.parseLine(line);

            if (data.length == 0) {
                continue;
            }

            String prefix = data[0];

            switch (prefix) {
                case "A":
                    String appID = data[1];
                    double gpa = CSVParser.parseDouble(data[3], 0.0);
                    double income = CSVParser.parseDouble(data[4], 0.0);
                    applicants.put(appID, new Applicant(appID, data[2], gpa, income));
                    break;

                case "T":
                    transcripts.put(data[1], new TranscriptInfo(data[1], data[2]));
                    break;

                case "I":
                    double famIncome = CSVParser.parseDouble(data[2], 0.0);
                    int deps = CSVParser.parseInt(data[3], 0);
                    familyInfos.put(data[1], new FamilyInfo(famIncome, deps));
                    break;

                case "D":
                    String docID = data[1];
                    int duration = CSVParser.parseInt(data[3], 0);
                    Document doc = new Document(docID, data[2], duration);

                    if (!documents.containsKey(docID)) {
                        documents.put(docID, new ArrayList<>());
                    }
                    documents.get(docID).add(doc);
                    break;

                case "P":
                    String pubID = data[1];
                    double impact = CSVParser.parseDouble(data[3], 0.0);
                    Publication pub = new Publication(pubID, data[2], impact);

                    if (!publications.containsKey(pubID)) {
                        publications.put(pubID, new ArrayList<>());
                    }
                    publications.get(pubID).add(pub);
                    break;
            }
        }

        reader.close();

        return createApplications(applicants, transcripts, documents, publications, familyInfos);
    }

    private ArrayList<Application> createApplications(
            Map<String, Applicant> applicants,
            Map<String, TranscriptInfo> transcripts,
            Map<String, ArrayList<Document>> documents,
            Map<String, ArrayList<Publication>> publications,
            Map<String, FamilyInfo> familyInfos) {

        ArrayList<Application> applications = new ArrayList<>();

        for (Map.Entry<String, Applicant> entry : applicants.entrySet()) {
            String applicantID = entry.getKey();
            Applicant applicant = entry.getValue();

            Application application = createApplicationByType(applicant);

            if (transcripts.containsKey(applicantID)) {
                application.setTranscriptStatus(transcripts.get(applicantID).isValid());
            }

            if (documents.containsKey(applicantID)) {
                for (Document doc : documents.get(applicantID)) {
                    application.addDocument(doc);
                }
            }

            if (publications.containsKey(applicantID)) {
                for (Publication pub : publications.get(applicantID)) {
                    application.addPublication(pub);
                }
            }

            if (application instanceof NeedBasedScholarship) {
                FamilyInfo info = familyInfos.get(applicantID);
                if (info != null) {
                    ((NeedBasedScholarship) application).setFamilyInfo(info.getFamilyIncome(), info.getDependents());
                }
            }

            applications.add(application);
        }

        return applications;
    }

    private Application createApplicationByType(Applicant applicant) {
        String typeCode = applicant.getScholarshipTypeCode();
        switch (typeCode) {
            case "11": return new MeritBasedScholarship(applicant);
            case "22": return new NeedBasedScholarship(applicant);
            case "33": return new ResearchGrant(applicant);
            default: throw new IllegalArgumentException("Unknown scholarship type: " + typeCode);
        }
    }

    private static class FamilyInfo {
        private double familyIncome;
        private int dependents;

        public FamilyInfo(double familyIncome, int dependents) {
            this.familyIncome = familyIncome;
            this.dependents = dependents;
        }
        public double getFamilyIncome() { return familyIncome; }
        public int getDependents() { return dependents; }
    }
}