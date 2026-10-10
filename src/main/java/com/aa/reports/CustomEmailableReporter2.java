package com.aa.reports;
 // <-- Change this to your actual package
import org.testng.*;
import org.testng.internal.Utils;
import org.testng.reporters.EmailableReporter2;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
//com.aa.reports.CustomEmailableReporter
public class CustomEmailableReporter2 extends EmailableReporter2 {

    private static final String REPORT_FILE = "custom-emailable-report.html";
    private static final NumberFormat PERCENTAGE = new DecimalFormat("#0.00%");
    private static final String companyName = "Custome";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            File outputFile = new File(outputDirectory, REPORT_FILE);
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));

            writer.write("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Custom TestNG Report</title>");
            writer.write("<style>");
            writer.write(getCss());
            writer.write("</style></head><body>");
            writer.write("<h1>"+companyName+" Testing Execution Report</h1>");

            // Summary Table
            generateSummaryTable(writer, suites);

            // Passed / Failed / Skipped tables
            generateResultTables(writer, suites);

            // Detailed view
            generateDetails(writer, suites);

            writer.write("</body></html>");
            writer.close();

            System.out.println("Custom Emailable Report generated at: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateSummaryTable(Writer writer, List<ISuite> suites) throws IOException {
        int totalPassed = 0, totalFailed = 0, totalSkipped = 0;
        long totalTime = 0;

        writer.write("<h2 id=\"summary\">Summary</h2>");
        
        
        writer.write("<button style=\"background-color:#4CAF50;color:white;padding:5px 10px;margin-right:5px;border:none;border-radius:4px;cursor:pointer;\" "
        	    + "onclick=\"document.getElementById('Passed').scrollIntoView({behavior:'smooth'});\">LookPassed</button>");

        	writer.write("<button style=\"background-color:#FF9800;color:white;padding:5px 10px;margin-right:5px;border:none;border-radius:4px;cursor:pointer;\" "
        	    + "onclick=\"document.getElementById('Skipped').scrollIntoView({behavior:'smooth'});\">LookSkipped</button>");

        	writer.write("<button style=\"background-color:#F44336;color:white;padding:5px 10px;margin-right:5px;border:none;border-radius:4px;cursor:pointer;\" "
        	    + "onclick=\"document.getElementById('Failed').scrollIntoView({behavior:'smooth'});\">LookFailed</button>");

        writer.write("<table>");
        writer.write("<thead><tr>");
        writer.write("<th>Test</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Start Time</th><th>End Time</th><th>Duration</th>");
        writer.write("</tr></thead>");

        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                int passed = context.getPassedTests().size();
                int failed = context.getFailedTests().size();
                int skipped = context.getSkippedTests().size();
                long duration = context.getEndDate().getTime() - context.getStartDate().getTime();

                totalPassed += passed;
                totalFailed += failed;
                totalSkipped += skipped;
                totalTime += duration;

                writer.write("<tr>");
                writer.write("<td>" + escapeHtml(context.getName()) + "</td>");
                writer.write("<td class='passed'>" + passed + "</td>");
                writer.write("<td class='failed'>" + failed + "</td>");
                writer.write("<td class='skipped'>" + skipped + "</td>");
                writer.write("<td>" + formatDate(context.getStartDate().getTime()) + "</td>");
                writer.write("<td>" + formatDate(context.getEndDate().getTime()) + "</td>");
                writer.write("<td>" + formatDuration(duration) + "</td>");
                writer.write("</tr>");
            }
        }

        int totalMethods = totalPassed + totalFailed + totalSkipped;
        writer.write("<tfoot><tr>");
        writer.write("<td>Total</td>");
        writer.write("<td class='passed'>" + totalPassed + "<br/>(" + PERCENTAGE.format(totalPassed / (double) totalMethods) + ")</td>");
        writer.write("<td class='failed'>" + totalFailed + "<br/>(" + PERCENTAGE.format(totalFailed / (double) totalMethods) + ")</td>");
        writer.write("<td class='skipped'>" + totalSkipped + "<br/>(" + PERCENTAGE.format(totalSkipped / (double) totalMethods) + ")</td>");
        writer.write("<td colspan='3'>" + formatDuration(totalTime) + "</td>");
        writer.write("</tr></tfoot>");

        writer.write("</table>");
    }

    
    
    
    private void generateResultTables(Writer writer, List<ISuite> suites) throws IOException {
        writeResultTable(writer, suites, "Failed Tests", "failed");
        writeResultTable(writer, suites, "Skipped Tests", "skipped");
        writeResultTable(writer, suites, "Passed Tests", "passed");
    }
    
    private void writeResultTable(Writer writer, List<ISuite> suites, String title, String cssClass) throws IOException {
//    	 // Assign IDs for scrolling
//        String tableId = cssClass.equals("passed") ? "Passed"
//                       : cssClass.equals("skipped") ? "Skipped"
//                       : "Failed";
//
//        // Add table header with ID
//        writer.write("<h2 id='" + tableId + "'>" + title + "</h2>");
//    
    
     // Assign IDs for scrolling
        String tableId = cssClass.equals("passed") ? "Passed"
                       : cssClass.equals("skipped") ? "Skipped"
                       : "Failed";

        // Choose gradient & text color based on table type
        String bgGradient = cssClass.equals("passed") ? "linear-gradient(to right, #a8e063, #56ab2f)"   // green gradient
                          : cssClass.equals("skipped") ? "linear-gradient(to right, #fddb92, #d1fdff)" // yellow/blue gradient
                          : "linear-gradient(to right, #f85032, #e73827)";                               // red gradient

        String textColor = cssClass.equals("passed") ? "#1b4d1b"
                         : cssClass.equals("skipped") ? "#5a4f1b"
                         : "#4d0000";

        // Add table header with styled div
        writer.write("<div id='" + tableId + "' "
                + "style=\"background: " + bgGradient + "; "
                + "border: 2px solid #333; "
                + "border-radius: 10px; "
                + "padding: 12px 18px; "
                + "margin: 12px 0; "
                + "box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.1); "
                + "font-family: Arial, sans-serif; "
                + "text-align: left; "
                + "display: inline-block;\">");
        writer.write("<p style=\"font-size: 20px; font-weight: bold; color: " + textColor + "; margin: 0;\">" 
                + escapeHtml(title) + "</p></div>");

        
    
    writer.write("<table>");
    writer.write("<thead><tr><th>Class</th><th>Method</th><th>Start Time</th><th>Duration</th></tr></thead>");

    for (ISuite suite : suites) {
        for (ISuiteResult result : suite.getResults().values()) {
            ITestContext context = result.getTestContext();
            IResultMap resultMap;
            switch (cssClass) {
                case "failed": resultMap = context.getFailedTests(); break;
                case "skipped": resultMap = context.getSkippedTests(); break;
                default: resultMap = context.getPassedTests();
            }

            // Group results by class name
            Map<String, List<ITestResult>> resultsByClass = new TreeMap<>();
            for (ITestResult tr : resultMap.getAllResults()) {
                resultsByClass.computeIfAbsent(tr.getTestClass().getName(), k -> new ArrayList<>()).add(tr);
            }

            for (Map.Entry<String, List<ITestResult>> entry : resultsByClass.entrySet()) {
                String className = entry.getKey();
                List<ITestResult> testResults = entry.getValue();
                testResults.sort(Comparator.comparingLong(ITestResult::getStartMillis)); // optional sorting

                boolean firstRow = true;
                for (ITestResult tr : testResults) {
                    
                	String uniqueId = "m" + tr.getMethod().getMethodName() + "_" + tr.getStartMillis();
                	///String uniqueId = tr.getTestClass().getName() + "_" + tr.getMethod().getMethodName();
                    writer.write("<tr class='" + cssClass + "'>");
                    if (firstRow) {
                        writer.write("<td rowspan='" + testResults.size() + "'>" + escapeHtml(className) + "</td>");
                        firstRow = false;
                    }
                    writer.write("<td><a href='#m" + uniqueId + "'>" + escapeHtml(tr.getMethod().getMethodName()) + "</a></td>");
                    writer.write("<td>" + formatDate(tr.getStartMillis()) + "</td>");
                    writer.write("<td>" + formatDuration(tr.getEndMillis() - tr.getStartMillis()) + "</td>");
                    writer.write("</tr>");
                }
            }
        }
    }
    writer.write("</table>");
}

    
    
    private void generateDetails(Writer writer, List<ISuite> suites) throws IOException {
        writer.write("<h2>Test Method Details</h2>");
        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                writeDetailedResults(writer, context.getFailedTests(), "failed");
                writeDetailedResults(writer, context.getSkippedTests(), "skipped");
                writeDetailedResults(writer, context.getPassedTests(), "passed");
            }
        }
    }

    private void writeDetailedResultsOLDMATE(Writer writer, IResultMap results, String cssClass) throws IOException {
        for (ITestResult tr : results.getAllResults()) {
           /// String uniqueId = tr.getTestClass().getName() + "_" + tr.getMethod().getMethodName();
         
        	String uniqueId = "m" + tr.getMethod().getMethodName() + "_" + tr.getStartMillis();

        	// Choose gradient & text color based on test result type
        	String bgGradient = cssClass.equals("passed") ? "linear-gradient(to right, #a8e063, #56ab2f)"   // green
        	                  : cssClass.equals("skipped") ? "linear-gradient(to right, #fddb92, #d1fdff)" // yellow/blue
        	                  : "linear-gradient(to right, #f85032, #e73827)";                              // red

        	String textColor = cssClass.equals("passed") ? "#1b4d1b"
        	                 : cssClass.equals("skipped") ? "#5a4f1b"
        	                 : "#4d0000";

        	// Styled method header
        	writer.write("<div id='m" + uniqueId + "' "
        	        + "style=\"background: " + bgGradient + "; "
        	        + "border: 2px solid #333; "
        	        + "border-radius: 10px; "
        	        + "padding: 10px 15px; "
        	        + "margin: 10px 0; "
        	        + "box-shadow: 2px 2px 8px rgba(0, 0, 0, 0.1); "
        	        + "font-family: Arial, sans-serif; "
        	        + "display: inline-block;\">");

        	writer.write("<p style=\"font-size: 16px; font-weight: bold; color: " + textColor + "; margin: 0;\">"
        	        + escapeHtml(tr.getTestClass().getName()) + " ---> "
        	        + escapeHtml(tr.getMethod().getMethodName()) + "()</p></div>");

            
            
            writer.write("<div class='testDetails'>");
            writer.write("<table class='methodDetails'>");
//            
//            writer.write("<tr><td>Status:</td><td class='" + cssClass + "'>" + getStatus(tr.getStatus()) + "</td></tr>");
//            writer.write("<tr><td>Start Time:</td><td>" + formatDate(tr.getStartMillis()) + "</td></tr>");
//            writer.write("<tr><td>Duration:</td><td>" + formatDuration(tr.getEndMillis() - tr.getStartMillis()) + "</td></tr>");
//            
            writer.write("</table>");

            // Reporter.log output
            List<String> output = Reporter.getOutput(tr);
            if (!output.isEmpty()) {
                writer.write("<h4>Test Output</h4><div class='testOutput'>");
                for (String line : output) {
                    writer.write(line + "<br/>"); // no escaping so HTML stays formatted
                }
                writer.write("</div>");
            }

            if (tr.getThrowable() != null) {
                writer.write("<h4>Exception</h4><div class='stackTrace'>");
                String className = tr.getTestClass().getName();

                String[] lines = Utils.longStackTrace(tr.getThrowable(), true).split("\n");
                for (String line : lines) {
                    if (line.contains(className)) {
                        // Highlight only lines from the current test class
                        writer.write("<h5 style='background-color: yellow; font-weight: bold;'>" 
                                     + escapeHtml(line) + "</h5><br/>");
                    } else {
                        writer.write(escapeHtml(line) + "<br/>");
                    }
                }
                writer.write("</div>");
            }

            
            writer.write("</div>");
            writer.write("<hr style='margin:20px 0;'>"); // separator
            writer.write("</div>");
        }
    }

    private void writeDetailedResults(Writer writer, IResultMap results, String cssClass) throws IOException {
        // Count total runs per method
        Map<String, Integer> totalMethodRuns = new HashMap<>();
        for (ITestResult tr : results.getAllResults()) {
            String key = tr.getTestClass().getName() + "." + tr.getMethod().getMethodName();
            totalMethodRuns.put(key, totalMethodRuns.getOrDefault(key, 0) + 1);
        }

        // Track current iteration per method
        Map<String, Integer> currentIteration = new HashMap<>();

        for (ITestResult tr : results.getAllResults()) {
            String methodKey = tr.getTestClass().getName() + "." + tr.getMethod().getMethodName();
            int totalRuns = totalMethodRuns.get(methodKey);
            int iteration = currentIteration.getOrDefault(methodKey, 0) + 1;
            currentIteration.put(methodKey, iteration);

            String uniqueId = "m" + tr.getMethod().getMethodName() + "_" + tr.getStartMillis();

            // Choose gradient & text color
            String bgGradient = cssClass.equals("passed") ? "linear-gradient(to right, #a8e063, #56ab2f)"
                    : cssClass.equals("skipped") ? "linear-gradient(to right, #fddb92, #d1fdff)"
                    : "linear-gradient(to right, #f85032, #e73827)";

            String textColor = cssClass.equals("passed") ? "#1b4d1b"
                    : cssClass.equals("skipped") ? "#5a4f1b"
                    : "#4d0000";

            // Header text
            String headerText = escapeHtml(tr.getTestClass().getName()) + " ---> " + escapeHtml(tr.getMethod().getMethodName()) + "()";
            if (totalRuns > 1) {
                headerText += " --> Iteration No. " + iteration;
            }

            // Styled header box
            writer.write("<div id='m" + uniqueId + "' "
                    + "style=\"background: " + bgGradient + "; "
                    + "border: 2px solid #333; "
                    + "border-radius: 10px; "
                    + "padding: 10px 15px; "
                    + "margin: 10px 0; "
                    + "box-shadow: 2px 2px 8px rgba(0, 0, 0, 0.1); "
                    + "font-family: Arial, sans-serif; "
                    + "display: inline-block;\">");
            writer.write("<p style=\"font-size: 16px; font-weight: bold; color: " + textColor + "; margin: 0;\">" + headerText + "</p></div>");

            // Reporter output
            List<String> output = Reporter.getOutput(tr);
            if (!output.isEmpty()) {
                writer.write("<h4>Test Output</h4><div class='testOutput'>");
                for (String line : output) {
                    writer.write(line + "<br/>");
                }
                writer.write("</div>");
            }

            // Exception output
            if (tr.getThrowable() != null) {
                writer.write("<h4>Exception</h4><div class='stackTrace'>");
                String className = tr.getTestClass().getName();
                String[] lines = Utils.longStackTrace(tr.getThrowable(), true).split("\n");
                for (String line : lines) {
                    if (line.contains(className)) {
                        writer.write("<h5 style='background-color: yellow; font-weight: bold;'>" + escapeHtml(line) + "</h5><br/>");
                    } else {
                        writer.write(escapeHtml(line) + "<br/>");
                    }
                }
                writer.write("</div>");
            }

            writer.write("<hr style='margin:20px 0;'>");
        }
    }

    
    private String getStatus(int status) {
        switch (status) {
            case ITestResult.SUCCESS: return "PASSED";
            case ITestResult.FAILURE: return "FAILED";
            case ITestResult.SKIP: return "SKIPPED";
            default: return "UNKNOWN";
        }
    }

    private String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

    private String formatDate(long millis) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(millis));
    }

    private String formatDuration(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        return String.format("%02d hr %02d min %02d sec", hours, minutes, seconds);
    }

    private String getCss() {
        return "body{font-family:Arial,sans-serif;margin:20px;color:#333;}" +
                "h1{color:#444;}h2{color:#555;border-bottom:1px solid #ddd;padding-bottom:5px;}" +
                "table{border-collapse:collapse;width:100%;margin-bottom:20px;}" +
                "th,td{border:1px solid #ddd;padding:8px;text-align:left;}" +
                "th{background:#f2f2f2;}tr:hover{background:#f9f9f9;}" +
                ".passed{color:green;font-weight:bold;}.failed{color:red;font-weight:bold;}.skipped{color:orange;font-weight:bold;}" +
                ".testOutput{background:#f8f8f8;border:1px solid #ddd;padding:10px;}" +
                ".stackTrace{background:#ffe6e6;border:1px solid #ff0000;padding:10px;color:#a00;}" +
                ".testMethod{border:1px solid #ccc;padding:10px;margin-bottom:10px;border-radius:5px;}" +
                "a{text-decoration:none;color:#1976d2;}a:hover{text-decoration:underline;}";
    }
}