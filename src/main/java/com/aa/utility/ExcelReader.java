package com.aa.utility;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFDataValidation;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFPicture;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.coordinates.WebDriverCoordsProvider;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;


public class ExcelReader implements AutoCloseable {
    public String path;
    public FileInputStream fis = null;
    public FileOutputStream fileOut = null;
    private Workbook workbook = null;
    private Sheet sheet = null;
    private Row row = null;
    private Cell cell = null;

    @Override
    public void close() throws IOException {
        IOException closeException = null;
        try {
            if (fileOut != null) {
                fileOut.close();
            }
        } catch (IOException e) {
            closeException = e;
        }
        try {
            if (fis != null) {
                fis.close();
            }
        } catch (IOException e) {
            if (closeException == null) {
                closeException = e;
            } else {
                closeException.addSuppressed(e);
            }
        }
        try {
            if (workbook != null) {
                workbook.close();
            }
        } catch (IOException e) {
            if (closeException == null) {
                closeException = e;
            } else {
                closeException.addSuppressed(e);
            }
        }
        if (closeException != null) {
            throw closeException;
        }
    }

    public ExcelReader(String path) {
        String exceptionMAssageMate = "";
        this.path = path;
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheetAt(0);
            fis.close();
        } catch (Exception e) {
            exceptionMAssageMate = e.getMessage();
            e.printStackTrace();
        } finally {
            System.out.println(exceptionMAssageMate);
        }

        System.out.println("========================================================");
        System.out.println("Excel Sheet Path :: " + path);
        System.out.println("========================================================");
    }

    public void logAndCreatePieChartInExcelSheet(String newSheetName, Map<String, Integer> inputMap) {
        try {
            // Note: Chart creation is only supported in XSSF (xlsx format)
            if (!(workbook instanceof XSSFWorkbook)) {
                System.out.println("Chart creation is only supported for .xlsx files");
                return;
            }

            FileInputStream fis = new FileInputStream(this.path);
            XSSFWorkbook wb = (XSSFWorkbook) WorkbookFactory.create(fis);

            // Create or get the sheet
            XSSFSheet sheet = wb.getSheet(newSheetName);
            if (sheet == null) {
                sheet = wb.createSheet(newSheetName);
            }

            int rowIndex = 0;
            // Write the map data into the Excel sheet
            for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(entry.getKey());
                row.createCell(1).setCellValue(entry.getValue());
            }

            // Create the pie chart
            XSSFDrawing drawing = sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = drawing.createAnchor(0, 0, 0, 0, 3, 1, 10, 20);

            org.apache.poi.xssf.usermodel.XSSFChart chart = drawing.createChart(anchor);
            chart.setTitleText("Test Summary");
            chart.setTitleOverlay(false);

            org.apache.poi.xddf.usermodel.chart.XDDFChartLegend legend = chart.getOrAddLegend();
            legend.setPosition(org.apache.poi.xddf.usermodel.chart.LegendPosition.RIGHT);

            org.apache.poi.xddf.usermodel.chart.XDDFDataSource<String> categories = org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory.fromStringCellRange(
                sheet, new CellRangeAddress(0, inputMap.size() - 1, 0, 0));
            org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource<Double> values = org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory.fromNumericCellRange(
                sheet, new CellRangeAddress(0, inputMap.size() - 1, 1, 1));

            org.apache.poi.xddf.usermodel.chart.XDDFChartData data = chart.createData(
                org.apache.poi.xddf.usermodel.chart.ChartTypes.PIE, null, null);
            data.setVaryColors(true);
            org.apache.poi.xddf.usermodel.chart.XDDFChartData.Series series = data.addSeries(categories, values);
            series.setTitle("Test Result Breakdown", null);
            chart.plot(data);

            fis.close();
            FileOutputStream fileOut = new FileOutputStream(this.path);
            wb.write(fileOut);
            fileOut.close();
            wb.close();

            System.out.println("✅ Pie chart successfully created in '" + newSheetName + "' sheet.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ExcelReader(String path, String DoYouWantsToResultInExcelSheet) {
        if (DoYouWantsToResultInExcelSheet.equalsIgnoreCase("NO")) {
            return;
        }

        this.path = path;

        try {
            Path resultFile = Path.of(path).toAbsolutePath().normalize();
            Path resultDirectory = resultFile.getParent();
            if (resultDirectory != null) {
                Files.createDirectories(resultDirectory);
            }

            if (Files.notExists(resultFile)) {
                createNewExcelFile(resultFile.toString());
            }

            loadWorkbook();
        } catch (Exception e) {
            if (e.getMessage() != null && e.getMessage().contains("Truncated ZIP file")) {
                try {
                    Files.deleteIfExists(Path.of(path));
                    createNewExcelFile(path);
                    loadWorkbook();
                } catch (Exception recoveryException) {
                    throw new IllegalStateException("Unable to recreate result Excel file: " + path,
                            recoveryException);
                }
            } else {
                throw new IllegalStateException("Unable to initialize result Excel file: " + path, e);
            }
        }
    }

    private void loadWorkbook() throws IOException {
        try (FileInputStream input = new FileInputStream(path)) {
            workbook = WorkbookFactory.create(input);
            sheet = workbook.getSheetAt(0);
        }
    }

    public boolean isFileValid(String filePath) {
        File file = new File(filePath);

        // Check if file exists
        if (!file.exists()) {
            System.out.println("File does not exist: " + filePath);
            return false;
        }

        // Allowed file extensions
        String allowedExtensions = ".xls,.xlsx,.pdf,.jpg,.jpeg,.png,.doc,.docx,.PNG,.JPEG,.JPG,.PDF,.XLS,.XLSX,.DOC,.DOCX,.dwg,.DWG,.zip,.ZIP,.rar,.RAR";

        // Extract file extension
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf(".");

        if (lastIndex == -1) {
            System.out.println("File has no extension: " + filePath);
            return false;
        }

        String fileExtension = fileName.substring(lastIndex);

        // Check if extension is allowed
        if (allowedExtensions.contains(fileExtension)) {
            return true;
        } else {
            System.out.println("Invalid file extension: " + fileExtension);
            return false;
        }
    }

    // returns true if data is set successfully else false
    public boolean setCellData(String sheetName, String colName, int rowNum, String data) {
        if (isColumnExist(sheetName, colName)) {
            // Library.dialogMassageLog("Column Already Exist mate");
        } else {
            addColumn(sheetName, colName);
        }

        String alraedyData = getCellData(sheetName, colName, rowNum);

        if (alraedyData.equals(data)) {
            return true;
        }

        System.out.println("DATA FOR columnname==" + colName + "==" + data);

        rowNum = rowNum + 1;

        CellStyle style = workbook.createCellStyle();
        if (data.toUpperCase().contains("ON") || data.contains("OFF")) {
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.DARK_BLUE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            
            org.apache.poi.ss.usermodel.Font font = workbook.createFont();

            if (data.equalsIgnoreCase("ON")) {
                font.setColor(IndexedColors.GREEN.getIndex());  // Green text for "ON"
            } else if (data.equalsIgnoreCase("OFF")) {
                font.setColor(IndexedColors.RED.getIndex());  // Red text for "OFF"
            }
            style.setFont(font);
        }

        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return false;

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);

            try {
                cell.setCellStyle(style);
            } catch (Exception e) {
                // TODO: handle exception
            }

            System.out.println("Data Succesfully Write " + data);
            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // Function to write data from a String array to cells starting from a specific row
    public boolean setCellDataFromArray(String sheetName, String colName, int rowNum, String[] dataList) {
        if (isColumnExist(sheetName, colName)) {
            // Library.dialogMassageLog("Column Already Exist mate");
        } else {
            addColumnDropDown(sheetName, colName);
        }

        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            if (rowNum <= 0) {
                return false;
            }

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;

            if (index == -1) {
                return false;
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);

            // Identify the column index based on column name
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName)) {
                    colNum = i;
                    break;
                }
            }

            if (colNum == -1) {
                return false;
            }

            sheet.autoSizeColumn(colNum);

            // Write data from the array to the column, starting at rowNum
            for (int i = 0; i < dataList.length; i++) {
                int currentRow = rowNum + i; // Adjust for 0-based index
                row = sheet.getRow(currentRow);
                if (row == null) {
                    row = sheet.createRow(currentRow);
                }

                cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum);
                }

                cell.setCellValue(dataList[i]);
                System.out.println("Data written to row " + (currentRow + 1) + ": " + dataList[i]);
            }

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // Function to delete corrupted file
    private void deleteCorruptedFile() {
        File file = new File(path);
        if (file.exists()) {
            System.out.println("Deleting corrupted file: " + path);
            boolean deleted = file.delete();
            if (deleted) {
                System.out.println("Corrupted file deleted successfully.");
            } else {
                System.err.println("Failed to delete the corrupted file.");
            }
        }
    }

    // Function to create a new Excel file
    public void createNewExcelFile(String newFilePath) {
        Path resultFile = Path.of(newFilePath).toAbsolutePath().normalize();
        Path resultDirectory = resultFile.getParent();
        try {
            if (resultDirectory != null) {
                Files.createDirectories(resultDirectory);
            }

            try (Workbook newWorkbook = newFilePath.toLowerCase().endsWith(".xlsx")
                    ? new XSSFWorkbook()
                    : new org.apache.poi.hssf.usermodel.HSSFWorkbook();
                 FileOutputStream fileOut = new FileOutputStream(resultFile.toFile())) {
                newWorkbook.createSheet("Result").createRow(0);
                newWorkbook.write(fileOut);
            }
            System.out.println("New Excel file created at: " + resultFile);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create result Excel file: " + resultFile, e);
        }
    }

    public boolean addScreenshotFromPathToSheetByColumnName(String screenshotPath, String sheetName, String columnName, int rowNum) {
        if (screenshotPath.equals("") || true) {
            return false;
        }

        addColumn(sheetName, columnName);
        try {
            // Load the screenshot file as byte array
            File screenshotFile = new File(screenshotPath);
            byte[] screenshotBytes = Files.readAllBytes(screenshotFile.toPath());

            // Load the Excel workbook and sheet
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet " + sheetName + " does not exist.");
                return false;
            }

            // Find the column index by header name
            row = sheet.getRow(0);  // Assuming headers are in the first row (index 0)
            int colNum = -1;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) {
                System.out.println("Column " + columnName + " does not exist in the sheet.");
                return false;
            }

            // Add screenshot to the specified cell
            int pictureIdx = workbook.addPicture(screenshotBytes, Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            Picture picture = drawing.createPicture(anchor, pictureIdx);
            
            // Resize to fit the cell (only available in XSSF)
            if (picture instanceof XSSFPicture) {
                ((XSSFPicture) picture).resize();
            }

            // Save workbook with changes
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

            return true; // Successfully added screenshot
        } catch (IOException e) {
            e.printStackTrace();
            return false; // Failed to add screenshot
        }
    }

    public boolean addScreenshotToSheetByColumnNameAshot(WebDriver driver, String sheetName, String columnName, int rowNum) {
        addColumn(sheetName, columnName);
        try {
            // Capture full-page screenshot using AShot
            Screenshot screenshot = new AShot()
                    .shootingStrategy(ShootingStrategies.viewportPasting(1000))  // Full-page strategy with 1 second interval
                    .coordsProvider(new WebDriverCoordsProvider())
                    .takeScreenshot(driver);

            BufferedImage image = screenshot.getImage();

            // Convert BufferedImage to byte array (PNG format)
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            byte[] screenshotBytes = baos.toByteArray();

            // Load the sheet
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet " + sheetName + " does not exist.");
                return false;
            }

            // Find the column index by header name
            row = sheet.getRow(0);  // Assuming headers are in the first row (index 0)
            int colNum = -1;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) {
                System.out.println("Column " + columnName + " does not exist in the sheet.");
                return false;
            }

            // Add screenshot to the specified cell
            int pictureIdx = workbook.addPicture(screenshotBytes, Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            Picture picture = drawing.createPicture(anchor, pictureIdx);
            
            // Resize to fit the cell (only available in XSSF)
            if (picture instanceof XSSFPicture) {
                ((XSSFPicture) picture).resize();
            }

            // Save workbook with changes
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

            return true; // Successfully added screenshot
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Failed to add screenshot
        }
    }

    public boolean addScreenshotToSheetByColumnName(WebDriver driver, String sheetName, String columnName, int rowNum) {
        addColumn(sheetName, columnName);
        try {
            // Capture screenshot as byte array
            byte[] screenshotBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

            // Load the sheet
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("Sheet " + sheetName + " does not exist.");
                return false;
            }

            // Find the column index by header name
            row = sheet.getRow(0);  // Assuming headers are in the first row (index 0)
            int colNum = -1;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().equalsIgnoreCase(columnName)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) {
                System.out.println("Column " + columnName + " does not exist in the sheet.");
                return false;
            }

            // Add screenshot to the specified cell
            int pictureIdx = workbook.addPicture(screenshotBytes, Workbook.PICTURE_TYPE_PNG);
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = workbook.getCreationHelper().createClientAnchor();
            anchor.setCol1(colNum);
            anchor.setRow1(rowNum);
            Picture picture = drawing.createPicture(anchor, pictureIdx);
            
            // Resize to fit the cell (only available in XSSF)
            if (picture instanceof XSSFPicture) {
                ((XSSFPicture) picture).resize();
            }

            // Save workbook with changes
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            workbook.close();
            fileOut.close();

            return true; // Successfully added screenshot
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Failed to add screenshot
        }
    }

    // returns the row count in a sheet
    public int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }
    }

    // Function to fetch all sheet names and return as String array
    public String[] getSheetNames() {
        int sheetCount = workbook.getNumberOfSheets();
        String[] sheetNames = new String[sheetCount];

        for (int i = 0; i < sheetCount; i++) {
            sheetNames[i] = workbook.getSheetName(i);
        }

        return sheetNames;
    }

    // returns the data from a cell
    public String getCellData(String sheetName, String colName, int rowNum) {
        boolean sheetexiatornot = isSheetExist(sheetName);

        if (sheetexiatornot == false) {
            try {
                // Library.massagelog("Excel Sheet Does Not Present :: "+sheetName);
            } catch (Exception ti) {
            }
        }

        rowNum = rowNum + 1;

        try {
            if (rowNum <= 0)
                return "";

            int index = workbook.getSheetIndex(sheetName);
            int col_Num = -1;
            if (index == -1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                    col_Num = i;
            }
            if (col_Num == -1)
                return "NODATAPRESENT";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "NODATAPRESENT";
            cell = row.getCell(col_Num);

            if (cell == null)
                return "NODATAPRESENT";

            if (cell.getCellType() == CellType.STRING) {
                if (cell.getStringCellValue().trim().equals("")) {
                    return "NODATAPRESENT";
                } else {
                    return cell.getStringCellValue().trim();
                }
            } else if (cell.getCellType() == CellType.NUMERIC) {
                DataFormatter dataFormatter = new DataFormatter();
                String valuemate = dataFormatter.formatCellValue(cell);

                int value = (int) cell.getNumericCellValue();

                System.out.println("Integer Value is equals to " + value);
                String cellText = String.valueOf(value).trim();

                cellText = valuemate;

                System.out.println("Converted ");

                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    System.out.println("It is Found that Entered Value Is Date Type So converting It into Date string ");
                    // Format the date value into a readable format.
                    String formattedDate = new SimpleDateFormat("dd/MM/yyyy").format(date);

                    // Print the formatted date to the console.
                    System.out.println("Original Date string is " + formattedDate);
                    return formattedDate;
                } else {
                    return cellText.trim();// Only trim is added change if any problem occurs
                }
            } else if (cell.getCellType() == CellType.FORMULA) {
                DataFormatter formatter = new DataFormatter();
                String valuemate = formatter.formatCellValue(cell).toString();
                System.out.println(valuemate);

                try {
                    double d = cell.getNumericCellValue();
                    int value = (int) d;
                    String str = Integer.toString(value);
                    valuemate = str;
                } catch (Exception tai) {
                    // Library.errorMassageLogRedColor(tai.getMessage());
                }
                try {
                    valuemate = cell.getStringCellValue().trim();
                    String CellTypeof = CellType.FORMULA.toString();
                } catch (Exception t) {
                    // Library.errorMassageLogRedColor(t.getMessage());
                }
                return valuemate.trim();
            } else if (cell.getCellType() == CellType.BLANK)
                return "NODATAPRESENT";
            else
                return String.valueOf(cell.getBooleanCellValue()).trim();
            // only trim method is added change if any problem occurs
        } catch (Exception e) {
            e.printStackTrace();
            return "NODATAPRESENT";
            // return "row "+rowNum+" or column "+colName +" does not exist in xls";
        }
    }

    public String returnCurrentUsedFilePath() {
        return path;
    }

    // returns the data from a cell
    public String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            if (rowNum <= 0)
                return "";

            int index = workbook.getSheetIndex(sheetName);

            if (index == -1)
                return "";

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                return "";
            cell = row.getCell(colNum);
            if (cell == null)
                return "";

            if (cell.getCellType() == CellType.STRING)
                return cell.getStringCellValue();
            else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {
                String cellText = String.valueOf(cell.getNumericCellValue());
                return cellText;
            } else if (cell.getCellType() == CellType.BLANK)
                return "";
            else
                return String.valueOf(cell.getBooleanCellValue());
        } catch (Exception e) {
            e.printStackTrace();
            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }

    // Adds a dropdown (data validation list) to a specific column in an Excel sheet.
    public boolean addDropdownToExcelSheet(String sheetName, String columnName, int rowStart, String[] dropdownValues) {
        if (true) {
            return true;
        }
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                System.out.println("Sheet " + sheetName + " does not exist.");
                return false;
            }

            sheet = workbook.getSheetAt(index);

            // Check if the column exists, if not, add it
            if (!isColumnExist(sheetName, columnName)) {
                addColumn(sheetName, columnName);
            }

            // Find the column number
            row = sheet.getRow(0);
            int colNum = -1;
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(columnName)) {
                    colNum = i;
                    break;
                }
            }
            if (colNum == -1) {
                System.out.println("Column " + columnName + " not found.");
                return false;
            }

            // Create a dropdown constraint directly from the array of values
            DataValidationHelper validationHelper = sheet.getDataValidationHelper();
            DataValidationConstraint constraint = validationHelper.createExplicitListConstraint(dropdownValues);

            // Set the dropdown range (column and rowStart)
            CellRangeAddressList addressList = new CellRangeAddressList(rowStart - 1, rowStart - 1, colNum, colNum);
            DataValidation validation = validationHelper.createValidation(constraint, addressList);

            // Suppress the dropdown arrow and enable error messages
            if (validation instanceof XSSFDataValidation) {
                validation.setSuppressDropDownArrow(false);
                validation.setShowErrorBox(true);
            }

            sheet.addValidationData(validation);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            fis.close();

            System.out.println("Dropdown successfully added to column " + columnName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setCellData(String sheetName, String colName, int rowNum, String data, String url) {
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return false;

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equalsIgnoreCase(colName))
                    colNum = i;
            }

            if (colNum == -1)
                return false;
            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            org.apache.poi.ss.usermodel.CreationHelper createHelper = workbook.getCreationHelper();

            // cell style for hyperlinks
            CellStyle hlink_style = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font hlink_font = workbook.createFont();
            hlink_font.setUnderline(org.apache.poi.ss.usermodel.Font.U_SINGLE);
            hlink_font.setColor(IndexedColors.BLUE.getIndex());
            hlink_style.setFont(hlink_font);
            // hlink_style.setWrapText(true);

            org.apache.poi.ss.usermodel.Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
            link.setAddress(url);
            cell.setHyperlink(link);
            cell.setCellStyle(hlink_style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if sheet is created successfully else false
    public boolean addSheet(String sheetname) {
        if (isSheetExist(sheetname)) {
            // Library.dialogMassageLog("SheetName :: "+sheetname);
            // Library.dialogMassageLog("Sheet Already Exist In ExcelFilePath :: "+path);
            return true;
        }

        // Replace invalid characters
        String invalidChars = "\\/?*[]";
        for (char c : invalidChars.toCharArray()) {
            sheetname = sheetname.replace(String.valueOf(c), "_"); // Replace with an underscore or any desired character
        }

        // Trim sheet name if it's longer than 31 characters
        if (sheetname.length() > 31) {
            sheetname = sheetname.substring(0, 31);
        }

        FileOutputStream fileOut;
        try {
            Sheet sheet = workbook.createSheet(sheetname);

            // Set the tab color (only for XSSF)
            if (workbook instanceof XSSFWorkbook) {
                String hexcode = ColorUtils.generateRandomHexColor();
                XSSFColor tabColor = new XSSFColor(Color.decode(hexcode), null); // Converts hex color code to color
                ((XSSFSheet) sheet).setTabColor(tabColor);
            }

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            String massagemate = e.getMessage();
            try {
                // Library.dialogMassageLog(massagemate);
            } catch (Exception e2) {
                // TODO: handle exception
            }
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if the sheet is created successfully, else returns false
    public boolean addSheet(String sheetName, String colorHex) {
        if (isSheetExist(sheetName)) {
            return true;
        }

        FileOutputStream fileOut;
        try {
            // Create a new sheet
            Sheet sheet = workbook.createSheet(sheetName);

            // Set the tab color (only for XSSF)
            if (workbook instanceof XSSFWorkbook) {
                XSSFColor tabColor = new XSSFColor(Color.decode(colorHex), null); // Converts hex color code to color
                ((XSSFSheet) sheet).setTabColor(tabColor);
            }

            // Write changes to the file
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
            try {
                // Library.Savemassagelog("Sheet Added Succesfully :: "+sheetName);
            } catch (Exception e) {
                // TODO: handle exception
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // returns true if sheet is removed successfully else false if sheet does not exist
    public boolean removeSheet(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return false;

        FileOutputStream fileOut;
        try {
            workbook.removeSheetAt(index);
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    // returns true if column is created successfully
    public boolean addColumnDropDown(String sheetName, String colName) {
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                return false;

            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            if (row == null)
                row = sheet.createRow(0);

            // Check if column name already exists
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell existingCell = row.getCell(i);
                if (existingCell != null && existingCell.getStringCellValue().equals(colName)) {
                    // Column name already exists, return false
                    return false;
                }
            }

            // If the column name doesn't exist, proceed to create it
            Cell newCell;
            if (row.getLastCellNum() == -1)
                newCell = row.createCell(0);
            else
                newCell = row.createCell(row.getLastCellNum());

            newCell.setCellValue(colName);
            newCell.setCellStyle(style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // removes a column and all the contents
    public boolean removeColumn(String sheetName, int colNum) {
        try {
            if (!isSheetExist(sheetName))
                return false;
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            sheet = workbook.getSheet(sheetName);
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_40_PERCENT.getIndex());
            org.apache.poi.ss.usermodel.CreationHelper createHelper = workbook.getCreationHelper();
            style.setFillPattern(FillPatternType.NO_FILL);

            for (int i = 0; i < getRowCount(sheetName); i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    cell = row.getCell(colNum);
                    if (cell != null) {
                        cell.setCellStyle(style);
                        row.removeCell(cell);
                    }
                }
            }
            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // find whether sheets exists
    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1)
                return false;
            else
                return true;
        } else
            return true;
    }

    // Returns true if the column exists in the specified sheet
    public boolean isColumnExist(String sheetName, String colName) {
        try {
            // Open the Excel file
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            // Check if the sheet exists
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                return false; // Sheet doesn't exist
            }

            // Get the sheet and the first row
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0); // Assuming the first row contains column headers
            if (row == null) {
                return false; // No columns in the sheet
            }

            // Iterate through the columns in the first row
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell cell = row.getCell(i);
                if (cell != null && cell.getStringCellValue().trim().equalsIgnoreCase(colName)) {
                    return true; // Column exists
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false in case of an exception
        } finally {
            try {
                if (fis != null) {
                    fis.close(); // Ensure file input stream is closed
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false; // Column not found
    }

    // returns number of columns in a sheet
    public int getColumnCount(String sheetName) {
        // check if sheet exists
        if (!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null)
            return -1;

        return row.getLastCellNum();
    }

    // String sheetName, String testCaseName,String keyword ,String URL,String message
    public boolean addHyperLink(String sheetName, String screenShotColName, String testCaseName, int index, String url, String message) {
        url = url.replace('\\', '/');
        if (!isSheetExist(sheetName))
            return false;

        sheet = workbook.getSheet(sheetName);

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, 0, i).equalsIgnoreCase(testCaseName)) {
                setCellData(sheetName, screenShotColName, i + index, message, url);
                break;
            }
        }

        return true;
    }

    public int getCellRowNum(String sheetName, String colName, String cellValue) {
        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;
    }

    // returns true if data is set successfully else false
    public boolean setCellDataforpoedittemp(String sheetName, String colName, int rowNum, String data) {
        System.out.println("DATA FOR columnname==" + colName + "==" + data);

        colName = "E" + colName;
        rowNum = rowNum + 1;
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);

            if (rowNum <= 0)
                return false;

            int index = workbook.getSheetIndex(sheetName);
            int colNum = -1;
            if (index == -1)
                return false;

            sheet = workbook.getSheetAt(index);

            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                if (row.getCell(i).getStringCellValue().trim().equals(colName))
                    colNum = i;
            }
            if (colNum == -1)
                return false;

            sheet.autoSizeColumn(colNum);
            row = sheet.getRow(rowNum - 1);
            if (row == null)
                row = sheet.createRow(rowNum - 1);

            cell = row.getCell(colNum);
            if (cell == null)
                cell = row.createCell(colNum);

            cell.setCellValue(data);
            System.out.println("Data Succesfully Write " + data);
            fileOut = new FileOutputStream(path);

            workbook.write(fileOut);

            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String[] getColumnData(String sheetName, String colName) {
        if (!isSheetExist(sheetName)) {
            // Library.massagelog("Excel Sheet Does Not Present :: " + sheetName);
            return new String[0]; // Return empty array if sheet does not exist.
        }

        Sheet sheet = workbook.getSheet(sheetName);
        int colNum = -1;

        // Assuming the first row contains headers
        Row firstRow = sheet.getRow(0);
        if (firstRow == null) {
            return new String[0];
        }

        for (Cell cell : firstRow) {
            if (cell.getStringCellValue().trim().equals(colName.trim())) {
                colNum = cell.getColumnIndex();
                break;
            }
        }

        if (colNum == -1) {
            // Library.massagelog("Column '" + colName + "' does not exist in the sheet: " + sheetName);
            return new String[0]; // Return empty array if column does not exist.
        }

        List<String> dataList = new ArrayList<>();
        int lastRowNum = sheet.getLastRowNum();

        for (int i = 0; i <= lastRowNum; i++) {
            if (i == 0) {
                continue;
            }
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell cell = row.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                String cellValue = cellToString(cell);
                // Only add non-empty values
                if (!cellValue.equals("")) {
                    dataList.add(cellValue);
                }
            }
        }

        // Convert the List to a String array before returning
        String[] data = new String[dataList.size()];
        data = dataList.toArray(data);
        return data;
    }

    private String cellToString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString(); // You might want to format this.
                } else {
                    return Double.toString(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return Boolean.toString(cell.getBooleanCellValue());
            case FORMULA:
                // Handle formula cells; the result type of the formula evaluation is checked.
                org.apache.poi.ss.usermodel.FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
                CellValue cellValue = evaluator.evaluate(cell);
                switch (cellValue.getCellType()) {
                    case STRING:
                        return cellValue.getStringValue();
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            return cell.getDateCellValue().toString();
                        } else {
                            return Double.toString(cellValue.getNumberValue());
                        }
                    case BOOLEAN:
                        return Boolean.toString(cellValue.getBooleanValue());
                    default:
                        return "";
                }
            default:
                return "";
        }
    }

    // returns true if column is created successfully
    public boolean addColumn(String sheetName, String colName) {
        try {
            fis = new FileInputStream(path);
            workbook = WorkbookFactory.create(fis);
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1)
                return false;

            CellStyle style = workbook.createCellStyle();
            if (colName.toUpperCase().contains("ENTRY") || colName.contains("Master")) {
                style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.YELLOW.getIndex());
                style.setFillPattern(FillPatternType.LESS_DOTS);
            }

            if (colName.toUpperCase().contains("MASSAGE")) {
                style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREEN.getIndex());
                style.setFillPattern(FillPatternType.DIAMONDS);
            } else {
                style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_ORANGE.getIndex());
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }

            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            if (row == null)
                row = sheet.createRow(0);

            // Check if column name already exists
            for (int i = 0; i < row.getLastCellNum(); i++) {
                Cell existingCell = row.getCell(i);
                if (existingCell != null && existingCell.getStringCellValue().equals(colName)) {
                    // Column name already exists, return false
                    return false;
                }
            }

            // If the column name doesn't exist, proceed to create it
            Cell newCell;
            if (row.getLastCellNum() == -1)
                newCell = row.createCell(0);
            else
                newCell = row.createCell(row.getLastCellNum());

            newCell.setCellValue(colName);
            newCell.setCellStyle(style);

            fileOut = new FileOutputStream(path);
            workbook.write(fileOut);
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    

    public static void closeExcelFile(String filePath) {
        try (FileInputStream excelFileInputStream = new FileInputStream(filePath);
             FileOutputStream excelFileOutputStream = new FileOutputStream(filePath)) {
            // Perform any operations if needed

            // No explicit close needed here, try-with-resources will automatically close the streams
            System.out.println("Excel file closed successfully.");
        } catch (IOException e) {
            System.err.println("Error occurred while closing the Excel file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}