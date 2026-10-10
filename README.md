# AnkushSeleniumJavaHybridFramework

## Run the TestNG suite

This project uses Maven Surefire to run the suite configured in `testng.xml`.
With Java 17 and Maven installed, run:

```powershell
mvn --batch-mode --no-transfer-progress test
```

The GitHub Actions workflow runs the same command on a Windows runner when
changes are pushed to `main` or a pull request targets `main`.

## Local configuration

Copy `.env.example` to `.env` and fill in values needed by your local setup.
Do not commit `.env`. The current TestNG suite does not require Sauce Labs
credentials; if you run tests with the `sauce-chrome` browser, configure
`SAUCE_USERNAME` and `SAUCE_ACCESS_KEY` as environment variables or GitHub
Actions repository secrets.
