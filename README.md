# MagicBricks Test Automation Framework

An enterprise-grade, Page Object Model (POM) test automation framework built with **Selenium WebDriver**, **Java 11**, **TestNG**, **Apache POI**, and **ExtentReports** covering the **Home Page** and **Login Module** of [MagicBricks](https://www.magicbricks.com/).

---

## 1. Project Overview & Scope
- **Target Application**: [MagicBricks Desktop Web](https://www.magicbricks.com/)
- **Modules Covered**:
  1. **Home Page**: Header branding, city selector, login triggers, search category tabs (Buy, Rent, PG, Plot, Commercial, New Projects), autocomplete suggestions, tab switching, and progressive page traversal.
  2. **Login Module**: Login dropdown, CTA navigation to login tab/modal, user-type selection, valid mobile number input (`9518306867`), console-prompted live OTP flow, and negative input validation (short digits, alpha strings, special characters).
- **Out of Scope**: Listings, property detail pages (PDP), post-property submission, and financial calculators.

---

## 2. Technical Stack & Architecture
- **Language**: Java 11 (LTS)
- **Automation Tool**: Selenium WebDriver 4.34.0
- **Driver Management**: Boni Garcia WebDriverManager 6.1.0
- **Testing Engine**: TestNG 7.11.0
- **Design Pattern**: Page Object Model (POM) with PageFactory (`@FindBy`)
- **Data-Driven Engine**: Apache POI 5.2.5 (`TestData.xlsx`)
- **Reporting Engine**: ExtentReports 5.1.2 + TestNG Default Reporting (`emailable-report.html`)
- **Build & Dependency Management**: Apache Maven 3.9+
- **Architectural Principles**: SOLID Principles (SRP, OCP, LSP, ISP, DIP)

---

## 3. Directory & Folder Structure

```text
MagicBricks/
├── pom.xml                               # Maven project configuration and dependencies
├── testng.xml                            # TestNG suite runner with listeners and parallel scaffolding
├── README.md                             # Comprehensive framework documentation
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
- **Java Development Kit**: JDK 11 installed (`java -version` verifies Java 11).
- **Build Tool**: Apache Maven 3.8+ (`mvn -version`).
- **Browser**: Google Chrome (ChromeDriver handled automatically by WebDriverManager).
- **IDE**: Eclipse IDE for Java Developers with TestNG and Maven plugins installed.

---

## 5. Setup & Installation in Eclipse
1. **Clone or Open Project**:
   - In Eclipse: `File` → `Import...` → `Maven` → `Existing Maven Projects` → Select project directory `/Users/sujalkamble/Desktop/Testing/MagicBricks`.
2. **Download Dependencies**:
   - Right-click project root → `Maven` → `Update Project...` (or run `mvn clean install -DskipTests`).
3. **Verify Build**:
   - Run `mvn test-compile` in terminal or Eclipse Console to ensure zero compilation errors.

---

## 6. How to Run Tests

### Option A: Via Maven (Terminal or Eclipse)
Run the full test suite sequentially:
```bash
mvn clean test
```

### Option B: Via Eclipse TestNG Runner
1. Right-click on `testng.xml` in the project root.
2. Select **Run As** → **TestNG Suite**.

---

## 7. Reports & Output Locations

After execution, two independent, complementary reports are generated:

1. **ExtentReports (Rich HTML Report with Screenshots & Timestamps)**:
   - File Path: `reports/TestReport_HomeLogin_<yyyy-MM-dd_HH-mm-ss>.html`
   - Features: Visual charts, execution duration, test categories, step logs, and embedded failure screenshots.
2. **TestNG Default Reports**:
   - File Path: `target/surefire-reports/emailable-report.html`
   - File Path: `target/surefire-reports/testng-results.xml`

---

## 8. Test Data Structure (`TestData.xlsx`)

Located at `src/test/resources/testdata/TestData.xlsx`:
- **`ValidLoginData`**: Primary mobile number (`9518306867`).
- **`InvalidLoginData`**: Negative test inputs (`12345`, `abcdefghij`, `!@#$%^&*`).
- **`SearchData`**: Parameterized search localities (`Whitefield`, `Koramangala`, `Indiranagar`).

---

## 9. Known Limitations & Assumptions
- **Live OTP Delivery**: For the positive login test (`TC_LG_002`), real OTP delivery requires SMS access. The framework triggers the OTP request and provides an interactive console `Scanner` hook (`OtpHelper.waitForOtpInput()`) for manual entry.
- **Login Launch Style**: MagicBricks opens `https://accounts.magicbricks.com/userauth/login` in a new browser tab; the framework automatically switches window handles and focuses the tab in foreground.
