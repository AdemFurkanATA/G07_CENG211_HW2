package enums;

import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Collections;
import java.util.stream.Collectors;

public enum DocumentType {

    // Enum Values
    ENR(
            "ENR",
            "Enrollment Certificate",
            "Enrollment Certificate - Required for all applications",
            true,
            5,
            "ALL"
    ),

    REC(
            "REC",
            "Recommendation Letter",
            "Recommendation Letter - Extends the merit application period to 2 years",
            false,
            4,
            "11"
    ),

    SAV(
            "SAV",
            "Savings Document",
            "Savings Document - Increases need-based income thresholds by 20%",
            false,
            3,
            "22"
    ),

    RSV(
            "RSV",
            "Research Supervisor Approval",
            "Research Supervisor Approval - Adds +1 year to the research grant period",
            false,
            4,
            "33"
    ),

    GRP(
            "GRP",
            "Grant Proposal",
            "Grant Proposal - If there is no publication for the research grant, it is required.",
            false,
            3,
            "33"
    );

    // Immutable Fields

    private final String code;
    private final String description;
    private final String detailedDescription;
    private final boolean mandatory;
    private final int importanceLevel;
    private final String relevantScholarshipType;

    private static final Map<String, DocumentType> CODE_MAP;

    static {
        Map<String, DocumentType> map = new HashMap<>();
        for (DocumentType type : DocumentType.values()) {
            map.put(type.code.toUpperCase(), type);
        }
        CODE_MAP = Collections.unmodifiableMap(map);
    }

    private static final Set<DocumentType> MANDATORY_TYPES;

    static {
        Set<DocumentType> mandatory = EnumSet.noneOf(DocumentType.class);
        for (DocumentType type : DocumentType.values()) {
            if (type.mandatory) {
                mandatory.add(type);
            }
        }
        MANDATORY_TYPES = Collections.unmodifiableSet(mandatory);
    }

    private static final Set<DocumentType> OPTIONAL_TYPES;

    static {
        Set<DocumentType> optional = EnumSet.noneOf(DocumentType.class);
        for (DocumentType type : DocumentType.values()) {
            if (!type.mandatory) {
                optional.add(type);
            }
        }
        OPTIONAL_TYPES = Collections.unmodifiableSet(optional);
    }

    // Constructor
    private DocumentType(
            String code,
            String description,
            String detailedDescription,
            boolean mandatory,
            int importanceLevel,
            String relevantScholarshipType) {

        this.code = code;
        this.description = description;
        this.detailedDescription = detailedDescription;
        this.mandatory = mandatory;
        this.importanceLevel = importanceLevel;
        this.relevantScholarshipType = relevantScholarshipType;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public boolean isOptional() {
        return !mandatory;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }

    public String getRelevantScholarshipType() {
        return relevantScholarshipType;
    }

    // Business Logic Methods

    public boolean isCritical() {
        return mandatory || importanceLevel >= 4;
    }

    public boolean isUniversal() {
        return relevantScholarshipType.equals("ALL");
    }

    public boolean isRelevantFor(String scholarshipTypeCode) {
        if (scholarshipTypeCode == null || scholarshipTypeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Scholarship type code cannot be null or empty");
        }

        String normalized = scholarshipTypeCode.trim();

        // Validate code
        if (!normalized.equals("11") && !normalized.equals("22") && !normalized.equals("33")) {
            throw new IllegalArgumentException(
                    "Invalid scholarship type code: '" + normalized + "'. Must be 11, 22, or 33"
            );
        }

        // Check relevance
        return relevantScholarshipType.equals("ALL") ||
                relevantScholarshipType.equals(normalized);
    }

    public boolean isRelevantForMerit() {
        return isUniversal() || relevantScholarshipType.equals("11");
    }

    public boolean isRelevantForNeedBased() {
        return isUniversal() || relevantScholarshipType.equals("22");
    }

    public boolean isRelevantForResearch() {
        return isUniversal() || relevantScholarshipType.equals("33");
    }

    public String getScholarshipName() {
        switch (relevantScholarshipType) {
            case "11":
                return "Merit";
            case "22":
                return "Need-Based";
            case "33":
                return "Research";
            case "ALL":
                return "All Scholarships";
            default:
                return "Unknown";
        }
    }

    public String getImpact() {
        switch (this) {
            case ENR:
                return "Required for all applications - No impact beyond acceptance";
            case REC:
                return "Extends Merit scholarship duration from 1 to 2 years";
            case SAV:
                return "Increases Need-Based income thresholds by 20%";
            case RSV:
                return "Adds +1 year to Research grant duration";
            case GRP:
                return "Can substitute for publications in Research grant";
            default:
                return "No specific impact documented";
        }
    }

    public String getImportanceCategory() {
        if (mandatory || importanceLevel == 5) {
            return "Critical";
        } else if (importanceLevel == 4) {
            return "High";
        } else if (importanceLevel == 3) {
            return "Medium";
        } else {
            return "Low";
        }
    }

    public String getColorCode() {
        if (mandatory) {
            return "RED";
        } else if (importanceLevel == 4) {
            return "ORANGE";
        } else if (importanceLevel == 3) {
            return "YELLOW";
        } else {
            return "GREEN";
        }
    }

    public String getIcon() {
        switch (this) {
            case ENR:
                return "📋";
            case REC:
                return "✉️";
            case SAV:
                return "💰";
            case RSV:
                return "✅";
            case GRP:
                return "📄";
            default:
                return "📎";
        }
    }

    // Comparison Methods

    public int compareByImportance(DocumentType other) {
        Objects.requireNonNull(other, "Cannot compare with null DocumentType");
        return Integer.compare(this.importanceLevel, other.importanceLevel);
    }

    public int compareByCode(DocumentType other) {
        Objects.requireNonNull(other, "Cannot compare with null DocumentType");
        return this.code.compareTo(other.code);
    }

    public boolean isMoreImportantThan(DocumentType other) {
        Objects.requireNonNull(other, "Cannot compare with null DocumentType");
        return this.importanceLevel > other.importanceLevel;
    }

    public boolean isLessImportantThan(DocumentType other) {
        Objects.requireNonNull(other, "Cannot compare with null DocumentType");
        return this.importanceLevel < other.importanceLevel;
    }

    // Static Utility Methods

    public static DocumentType fromCode(String code) {
        Objects.requireNonNull(code, "Document type code cannot be null");

        String normalized = code.trim().toUpperCase();

        if (normalized.isEmpty()) {
            throw new IllegalArgumentException("Document type code cannot be empty");
        }

        DocumentType type = CODE_MAP.get(normalized);

        if (type == null) {
            throw new IllegalArgumentException(
                    "Unknown document type: '" + code + "'. " +
                            "Valid types: " + getValidCodeList()
            );
        }

        return type;
    }

    public static boolean isValidCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }
        return CODE_MAP.containsKey(code.trim().toUpperCase());
    }

    public static DocumentType[] getAllTypes() {
        return DocumentType.values();
    }

    public static Set<DocumentType> getMandatoryTypes() {
        return MANDATORY_TYPES;
    }

    public static Set<DocumentType> getOptionalTypes() {
        return OPTIONAL_TYPES;
    }

    public static Set<DocumentType> getRelevantTypes(String scholarshipTypeCode) {
        if (scholarshipTypeCode == null || scholarshipTypeCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Scholarship type code cannot be null or empty");
        }

        String normalized = scholarshipTypeCode.trim();

        if (!normalized.equals("11") && !normalized.equals("22") && !normalized.equals("33")) {
            throw new IllegalArgumentException(
                    "Invalid scholarship type code: '" + normalized + "'. Must be 11, 22, or 33"
            );
        }

        Set<DocumentType> relevant = EnumSet.noneOf(DocumentType.class);
        for (DocumentType type : DocumentType.values()) {
            if (type.isRelevantFor(normalized)) {
                relevant.add(type);
            }
        }

        return Collections.unmodifiableSet(relevant);
    }

    public static Set<DocumentType> getCriticalTypes() {
        Set<DocumentType> critical = EnumSet.noneOf(DocumentType.class);
        for (DocumentType type : DocumentType.values()) {
            if (type.isCritical()) {
                critical.add(type);
            }
        }
        return Collections.unmodifiableSet(critical);
    }

    public static java.util.List<DocumentType> getTypesByImportance() {
        return java.util.Arrays.stream(DocumentType.values())
                .sorted((a, b) -> Integer.compare(b.importanceLevel, a.importanceLevel))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        Collections::unmodifiableList
                ));
    }

    public static String getValidCodeList() {
        return java.util.Arrays.stream(DocumentType.values())
                .map(DocumentType::getCode)
                .collect(Collectors.joining(", "));
    }

    public static String getEnumInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("DocumentType Enum - Available Document Types:\n");
        sb.append("=".repeat(50)).append("\n\n");

        for (DocumentType type : DocumentType.values()) {
            sb.append(type.getIcon()).append(" ").append(type.code).append(" - ").append(type.description).append("\n");
            sb.append("   ").append(type.detailedDescription).append("\n");
            sb.append("   Mandatory: ").append(type.mandatory ? "YES" : "No").append("\n");
            sb.append("   Importance: ").append(type.importanceLevel).append("/5 (").append(type.getImportanceCategory()).append(")\n");
            sb.append("   Relevant for: ").append(type.getScholarshipName()).append("\n");
            sb.append("   Impact: ").append(type.getImpact()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    // Object Methods
    @Override
    public String toString() {
        return code + " (" + description + ")";
    }

    public String toDetailedString() {
        return String.format(
                "%s %s - %s [%s, Importance: %d/5, Relevant: %s]",
                getIcon(),
                code,
                description,
                mandatory ? "Mandatory" : "Optional",
                importanceLevel,
                getScholarshipName()
        );
    }

    public String toUserFriendlyString() {
        return String.format(
                "%s %s\n%s\nImportance: %s (%d/5)\n%s",
                getIcon(),
                description,
                detailedDescription,
                getImportanceCategory(),
                importanceLevel,
                getImpact()
        );
    }
}