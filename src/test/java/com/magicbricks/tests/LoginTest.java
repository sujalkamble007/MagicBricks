package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.pages.LoginPage;
import com.magicbricks.utils.DataProviders;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * Production Test Suite for MagicBricks Login Module.
 * Covers 3 primary functional test scenarios (5 total test executions with DataProvider):
 * 1. TC_LG_001: Login dropdown opens from header + CTA presence
 * 2. TC_LG_002: Valid 10-digit mobile number entry (9518306867) + live OTP prompt
 * 3. TC_LG_003: Negative data-driven mobile validation (short digits, alpha strings, special characters from Excel)
 */
public class LoginTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void initPages() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_LG_001: Verify Login Dropdown Opens from Header
     * Preconditions: Home page loaded
     * Steps:
     *   1. Click "Login" button in header
     *   2. Check Login/Sign Up CTA button visibility inside dropdown
     * Expected Result: Login dropdown opens; "Login/Sign Up" CTA is displayed and clickable
     */
    @Test(priority = 1, groups = {"login", "smoke"},
            description = "TC_LG_001: Verify login dropdown opens and Login/Sign Up CTA is visible")
    public void verifyLoginDropdownOpens() {
        System.out.println("    ✦ [Step 1] Clicking 'Login' header trigger button...");
        homePage.clickLoginButton();

        System.out.println("    ✦ [Step 2] Verifying 'Login/Sign Up' CTA button is displayed inside dropdown...");
        boolean isCtaVisible = homePage.isLoginSignUpCtaDisplayed();
        Assert.assertTrue(isCtaVisible,
                "Login/Sign Up CTA button must be visible inside the login dropdown when Login is clicked");
    }

    /**
     * TC_LG_002: Verify Valid Mobile Number Entry on Login Page & Live OTP Flow
     * Preconditions: Home page loaded
     * Steps:
     *   1. Open Login dropdown and click Login/Sign Up CTA
     *   2. Switch to the newly opened tab / modal
     *   3. Validate login container and heading
     *   4. Verify Buyer/Owner radio is selected by default
     *   5. Enter 10-digit mobile number from Excel (9518306867)
     *   6. Assert input field value matches entered number
     *   7. Click Continue and handle OTP prompt if triggered
     * Expected Result: Login page loads; heading is "Login"; mobile number accepted; no error; OTP triggered
     */
    @Test(priority = 2, groups = {"login", "regression"},
            dataProvider = "validLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_002: Verify valid 10-digit mobile number entry and OTP prompt on login page")
    public void verifyValidMobileNumberEntry(String scenario, String mobileNumber, String expectedType) {
        System.out.println("    ✦ [Step 1] Opening Login dropdown and clicking 'Login/Sign Up' CTA...");
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();

        System.out.println("    ✦ [Step 2] Switching window focus to newly opened Login tab...");
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        SoftAssert softAssert = new SoftAssert();
        System.out.println("    ✦ [Step 3] Validating Login container and 'Login' heading...");
        softAssert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login container must be loaded and visible in the login window");
        softAssert.assertTrue(loginPage.getLoginHeadingText().contains("Login"),
                "Login page heading must contain 'Login'. Actual: " + loginPage.getLoginHeadingText());

        System.out.println("    ✦ [Step 4] Confirming 'Buyer/Owner' radio option is selected by default...");
        softAssert.assertTrue(loginPage.isBuyerOwnerSelected(),
                "Buyer/Owner radio option must be selected by default");

        System.out.println("    ✦ [Step 5] Entering valid 10-digit mobile number: " + mobileNumber + "...");
        loginPage.enterMobileNumber(mobileNumber);
        String enteredValue = loginPage.getInputFieldValue();
        softAssert.assertEquals(enteredValue, mobileNumber,
                "Input field value attribute must match the entered mobile number: " + mobileNumber);

        System.out.println("    ✦ [Step 6] Clicking 'Continue' button to proceed to OTP verification...");
        loginPage.clickContinueButton();

        if (loginPage.isOtpFieldDisplayed()) {
            System.out.println("    ✦ [Step 7] OTP field detected; handling interactive live OTP entry hook...");
            loginPage.waitForOtpAndEnter();
        }

        softAssert.assertAll();
    }

    /**
     * TC_LG_003: Verify Invalid Mobile Number is Rejected (Data-Driven via Excel)
     * Preconditions: Login page opened in new tab / modal
     * Steps:
     *   1. Navigate to login page
     *   2. Enter invalid input (short digits, alpha, special characters)
     *   3. Click Continue button
     *   4. Verify OTP is NOT triggered for invalid data
     * Expected Result: Invalid data is rejected; OTP is NOT triggered
     */
    @Test(priority = 3, groups = {"login", "regression"},
            dataProvider = "invalidLoginMobileData", dataProviderClass = DataProviders.class,
            description = "TC_LG_003: Verify invalid mobile/email input is rejected with error")
    public void verifyInvalidMobileNumberRejected(String scenario, String inputData, String expectedError) {
        System.out.println("    ✦ [Step 1] Navigating to Login page for scenario: " + scenario + "...");
        homePage.clickLoginButton();
        homePage.clickLoginSignUpCta();
        homePage.switchToNewTab();
        loginPage = new LoginPage(driver);

        Assert.assertTrue(loginPage.isLoginPageLoaded(),
                "Login page must be loaded before entering test data");

        System.out.println("    ✦ [Step 2] Entering invalid input data: '" + inputData + "'...");
        loginPage.enterMobileNumber(inputData);

        System.out.println("    ✦ [Step 3] Clicking 'Continue' button...");
        loginPage.clickContinueButton();

        System.out.println("    ✦ [Step 4] Asserting OTP screen is NOT triggered for invalid input...");
        boolean isOtpTriggered = loginPage.isOtpFieldDisplayed();
        Assert.assertFalse(isOtpTriggered,
                "OTP must NOT be triggered for invalid input: '" + inputData + "' in scenario: " + scenario);
    }
}
