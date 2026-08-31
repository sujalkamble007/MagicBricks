package com.magicbricks.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Utility to generate the standardized Test Scenarios and Test Cases Excel document
 * in the exact required enterprise QA template format.
 */
public class TestDocumentGenerator {

    public static void main(String[] args) {
        generateExcelDocument();
    }

    public static void generateExcelDocument() {
        File dir = new File("Test_Documents");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = "Test_Documents/MagicBricks_Test_Scenarios_and_Test_Cases.xlsx";

        try (Workbook workbook = new XSSFWorkbook()) {

            // ==================== STYLES ====================
            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());
            headerFont.setFontHeightInPoints((short) 11);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);
            headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerStyle.setWrapText(true);
            setBorders(headerStyle);

            // Data Style - Left aligned, wrapped
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setWrapText(true);
            dataStyle.setVerticalAlignment(VerticalAlignment.TOP);
            setBorders(dataStyle);

            // Data Style - Center aligned (for IDs, counts, status)
            CellStyle centerStyle = workbook.createCellStyle();
            centerStyle.setAlignment(HorizontalAlignment.CENTER);
            centerStyle.setVerticalAlignment(VerticalAlignment.TOP);
            setBorders(centerStyle);

            // Pass Status Style
            CellStyle passStyle = workbook.createCellStyle();
            Font passFont = workbook.createFont();
            passFont.setBold(true);
            passFont.setColor(IndexedColors.GREEN.getIndex());
            passStyle.setFont(passFont);
            passStyle.setAlignment(HorizontalAlignment.CENTER);
            passStyle.setVerticalAlignment(VerticalAlignment.TOP);
            setBorders(passStyle);

            // ==================== SHEET 1: Test Scenarios ====================
            Sheet scenarioSheet = workbook.createSheet("Test Scenarios");
            scenarioSheet.setDisplayGridlines(true);

            String[] scenarioHeaders = {
                    "TestScenarioID",
                    "Requirement ID",
                    "Test Scenario Description",
                    "Type of Testing",
                    "Possible No. of TestCases",
                    "Test Case Details"
            };

            Row scHeaderRow = scenarioSheet.createRow(0);
            scHeaderRow.setHeightInPoints(28);
            for (int i = 0; i < scenarioHeaders.length; i++) {
                Cell cell = scHeaderRow.createCell(i);
                cell.setCellValue(scenarioHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            String[][] scenarioData = {
                    {
                            "TS_MB_01",
                            "REQ_MB_01",
                            "Verify MagicBricks Home Page Header, Branding & Navigation Functionality",
                            "Functional / Smoke Testing",
                            "2",
                            "1. To validate home page title contains MagicBricks\n2. To validate header elements (Logo, City Selector, Login, Post Property) are displayed"
                    },
                    {
                            "TS_MB_02",
                            "REQ_MB_02",
                            "Verify Search Category Tabs Presence & Interactive Switching Functionality",
                            "Functional / Regression Testing",
                            "2",
                            "1. To validate search category tabs (Buy, Rent, PG, Plot, Commercial) presence and Buy is default active\n2. To validate interactive tab switching between Buy and Rent"
                    },
                    {
                            "TS_MB_03",
                            "REQ_MB_03",
                            "Verify Search Input & Live Autocomplete Suggestions Functionality",
                            "Data-Driven / Regression Testing",
                            "3",
                            "1. To validate autocomplete suggestions appear for Whitefield\n2. To validate autocomplete suggestions appear for Koramangala\n3. To validate autocomplete suggestions appear for Indiranagar"
                    },
                    {
                            "TS_MB_04",
                            "REQ_MB_04",
                            "Verify Progressive Page Traversal, Mid-Page & Footer Content Locators",
                            "Functional / UI Testing",
                            "3",
                            "1. To validate smooth scroll to mid-page section and heading/card locators\n2. To validate smooth scroll to bottom footer container and link locators\n3. To validate smooth scroll to bottom and return to top anchor"
                    },
                    {
                            "TS_MB_05",
                            "REQ_MB_05",
                            "Verify User Authentication, Mobile Login & Validation Flow",
                            "Functional / Data-Driven Testing",
                            "5",
                            "1. To validate Login dropdown menu and CTA button trigger\n2. To validate positive login with valid 10-digit mobile number (9518306867) and OTP trigger\n3. To validate rejection of short 5-digit number (12345) with error\n4. To validate rejection of alphabetic characters (abcdefghij) with error\n5. To validate rejection of special characters (!@#$%^&*) with error"
                    }
            };

            for (int r = 0; r < scenarioData.length; r++) {
                Row row = scenarioSheet.createRow(r + 1);
                row.setHeightInPoints(45);
                for (int c = 0; c < scenarioData[r].length; c++) {
                    Cell cell = row.createCell(c);
                    cell.setCellValue(scenarioData[r][c]);
                    if (c == 0 || c == 1 || c == 4) {
                        cell.setCellStyle(centerStyle);
                    } else {
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            scenarioSheet.setColumnWidth(0, 16 * 256);
            scenarioSheet.setColumnWidth(1, 16 * 256);
            scenarioSheet.setColumnWidth(2, 40 * 256);
            scenarioSheet.setColumnWidth(3, 28 * 256);
            scenarioSheet.setColumnWidth(4, 22 * 256);
            scenarioSheet.setColumnWidth(5, 55 * 256);

            // ==================== SHEET 2: Test Cases ====================
            Sheet testCaseSheet = workbook.createSheet("Test Cases");
            testCaseSheet.setDisplayGridlines(true);

            String[] tcHeaders = {
                    "Test Case ID",
                    "Test Scenario",
                    "Precondition",
                    "Test Condition",
                    "Test Case Steps",
                    "Test Data",
                    "Expected Result",
                    "Actual Result Iteration 1",
                    "Status Iteration 1",
                    "Actual Result Iteration 2\n(to be updated only if test case failed in Iteration 1)",
                    "Status Iteration 2\n(to be updated only if test case failed in Iteration 1)",
                    "Comments",
                    "Req: Reference"
            };

            Row tcHeaderRow = testCaseSheet.createRow(0);
            tcHeaderRow.setHeightInPoints(35);
            for (int i = 0; i < tcHeaders.length; i++) {
                Cell cell = tcHeaderRow.createCell(i);
                cell.setCellValue(tcHeaders[i]);
                cell.setCellStyle(headerStyle);
            }

            String[][] testCaseData = {
                    {
                            "TC_MB_HP_01",
                            "TS_MB_01",
                            "1) Browser is open and internet is active.\n2) https://www.magicbricks.com/ is reachable.",
                            "Verify Home Page Title",
                            "1. Open Chrome Browser\n2. Navigate to https://www.magicbricks.com/\n3. Retrieve page title from browser",
                            "URL: https://www.magicbricks.com/",
                            "Page title should contain 'MagicBricks' or 'Real Estate'",
                            "Page title contained 'Real Estate | Property in India | Magicbricks' as expected.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_01"
                    },
                    {
                            "TC_MB_HP_02",
                            "TS_MB_01",
                            "1) Browser is open.\n2) MagicBricks home page has fully loaded.",
                            "Verify Header Elements Visibility",
                            "1. Verify MagicBricks brand logo\n2. Verify City selector link with non-empty text\n3. Verify Login button in header\n4. Verify Post Property link",
                            "N/A (UI Elements)",
                            "Logo, Login button, Post Property link displayed; City selector text is non-empty",
                            "All header elements displayed with valid city name (Bangalore).",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_01"
                    },
                    {
                            "TC_MB_HP_03",
                            "TS_MB_02",
                            "1) MagicBricks home page is loaded.\n2) Search category bar is visible.",
                            "Verify Search Tabs Presence and Default State",
                            "1. Check Buy tab has 'active' class\n2. Check Rent, PG, Plot, Commercial tabs are displayed",
                            "Category Tabs: Buy, Rent, PG, Plot, COMM",
                            "All 5 search category tabs displayed; Buy tab active by default",
                            "Buy tab was active by default; all category tabs visible.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_02"
                    },
                    {
                            "TC_MB_HP_04_1",
                            "TS_MB_03",
                            "1) MagicBricks home page is loaded.\n2) Search box is visible.",
                            "Verify Autocomplete Suggestions for Whitefield",
                            "1. Focus search input field\n2. Type locality 'Whitefield' character by character\n3. Observe autocomplete suggestions dropdown",
                            "Locality: Whitefield",
                            "Autocomplete suggestions dropdown should become visible with relevant localities",
                            "Autocomplete suggestions dropdown displayed with matching results for Whitefield.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_03"
                    },
                    {
                            "TC_MB_HP_04_2",
                            "TS_MB_03",
                            "1) MagicBricks home page is loaded.\n2) Search box is visible.",
                            "Verify Autocomplete Suggestions for Koramangala",
                            "1. Focus search input field\n2. Type locality 'Koramangala' character by character\n3. Observe autocomplete suggestions dropdown",
                            "Locality: Koramangala",
                            "Autocomplete suggestions dropdown should become visible with relevant localities",
                            "Autocomplete suggestions dropdown displayed with matching results for Koramangala.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_03"
                    },
                    {
                            "TC_MB_HP_04_3",
                            "TS_MB_03",
                            "1) MagicBricks home page is loaded.\n2) Search box is visible.",
                            "Verify Autocomplete Suggestions for Indiranagar",
                            "1. Focus search input field\n2. Type locality 'Indiranagar' character by character\n3. Observe autocomplete suggestions dropdown",
                            "Locality: Indiranagar",
                            "Autocomplete suggestions dropdown should become visible with relevant localities",
                            "Autocomplete suggestions dropdown displayed with matching results for Indiranagar.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_03"
                    },
                    {
                            "TC_MB_HP_05",
                            "TS_MB_02",
                            "1) MagicBricks home page is loaded.\n2) Buy tab is active initially.",
                            "Verify Buy/Rent Tab Switching Interactivity",
                            "1. Verify Buy tab active initially\n2. Click Rent tab -> Verify Rent active and Buy inactive\n3. Click Buy tab back -> Verify Buy active and Rent inactive",
                            "Tabs: Buy, Rent",
                            "Active CSS class toggles correctly between Buy and Rent; only one active tab at a time",
                            "Tab switched smoothly to Rent, then toggled back to Buy with active class updated.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_02"
                    },
                    {
                            "TC_MB_HP_06",
                            "TS_MB_04",
                            "1) MagicBricks home page is loaded.\n2) Browser window is maximized.",
                            "Verify Mid-Page Section Scroll & Content Locators",
                            "1. Smoothly scroll down 800px to mid-page section\n2. Highlight and assert section container visibility\n3. Verify section heading text is displayed",
                            "N/A (DOM Locators)",
                            "Mid-page section container and heading locators should be visible after scroll",
                            "Mid-page section scrolled smoothly; heading and cards displayed with highlights.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_04"
                    },
                    {
                            "TC_MB_HP_07",
                            "TS_MB_04",
                            "1) MagicBricks home page is loaded.",
                            "Verify Bottom Footer Section Scroll & Content Locators",
                            "1. Smoothly scroll down to bottom of page\n2. Highlight and assert footer container visibility\n3. Verify footer link/title text is displayed",
                            "N/A (DOM Locators)",
                            "Footer container and footer links should be visible at bottom of page",
                            "Footer container scrolled smoothly; footer title and links verified.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_04"
                    },
                    {
                            "TC_MB_HP_08",
                            "TS_MB_04",
                            "1) MagicBricks home page is loaded.\n2) Page scrolled to bottom.",
                            "Verify Scroll To Bottom and Return To Top Behavior",
                            "1. Scroll down to bottom footer container\n2. Scroll smoothly back to top of page\n3. Verify MagicBricks brand Logo visibility",
                            "N/A (Full Page Scroll)",
                            "Page should scroll to bottom and smoothly return to top with Logo visible",
                            "Page scrolled to bottom and smoothly returned to top; Logo visible.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_04"
                    },
                    {
                            "TC_MB_LG_01",
                            "TS_MB_05",
                            "1) MagicBricks home page is loaded.\n2) Header Login button is visible.",
                            "Verify Login Dropdown Menu & CTA Trigger",
                            "1. Click/hover 'Login' in header\n2. Observe dropdown menu container\n3. Verify 'Login / Sign Up' CTA button is displayed",
                            "N/A (UI Dropdown)",
                            "Login dropdown container and Login/Sign Up CTA button should become visible",
                            "Login dropdown menu opened; Login/Sign Up CTA displayed.",
                            "Passed",
                            "",
                            "",
                            "Verified in Iteration 1",
                            "REQ_MB_05"
                    },
                    {
                            "TC_MB_LG_02",
                            "TS_MB_05",
                            "1) MagicBricks home page is loaded.\n2) Valid 10-digit mobile number available.",
                            "Verify Valid Mobile Number Entry & OTP Trigger",
                            "1. Click Login -> Click Login/Sign Up CTA\n2. Switch to new login tab/window in foreground\n3. Enter valid 10-digit mobile number: 9518306867\n4. Click Continue/Submit button\n5. Verify OTP input field is triggered",
                            "Mobile: 9518306867",
                            "Mobile number entered successfully; Continue button triggers OTP verification field",
                            "Mobile 9518306867 typed live; OTP field triggered successfully.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_05"
                    },
                    {
                            "TC_MB_LG_03_1",
                            "TS_MB_05",
                            "1) MagicBricks login page/modal is open.",
                            "Verify Invalid Mobile Rejection: Too Short (5 digits)",
                            "1. Navigate to Login page/modal\n2. Enter 5-digit number '12345'\n3. Click Continue button\n4. Observe validation error or input constraint",
                            "Input: 12345",
                            "System rejects short mobile number and remains on login form or shows error",
                            "System rejected 5-digit number; remained on login screen.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_05"
                    },
                    {
                            "TC_MB_LG_03_2",
                            "TS_MB_05",
                            "1) MagicBricks login page/modal is open.",
                            "Verify Invalid Mobile Rejection: Alphabetic Characters",
                            "1. Navigate to Login page/modal\n2. Enter alphabetic string 'abcdefghij'\n3. Click Continue button\n4. Observe validation error or input rejection",
                            "Input: abcdefghij",
                            "System rejects alphabetic input for mobile login and shows validation error",
                            "System rejected alphabetic input; error displayed.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_05"
                    },
                    {
                            "TC_MB_LG_03_3",
                            "TS_MB_05",
                            "1) MagicBricks login page/modal is open.",
                            "Verify Invalid Mobile Rejection: Special Characters",
                            "1. Navigate to Login page/modal\n2. Enter special characters '!@#$%^&*'\n3. Click Continue button\n4. Observe validation error or input rejection",
                            "Input: !@#$%^&*",
                            "System rejects special characters for mobile login and shows validation error",
                            "System rejected special characters; error displayed.",
                            "Passed",
                            "",
                            "",
                            "Data-driven via TestData.xlsx",
                            "REQ_MB_05"
                    }
            };

            for (int r = 0; r < testCaseData.length; r++) {
                Row row = testCaseSheet.createRow(r + 1);
                row.setHeightInPoints(50);
                for (int c = 0; c < testCaseData[r].length; c++) {
                    Cell cell = row.createCell(c);
                    cell.setCellValue(testCaseData[r][c]);
                    if (c == 0 || c == 1 || c == 12) {
                        cell.setCellStyle(centerStyle);
                    } else if (c == 8 || c == 10) {
                        cell.setCellStyle("Passed".equalsIgnoreCase(testCaseData[r][c]) ? passStyle : centerStyle);
                    } else {
                        cell.setCellStyle(dataStyle);
                    }
                }
            }

            testCaseSheet.setColumnWidth(0, 18 * 256);  // Test Case ID
            testCaseSheet.setColumnWidth(1, 15 * 256);  // Test Scenario
            testCaseSheet.setColumnWidth(2, 35 * 256);  // Precondition
            testCaseSheet.setColumnWidth(3, 35 * 256);  // Test Condition
            testCaseSheet.setColumnWidth(4, 45 * 256);  // Test Case Steps
            testCaseSheet.setColumnWidth(5, 25 * 256);  // Test Data
            testCaseSheet.setColumnWidth(6, 40 * 256);  // Expected Result
            testCaseSheet.setColumnWidth(7, 40 * 256);  // Actual Result Iteration 1
            testCaseSheet.setColumnWidth(8, 15 * 256);  // Status Iteration 1
            testCaseSheet.setColumnWidth(9, 25 * 256);  // Actual Result Iteration 2
            testCaseSheet.setColumnWidth(10, 15 * 256); // Status Iteration 2
            testCaseSheet.setColumnWidth(11, 28 * 256); // Comments
            testCaseSheet.setColumnWidth(12, 16 * 256); // Req: Reference

            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                workbook.write(fos);
            }
            System.out.println("Test document successfully generated at: " + filePath);

        } catch (IOException e) {
            System.err.println("Error generating test document: " + e.getMessage());
        }
    }

    private static void setBorders(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }
}
