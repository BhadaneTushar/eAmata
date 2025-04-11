package utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtility {

    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private XSSFRow row;
    private XSSFCell cell;
    private String filePath;

    /**
     * Constructor to initialize the Excel file path.
     *
     * @param filePath Path to the Excel file.
     */
    public ExcelUtility(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Get the total number of rows in the specified sheet.
     *
     * @param sheetName Name of the sheet.
     * @return The total number of rows.
     * @throws IOException If there is an issue reading the file.
     */
    public int getRowCount(String sheetName) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheet(sheetName);
        int rowCount = sheet.getPhysicalNumberOfRows();
        closeResources();
        return rowCount;
    }

    /**
     * Get the total number of columns (cells) in a specific row.
     *
     * @param sheetName Name of the sheet.
     * @param rowIndex Index of the row.
     * @return The total number of columns.
     * @throws IOException If there is an issue reading the file.
     */
    public int getColumnCount(String sheetName, int rowIndex) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rowIndex);
        int columnCount = row.getPhysicalNumberOfCells();
        closeResources();
        return columnCount;
    }

    /**
     * Retrieve cell data from the specified sheet, row, and column.
     *
     * @param sheetName Name of the sheet.
     * @param rowIndex Index of the row.
     * @param colIndex Index of the column.
     * @return The cell data as a string.
     * @throws IOException If there is an issue reading the file.
     */
    public String getCellData(String sheetName, int rowIndex, int colIndex) throws IOException {
        fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);
        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(rowIndex);
        cell = row.getCell(colIndex);
        DataFormatter dataFormatter = new DataFormatter();
        String cellValue = "";

        try {
            cellValue = dataFormatter.formatCellValue(cell);
        } catch (Exception e) {
            cellValue = ""; // Return an empty string if there's an exception
        }

        closeResources();
        return cellValue;
    }

    /**
     * Set data in a specific cell.
     *
     * @param sheetName Name of the sheet.
     * @param rowIndex Index of the row.
     * @param colIndex Index of the column.
     * @param data The data to set in the cell.
     * @throws IOException If there is an issue reading or writing the file.
     */
    public void setCellData(String sheetName, int rowIndex, int colIndex, String data) throws IOException {
        File file = new File(filePath);

        // If file doesn't exist, create a new workbook
        if (!file.exists()) {
            workbook = new XSSFWorkbook();
            fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
        }

        fileInputStream = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fileInputStream);

        // Create sheet if it doesn't exist
        if (workbook.getSheetIndex(sheetName) == -1) {
            workbook.createSheet(sheetName);
        }

        sheet = workbook.getSheet(sheetName);

        // Create row if it doesn't exist
        if (sheet.getRow(rowIndex) == null) {
            sheet.createRow(rowIndex);
        }

        row = sheet.getRow(rowIndex);

        // Create cell and set value
        cell = row.createCell(colIndex);
        cell.setCellValue(data);

        fileOutputStream = new FileOutputStream(filePath);
        workbook.write(fileOutputStream);
        closeResources();
    }

    /**
     * Close resources after file operations.
     * @throws IOException If there is an issue closing the resources.
     */
    private void closeResources() throws IOException {
        if (fileInputStream != null) {
            fileInputStream.close();
        }
        if (fileOutputStream != null) {
            fileOutputStream.close();
        }
        if (workbook != null) {
            workbook.close();
        }
    }
}
