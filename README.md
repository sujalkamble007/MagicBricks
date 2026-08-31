# MagicBricks Test Automation Framework

An enterprise-grade, Page Object Model (POM) test automation framework built with **Selenium WebDriver**, **Java 11**, **TestNG**, **Apache POI**, and **ExtentReports** covering the **Home Page** and **Login Module** of [MagicBricks](https://www.magicbricks.com/).

---

## 1. Project Overview & Scope
- **Target Application**: [MagicBricks Desktop Web](https://www.magicbricks.com/)
- **Explicit Scope Statement**: This framework automates the **Home Page** and **Login Module** only, per the case study project scope.
- **Key Modules Covered**:
  1. **Home Page**: Header branding, city selector, login triggers, search category tabs (Buy, Rent, PG, Plot, Commercial, New Projects), autocomplete suggestions, interactive tab switching, and progressive top-to-bottom page traversal (mid-page content & bottom footer locators).
  2. **Login Module**: Login dropdown menu, CTA navigation, buyer/agent selection, valid mobile number input (`9518306867`), console-prompted live OTP hook, and data-driven negative input validation (short digits, alpha strings, special characters).

---

## 2. Technical Stack
- **Language**: Java 11 (LTS)
- **Automation Tool**: Selenium WebDriver 4.34.0
- **Driver Management**: Boni Garcia WebDriverManager 6.1.0
- **Testing Engine**: TestNG 7.11.0
- **Design Pattern**: Page Object Model (POM) with PageFactory (`@FindBy`)
- **Data-Driven Engine**: Apache POI 5.2.5 (`TestData.xlsx`)
- **Reporting Engine**: ExtentReports 5.1.2 + TestNG Default Reporting (`emailable-report.html`)
- **Build Tool**: Apache Maven 3.8+
- **Architecture**: SOLID Principles (SRP, OCP, LSP, ISP, DIP)

---

## 3. Directory & Folder Structure

```text
MagicBricks/
├── pom.xml                               # Maven project configuration and dependencies
├── testng.xml                            # TestNG suite runner with listeners and parallel scaffolding
├── .gitignore                            # Comprehensive git exclusion rules
├── README.md                             # Clone-and-run framework documentation
├── screenshots/
│   └── .gitkeep                          # Directory for captured runtime screenshots
├── reports/
│   └── .gitkeep                          # Directory for ExtentReports HTML output
├── src/test/java/com/magicbricks/
│   ├── base/
│   │   ├── BasePage.java                 # Base page object with scrolling, highlighting, and tab/frame helpers
│   │   └── BaseTest.java                 # Base test lifecycle with driver setup/teardown and popup handling
│   ├── listeners/
│   │   └── ExtentReportListener.java     # TestNG ITestListener for ExtentReports and screenshot attachment
│   ├── pages/
│   │   ├── HomePage.java                 # PageFactory object for Home Page navigation & search
│   │   └── LoginPage.java                # PageFactory object for Login tab & modal validation
│   ├── tests/
│   │   ├── HomePageTest.java             # Production test cases for Home Page module
│   │   └── LoginTest.java                # Data-driven test cases for Login module
│   └── utils/
│       ├── ConfigReader.java             # Properties reader for config.properties
│       ├── DataProviders.java            # Centralized TestNG @DataProvider bridge
│       ├── DriverManager.java            # Thread-safe WebDriver initialization
│       ├── ExcelUtils.java               # Single-purpose Apache POI Excel reader
│       ├── ExtentManager.java            # ExtentReports initialization and naming manager
│       ├── OtpHelper.java                # Interactive console Scanner for live OTP entry
│       ├── PopupHandler.java             # Instant zero-wait cookie and overlay popup dismisser
│       ├── ScreenshotUtil.java           # Base64 and file screenshot utility
│       └── WaitHelper.java               # Explicit wait wrapper (WebDriverWait + ExpectedConditions)
└── src/test/resources/
    ├── config.properties                 # Global environment configuration
    └── testdata/
        └── TestData.xlsx                 # Excel workbook with ValidLoginData, InvalidLoginData, SearchData
```

---

## 4. Prerequisites
Before running the project on a fresh machine, ensure you have:
1. **JDK 11**: Java Development Kit 11 installed (`java -version` returns 11.x).
2. **Apache Maven**: Maven 3.8+ installed (`mvn -version`).
3. **Google Chrome**: Latest stable Google Chrome browser installed (WebDriverManager handles the binary automatically).
4. **Eclipse IDE**: Eclipse IDE for Java Developers (2023-06 or newer recommended).

---

## 5. Clone & Setup Instructions (Under 5 Minutes)

### Step 1: Clone the Repository
```bash
git clone <repository-url>
cd MagicBricks
```

### Step 2: Import into Eclipse IDE
1. Launch Eclipse IDE.
2. Go to **File** → **Import...**
3. Select **Maven** → **Existing Maven Projects** and click **Next**.
4. Browse and select the cloned `MagicBricks` folder.
5. Click **Finish** and wait for Eclipse/Maven to download dependencies.

### Step 3: Verify Java Build Path
1. Right-click the project root → **Properties** → **Java Build Path**.
2. Ensure **JRE System Library** points to **JavaSE-11** (or compatible JDK 11).
3. If dependencies show warnings, right-click project → **Maven** → **Update Project...** (Check *Force Update of Snapshots/Releases*).

---

## 6. How to Run Tests

### Option A: Via Eclipse IDE (Recommended)
1. Locate `testng.xml` at the project root.
2. Right-click `testng.xml` → **Run As** → **TestNG Suite**.
3. Chrome will launch in maximized mode and execute all test cases with visual highlights and smooth scrolling.

### Option B: Via Terminal / Command Line
Execute all tests sequentially:
```bash
mvn clean test
```

---

## 7. Reports & Output Locations

After test execution, both reporting engines generate output automatically:

1. **ExtentReports HTML Report (With Embedded Screenshots & Interactive Charts)**:
   - Location: `reports/TestReport_HomeLogin_<yyyy-MM-dd_HH-mm-ss>.html`
   - Features: Visual summary dashboard, category filtering, execution logs, and full-resolution screenshot snapshots attached to every test case.

2. **TestNG Default Reports**:
   - Location: `target/surefire-reports/emailable-report.html`
   - Location: `target/surefire-reports/testng-results.xml`

3. **Disk Screenshots**:
   - Location: `screenshots/` (Contains `.png` files captured for all executed test scenarios).

---

## 8. Troubleshooting & Common Issues

| Issue / Symptom | Root Cause | Solution |
|---|---|---|
| `TestNG plugin not found in Eclipse` | Eclipse lacks TestNG runner | Go to **Help** → **Eclipse Marketplace** → Search `TestNG for Eclipse` → Click **Install** and restart Eclipse. |
| `ChromeDriver download failure on first run` | WebDriverManager requires internet access on first run to resolve the driver binary | Ensure internet connectivity is active during the initial execution. Once downloaded, the driver is cached locally in `~/.cache/selenium/`. |
| `Java compiler level mismatch` | Project JRE set to JDK 8 or JDK 17+ | Right-click project → **Properties** → **Java Compiler** → Enable project specific settings and set compiler compliance level to **11**. |
| `Target or report files not opening` | Path references | Refresh Eclipse project explorer (press `F5` on project root) to view newly generated `reports/` and `screenshots/`. |

---

## 9. Known Limitations & Assumptions
- **Live OTP Verification**: Positive login (`TC_LG_002`) automates the flow up to triggering the OTP request on MagicBricks accounts. Actual SMS delivery is external to web automation; the framework includes an interactive `OtpHelper.waitForOtpInput()` hook allowing the tester to type the live OTP via the console prompt if desired.
- **Login Tab Handling**: Clicking Login CTA opens `https://accounts.magicbricks.com/userauth/login` in a new browser tab. The framework automatically switches window handles and focuses the login window in the foreground.
