package com.magicbricks.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Single-responsibility utility for reading test data from Excel spreadsheets (.xlsx).
 * Uses Apache POI WorkbookFactory and DataFormatter to safely extract tabular data
 * for consumption by TestNG @DataProvider methods.
 *
 * Adheres strictly to SRP and DIP:
 * - Pure data extraction only (no WebDriver, test logic, or assertion dependencies).
 * - File paths are parameter-driven or sourced via ConfigReader.
 */
public class ExcelUtils {

    private static final DataFormatter formatter = new DataFormatter();

    /**
     * Reads all data rows (excluding header row 0) from the specified sheet
     * in the default test data Excel workbook configured in config.properties.
     *
     * @param sheetName Name of the sheet to read
     * @return 2D Object array containing row data
     */
    public static Object[][] getSheetData(String sheetName) {
        String filePath = ConfigReader.getTestDataExcelPath();
        return getSheetData(filePath, sheetName);
    }

    /**
     * Reads all data rows (excluding header row 0) from the specified sheet of an Excel file.
     *
     * @param filePath Absolute or relative path to the .xlsx file
     * @param sheetName Name of the sheet to read
     * @return 2D Object array containing row data for TestNG @DataProvider
     */
    public static Object[][] getSheetData(String filePath, String sheetName) {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new RuntimeException("Excel file not found at: " + file.getAbsolutePath());
        }

        try (FileInputStream fis = new FileInputStream(file);
             Workbook workbook = WorkbookFactory.create(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet '" + sheetName + "' not found in: " + filePath);
            }

            int totalRows = sheet.getLastRowNum(); // 0-based index of last row
            if (totalRows <= 0) {
                return new Object[0][0];
            }

            Row headerRow = sheet.getRow(0);
            int totalCols = headerRow.getLastCellNum();

            Object[][] data = new Object[totalRows][totalCols];

            for (int i = 1; i <= totalRows; i++) {
                Row currentRow = sheet.getRow(i);
                for (int j = 0; j < totalCols; j++) {
                    if (currentRow == null) {
                        data[i - 1][j] = "";
                    } else {
                        data[i - 1][j] = formatter.formatCellValue(currentRow.getCell(j)).trim();
                    }
                }
            }

            return data;

        } catch (IOException e) {
            throw new RuntimeException("Error reading Excel data from " + filePath + " [Sheet: " + sheetName + "]", e);
        }
    }
}
