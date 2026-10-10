# 🌟 Ankush Selenium Java Hybrid Framework

> A Java 17, Maven-based UI automation framework combining Selenium WebDriver,
> TestNG, page objects, and reusable Excel/JSON test data.

## ✨ At a glance

| Area | Details |
| --- | --- |
| Language | Java 17 |
| Build | Maven |
| Browser automation | Selenium WebDriver 4 |
| Test runner | TestNG |
| Test data | JSON and Excel workbooks |
| CI | GitHub Actions |

## 🧭 How the framework is organized

```text
.
├── .github/workflows/       # GitHub Actions workflow
├── ExcelFiles/              # Excel-based test/configuration data
├── JsonFiles/               # JSON test data
├── UploadFiles/             # Local files used by upload tests
├── src/
│   ├── main/java/com/aa/
│   │   ├── constant/        # Shared constants and test groups
│   │   ├── drivers/         # WebDriver creation and lifecycle
│   │   ├── reports/         # Test reporting listeners
│   │   └── utility/         # Excel, JSON, environment, and helper utilities
│   ├── main/java/com/pom/   # Page objects
│   └── test/java/com/test/  # TestNG test classes
├── testng.xml               # Active TestNG suite and listeners
└── pom.xml                  # Dependencies, Java version, and Maven config
```

### 🧩 Main building blocks

- **Driver management:** `BrowserDriverManager` stores a WebDriver per thread;
  `DriverFactory` creates browser drivers.
- **Page objects:** classes under `com.pom` encapsulate page interactions and
  keep UI operations reusable from test classes.
- **Test lifecycle and reporting:** `BaseClass` provides TestNG setup and
  teardown; listeners in `com.aa.utility` and `com.aa.reports` handle suite
  events and reports.
- **Data-driven tests:** tests can read input from `JsonFiles/` or
  `ExcelFiles/`, with shared readers and helpers under `com.aa.utility`.
- **Environment configuration:** `EnvironmentUtils` reads process environment
  variables first, then values from a local `.env` file.

## ⚙️ Prerequisites

- JDK 17
- Maven 3.8 or later
- Google Chrome for the configured local browser tests
- Internet access for Maven dependencies, WebDriver setup, and the demo site

The active suite uses Chrome and navigates to the public DemoQA site. Headed
Chrome is used by default, so run locally on a desktop session. The CI workflow
uses a Windows GitHub-hosted runner to match the framework's current
Windows-style file paths.

## 🚀 Run the tests

Run the TestNG suite selected in `testng.xml`:

```powershell
mvn --batch-mode --no-transfer-progress test
```

To remove prior build output and then run the suite:

```powershell
mvn --batch-mode --no-transfer-progress clean test
```

Maven Surefire is configured to use `testng.xml`. The active suite registers
the framework and client-friendly report listeners and selects
`PracticeFormTestUsingJsonFile.fillPracticeFormTest` for the specified
invocations.

### 🔎 Run a different TestNG suite

The current Surefire configuration points to the root `testng.xml`. To run a
different suite file without changing that configuration:

```powershell
mvn --batch-mode --no-transfer-progress test `
  "-Dsurefire.suiteXmlFiles=path\to\your-suite.xml"
```

## 🤖 Continuous integration

The workflow at `.github/workflows/test.yml` runs the same Maven test command
on pushes and pull requests targeting `main`. It checks out the repository,
sets up Java 17, and caches Maven dependencies. It can also be started manually
from the GitHub Actions tab using **Run workflow**.

Test results and generated reports are written to local build/report
directories. These outputs are excluded from source control; use the Actions
run summary and logs to review CI results.

## 🔐 Local configuration and secrets

Copy `.env.example` to `.env` if you need local environment settings, then
replace the blank values with your own. Keep `.env` out of commits and never
paste credentials into source files, test data, or reports.

The current TestNG suite does not need Sauce Labs credentials. If you
intentionally run the `sauce-chrome` browser, provide `SAUCE_USERNAME` and
`SAUCE_ACCESS_KEY` as environment variables locally and configure them as
GitHub repository **Actions secrets** for CI. Do not put real secret values in
`.env.example`.

## 📁 What belongs in Git?

**Keep versioned:** Java source, `pom.xml`, `testng.xml`, CI workflow,
non-secret test data, required upload fixtures, `README.md`, and `.env.example`.

**Keep local / generated:** `.env`, IDE workspace settings, Maven `target/`,
test reports, screenshots, generated Excel results, logs, and temporary
recordings. These are covered by `.gitignore`.
