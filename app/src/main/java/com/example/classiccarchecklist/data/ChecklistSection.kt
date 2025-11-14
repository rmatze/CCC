package com.example.classiccarchecklist.data

/**
 * Enum representing the different sections of the classic car checklist
 */
enum class ChecklistSection(val displayName: String) {
    PAINT_BODY("Paint & Body"),
    ENGINE_BAY("Engine Bay"),
    LIGHTS("Lights"),
    INTERIOR("Interior"),
    WINDOWS_TRIM("Windows & Trim"),
    UNDERCARRIAGE("Undercarriage"),
    GAS_TANK("Gas Tank"),
    TRUNK("Trunk"),
    TIRES_LUG_NUTS("Tires & Lug Nuts"),
    VIN_TITLE("VIN Number & Title"),
    TEST_DRIVE("Test Drive")
}

