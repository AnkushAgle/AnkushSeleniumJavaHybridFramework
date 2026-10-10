package com.test.singletoon;

import org.testng.TestNG;
import com.test.demoqa.YouTube;
import com.aa.reports.CustomEmailableReporter; // import your listener

public class YouTubeRunner {

    public static void main(String[] args) {
        System.out.println("📦 Starting TestNG execution from Runnable JAR...");

        // Create TestNG object
        TestNG testng = new TestNG();

        // Set the test class(es) to run
        testng.setTestClasses(new Class[] { YouTube.class });

        // Optional: suite/test naming
        testng.setDefaultSuiteName("YouTube Automation Suite");
        testng.setDefaultTestName("YouTube Search & Play Test");

        // ✅ Add your custom listener
        testng.addListener(new CustomEmailableReporter());

        // Run the test
        testng.run();

        System.out.println("✅ TestNG execution finished.");
    }
}
