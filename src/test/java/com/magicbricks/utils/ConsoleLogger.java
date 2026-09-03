package com.magicbricks.utils;

/**
 * Utility for formatting terminal test logs with clean ================= separators,
 * bold ANSI colors, prominent Test Case Number + Name, and numbered step breadcrumbs.
 */
public class ConsoleLogger {

    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";

    // Standard & Bold ANSI Colors
    public static final String CYAN_BOLD = "\u001B[1;36m";
    public static final String GREEN_BOLD = "\u001B[1;32m";
    public static final String YELLOW_BOLD = "\u001B[1;33m";
    public static final String BLUE_BOLD = "\u001B[1;34m";
    public static final String PURPLE_BOLD = "\u001B[1;35m";
    public static final String RED_BOLD = "\u001B[1;31m";
    public static final String WHITE_BOLD = "\u001B[1;37m";

    public static final String CYAN = "\u001B[0;36m";
    public static final String GREEN = "\u001B[0;32m";
    public static final String YELLOW = "\u001B[0;33m";
    public static final String BLUE = "\u001B[0;34m";
    public static final String PURPLE = "\u001B[0;35m";
    public static final String RED = "\u001B[0;31m";
    public static final String WHITE = "\u001B[0;37m";

    private static final String DOUBLE_BAR = "=================================================";
    private static final String SINGLE_BAR = "-------------------------------------------------";

    /**
     * Prints a clean, prominent ================= header before each test starts.
     */
    public static void logTestStart(String testName, String description, String module, String groups) {
        String tcId = extractTestCaseId(description);
        String displayName = (tcId != null ? tcId + " : " : "") + testName;

        System.out.println("\n" + CYAN_BOLD + DOUBLE_BAR);
        System.out.println("TEST CASE   : " + displayName);
        if (description != null && !description.isEmpty()) {
            System.out.println("DESCRIPTION : " + description);
        }
        System.out.println("MODULE      : " + module + " | GROUPS: " + groups);
        System.out.println(DOUBLE_BAR + RESET);
    }

    /**
     * Prints a colorful, numbered step breadcrumb.
     */
    public static void logStep(int stepNumber, String actionDescription) {
        System.out.println(YELLOW_BOLD + "  [STEP " + stepNumber + "] " + RESET + WHITE + actionDescription + RESET);
    }

    /**
     * Prints a green completion status banner with duration.
     */
    public static void logSuccess(String testName, double durationSec) {
        System.out.println(GREEN_BOLD + SINGLE_BAR);
        System.out.printf("✔ STATUS: [PASSED] - %s (%.2fs)%n", testName, durationSec);
        System.out.println(SINGLE_BAR + RESET + "\n");
    }

    /**
     * Prints a red failure banner with cause and duration.
     */
    public static void logFailure(String testName, String errorMsg, double durationSec) {
        System.out.println(RED_BOLD + SINGLE_BAR);
        System.out.printf("✖ STATUS: [FAILED] - %s (%.2fs)%n", testName, durationSec);
        if (errorMsg != null && !errorMsg.isEmpty()) {
            System.out.println("  ERROR: " + errorMsg);
        }
        System.out.println(SINGLE_BAR + RESET + "\n");
    }

    /**
     * Prints a yellow skipped banner.
     */
    public static void logSkipped(String testName) {
        System.out.println(YELLOW_BOLD + SINGLE_BAR);
        System.out.printf("⚠ STATUS: [SKIPPED] - %s%n", testName);
        System.out.println(SINGLE_BAR + RESET + "\n");
    }

    /**
     * Prints the module completion block with the generated report path.
     */
    public static void logReportGenerated(String moduleName, String reportPath) {
        System.out.println(CYAN_BOLD + DOUBLE_BAR);
        System.out.println("✔ " + moduleName + " Module Execution Finished.");
        System.out.println("  ExtentReport generated at: " + reportPath);
        System.out.println(DOUBLE_BAR + RESET + "\n");
    }

    private static String extractTestCaseId(String description) {
        if (description == null) return null;
        if (description.startsWith("TC_")) {
            int colonIdx = description.indexOf(":");
            if (colonIdx != -1) {
                return description.substring(0, colonIdx).trim();
            }
        }
        return null;
    }
}
