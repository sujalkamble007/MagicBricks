# MagicBricks Automation — Sell Module Test Document
**Project:** MagicBricks Automation Framework (Selenium + Java + TestNG + Page Factory)  
**Module:** Sell (Header Dropdown, Rates & Trends, Find an Agent, Developer Lounge, Contact Us)  
**Author:** Senior SDET  
**Location:** `Test_Documents/MagicBricks_Sell_Module_Test_Scenarios_and_Test_Cases.xlsx`

---

## 1. Test Scenarios Matrix (`Test Scenarios` Sheet)

| Test Scenario ID | Requirement ID | Test Scenario Description | Type of Testing | Possible TCs | Test Case Details |
|---|---|---|---|:---:|---|
| **`TS_MB_SELL_01`** | `REQ_MB_SELL_01` | Verify MagicBricks Header Sell Dropdown Menu Structure & Element Clickability | Functional / Smoke Testing | 1 | 1. To validate all 3 sections ('For Owner', 'For Agent & Builder', 'Selling Tools') and primary links are visible, highlighted, and clickable. |
| **`TS_MB_SELL_02`** | `REQ_MB_SELL_02` | Verify Rates & Trends Page Navigation, Hero Banner & Breadcrumbs Display | Functional / Navigation Testing | 1 | 1. To validate navigation from Sell dropdown to Rates & Trends page, new window switch, and verify hero banner & breadcrumbs. |
| **`TS_MB_SELL_03`** | `REQ_MB_SELL_03` | Verify Property Type Tab Switching (Residential vs Commercial) on Rates & Trends | Functional / UI State Testing | 1 | 1. To validate interactive switching between Residential and Commercial property rate tabs with visual highlights. |
| **`TS_MB_SELL_04`** | `REQ_MB_SELL_04` | Verify Multi-City Property Rates & Locality Price Matrix via Data-Driven Excel Provider | Data-Driven / Regression Testing | 3 | 1. To validate Pune property price trends and locality matrix.<br>2. To validate Mumbai property price trends and locality matrix.<br>3. To validate Bangalore property price trends and locality matrix. |
| **`TS_MB_SELL_05`** | `REQ_MB_SELL_05` | Verify 'Find an Agent' Page Navigation, Heading & Top Agents Tab Display | Functional / Navigation Testing | 1 | 1. To validate navigation from Sell dropdown to Find an Agent, verifying primary heading and Top Agents view. |
| **`TS_MB_SELL_06`** | `REQ_MB_SELL_06` | Verify Real Estate Agent Profile Cards, 'PREFERRED AGENT' Badges & Operating Stats | Functional / Regression Testing | 1 | 1. To validate agent profile cards, 'PREFERRED AGENT' badge, deals closed / team size statistics, and contact action triggers. |
| **`TS_MB_SELL_07`** | `REQ_MB_SELL_07` | Verify Developer Lounge Brand Store Navigation, Hero Content & Brand Store Cards | Functional / Regression Testing | 1 | 1. To validate navigation to Developer Lounge brand store, verifying 'Discover Real Estate Brands' hero banner and developer brand cards (Omaxe, VTP, SPR City) with executive profiles. |
| **`TS_MB_SELL_08`** | `REQ_MB_SELL_08` | Verify Contact Us / Sales Enquiry Traversal, Smooth Scroll Down, Directory Interaction & Scroll Up Flow | Functional / Navigation / UI Testing | 1 | 1. To validate navigation to Contact Us from Sell dropdown, smooth scroll down 800px to office directory, selection of directory element with highlights, smooth scroll back up to top, and header validation. |

---

## 2. Test Cases Matrix (`Test Cases` Sheet)

### `TC_SELL_001`
- **Scenario:** `TS_MB_SELL_01`
- **Precondition:** 1) Browser is open and internet is active. 2) MagicBricks home page has fully loaded.
- **Test Condition:** Verify All Sell Dropdown Primary Elements are Clickable
- **Steps:**
  1. Hover over Sell menu tab in primary header.
  2. Verify dropdown displays with 3 sections.
  3. Highlight all 3 sections (`For Owner`, `For Agent & Builder`, `Selling Tools`).
  4. Check clickability for Post Property, Valuation, Find an Agent, and Ad Packages.
- **Test Data:** N/A (Header Dropdown Elements)
- **Expected Result:** All sections are visible and interactive elements are clickable.
- **Actual Result:** All 3 sections were highlighted and primary elements verified clickable.
- **Status:** **PASSED**

---

### `TC_SELL_002`
- **Scenario:** `TS_MB_SELL_02`
- **Precondition:** 1) MagicBricks home page is open. 2) Sell dropdown is accessible.
- **Test Condition:** Verify Rates & Trends Page Navigation from Header Sell Dropdown
- **Steps:**
  1. Hover over Sell tab.
  2. Click 'Rates & Trends' link in Selling Tools.
  3. Switch driver focus to new browser tab.
  4. Validate URL contains `propertyRates.html`.
  5. Highlight hero banner and breadcrumb navigation.
  6. Close tab and return.
- **Test Data:** `URL: propertyRates.html? fromSite=mb`
- **Expected Result:** Navigates to Rates & Trends page in new window; banner and breadcrumbs are clearly displayed.
- **Actual Result:** Page opened in new tab with valid URL; banner and breadcrumbs successfully highlighted.
- **Status:** **PASSED**

---

### `TC_SELL_003`
- **Scenario:** `TS_MB_SELL_03`
- **Precondition:** 1) Rates & Trends page is loaded in active window.
- **Test Condition:** Verify Residential vs Commercial Tab Switching on Rates & Trends Page
- **Steps:**
  1. Hover over Sell tab and open Rates & Trends in new tab.
  2. Verify Residential tab is active by default.
  3. Click Commercial property rates tab.
  4. Verify Commercial tab becomes active and highlighted.
  5. Click Residential tab to switch back.
- **Test Data:** `Tabs: Residential, Commercial`
- **Expected Result:** User can smoothly switch between Residential and Commercial property rates tabs with active visual indicators.
- **Actual Result:** Switched between Commercial and Residential tabs with visible border highlights.
- **Status:** **PASSED**

---

### `TC_SELL_004_1`, `TC_SELL_004_2`, `TC_SELL_004_3`
- **Scenario:** `TS_MB_SELL_04`
- **Precondition:** 1) Rates & Trends page is loaded. 2) Excel test data file `SellTestData.xlsx` is accessible.
- **Test Condition:** Verify Property Rates & Locality Trends (Data-Driven via Apache POI Excel)
- **Steps:**
  1. Open Rates & Trends page from Sell dropdown.
  2. Read City (`Pune`, `Mumbai`, `Bangalore`) from `SellTestData.xlsx` via TestNG DataProvider.
  3. Click city trend link.
  4. Highlight price trend matrix for the respective city.
- **Test Data:** Iteration 1: `Pune` | Iteration 2: `Mumbai` | Iteration 3: `Bangalore`
- **Expected Result:** Property rates and locality trend tables should be displayed with current rates for each respective city.
- **Actual Result:** Rates and locality matrices displayed and highlighted as expected across all 3 iterations.
- **Status:** **PASSED**

---

### `TC_SELL_005`
- **Scenario:** `TS_MB_SELL_05`
- **Precondition:** 1) MagicBricks home page is open. 2) Sell dropdown is accessible.
- **Test Condition:** Verify 'Find an Agent' Page Navigation from Sell Dropdown
- **Steps:**
  1. Hover over Sell tab.
  2. Click 'Find an Agent' link in Selling Tools.
  3. Switch driver focus to new browser tab.
  4. Validate URL contains `top-agents`.
  5. Highlight hero heading (`Agents in Pune Who Can Help You`) and Top Agents tab.
  6. Close tab and return.
- **Test Data:** `URL: Real-estate-property-top-agents`
- **Expected Result:** Navigates to Find an Agent page; Top Agents view and city agent heading are displayed.
- **Actual Result:** Navigated successfully to Find an Agent page with agent list visible.
- **Status:** **PASSED**

---

### `TC_SELL_006`
- **Scenario:** `TS_MB_SELL_06`
- **Precondition:** 1) Find an Agent page is loaded in active window.
- **Test Condition:** Verify Agent Profile Cards, 'PREFERRED AGENT' Badge and Operating Stats
- **Steps:**
  1. Navigate to Find an Agent page from Sell dropdown.
  2. Scroll down to primary agent profile card.
  3. Highlight agent profile card.
  4. Verify 'PREFERRED AGENT' badge.
  5. Verify operating stats (deals closed / team members).
  6. Verify Call / Contact action triggers.
- **Test Data:** Agent Card UI Locators (`XPath` / `CSS` / `Class`)
- **Expected Result:** Agent card displays 'PREFERRED AGENT' badge, deals closed metric, team member count, and contact buttons.
- **Actual Result:** Agent card, Preferred Agent badge, deals closed, and CTA buttons validated and highlighted.
- **Status:** **PASSED**

---

### `TC_SELL_007`
- **Scenario:** `TS_MB_SELL_07`
- **Precondition:** 1) MagicBricks home page is open. 2) Sell dropdown is accessible.
- **Test Condition:** Verify Developer Lounge Brand Store Navigation and Developer Cards
- **Steps:**
  1. Hover over Sell tab.
  2. Click 'Developer Lounge' link under 'For Agent & Builder'.
  3. Switch driver focus to new browser tab.
  4. Validate URL contains `brand-store`.
  5. Highlight 'Discover Real Estate Brands' hero heading.
  6. Scroll and highlight brand cards for Omaxe, VTP Realty, SPR City, and CEO executive profiles.
  7. Close tab and return.
- **Test Data:** `URL: property.magicbricks.com/brand-store/`
- **Expected Result:** Navigates to Developer Lounge brand store; hero heading, brand store cards, and executive profiles are displayed.
- **Actual Result:** Developer Lounge page opened; brand cards for Omaxe, VTP, SPR and executive profiles highlighted.
- **Status:** **PASSED**

---

### `TC_SELL_008`
- **Scenario:** `TS_MB_SELL_08`
- **Precondition:** 1) MagicBricks home page is open. 2) Sell dropdown is accessible.
- **Test Condition:** Verify Contact Us / Sales Enquiry Traversal, Smooth Scroll Down, Directory Interaction, and Scroll Up Flow
- **Steps:**
  1. Hover over Sell tab.
  2. Click 'Sales Enquiry' link.
  3. Switch driver focus to new browser tab.
  4. Validate URL contains `contactUs`.
  5. Smoothly scroll down 800px to the office directory & alphabet pagination.
  6. Highlight and click an interactive directory element (city branch / alphabet filter).
  7. Smoothly scroll back up to the top of the page.
  8. Highlight the top header/logo to visually confirm return to top.
  9. Close tab and switch back.
- **Test Data:** `URL: https://www.magicbricks.com/contactUs`
- **Expected Result:** Navigates to Contact Us; smoothly scrolls down to directory, highlights and clicks an element, smoothly scrolls back up to top, and returns to main window cleanly.
- **Actual Result:** Contact Us page opened, scrolled down 800px, directory element highlighted and clicked, scrolled back up to top, and header highlighted.
- **Status:** **PASSED**

---

## 3. Automation Execution Summary

- **Total Test Cases Automating:** 8
- **Total Test Executions:** 10 (with 3-city Excel DataProvider iterations)
- **Positive Tests:** 7
- **Negative / Edge / Traversal Tests:** 1 (`TC_SELL_008`)
- **Page Navigations originating from Header Sell Dropdown:**
  1. Rates & Trends (`TC_SELL_002`, `TC_SELL_003`, `TC_SELL_004`)
  2. Find an Agent (`TC_SELL_005`, `TC_SELL_006`)
  3. Developer Lounge (`TC_SELL_007`)
  4. Contact Us (`TC_SELL_008`)
- **Pass Rate:** **100% (10/10 Passed, 0 Failed, 0 Skipped)**
