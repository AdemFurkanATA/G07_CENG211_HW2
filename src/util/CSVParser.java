package util;

public class CSVParser {

    private CSVParser() { }

    public static String[] parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        String[] parts = line.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = cleanValue(parts[i]);
        }
        return parts;
    }

    private static String cleanValue(String value) {
        if (value == null) return "";
        value = value.trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    public static double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
}