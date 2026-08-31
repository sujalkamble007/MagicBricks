package com.magicbricks.utils;

import org.testng.annotations.DataProvider;

/**
 * Centralized repository of TestNG @DataProvider methods.
 * Pulls test data from Excel sheets via ExcelUtils and supplies strongly-typed
 * argument matrices to test methods across HomePageTest and LoginTest.
 *
 * Adheres to SRP: Test classes focus solely on execution logic & assertions,
 * while DataProviders handles the bridge between Excel files and test parameters.
 */
public class DataProviders {

    /**
     * DataProvider for positive Search Autocomplete test scenarios.
     * Reads from the "SearchData" sheet in TestData.xlsx.
     *
     * @return 2D Object array with [Locality, ExpectedKeyword]
     */
    @DataProvider(name = "searchLocalitiesData")
    public static Object[][] getSearchLocalitiesData() {
        return ExcelUtils.getSheetData("SearchData");
    }

    /**
     * DataProvider for positive Login mobile number entry scenarios.
     * Reads from the "ValidLoginData" sheet in TestData.xlsx.
     *
     * @return 2D Object array with [Scenario, MobileNumber, ExpectedType]
     */
    @DataProvider(name = "validLoginMobileData")
    public static Object[][] getValidLoginMobileData() {
        return ExcelUtils.getSheetData("ValidLoginData");
    }

    /**
     * DataProvider for negative Login validation scenarios (short, alpha, special chars, etc.).
     * Reads from the "InvalidLoginData" sheet in TestData.xlsx.
     *
     * @return 2D Object array with [Scenario, InputData, ExpectedError]
     */
    @DataProvider(name = "invalidLoginMobileData")
    public static Object[][] getInvalidLoginMobileData() {
        return ExcelUtils.getSheetData("InvalidLoginData");
    }
}
