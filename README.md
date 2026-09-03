# MagicBricks Test Automation Framework

An enterprise-grade, Page Object Model (POM) test automation framework built with **Selenium WebDriver**, **Java 11**, **TestNG**, **Apache POI**, and **ExtentReports** covering the **Home Page**, **Login Module**, and **Sell Module (Case Study 1)** of [MagicBricks](https://www.magicbricks.com/).

---

## 1. Project Overview & Scope
- **Target Application**: [MagicBricks Desktop Web](https://www.magicbricks.com/)
- **Key Modules Covered**:
  1. **Home Page**: Header branding, city selector, login triggers, search category tabs (Buy, Rent, PG, Plot, Commercial, New Projects), autocomplete suggestions, interactive tab switching, and progressive top-to-bottom page traversal (mid-page content & bottom footer locators).
  2. **Login Module**: Login dropdown menu, CTA navigation, buyer/agent selection, valid mobile number input (`9518306867`), console-prompted live OTP hook, and data-driven negative input validation (short digits, alpha strings, special characters).
  3. **Sell Module (Case Study 1)**: All primary header Sell dropdown element clickability, 3 major page navigations (Rates & Trends, Find an Agent, Developer Lounge Brand Store), Excel DataProvider multi-city property trends (Pune, Mumbai, Bangalore), agent card details with "PREFERRED AGENT" badges, and Contact Us page traversal with smooth scroll down, directory interaction, and smooth scroll up.

---

## 2. Technical Stack
- **Language**: Java 11 (LTS)
- **Automation Tool**: Selenium WebDriver 4.34.0
- **Driver Management**: Boni Garcia WebDriverManager 6.1.0
- **Testing Engine**: TestNG 7.11.0
- **Design Pattern**: Page Object Model (POM) with PageFactory (`@FindBy`)
- **Data-Driven Engine**: Apache POI 5.2.5 (`TestData.xlsx`, `SellTestData.xlsx`)
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
├── Test_Documents/                       # QA Deliverables (Scenarios & Cases in .xlsx and .md)
│   ├── MagicBricks_Sell_Module_Test_Scenarios_and_Test_Cases.xlsx
│   ├── MagicBricks_Sell_Module_Test_Document.md
│   └── MagicBricks_Test_Scenarios_and_Test_Cases.xlsx
├── reports/
│   └── .gitkeep                          # Directory for ExtentReports HTML output
├── screenshots/
│   └── .gitkeep                          # Directory for captured runtime screenshots
├── src/test/java/com/magicbricks/
│   ├── base/
│   │   ├── BasePage.java                 # Base page object with scrolling, highlighting, and tab/frame helpers
│   │   └── BaseTest.java                 # Base test lifecycle with driver setup/teardown and popup handling
│   ├── listeners/
│   │   └── ExtentReportListener.java     # TestNG ITestListener for ExtentReports and screenshot attachment
│   ├── pages/
│   │   ├── HomePage.java                 # PageFactory object for Home Page navigation & search
│   │   ├── LoginPage.java                # PageFactory object for Login tab & modal validation
│   │   ├── SellDropdownPage.java         # PageFactory object for Header Sell dropdown navigation
│   │   ├── RatesAndTrendsPage.java       # PageFactory object for Rates & Trends page & city matrix
│   │   ├── FindAgentPage.java            # PageFactory object for Find an Agent & agent profile cards
│   │   ├── DeveloperLoungePage.java      # PageFactory object for Developer Lounge Brand Store
│   │   └── ContactUsPage.java            # PageFactory object for Contact Us traversal & directory
│   ├── tests/
│   │   ├── HomePageTest.java             # Production test cases for Home Page module
│   │   ├── LoginTest.java                # Data-driven test cases for Login module
│   │   └── SellPageTest.java             # Case Study 1 test cases for Sell module (8 curated tests)
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
        ├── TestData.xlsx                 # Excel workbook for Home & Login data
        └── SellTestData.xlsx             # Excel workbook for Sell city trends
```

---

## 4. Prerequisites
Before running the project on a fresh machine:
1. **JDK 11**: Java Development Kit 11 installed (`java -version` returns 11.x).
2. **Apache Maven**: Maven 3.8+ installed (`mvn -version`).
3. **Google Chrome**: Latest stable Google Chrome browser installed (WebDriverManager handles the binary automatically).
4. **Eclipse IDE**: Eclipse IDE for Java Developers (2023-06 or newer recommended).

---

## 5. Clone & Setup Instructions for Eclipse IDE (Direct Import)

### Step 1: Clone the Repository
```bash
git clone https://github.com/sujalkamble007/MagicBricks.git
cd MagicBricks
```

### Step 2: Import into Eclipse IDE
1. Launch **Eclipse IDE**.
2. Go to **File** → **Import...**
3. Expand **Maven** → select **Existing Maven Projects** and click **Next**.
4. Click **Browse...** and select the cloned `MagicBricks` folder (where `pom.xml` is located).
5. Ensure `pom.xml` is checked in the Projects list and click **Finish**.
6. Eclipse's M2E plugin will automatically:
   - Read `pom.xml` and resolve all dependencies (Selenium 4.34.0, TestNG 7.11.0, Apache POI 5.2.5, ExtentReports 5.1.2, slf4j-simple 2.0.12).
   - Configure the classpath and build paths for `src/main/java`, `src/test/java`, `src/test/resources`.

### Step 3: Run Tests in Eclipse
1. In the **Package Explorer** or **Project Explorer**, expand `MagicBricks`.
2. Right-click **`testng.xml`** → **Run As** → **TestNG Suite**.
3. All tests across Home, Login, and Sell modules will execute sequentially in Chrome with visible element highlighting and smooth scrolling.

---

## 6. How to Run via Command Line / Terminal

### Option 1: Simple Runner Script (`./run.sh`)
```bash
# Run Whole Suite (Home + Login + Sell)
./run.sh

# Run Home & Login Modules Together
./run.sh homelogin

# Run Sell Module Separately
./run.sh sell

# Run Individual Modules
./run.sh home
./run.sh login
```

---

### Option 2: Maven Profile Commands (Direct `mvn`)

#### 1. Run Complete Test Suite (All Modules):
```bash
mvn clean test
```
*Generates all 3 separate HTML reports:*
- `reports/TestReport_Home.html`
- `reports/TestReport_Login.html`
- `reports/TestReport_Sell.html`

#### 2. Run Home & Login Modules Together:
```bash
mvn test -P homelogin
# Or: mvn test -Dhomelogin
```
*Executes both and generates:*
- `reports/TestReport_Home.html`
- `reports/TestReport_Login.html`

#### 3. Run Sell Module Separately:
```bash
mvn test -P sell
# Or: mvn test -Dsell
```
*Executes Sell and generates:*
- `reports/TestReport_Sell.html`

#### 4. Run Individual Test Classes:
```bash
# Run Home Page Module Only
mvn test -Dtest=HomePageTest

# Run Login Module Only
mvn test -Dtest=LoginTest

# Run Sell Module Only
mvn test -Dtest=SellPageTest
```

---

## 7. Reports & Output Locations

Each module generates its own **dedicated HTML report** with interactive charts, execution logs, and embedded full-resolution screenshots:

1. **Dedicated Module ExtentReports**:
   - 🏠 **Home Page Report**: `reports/TestReport_Home.html`
   - 🔐 **Login Module Report**: `reports/TestReport_Login.html`
   - 🏢 **Sell Module Report**: `reports/TestReport_Sell.html`

2. **TestNG Default Reports**:
   - Location: `target/surefire-reports/emailable-report.html`
   - Location: `target/surefire-reports/testng-results.xml`

3. **QA Test Documents**:
   - Location: `Test_Documents/MagicBricks_Sell_Module_Test_Scenarios_and_Test_Cases.xlsx`
   - Location: `Test_Documents/MagicBricks_Sell_Module_Test_Document.md`

2. **TestNG Default Reports**:
   - Location: `target/surefire-reports/emailable-report.html`
   - Location: `target/surefire-reports/testng-results.xml`

3. **QA Test Documents**:
   - Location: `Test_Documents/MagicBricks_Sell_Module_Test_Scenarios_and_Test_Cases.xlsx`
   - Location: `Test_Documents/MagicBricks_Sell_Module_Test_Document.md`

---

## 8. Troubleshooting & Common Issues in Eclipse

| Issue / Symptom | Root Cause | Solution |
|---|---|---|
| `TestNG plugin not found in Eclipse` | Eclipse lacks TestNG runner | Go to **Help** → **Eclipse Marketplace** → Search `TestNG for Eclipse` → Click **Install** and restart Eclipse. |
| `Red exclamation mark on project` | Maven dependencies need updating | Right-click project → **Maven** → **Update Project...** → Check *Force Update of Snapshots/Releases* → Click **OK**. |
| `Java compiler level mismatch` | Project JRE set to JDK 8 or JDK 17+ | Right-click project → **Properties** → **Java Compiler** → Set compiler compliance level to **11**. |
| `Target or report files not visible` | Eclipse Project Explorer caching | Press `F5` on the project root to refresh and view generated `reports/` and `screenshots/`. |
