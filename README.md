# Hybrid Selenium Automation Framework

A hybrid (Data-Driven + Page Object Model) UI test automation framework built with **Java 21**, **Selenium 4**, and **TestNG**. Test data is read from Excel, configuration is externalized to a properties file, and rich HTML reports with on-failure screenshots are generated using **ChainTest**.

The framework currently automates the login flow of the demo application:
`https://freelance-learn-automation.vercel.app/login`

---

## Tech Stack

| Tool / Library | Version | Purpose |
|----------------|---------|---------|
| Java           | 21      | Language |
| Selenium Java  | 4.39.0  | Browser automation |
| TestNG         | 7.10.2  | Test runner & assertions |
| Apache POI     | 5.5.1   | Reading Excel (`.xlsx`) test data |
| ChainTest      | 1.0.12  | HTML reporting & screenshot embedding |
| Maven          | —       | Build & dependency management |

---

## Project Structure

```
hybridframework/
├── config/
│   └── config.properties          # Environment, browser & runtime settings
├── testdata/
│   └── testdata.xlsx              # Excel test data (sheet: "login")
├── xmlfiles/
│   └── testng.xml                 # TestNG suite definition
├── src/
│   ├── main/java/
│   │   ├── base/
│   │   │   ├── BaseClass.java     # @BeforeClass/@AfterClass driver setup & teardown
│   │   │   └── BasePage.java      # Reusable element actions (type, click, waits)
│   │   ├── factory/
│   │   │   └── BrowserFactory.java# Launches Chrome/Firefox/Edge, applies options
│   │   ├── pages/                 # Page Object classes
│   │   │   ├── LoginPage.java
│   │   │   ├── DashboardPage.java
│   │   │   └── RegistrationPage.java
│   │   ├── helper/
│   │   │   ├── ConfigReader.java  # Reads config.properties
│   │   │   ├── ExcelReader.java   # Reads testdata.xlsx via POI
│   │   │   ├── DataProviders.java # TestNG @DataProvider source
│   │   │   └── Utility.java       # Screenshot & misc utilities
│   │   └── listeners/
│   │       └── ReportListener.java# TestNG listener for logging & screenshots
│   └── test/
│       ├── java/testcases/
│       │   └── LoginTest.java     # Test classes
│       └── resources/
│           └── chaintest.properties # ChainTest report configuration
├── reports/chaintest/             # Generated HTML reports (Index.html, Email.html)
├── screenshots/                   # Captured screenshots
└── pom.xml
```

---

## Prerequisites

- **JDK 21** (or newer) installed and `JAVA_HOME` set
- **Maven 3.x** installed
- A supported browser installed: **Chrome** (default), **Firefox**, or **Edge**
  - Selenium 4 auto-manages drivers via Selenium Manager — no manual driver download needed.

Verify your setup:

```bash
java -version
mvn -version
```

---

## Configuration

All runtime behavior is controlled from `config/config.properties`:

| Key | Description | Example |
|-----|-------------|---------|
| `qaenv` / `stagenv` | Application base URLs | `https://freelance-learn-automation.vercel.app` |
| `browser` | Browser to run on | `chrome`, `firefox`, `edge` |
| `headless` | Run browser headless | `true` / `false` |
| `pageloadtime` | Page load timeout (seconds) | `60` |
| `implicitwait` | Implicit wait (seconds) | `10` |
| `screenshot_on_failure` | Capture screenshot on test failure | `true` |
| `screenshot_on_success` | Capture screenshot on test pass | `false` |
| `screenshot_on_skip` | Capture screenshot on skip | `false` |
| `retry` | Retry count (reserved) | `2` |
| `lambdahub`, `lt_username`, `lt_api_key` | LambdaTest grid settings (optional) | — |

Update these values before running to change environment, browser, or screenshot behavior.

### Test Data

Login credentials are read from `testdata/testdata.xlsx`, sheet named **`login`**. Each row supplies a `username` / `password` pair to the `validLoginTest` data provider. Add rows to run additional data sets.

---

## How to Run

### 1. Run the full TestNG suite (default)

The Surefire plugin reads the suite file from the `suitefile` system property:

```bash
mvn clean test -Dsuitefile=testng.xml
```

### 2. Run with a different browser (override config at runtime)

Edit `config/config.properties` (`browser=firefox`) and run:

```bash
mvn clean test -Dsuitefile=testng.xml
```

### 3. Run in headless mode

Set `headless=true` in `config/config.properties`, then run the suite as above.

### 4. Run from an IDE (Eclipse / IntelliJ / VS Code)

- Import as an existing **Maven** project.
- Right-click `xmlfiles/testng.xml` → **Run As → TestNG Suite**, or
- Right-click `src/test/java/testcases/LoginTest.java` → **Run As → TestNG Test**.

> Note: When running via Maven you must pass `-Dsuitefile=testng.xml` because `pom.xml` references `xmlfiles/${suitefile}`. Omitting it will cause the build to look for an unresolved suite file.

---

## Reports

After a run, open the generated HTML reports:

- **ChainTest report:** `reports/chaintest/Index.html`
- **Email-friendly report:** `reports/chaintest/Email.html`
- **TestNG default report:** `test-output/index.html`

Reports include pass/fail status, logs, and embedded screenshots (per the `screenshot_on_*` config flags). Screenshots are also saved to the `screenshots/` directory.

---

## How It Works (Flow)

1. `testng.xml` registers the `ChainTestListener` and the custom `ReportListener`, then runs `LoginTest`.
2. `BaseClass` (`@BeforeClass`) reads the configured browser and launches it via `BrowserFactory`, which navigates to the login page.
3. `DataProviders.getData()` pulls login rows from `testdata.xlsx` using `ExcelReader`.
4. `LoginTest` drives the `LoginPage` page object, which extends `BasePage` for reusable, wait-backed actions (`type`, `click`, explicit waits).
5. A successful login returns a `DashboardPage`; the test asserts the welcome text.
6. `ReportListener` logs results and embeds screenshots into the ChainTest report based on config flags.
7. `BaseClass` (`@AfterClass`) quits the driver.

---

## Extending the Framework

- **Add a new page:** create a class in `src/main/java/pages/` extending `BasePage`, define `By` locators, and expose action methods.
- **Add a new test:** create a class in `src/test/java/testcases/` extending `BaseClass`, then register it under `<classes>` in `xmlfiles/testng.xml`.
- **Add new test data:** add a sheet/rows to `testdata/testdata.xlsx` and a matching `@DataProvider` in `DataProviders.java`.
- **Add reusable actions:** extend `helper/Utility.java` (planned: waits, scroll, JS executor, alerts/windows/frames handling, random data generation).
```
