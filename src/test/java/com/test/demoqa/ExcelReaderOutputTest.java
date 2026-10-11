package com.test.demoqa;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.aa.utility.ExcelReader;

public class ExcelReaderOutputTest {

    @Test
    public void createsResultWorkbookWhenOutputDirectoryDoesNotExist() throws IOException {
        Path tempDirectory = Files.createTempDirectory("excel-reader-output-test");
        Path resultFile = tempDirectory.resolve("ResultExcel").resolve("RESULT.xlsx");

        try {
            try (ExcelReader excelReader = new ExcelReader(resultFile.toString(), "YES")) {
                Assert.assertTrue(Files.isDirectory(resultFile.getParent()));
                Assert.assertTrue(Files.isRegularFile(resultFile));
                Assert.assertTrue(excelReader.setCellData("Result", "Test Case Status", 1, "PASS"));
                Assert.assertEquals(excelReader.getCellData("Result", "Test Case Status", 1), "PASS");

            }
        } finally {
            try (var paths = Files.walk(tempDirectory)) {
                paths.sorted(Comparator.reverseOrder()).forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (IOException e) {
                        throw new IllegalStateException("Unable to clean up test output: " + path, e);
                    }
                });
            }
        }
    }
}
