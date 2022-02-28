package com.generalstoreapp.utilities;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;

public class ExcelUtils {
    public static Object[][] getTableArray(String FilePath, String SheetName) throws Exception {
        try {
            File file = new File( FilePath);
            FileInputStream fis = new FileInputStream(file);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheet(SheetName);
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();
            Object[][] tableArray = new Object[rowCount-1][colCount-1];
            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 1; j < colCount; j++) {
                    try {
                        Cell cell = row.getCell(j);
                        switch (cell.getCellType()) {
                            case STRING:    //field that represents string cell type
                                tableArray[i-1][j-1] = cell.getStringCellValue();
                                break;
                            case NUMERIC:    //field that represents number cell type
                                tableArray[i-1][j-1] = cell.getNumericCellValue();
                                break;
                            case BOOLEAN:    //field that represents boolean cell type
                                tableArray[i-1][j-1] = cell.getBooleanCellValue();
                                break;
                            default:
                                break;
                        }
                    } catch (NullPointerException e) {
                        tableArray[i-1][j-1] = "";
                    }
                }
            }
            return tableArray;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

