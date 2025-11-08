package model;

/**
 * Applicant class - Başvuran kişinin temel bilgilerini tutar
 * CSV'deki "A" ile başlayan satırlardan oluşturulur
 * Format: A, applicantID, name, GPA, income
 */
public class Applicant {

    // Fields (Özellikler)
    private String applicantID;
    private String name;
    private double gpa;
    private double income;

    // Constructor (Yapıcı Metod)
    /**
     * Applicant nesnesi oluşturur
     * @param applicantID - Başvuran ID'si (örn: 1101, 2201, 3301)
     * @param name - Başvuran adı
     * @param gpa - Not ortalaması (0.0 - 4.0 arası)
     * @param income - Aylık gelir
     */
    public class Applicant {
        private String applicantID;
        private String name;
        private double gpa;
        private double income;

        public Applicant(String applicantID, String name, double gpa, double income) {
            this.applicantID = applicantID;
            this.name = name;
            this.gpa = gpa;
            this.income = income;
        }

        // Getters (Değerleri almak için)
        public String getApplicantID() {
            return applicantID;
        }

        public String getName() {
            return name;
        }

        public double getGpa() {
            return gpa;
        }

        public double getIncome() {
            return income;
        }

        // Setters (Değerleri değiştirmek için - gerekirse)
        public void setApplicantID(String applicantID) {
            this.applicantID = applicantID;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setGpa(double gpa) {
            this.gpa = gpa;
        }

        public void setIncome(double income) {
            this.income = income;
        }

        /**
         * Başvuranın hangi burs türüne ait olduğunu belirler
         * 11xx -> Merit-Based
         * 22xx -> Need-Based
         * 33xx -> Research Grant
         * @return Burs türü kodu (11, 22, veya 33)
         */
        public String getScholarshipTypeCode() {
            if (applicantID != null && applicantID.length() >= 2) {
                return applicantID.substring(0, 2);
            }
            return "";
        }

        /**
         * GPA'nın minimum şartı sağlayıp sağlamadığını kontrol eder
         * Minimum GPA: 2.50
         * @return true ise GPA yeterli, false ise yetersiz
         */
        public boolean meetsMinimumGPA() {
            return gpa >= 2.50;
        }

        /**
         * Applicant bilgilerini string olarak döndürür (debug için kullanışlı)
         */
        @Override
        public String toString() {
            return "Applicant{" +
                    "ID='" + applicantID + '\'' +
                    ", name='" + name + '\'' +
                    ", GPA=" + gpa +
                    ", income=" + income +
                    '}';
        }

        /**
         * İki Applicant nesnesini karşılaştırır (ID'ye göre)
         * ArrayList'te arama yaparken kullanışlı
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Applicant applicant = (Applicant) obj;
            return applicantID.equals(applicant.applicantID);
        }

        /**
         * Hash code oluşturur (equals ile birlikte kullanılır)
         */
        @Override
        public int hashCode() {
            return applicantID.hashCode();
        }
    }