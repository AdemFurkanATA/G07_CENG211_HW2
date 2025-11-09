package util;

public class CSVParser {

    private CSVParser() { }

    //Returns Parsed string array
    public static String[] parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return new String[0];
        }
        // Parse with coma
        String[] parts = line.split(",");

        // Clean up every part of it (remove spaces and quotes)
        for (int i = 0; i < parts.length; i++) {
            parts[i] = cleanValue(parts[i]);
        }
        return parts;
    }

    //Clears the string value (removes spaces and quotes)
    private static String cleanValue(String value) {
        if (value == null) {
            return "";
        }
        // Remove leading and trailing spaces
        value = value.trim();

        // Remove quotation marks
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        return value;
    }

    //Checks if CSV row is valid
    public static boolean isValidLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return false;
        }
        // There must be at least one comma
        if (!line.contains(",")) {
            return false;
        }
        return true;
    }

    //Returns the prefix (first character) of the line
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

    //Converts string to double, returns default value in case of error
    public static double parseDouble(String value, double defaultValue) {
        try {
            return Double.parseDouble(value.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    //Converts string to integer, returns default value in case of error
    public static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException | NullPointerException e) {
            return defaultValue;
        }
    }

    /**
     * Parses boolean value (for Y/N format)
     * Y, Yes, y, yes → true
     * N, No, n, no → false
     *
     * @param value - Value to be converted
     * @return Boolean value
     */
    public static boolean parseBoolean(String value) {
        if (value == null) {
            return false;
        }

        value = value.trim().toUpperCase();
        return value.equals("Y") || value.equals("YES") || value.equals("TRUE");
    }

    //Prints CSV line for debug
    public static void debugPrintLine(String line) {
        System.out.println("Line: " + line);
        String[] parts = parseLine(line);
        for (int i = 0; i < parts.length; i++) {
            System.out.println("  [" + i + "] = \"" + parts[i] + "\"");
        }
    }
}