package com.aa.utility;


import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.io.FileHandler;
import org.testng.IAnnotationTransformer;
import org.testng.IExecutionListener;
import org.testng.IRetryAnalyzer;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.ITestAnnotation;

import com.aa.constant.ITestConstants;
import com.aa.drivers.BaseClass;
import com.aa.reports.ClientFriendlyReporter;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;

import net.bytebuddy.utility.RandomString;
import org.testng.ISuiteListener;

@SuppressWarnings("deprecation")
public class Listener extends BaseClass implements ITestListener, IRetryAnalyzer, IAnnotationTransformer, ISuiteListener, ITestConstants, IExecutionListener {

    // Thread-safe counters
    private AtomicInteger excelsheetRowCounter = new AtomicInteger(1);
    private AtomicInteger targetWindowCloseexceptionCount = new AtomicInteger(0);
    
    // Thread-safe test result counters
    private static ConcurrentHashMap<String, AtomicInteger> testCounters = new ConcurrentHashMap<>();
    
    // Retry counters - now thread-local
    private ThreadLocal<Integer> retryCnt = ThreadLocal.withInitial(() -> 0);
    private static final int MAX_RETRY_CNT = 0; // Max retry count
    
    // Thread-safe ExtentTest handling
    private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static ExtentReports extent = ExtentReportGenerator.getReports();
    
    // Thread-safe parent window reference
    private static ThreadLocal<String> parentWindowAddress = new ThreadLocal<>();

    int passed;
    
    int total;
    
    int skipped;
    
    int failed;
    String suiteName;
    @Override
    public void onStart(ISuite suite) {
        System.out.println("Test Suite Started: " + suite.getName() + " in thread: " + Thread.currentThread().getId());
        int totalTestCases = suite.getAllMethods().size();
        TOTAL_NUMBER_OF_TEST_CASES = totalTestCases;
        System.out.println("Total number of test cases in the suite: " + totalTestCases);
        Reporter.log("Suite Start: " + suite.getName() + " at " + Library.timeStampLogging());
        
        // Initialize counters for this suite
        testCounters.put("passedTests", new AtomicInteger(0));
        testCounters.put("failedTests", new AtomicInteger(0));
        testCounters.put("skippedTests", new AtomicInteger(0));
    }

    @Override
    public void onTestStart(ITestResult result) {
    	
    	
    	  Date endTime = new Date();
          TEST_START_TIME = endTime.getTime();
        
    	
    	
        // Reset step counter for this test
        logStepVar = 1;
        
        String testSuiteName = result.getTestContext().getName();
        String testClassName = result.getTestClass().getName().replace("_", "");
        String testCaseName = result.getMethod().getMethodName().replace("_", "");
        
        // Create thread-safe ExtentTest instance
        ExtentTest test = extent.createTest(testClassName + "==" + testCaseName);
        extentTest.set(test);
        Library.test = test; // Update the Library reference if needed
        
        System.out.println("Test case started in thread " + Thread.currentThread().getId() + 
                         ": " + testClassName + "==" + testCaseName);

        // Initialize test-specific counters
        int currentRow = excelsheetRowCounter.getAndIncrement();
        
        try {
            if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                EXCELREADER.setCellData(NewSheet, "Test Suite Name", currentRow, testSuiteName);
                EXCELREADER.setCellData(NewSheet, "Test Class Name", currentRow, testClassName);
                EXCELREADER.setCellData(NewSheet, "Test Case Name", currentRow, testCaseName);
            }
            
            logTestCaseStart("Start ", Library.timeStampLogging());
            
            if (DO_YOU_WANTS_TO_ADD_EXPANDED_MENU_REPORT) {
                Reporter.log("<details>");
                Reporter.log("<summary><b>🟢 Test Case Started: " + testCaseName + "</b></summary>");
            }
            
            // Handle video recording if enabled
            if (DO_YOU_WANTS_TO_RECORD_TEST_CASE_LEVEL_VIDEO) {
                String testVideoFileName = testClassName + "_" + testCaseName;
                try {
                    ScreenRecorderUtil.startRecord(testVideoFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testCounters.get("passedTests").incrementAndGet();
        TEST_CASE_STATUS = "PASS";
        
        try {
            //Library.closeAllWindowExceptParent(getDriver());
            //Library.homePageNavigationFunction();
            
            int currentRow = excelsheetRowCounter.get() - 1; // Get current row without incrementing
            
            if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                EXCELREADER.setCellData(NewSheet, "Test Case Status", currentRow, TEST_CASE_STATUS);
                EXCELREADER.setCellData(NewSheet, "Failed Reason", currentRow, "");
            }
            
            // Handle execution time reporting
            handleExecutionTimeReporting(result, currentRow);
            
            extentTest.get().log(Status.PASS, MarkupHelper.createLabel("Test Case PASS", ExtentColor.GREEN));
            logTestCaseStart("Success ", Library.timeStampLogging());
            
            if (DO_YOU_WANTS_TO_ADD_EXPANDED_MENU_REPORT) {
                Reporter.log("</details>");
                Reporter.log("<summary><b>✅ Test Case Passed: " + result.getMethod().getMethodName() + "</b></summary>");
            }
            
            // Capture success screenshot
            try {
                Library.test.addScreenCaptureFromBase64String(reportpathBase(result), "<b>Final PASS ScreenShot</b>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Library.backToSummaryButton();
            
            // Handle video recording stop
            stopVideoRecording(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testCounters.get("failedTests").incrementAndGet();
        TEST_CASE_STATUS = "FAIL";
        
        try {
            Library.closeAllWindowExceptParent(getDriver());
            
            String msg = result.getThrowable().getMessage();
            msg = msg == null ? "null" : msg;
            
            if (msg.contains("no such window")) {
                if (targetWindowCloseexceptionCount.incrementAndGet() > 2) {
                    handleTestCompletion(result);
                    return;
                }
            }
            
            int currentRow = excelsheetRowCounter.get() - 1;
            
            if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                EXCELREADER.setCellData(NewSheet, "Test Case Status", currentRow, TEST_CASE_STATUS);
                EXCELREADER.setCellData(NewSheet, "Toaster Massage", currentRow, TOASTERMASSAGE);
                EXCELREADER.setCellData(NewSheet, "Failed Reason", currentRow, msg);
            }
            
            // Capture failure screenshot
            ScreenshotUtil.captureScreenshotWithRandomName();
            
            extentTest.get().log(Status.FAIL, MarkupHelper.createLabel("Test Cases Failed Because of ==" + msg, ExtentColor.RED));
            logTestCaseStart("Failure ", Library.timeStampLogging());
            
            try {
                Library.test.addScreenCaptureFromBase64String(reportpathBase(result), "<font color=red><b>Final Failed ScreenShot</b></font>");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // Handle execution time reporting
            handleExecutionTimeReporting(result, currentRow);
            
            if (DO_YOU_WANTS_TO_ADD_EXPANDED_MENU_REPORT) {
                Reporter.log("</details>");
                Reporter.log("<details open>");
                Reporter.log("<summary><b>❌ Test Case Failed: " + result.getMethod().getMethodName() + "</b></summary>");
                Reporter.log("<p>Error: " + result.getThrowable() + "</p>");
                Reporter.log("<p><a href='" + SCREENSHOTPATH + "' target='_blank'>View Screenshot</a></p>");
                Reporter.log("</details>");
            }
            
            Library.backToSummaryButton();
            
            // Handle video recording stop
            stopVideoRecording(false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resetTestSpecificVariables();
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testCounters.get("skippedTests").incrementAndGet();
        TEST_CASE_STATUS = "SKIP";
        
        try {
            String msg = result.getThrowable().getMessage();
            msg = msg == null ? "null" : msg;
            
            if (msg.contains("no such window")) {
                if (targetWindowCloseexceptionCount.incrementAndGet() > 2) {
                    handleTestCompletion(result);
                    return;
                }
            }
            
            int currentRow = excelsheetRowCounter.get() - 1;
            
            if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                EXCELREADER.setCellData(NewSheet, "Test Case Status", currentRow, TEST_CASE_STATUS);
                EXCELREADER.setCellData(NewSheet, "Toaster Massage", currentRow, TOASTERMASSAGE);
            }
            
            extentTest.get().log(Status.SKIP, "<b>Test Case SKIP</b>");
            
            // Handle execution time reporting
            handleExecutionTimeReporting(result, currentRow);
            
            if (DO_YOU_WANTS_TO_ADD_EXPANDED_MENU_REPORT) {
                Reporter.log("</details>");
                Reporter.log("<summary><b>⏭️ Test Case Skipped: " + result.getMethod().getMethodName() + "</b></summary>");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resetTestSpecificVariables();
        }
    }

    @Override
    public void onFinish(ITestContext context) {
        try {
            logTestCaseStart("Finish ", Library.timeStampLogging());
            
            if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                EXCELREADER.setCellData(NewSheet, "Test Execution Summary", 1, "Total Passed");
                EXCELREADER.setCellData(NewSheet, "Test Execution Summary", 2, "Total Failed");
                EXCELREADER.setCellData(NewSheet, "Test Execution Summary", 3, "Total Skipped");
                EXCELREADER.setCellData(NewSheet, "Test Execution Summary", 4, "Total Test Cases");
                
                int passed = testCounters.get("passedTests").get();
                int failed = testCounters.get("failedTests").get();
                int skipped = testCounters.get("skippedTests").get();
                int total = passed + failed + skipped;
                
                EXCELREADER.setCellData(NewSheet, "Test Execution Count", 1, Integer.toString(passed));
                EXCELREADER.setCellData(NewSheet, "Test Execution Count", 2, Integer.toString(failed));
                EXCELREADER.setCellData(NewSheet, "Test Execution Count", 3, Integer.toString(skipped));
                EXCELREADER.setCellData(NewSheet, "Test Execution Count", 4, Integer.toString(total));
            }
            
            ScreenshotUtil.screenshot("Finish");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish(ISuite suite) {
    	
    	
    	
    	
    	suiteName=suite.getName();
        System.out.println("Test Suite Finished: " + suite.getName() + " in thread: " + Thread.currentThread().getId());
        Reporter.log("Suite Finished: " + suite.getName() + " at " + Library.timeStampLogging());
        
        // Generate summary report
         passed = testCounters.get("passedTests").get();
        int failed = testCounters.get("failedTests").get();
        int skipped = testCounters.get("skippedTests").get();
        int total = passed + failed + skipped;
        
        String summary = String.format("<b>Test Suite Summary:</b><br>" +
                "Total: %d<br>" +
                "Passed: %d<br>" +
                "Failed: %d<br>" +
                "Skipped: %d<br>", 
                total, passed, failed, skipped);
        
        Reporter.log(summary);
        
        // Flush extent reports
        extent.flush();
        
        // Clean up thread-local variables
        extentTest.remove();
        parentWindowAddress.remove();
        retryCnt.remove();
        
        /*
        
        // === Send Email at the End of Suite ===
        try {
            // Path to your generated Extent Report or Summary Report
            String reportPath = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "emailable-report.html";


            // Wait for file to be generated (max 30 seconds)
            File reportFile = new File(reportPath);
            int attempts = 0;
            while (!reportFile.exists() && attempts < 30) {  // wait up to 30s
                try {
                    Thread.sleep(1000); // 1 second
                    attempts++;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            
            
            String timestamp = Library.timeStampLogging(); // Get current timestamp

         // Email details
         String subject = "Automation Test Report - " + suite.getName();
         String toEmails = "ankushagle7@gmail.com, Mahi.panchal1991@gmail.com";
         // Email body details
         String greeting = "Hello Team,";
         String messageIntro = "Please find attached the Automation Test Execution Report.";
         String totalMsg = "Total: " + total;
         String passedMsg = "Passed: " + passed;
         String failedMsg = "Failed: " + failed;
         String skippedMsg = "Skipped: " + skipped;
         String timestampMsg = "Report Generated On: " + timestamp;
         String regards = "Regards,";
         String signature = "Automation Framework";

         // Combine all parts into the body
         String emailBody = greeting + "\n\n" 
                          + messageIntro + "\n\n" 
                          + totalMsg + "\n" 
                          + passedMsg + "\n" 
                          + failedMsg + "\n" 
                          + skippedMsg + "\n\n" 
                          + timestampMsg + "\n\n" 
                          + regards + "\n" 
                          + signature;

         // Send email
         EmailUtils.sendEmailWithAttachment(toEmails, subject, emailBody, reportPath);

            
            System.out.println("Email with report sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send email report: " + e.getMessage());
        }
        
        */
        
    }

    @Override
    public boolean retry(ITestResult result) {
        int currentRetry = retryCnt.get();
        if (currentRetry < MAX_RETRY_CNT) {
            System.out.println("ReExecuting Failed Test Script :: " + result.getName() + 
                            " again in thread " + Thread.currentThread().getId() + 
                            " and the count is " + (currentRetry + 1));
            try {
                Reporter.log("<br>");
                Library.massagelog_TIME_DATE("ReExecuting Failed Test Script :: " + result.getName() + 
                                           " :: On Time " + Library.timeStampLogging());
                Reporter.log("<br>");
                ScreenshotUtil.screenshot("Finish");
            } catch (Exception e) {
                e.printStackTrace();
            }
            retryCnt.set(currentRetry + 1);
            return true;
        }
        return false;
    }

    @Override
    public void transform(ITestAnnotation annotation, Class testClass, 
                         java.lang.reflect.Constructor testConstructor, 
                         java.lang.reflect.Method testMethod) {
        annotation.setRetryAnalyzer(this.getClass());
    }

    // Helper methods
    private void handleExecutionTimeReporting(ITestResult result, int currentRow) {
        try {
            Date endTime = new Date();
            TEST_END_TIME = endTime.getTime();
            
            long executionTimeMillis = TEST_END_TIME - TEST_START_TIME;
            long executionTimeSeconds = executionTimeMillis / 1000;
            long minutes = executionTimeSeconds / 60;
            long seconds = executionTimeSeconds % 60;
            
            if (DISPLAY_TOTAL_EXECUTION_TIME.equalsIgnoreCase("YES")) {
                String totalTimeString = minutes + " minutes " + seconds + " seconds";
                Library.subCalculationHeadingLoggingFunction("Total execution time: " + totalTimeString);
                
                if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
                    EXCELREADER.setCellData(NewSheet, "Total execution time", currentRow, totalTimeString);
                    if (TEST_CASE_STATUS.equals("FAIL")) {
                        EXCELREADER.addScreenshotFromPathToSheetByColumnName(SCREENSHOTPATH, NewSheet, "SnapShot", currentRow);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    
    
    
    private void stopVideoRecording(boolean isSuccess) {
        if (DO_YOU_WANTS_TO_RECORD_TEST_CASE_LEVEL_VIDEO) {
            String exceptionMsg = "NO";
            try {
                ScreenRecorderUtil.stopRecord();
                if (DO_YOU_WANTS_TO_ATTACH_TEST_CASE_LEVEL_VIDEO_TO_REPORT && exceptionMsg.equals("NO")) {
                    String videoPath = ScreenRecorderUtil.getMovieFilePath();
                    // Add video to report if needed
                }
            } catch (Exception e) {
                exceptionMsg = e.getMessage();
            }
        }
    }

    private void resetTestSpecificVariables() {
        SCREENSHOTPATH = "";
        TOASTERMASSAGE = "";
    }

    private void handleTestCompletion(ITestResult result) {
        int currentRow = excelsheetRowCounter.get() - 1;
        if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("YES")) {
            EXCELREADER.setCellData(NewSheet, "Test Case Status", currentRow, TEST_CASE_STATUS);
        }
        resetTestSpecificVariables();
    }

    // Thread-safe screenshot methods
    public String reportpath() throws Exception {
        String rm = RandomString.make(2);
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File src = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "\\ScreenShots\\failedtest" + rm + ".png";
        File destn = new File(path);
        FileHandler.copy(src, destn);
        return path;
    }

    public String reportpath(ITestResult result) throws Exception {
        String fille = result.getMethod().getMethodName();
        String rm = RandomString.make(2);
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        File src = ts.getScreenshotAs(OutputType.FILE);
        String dest = ".//Reports//ScreenShots//" + fille + rm + ".png";
        File destn = new File(dest);
        FileHandler.copy(src, destn);
        return dest;
    }

    public String reportpathBase(ITestResult result) throws Exception {
        TakesScreenshot ts = (TakesScreenshot) getDriver();
        String dest = ts.getScreenshotAs(OutputType.BASE64);
        return "data:image/jpg;base64, " + dest;
    }

    public static void logTestCaseStart(String status, String startDateTime) {
        String formattedLog = "<div style='border: 2px solid #007bff; padding: 10px; border-radius: 5px; background-color: #f9f9f9; display: inline-block; width: auto;'>"
                + "<strong style='color: #18122B;'>🕒 Test case " + status + "  :</strong> "
                + "<span style='font-weight: bold; color: #000;'>"
                + startDateTime + "</span>"
                + "</div>";
        Reporter.log(formattedLog);
    }
    
    
    
    @Override
    public void onExecutionStart() {
        // Runs before any suite starts
    }

    @Override
    public void onExecutionFinish() {

        try {
            String[] requiredEmailSettings = {"TO_EMAIL", "FROM_EMAIL", "EMAIL_API_KEY"};
            String missingSettings = java.util.Arrays.stream(requiredEmailSettings)
                    .filter(key -> !EnvironmentUtils.isConfigured(key))
                    .collect(java.util.stream.Collectors.joining(", "));
            if (!missingSettings.isEmpty()) {
                System.out.println("Skipping test report email; missing configuration: " + missingSettings);
                return;
            }

            String timestamp = Library.timeStampLogging(); // Get current timestamp

         String safeSuiteName = escapeHtml(suiteName);
         String subject = "Automation Test Report - " + suiteName;
         String toEmails = EnvironmentUtils.getValue("TO_EMAIL");
         String reportPath = ClientFriendlyReporter.FILE_PATH;
         if (reportPath == null || reportPath.isBlank() || !new File(reportPath).isFile()) {
             throw new IOException("Generated client-friendly report is missing: " + reportPath);
         }
         String emailBody = "<!DOCTYPE html>"
                 + "<html><body style=\"margin:0;padding:24px;background-color:#f3f6fb;"
                 + "font-family:Arial,Helvetica,sans-serif;color:#1f2937;\">"
                 + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" "
                 + "style=\"max-width:640px;margin:0 auto;background:#ffffff;border:1px solid #e5eaf1;"
                 + "border-radius:14px;overflow:hidden;\">"
                 + "<tr><td style=\"padding:28px 32px;background:#172554;color:#ffffff;\">"
                 + "<div style=\"font-size:13px;letter-spacing:1.5px;text-transform:uppercase;"
                 + "color:#bfdbfe;\">AUTOMATION TEST REPORT</div>"
                 + "<h1 style=\"margin:10px 0 6px;font-size:25px;line-height:1.3;\">"
                 + "&#9989; Test execution complete</h1>"
                 + "<div style=\"font-size:15px;color:#e0e7ff;\">" + safeSuiteName + "</div>"
                 + "</td></tr>"
                 + "<tr><td style=\"padding:26px 32px 10px;\">"
                 + "<p style=\"margin:0 0 20px;font-size:15px;line-height:1.6;\">"
                 + "Hello Team,<br>Your automated test run summary is ready. "
                 + "The detailed report is attached.</p>"
                 + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">"
                 + "<tr>"
                 + summaryCard("TOTAL", total, "#eff6ff", "#1d4ed8")
                 + summaryCard("PASSED", passed, "#ecfdf5", "#047857")
                 + summaryCard("FAILED", failed, "#fef2f2", "#b91c1c")
                 + summaryCard("SKIPPED", skipped, "#fffbeb", "#b45309")
                 + "</tr></table>"
                 + "<div style=\"margin-top:22px;padding:14px 16px;background:#f8fafc;"
                 + "border-radius:8px;font-size:13px;color:#475569;\">"
                 + "&#128336; <strong>Run time:</strong> " + escapeHtml(timestamp)
                 + "</div></td></tr>"
                 + "<tr><td style=\"padding:18px 32px 26px;font-size:13px;color:#64748b;\">"
                 + "Regards,<br><strong style=\"color:#334155;\">Automation Framework</strong>"
                 + "</td></tr></table></body></html>";

         // Send email
         EmailUtils.sendEmailWithAttachment(toEmails, subject, emailBody, reportPath);

            
            System.out.println("Email with report sent successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send email report: " + e.getMessage());
        }

    }
    
    private static String summaryCard(String label, int count, String background, String color) {
        return "<td width=\"25%\" style=\"padding:4px;\">"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" "
                + "style=\"background:" + background + ";border-radius:9px;text-align:center;\">"
                + "<tr><td style=\"padding:14px 6px;\">"
                + "<div style=\"font-size:11px;font-weight:bold;letter-spacing:.7px;color:" + color + ";\">"
                + label + "</div><div style=\"padding-top:7px;font-size:25px;font-weight:bold;color:"
                + color + ";\">" + count + "</div></td></tr></table></td>";
    }

    private static String escapeHtml(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
    
  
}