package com.aa.reports;

import org.testng.*;
import org.testng.reporters.EmailableReporter2;
import org.testng.xml.XmlSuite;

import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ClientFriendlyReporter extends EmailableReporter2 {

    private static final String REPORT_FILE = "client-friendly-report.html";
    private static final NumberFormat PERCENTAGE = new DecimalFormat("#0.00%");
    private static final String companyName = "Custome";
    
    public static String FILE_PATH="";
    // System Info Map - LinkedHashMap to preserve order
    private static final Map<String, String> systemInfoMap = new LinkedHashMap<>();

    static {
        try {

            
            String userName = System.getProperty("user.name").toUpperCase();
            String hostName = InetAddress.getLocalHost().getHostName();

            // Add entries here, can add/remove easily
            systemInfoMap.put("Project Name", "Book Keep Pro");
            systemInfoMap.put("OS", System.getProperty("os.name"));
            systemInfoMap.put("Executed By", userName);
            systemInfoMap.put("Executed Host", hostName);
            systemInfoMap.put("Created By", "Madhavi");
            systemInfoMap.put("Sprint No.", "2");

            //systemInfoMap.put("Java Runtime Version", System.getProperty("java.version"));

        } catch (Exception e) {
            e.printStackTrace();
            systemInfoMap.put("System Info", "Not Available");
        }
    }
    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        try {
            File outputFile = new File(outputDirectory, REPORT_FILE);
            
          String path= outputFile.getAbsolutePath();
           
          FILE_PATH=path;
          Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));

            writer.write("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>Test Execution Report</title>");
            writer.write("<style>" + getCss() + "</style>");
            writer.write("<script>" + getJs() + "</script>");
            writer.write("</head><body>");
           // writer.write("<h1>" + companyName + " Testing Execution Report</h1>");

         // Write System Info Table
            writeSystemInfoTable(writer);
            
            // Summary
            generateSummaryTable(writer, suites);

            // Result tables
            writeResultTable(writer, suites, "Passed Tests", "passed");
            writeResultTable(writer, suites, "Failed Tests", "failed");
            writeResultTable(writer, suites, "Skipped Tests", "skipped");

            writer.write("</body></html>");
            writer.close();

            System.out.println("Client Friendly Report generated at: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- Summary ----------------
    private void generateSummaryTable(Writer writer, List<ISuite> suites) throws IOException {
        int totalPassed = 0, totalFailed = 0, totalSkipped = 0;
        long totalTime = 0;

        writer.write("<h2 id=\"summary\">Summary</h2>");
        writer.write("<table>");
        writer.write("<thead><tr><th>Total</th><th>Passed</th><th>Failed</th><th>Skipped</th><th>Duration</th></tr></thead>");

        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                totalPassed += context.getPassedTests().size();
                totalFailed += context.getFailedTests().size();
                totalSkipped += context.getSkippedTests().size();
                totalTime += context.getEndDate().getTime() - context.getStartDate().getTime();
            }
        }

        int total = totalPassed + totalFailed + totalSkipped;

        writer.write("<tr>");
        writer.write("<td>" + total + "</td>");
        writer.write("<td class='passed'>" + totalPassed + " (" + PERCENTAGE.format(totalPassed / (double) total) + ")</td>");
        writer.write("<td class='failed'>" + totalFailed + " (" + PERCENTAGE.format(totalFailed / (double) total) + ")</td>");
        writer.write("<td class='skipped'>" + totalSkipped + " (" + PERCENTAGE.format(totalSkipped / (double) total) + ")</td>");
        writer.write("<td>" + formatDuration(totalTime) + "</td>");
        writer.write("</tr>");
        writer.write("</table>");
    }

    // ---------------- Tables ----------------
    private void writeResultTable(Writer writer, List<ISuite> suites, String title, String cssClass) throws IOException {
        writer.write("<h2>" + title + "</h2>");
        writer.write("<table>");
        writer.write("<thead><tr><th>TestCaseID</th><th>Description</th><th>Status</th><th>Duration</th><th>Details</th></tr></thead>");

        for (ISuite suite : suites) {
            for (ISuiteResult result : suite.getResults().values()) {
                ITestContext context = result.getTestContext();
                IResultMap resultMap;
                switch (cssClass) {
                    case "failed": resultMap = context.getFailedTests(); break;
                    case "skipped": resultMap = context.getSkippedTests(); break;
                    default: resultMap = context.getPassedTests();
                }

                for (ITestResult tr : resultMap.getAllResults()) {
                    String testCaseId = tr.getMethod().getMethodName();
                    String description = (tr.getMethod().getDescription() != null && !tr.getMethod().getDescription().isEmpty())
                            ? tr.getMethod().getDescription()
                            : "No Description Provided";
                    String uniqueId = "m" + tr.getMethod().getMethodName() + "_" + tr.getStartMillis();
                    long duration = tr.getEndMillis() - tr.getStartMillis();

                    writer.write("<tr class='" + cssClass + "'>");
                    writer.write("<td>" + escapeHtml(testCaseId) + "</td>");
                    writer.write("<td>" + escapeHtml(description) + "</td>");
                    writer.write("<td class='" + cssClass + "'>" + cssClass.toUpperCase() + "</td>");
                    writer.write("<td>" + formatDuration(duration) + "</td>");
                    writer.write("<td><button onclick=\"toggleDetails('" + uniqueId + "')\">View Details</button></td>");
                    writer.write("</tr>");

                    // hidden details row
                    writer.write("<tr id='" + uniqueId + "' style='display:none;'><td colspan='5'>");
                    writer.write("<div class='detailsBox'>");

                    // Logs
                    List<String> output = Reporter.getOutput(tr);
                    if (!output.isEmpty()) {
                        writer.write("<h4>Logs:</h4><div class='testOutput'>");
                        for (String line : output) {
                            writer.write(line + "<br/>");
                        }
                        writer.write("</div>");
                    }

                    // Screenshot hint
                    if (!output.isEmpty()) {
                        for (String line : output) {
                            if (line.contains(".png")) {
                                writer.write("<h4>Screenshot:</h4>");
                                writer.write(line); // line should have <img src=...>
                            }
                        }
                    }

                    // Error / Exception
                    if (tr.getThrowable() != null) {
                        writer.write("<h4>Error:</h4><div class='stackTrace'>");
                        writer.write("<pre>" + escapeHtml(tr.getThrowable().toString()) + "</pre>");
                        writer.write("</div>");
                    }

                    writer.write("</div></td></tr>");
                }
            }
        }
        writer.write("</table>");
    }

    // ---------------- Utility ----------------
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
        return "body{font-family:Arial,sans-serif;margin:20px;color:#333;}"
                + "h1{color:#444;}h2{color:#555;border-bottom:1px solid #ddd;padding-bottom:5px;}"
                + "table{border-collapse:collapse;width:100%;margin-bottom:20px;}"
                + "th,td{border:1px solid #ddd;padding:8px;text-align:left;}th{background:#f2f2f2;}"
                + "tr:hover{background:#f9f9f9;}"
                + ".passed{color:green;font-weight:bold;}.failed{color:red;font-weight:bold;}.skipped{color:orange;font-weight:bold;}"
                + ".testOutput{background:#f8f8f8;border:1px solid #ddd;padding:10px;margin:5px 0;}"
                + ".stackTrace{background:#ffe6e6;border:1px solid #ff0000;padding:10px;color:#a00;}"
                + ".detailsBox{background:#f9f9f9;border:1px solid #ccc;padding:10px;margin-top:10px;border-radius:5px;}";
    }

    private String getJs() {
        return "function toggleDetails(id){"
                + "var e=document.getElementById(id);"
                + "if(e.style.display==='none'){e.style.display='table-row';}"
                + "else{e.style.display='none';}"
                + "}";
    }
    
    
    private void writeSystemInfoTable(Writer writer) throws IOException {
        writer.write("<h2>System/Environment</h2>");
        writer.write("<table>");
        writer.write("<thead><tr><th>NAME</th><th>VALUE</th></tr></thead>");
        for (Map.Entry<String, String> entry : systemInfoMap.entrySet()) {
            writer.write("<tr><td>" + escapeHtml(entry.getKey()) + "</td><td>" + escapeHtml(entry.getValue()) + "</td></tr>");
        }
        writer.write("</table><br/>");
    }
}
