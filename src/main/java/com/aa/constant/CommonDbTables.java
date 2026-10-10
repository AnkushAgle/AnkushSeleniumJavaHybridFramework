package com.aa.constant;
public enum CommonDbTables {
    MATERIAL_POLICY("MSCSYSENG"),
    SALES_POLICY("MSCSYSSAL"),
    
    DB_TABLE_TextBoxData("TextBoxData"),
    DB_TABLE_PracticeFormData("PracticeFormData"),
  
    FINANCE_POLICY("MSCSYSFIN"),
    QC_POLICY("MSCSYSQC"),
    SHOP_POLICY("MSCSYSSHP"),
    MRP_POLICY("MSCMRP"),
    PURCHASE_POLICY("MSCSYSPUR"),
    SYSTEM_POLICY("MSCSysFlags"),
    INVENTORY_POLICY("MSCSYSINV");

    private final String tableName;

    // Constructor
    CommonDbTables(String tableName) {
        this.tableName = tableName;
    }

    // Getter method to retrieve the table name
    public String getTableName() {
        return tableName;
    }
}
