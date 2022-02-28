package com.generalstoreapp.dataprovider;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import com.generalstoreapp.utilities.ExcelUtils;


public class GetExcelData{
    @DataProvider(name = "getExcelData")
    public static Object [][] getData(ITestContext context) throws Exception {
        String excelFilePath = System.getProperty("user.dir") + context.getCurrentXmlTest().getParameter("excelFilePath");
        String sheetName = context.getCurrentXmlTest().getParameter("sheetName");
        ExcelUtils eu = new ExcelUtils();
        return eu.getTableArray(excelFilePath, sheetName);
    }
}
