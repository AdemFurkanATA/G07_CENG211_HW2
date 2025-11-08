package util;

/**
 * CSVParser - CSV satırlarını parse etmek için yardımcı sınıf
 *
 * Bu sınıf CSV dosyalarından okunan satırları işler ve
 * virgülle ayrılmış değerleri array'e çevirir.
 *
 * Özellikler:
 * - Basit virgül ayırma
 * - Boşlukları temizleme
 * - Tırnak işaretlerini temizleme
 * - Hata kontrolü
 */
public class CSVParser {

    /**
     * CSV satırını parse eder ve String array'i döndürür
     * Basit virgül ayırma kullanır
     *
     * @param line - CSV satırı (örn: "A,1101,Liam Smith,3.45,12000")
     * @return Parse edilmiş String array
     */
    public static String[] parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }

        // Virgüllerle ayır
        String[] parts = line.split(",");

        // Her parçayı temizle (boşlukları ve tırnakları kaldır)
        for (int i = 0; i < parts.length; i++) {
            parts[i] = cleanValue(parts[i]);
        }

        return parts;
    }

    /**
     * CSV satırını parse eder (gelişmiş - tırnak içindeki virgülleri göz ardı eder)
     * Örnek: "A,1101,\"Smith, John\",3.45,12000" → ["A", "1101", "Smith, John", "3.45", "12000"]
     *
     * @param line - CSV satırı
     * @return Parse edilmiş String array
     */
    public static String[] parseLineAdvanced(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }

        // Basit durumlar için (tırnak yoksa) hızlı yol
        if (!line.contains("\"")) {
            return parseLine(line);
        }

        // Tırnak içindeki virgülleri koruyarak parse et
        java.util.ArrayList<String> result = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(cleanValue(current.toString()));
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }

        // Son değeri ekle
        result.add(cleanValue(current.toString()));

        return result.toArray(new String[0]);
    }

    /**
     * String değeri temizler (boşlukları ve tırnakları kaldırır)
     *
     * @param value - Temizlenecek değer
     * @return Temizlenmiş değer
     */
    private static String cleanValue(String value) {
        if (value == null) {
            return "";
        }

        // Başındaki ve sonundaki boşlukları kaldır
        value = value.trim();

        // Tırnak işaretlerini kaldır
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return value;
    }

    /**
     * CSV satırının geçerli olup olmadığını kontrol eder
     *
     * @param line - Kontrol edilecek satır
     * @return true ise geçerli
     */
    public static boolean isValidLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }

        // En az bir virgül olmalı
        if (!line.contains(",")) {
            return false;
        }

        return true;
    }

    /**
     * Satırın prefix'ini döndürür (ilk karakteri)
     *
     * @param line - CSV satırı
     * @return Prefix (A, T, I, D, P) veya boş string
     */
    public static String getPrefix(String line) {
        if (line == null || line.trim().isEmpty()) {
            return "";
        }

        String[] parts = line.split(",");
        if (parts.length > 0) {
            return parts[0].trim();
        }

        return "";
    }

    /**
     * String'i double'a çevirir, hata durumunda default değer döndürür
     *
     * @param value - Çevrilecek değer
     * @param defaultValue - Hata durumunda dönecek değer
     * @return Double değer
     */
    public static double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * String'i int'e çevirir, hata durumunda default değer döndürür
     *
     * @param value - Çevrilecek değer
     * @param defaultValue - Hata durumunda dönecek değer
     * @return Integer değer
     */
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Boolean değeri parse eder (Y/N formatı için)
     * Y, Yes, y, yes → true
     * N, No, n, no → false
     *
     * @param value - Çevrilecek değer
     * @return Boolean değer
     */
    public static boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }

        value = value.trim().toUpperCase();
        return value.equals("Y") || value.equals("YES") || value.equals("TRUE");
    }

    /**
     * CSV satırını debug için yazdırır
     *
     * @param line - Yazdırılacak satır
     */
    public static void debugPrintLine(String line) {
        System.out.println("Line: " + line);
        String[] parts = parseLine(line);
        for (int i = 0; i < parts.length; i++) {
            System.out.println("  [" + i + "] = \"" + parts[i] + "\"");
        }
    }
}