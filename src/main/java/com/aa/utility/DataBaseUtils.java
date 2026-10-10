
package com.aa.utility;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.Reporter;

import com.aa.constant.CommonDbTables;
import com.aventstack.extentreports.Status;

public class DataBaseUtils extends Library {
    
    public static Statement adminStatement;
    public static Statement companyStatement;
    public static String COMPANY_DATABASE;
    public static String ADMIN_DATABASE;
    private Connection adminConnection;
   private Connection companyConnection;
	private String companyUrl;
	private String AdminUrl;
    private String dbUsername;
    private String dbPassword;
    public int databaseOpStep=1;
    
    
   

    /**
     * @Author: Ankush Agle at 14-02-2025 12:28
     *
     * @implNote :- Below Class Used To Interact With DataBase And Fetch The Values From The Database
     * */
    public DataBaseUtils(String LOGINSHEETPATH) throws Exception {
        String path = LOGINSHEETPATH;
        ExcelReader Excel = new ExcelReader(path);
        String sheet = "JDBC";
        String sqlserver = Excel.getCellData(sheet, "SQLSERVER", 1);
        String companyDatabase = Excel.getCellData(sheet, "DATABASE", 1);
        String adminDatabase = Excel.getCellData(sheet, "ADMIN_DATABASE", 1);
        
        if(adminDatabase.contains("NODATA")) {
        	  
            // Standardized error message with file path and sheet name
            String errorMessage = "❌ Missing required data in the Excel file.\n" +
                                  "📂 File Path: " + path + "\n" +
                                  "📄 Sheet Name: '" + sheet + "'\n\n" +
                                  "Please ensure the following:\n" +
                                  "✅ Column 'ADMIN_DATABASE' exists in the sheet.\n" +
                                  "✅ Your admin database value is provided in row number 2.";

            Library.dialogErrorMessageLog(errorMessage);
            Excel.addColumn(sheet, "ADMIN_DATABASE");
            Assert.fail(errorMessage);
        	
        }
        
        this.dbUsername = EnvironmentUtils.getValue("DB_USERNAME");
        this.dbPassword = EnvironmentUtils.getValue("DB_PASSWORD");
        String user = "";
        String pass = "";
        String encrypt = Excel.getCellData(sheet, "encrypt", 1);
        String trustServerCertificate = Excel.getCellData(sheet, "trustServerCertificate", 1);
        String loginTimeout = Excel.getCellData(sheet, "loginTimeout", 1);
        
        this.COMPANY_DATABASE = companyDatabase;
        this.ADMIN_DATABASE = adminDatabase;
      
        String adminConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + adminDatabase + ";"
                + "user=" + user + ";"
                + "password=" + pass + ";"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout="
                + "" + loginTimeout + ";";
        adminConnectionUrl = adminConnectionUrl.replaceAll("(?i)(?:user|password)=[^;]*;", "");
        
        Library.dialogSkipMessageLog("Admin Connection URL : "+adminConnectionUrl);
        String companyConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + companyDatabase + ";"
                + "user=" + user + ";"
                + "password=" + pass + ";"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";
        companyConnectionUrl = companyConnectionUrl.replaceAll("(?i)(?:user|password)=[^;]*;", "");
        Library.dialogSkipMessageLog("Company Connection URL : "+companyConnectionUrl);

        this.adminConnection = DriverManager.getConnection(adminConnectionUrl, dbUsername, dbPassword);
        this.companyConnection = DriverManager.getConnection(companyConnectionUrl, dbUsername, dbPassword);
        adminStatement = adminConnection.createStatement();
        companyStatement = companyConnection.createStatement();

	    this.companyUrl=companyConnectionUrl;
        this.AdminUrl=adminConnectionUrl;
        
      
    }
    
    
    
    
    public DataBaseUtils(String LOGINSHEETPATH,String windowsAuth,String old) throws Exception {
        String path = LOGINSHEETPATH;
        ExcelReader Excel = new ExcelReader(path);
        String sheet = "JDBC";
        String sqlserver = Excel.getCellData(sheet, "SQLSERVER", 1);
        String companyDatabase = Excel.getCellData(sheet, "DATABASE", 1);
        String adminDatabase = Excel.getCellData(sheet, "ADMIN_DATABASE", 1);

        if (adminDatabase.contains("NODATA")) {
            String errorMessage = "❌ Missing required data in the Excel file.\n" +
                                  "📂 File Path: " + path + "\n" +
                                  "📄 Sheet Name: '" + sheet + "'\n\n" +
                                  "Please ensure the following:\n" +
                                  "✅ Column 'ADMIN_DATABASE' exists in the sheet.\n" +
                                  "✅ Your admin database value is provided in row number 2.";
            Library.dialogErrorMessageLog(errorMessage);
            Excel.addColumn(sheet, "ADMIN_DATABASE");
            Assert.fail(errorMessage);
        }

        String encrypt = Excel.getCellData(sheet, "encrypt", 1);
        String trustServerCertificate = Excel.getCellData(sheet, "trustServerCertificate", 1);
        String loginTimeout = Excel.getCellData(sheet, "loginTimeout", 1);

        this.COMPANY_DATABASE = companyDatabase;
        this.ADMIN_DATABASE = adminDatabase;

        String adminConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + adminDatabase + ";"
                + "integratedSecurity=true;"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";

        Library.dialogSkipMessageLog("Admin Connection URL : " + adminConnectionUrl);

        String companyConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + companyDatabase + ";"
                + "integratedSecurity=true;"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";

        Library.dialogSkipMessageLog("Company Connection URL : " + companyConnectionUrl);

        this.adminConnection = DriverManager.getConnection(adminConnectionUrl);
        
  
        
        
        
        this.companyConnection = DriverManager.getConnection(companyConnectionUrl);
        adminStatement = adminConnection.createStatement();
        companyStatement = companyConnection.createStatement();
        
        this.companyUrl=companyConnectionUrl;
        this.AdminUrl=adminConnectionUrl;
        
    }
    public DataBaseUtils(String LOGINSHEETPATH, String windowsAuth) throws Exception {
        String path = LOGINSHEETPATH;
        ExcelReader Excel = new ExcelReader(path);
        String sheet = "JDBC";
        
        String sqlserver = Excel.getCellData(sheet, "SQLSERVER", 1);
        String companyDatabase = Excel.getCellData(sheet, "DATABASE", 1);
        String adminDatabase = Excel.getCellData(sheet, "ADMIN_DATABASE", 1);

        if (adminDatabase.contains("NODATA")) {
            String errorMessage = "❌ Missing required data in the Excel file.\n" +
                                  "📂 File Path: " + path + "\n" +
                                  "📄 Sheet Name: '" + sheet + "'\n\n" +
                                  "Please ensure the following:\n" +
                                  "✅ Column 'ADMIN_DATABASE' exists in the sheet.\n" +
                                  "✅ Your admin database value is provided in row number 2.";
            Library.dialogErrorMessageLog(errorMessage);
            Excel.addColumn(sheet, "ADMIN_DATABASE");
            Assert.fail(errorMessage);
        }

        // Read and sanitize connection parameters
        String encrypt = Excel.getCellData(sheet, "encrypt", 1).trim();
        String trustServerCertificate = Excel.getCellData(sheet, "trustServerCertificate", 1).trim();
        String loginTimeout = Excel.getCellData(sheet, "loginTimeout", 1).trim();

        // Validate encrypt value
        if (!encrypt.equalsIgnoreCase("true") && !encrypt.equalsIgnoreCase("false")) {
            Library.dialogSkipMessageLog("⚠️ Invalid or missing 'encrypt' value in Excel. Defaulting to 'false'.");
            encrypt = "false";
        }

        // Validate trustServerCertificate value
        if (!trustServerCertificate.equalsIgnoreCase("true") && !trustServerCertificate.equalsIgnoreCase("false")) {
            Library.dialogSkipMessageLog("⚠️ Invalid or missing 'trustServerCertificate' value in Excel. Defaulting to 'true'.");
            trustServerCertificate = "true";
        }

        if (loginTimeout.isEmpty()) {
            Library.dialogSkipMessageLog("⚠️ Missing 'loginTimeout' value in Excel. Defaulting to '30'.");
            loginTimeout = "30";
        }

        this.COMPANY_DATABASE = companyDatabase;
        this.ADMIN_DATABASE = adminDatabase;

        String adminConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + adminDatabase + ";"
                + "integratedSecurity=true;"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";

        String url = "jdbc:sqlserver://localhost:1433;databaseName=QADEMO;integratedSecurity=true;encrypt=false;trustServerCertificate=true;";

        Library.dialogSkipMessageLog("🔐 Admin Connection URL: " + adminConnectionUrl);

        String companyConnectionUrl = "jdbc:sqlserver://" + sqlserver + ";"
                + "database=" + companyDatabase + ";"
                + "integratedSecurity=true;"
                + "encrypt=" + encrypt + ";"
                + "trustServerCertificate=" + trustServerCertificate + ";"
                + "loginTimeout=" + loginTimeout + ";";

        Library.dialogSkipMessageLog("🔐 Company Connection URL: " + companyConnectionUrl);

        this.adminConnection = DriverManager.getConnection(adminConnectionUrl);
        this.companyConnection = DriverManager.getConnection(companyConnectionUrl);
        adminStatement = adminConnection.createStatement();
        companyStatement = companyConnection.createStatement();
        
        this.companyUrl=companyConnectionUrl;
        this.AdminUrl=adminConnectionUrl;
        
    }

    /**
     * @author ANKUSH
     * @INFO :- below function created is to connect again if company connecton is closed or null
     * @Created Date :- 25/07/2025 and Time 23:33
     */
    
    public Connection rebuildCompanyConnection() throws SQLException {
        return openConnection(companyUrl);
    }
    
    
    
    /**
     * @author ANKUSH
     * @INFO :- below function created is to connect again if Admin  connecton is closed or null
     * @Created Date :- 25/07/2025 and Time 23:33
     */
    
    public Connection rebuildAdminConnection() throws SQLException {
        return openConnection(AdminUrl);
    }

    private Connection openConnection(String url) throws SQLException {
        if (dbUsername == null || dbPassword == null) {
            return DriverManager.getConnection(url);
        }
        return DriverManager.getConnection(url, dbUsername, dbPassword);
    }
    
    
    
    /**
     * Updates the AutomationTestingStatus column for a given FormId in the specified table.
     * 
     * @param tableName Name of the table (e.g., "PracticeFormData")
     * @param formId The FormId to match in the WHERE clause
     * @param status The new status value to set (e.g., "PASSED", "FAILED")
     * @throws SQLException if database operation fails
     */
    public void updateAutomationTestingStatus(CommonDbTables header, String formId, String status) throws SQLException {
       
    	
    	String tableName=header.getTableName();
    	String query = "UPDATE " + tableName + " SET AutomationTestingStatus = ? WHERE FormId = ? or NameId =?";
       
    	
    	
    	if(companyConnection==null || companyConnection.isClosed()) {
    		
    		
    		companyConnection=rebuildCompanyConnection();
    	}
    	
    	
    	
    	
    	
    	try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, formId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                Library.massagelog("✅ Updated AutomationTestingStatus for FormId " + formId + " to '" + status + "'");
            } else {
                Library.massagelog("⚠️ No record found with FormId: " + formId);
            }
        }
    }

    
    /**
     * Updates the AutomationTestingStatus column for a given FormId in the specified table.
     * 
     * @param tableName Name of the table (e.g., "PracticeFormData")
     * @param formId The FormId to match in the WHERE clause
     * @param status The new status value to set (e.g., "PASSED", "FAILED")
     * @throws SQLException if database operation fails
     */
    public void updateAutomationTestingStatus(CommonDbTables header, String formId,String colname, String status) throws SQLException {
       
    	
    	String tableName=header.getTableName();
    	String query = "UPDATE " + tableName + " SET AutomationTestingStatus = ? WHERE "+colname+" =?";
    	

    	if(companyConnection==null || companyConnection.isClosed()) {
    		
    		
    		companyConnection=rebuildCompanyConnection();
    	}
    	
    	
    	
        try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query)) {
            preparedStatement.setString(1, status);
            preparedStatement.setString(2, formId);
            int rowsAffected = preparedStatement.executeUpdate();
            
            
           
            if (rowsAffected > 0) {
                Library.massagelog("✅ Updated AutomationTestingStatus for FormId " + formId + " to '" + status + "'");
            } else {
                Library.massagelog("⚠️ No record found with FormId: " + formId);
            }
        }
    }

    
   
    
    /**
     * Fetches all rows from the specified table and returns them as a list of maps.
     * Each map represents one row where keys are column names and values are cell values.
     *
     * @param tableName The name of the table to query.
     * @return List of maps representing the table data.
     * @throws SQLException if a database access error occurs.
     */
    public List<Map<String, String>> getAllDataFromTable(CommonDbTables header) throws SQLException {
       
    	
    	String tableName=header.getTableName();
    	List<Map<String, String>> tableData = new ArrayList<>();
        Connection connectionToUse = null;

        
        

    	if(companyConnection==null || companyConnection.isClosed()) {
    		
    		
    		companyConnection=rebuildCompanyConnection();
    	}
    	
    	

    	if(adminConnection==null || adminConnection.isClosed()) {
    		
    		
    		adminConnection=rebuildAdminConnection();
    	}
    	
        
        
        // Determine which connection to use
        if (tableExists(adminConnection, tableName)) {
            connectionToUse = adminConnection;
        } else if (tableExists(companyConnection, tableName)) {
            connectionToUse = companyConnection;
        } else {
            throw new SQLException("Table '" + tableName + "' not found in either ADMIN or COMPANY database.");
        }

        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connectionToUse.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, String> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = rs.getString(i);
                    row.put(columnName, value != null ? value.trim() : "");
                }
                tableData.add(row);
            }
        }
        return tableData;

    }
    
    
    
    
    /**
     * 
     * @author ANKUSH AGLE
     * 
     * @Modified Date  :-  22/07/2025
     * Fetches the first row from the specified table and returns it as a map.
     * Each entry in the map represents a column name as the key and its value from the first row.
     *
     * @param header The enum representing the table to query.
     * @return Map representing the first row data (columnName -> value).
     * @throws SQLException if a database access error occurs or table is not found.
     */
    public Map<String, String> getFirstRowAsMapFromTable(CommonDbTables header) throws SQLException {
        
        String tableName = header.getTableName();
        Map<String, String> firstRowData = new HashMap<>();
        Connection connectionToUse = null;

        
        
if(companyConnection==null || companyConnection.isClosed()) {
    		
    		
    		companyConnection=rebuildCompanyConnection();
    	}
    	
    	

    	if(adminConnection==null || adminConnection.isClosed()) {
    		
    		
    		adminConnection=rebuildAdminConnection();
    	}
    	
        
        
        // Determine which connection to use
        if (tableExists(adminConnection, tableName)) {
            connectionToUse = adminConnection;
        } else if (tableExists(companyConnection, tableName)) {
            connectionToUse = companyConnection;
        } else {
            throw new SQLException("Table '" + tableName + "' not found in either ADMIN or COMPANY database.");
        }

        String query = "SELECT * FROM " + tableName;
        try (PreparedStatement stmt = connectionToUse.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            if (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnLabel(i);
                    String value = rs.getString(i);
                    firstRowData.put(columnName, value != null ? value.trim() : "");
                }
            }
        }

        return firstRowData;
    }
    
    
    
    

    /**
     * Checks if a table exists in the given database connection.
     *
     * @param connection The database connection to check.
     * @param tableName The name of the table to look for.
     * @return true if the table exists; false otherwise.
     * @throws SQLException if a database access error occurs.
     */
    private boolean tableExists(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        try (ResultSet tables = meta.getTables(null, null, tableName, new String[]{"TABLE"})) {
            return tables.next();
        }
    }

    
    
    
    /**
     * @Author: Ankush Agle
     * @implNote: Fetch all data from the given column in the specified table and return it as a String array.
     * @param tableName The name of the table to query.
     * @param columnName The column whose data needs to be fetched.
     * @return A String array containing all values of the specified column.
     * @throws SQLException 
     */
    public String[] getColumnDataAsStringArray(String tableName, String columnName) throws SQLException {
        List<String> resultList = new ArrayList<>();
        String query = "SELECT " + columnName + " FROM " + tableName;

        
        
        
        
if(companyConnection==null || companyConnection.isClosed()) {
    		
    		
    		companyConnection=rebuildCompanyConnection();
    	}
    	
    	

    	if(adminConnection==null || adminConnection.isClosed()) {
    		
    		
    		adminConnection=rebuildAdminConnection();
    	}
    	
        
        
        try (PreparedStatement preparedStatement = companyConnection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                String value = resultSet.getString(columnName);
                
                
                
                resultList.add(value != null ? value.trim() : "");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            Library.massagelog("❌ Error fetching data from column '" + columnName + "' in table '" + tableName + "': " + e.getMessage());
            return new String[0]; // return empty array in case of exception
        }

        return resultList.toArray(new String[0]);
    }

    
  
}
