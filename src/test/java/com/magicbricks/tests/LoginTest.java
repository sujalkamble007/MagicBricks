package com.magicbricks.tests;

import com.magicbricks.base.BaseTest;
import com.magicbricks.pages.HomePage;
import com.magicbricks.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TestNG test class for MagicBricks Login module.
 * Covers: login dropdown from header, login page interaction, mobile number
 * validation (positive + negative), OTP entry flow.
 *
 * Login page opens in a NEW TAB — tests handle window switching.
 * Test logic and assertions will be completed in Sprint Day 2-3.
 */
public class LoginTest extends BaseTest {

    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeMethod
    public void initPages() {
        homePage = new HomePage(driver);
    }

    /**
     * TC_LG_001: Verify Login button on header opens login dropdown with Login/Sign Up CTA.
     * Steps: Hover/click Login in header → Observe dropdown
     * Expected: Login dropdown appears with "Login/Sign Up" button visible
     */
    @Test(priority = 1, description = "TC_LG_001: Verify login dropdown opens from header")
    public void verifyLoginDropdownOpens() {
        // TODO: Implement in Sprint Day 2-3
    }

    /**
     * TC_LG_002: Verify login page loads and accepts a valid 10-digit mobile number.
     * Steps: Click Login/Sign Up → Switch to new tab → Enter valid mobile → Observe
     * Expected: Login page loads; input accepts number without validation error
     */
    @Test(priority = 2, description = "TC_LG_002: Verify valid mobile number entry on login page")
    public void verifyValidMobileNumberEntry() {
        // TODO: Implement in Sprint Day 2-3
        // Will involve: window handle switch, LoginPage initialization, mobile entry
    }

    /**
     * TC_LG_003: Verify login page rejects invalid mobile number format.
     * Steps: Open login page → Enter 5-digit or alphabetical input → Attempt submit
     * Expected: Error/validation message appears; OTP not triggered
     */
    @Test(priority = 3, description = "TC_LG_003: Verify invalid mobile number is rejected")
    public void verifyInvalidMobileNumberRejected() {
        // TODO: Implement in Sprint Day 2-3
        // Negative test — will enter short/alpha strings and assert error
    }
}
