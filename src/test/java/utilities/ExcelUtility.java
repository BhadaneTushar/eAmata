package utilities;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for handling Excel file operations.
 * Supports both XLS and XLSX formats with enhanced error handling and bulk
 * operations.
 */
public class ExcelUtility {
    private Workbook workbook;
    private Sheet sheet;
    private String filePath;
    private static final Map<String, Integer> COLUMN_INDEX_MAP = new HashMap<>();

    /**
     * Constructor for ExcelUtility.
     *
     * @param filePath The path to the Excel file
     * @throws IOException              if file cannot be opened
     * @throws IllegalArgumentException if file format is not supported
     */
    public ExcelUtility(String filePath) throws IOException {
        this.filePath = filePath;
        initializeWorkbook();
        LoggerUtils.debug("Initialized ExcelUtility with file: " + filePath);
    }

    /**
     * Initializes the workbook based on file extension.
     *
     * @throws IOException              if file cannot be opened
     * @throws IllegalArgumentException if file format is not supported
     */
    private void initializeWorkbook() throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath)) {
            if (filePath.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(fis);
            } else if (filePath.endsWith(".xls")) {
                workbook = new HSSFWorkbook(fis);
            } else {
                throw new IllegalArgumentException("Unsupported file format. Only .xls and .xlsx are supported.");
            }
            sheet = workbook.getSheetAt(0);
            initializeColumnIndexMap();
        } catch (IOException e) {
            LoggerUtils.error("Failed to initialize workbook: " + filePath, e);
            throw e;
        }
    }

    /**
     * Initializes the column index map from the header row.
     */
    private void initializeColumnIndexMap() {
        Row headerRow = sheet.getRow(0);
        if (headerRow != null) {
            for (Cell cell : headerRow) {
                COLUMN_INDEX_MAP.put(cell.getStringCellValue().toLowerCase(), cell.getColumnIndex());
            }
        }
    }

    /**
     * Gets the number of rows in the sheet.
     *
     * @return The number of rows
     */
    public int getRowCount() {
        return sheet.getLastRowNum() + 1;
    }

    /**
     * Gets the number of columns in the sheet.
     *
     * @return The number of columns
     */
    public int getColumnCount() {
        return sheet.getRow(0).getLastCellNum();
    }

    /**
     * Gets the number of rows in the specified sheet.
     *
     * @param sheetName The name of the sheet
     * @return The number of rows
     */
    public int getRowCount(String sheetName) {
        Sheet targetSheet = workbook.getSheet(sheetName);
        if (targetSheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        return targetSheet.getLastRowNum() + 1;
    }

    /**
     * Gets the number of columns in the specified sheet and row.
     *
     * @param sheetName The name of the sheet
     * @param rowNum    The row number (0-based)
     * @return The number of columns
     */
    public int getColumnCount(String sheetName, int rowNum) {
        Sheet targetSheet = workbook.getSheet(sheetName);
        if (targetSheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        Row row = targetSheet.getRow(rowNum);
        if (row == null) {
            return 0;
        }
        return row.getLastCellNum();
    }

    /**
     * Gets the data from a specific cell in the specified sheet.
     *
     * @param sheetName The name of the sheet
     * @param rowNum    The row number (0-based)
     * @param colNum    The column number (0-based)
     * @return The cell data as a string
     */
    public String getCellData(String sheetName, int rowNum, int colNum) {
        Sheet targetSheet = workbook.getSheet(sheetName);
        if (targetSheet == null) {
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }
        return getCellData(rowNum, colNum);
    }

    /**
     * Gets the data from a specific cell.
     *
     * @param rowNum The row number (0-based)
     * @param colNum The column number (0-based)
     * @return The cell data as a string
     */
    public String getCellData(int rowNum, int colNum) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                return "";
            }
            Cell cell = row.getCell(colNum);
            if (cell == null) {
                return "";
            }
            return getCellValueAsString(cell);
        } catch (Exception e) {
            LoggerUtils.error("Failed to get cell data at row " + rowNum + ", column " + colNum, e);
            return "";
        }
    }

    /**
     * Gets the data from a specific cell using column name.
     *
     * @param rowNum     The row number (0-based)
     * @param columnName The column name
     * @return The cell data as a string
     */
    public String getCellData(int rowNum, String columnName) {
        try {
            Integer colNum = COLUMN_INDEX_MAP.get(columnName.toLowerCase());
            if (colNum == null) {
                throw new IllegalArgumentException("Column not found: " + columnName);
            }
            return getCellData(rowNum, colNum);
        } catch (Exception e) {
            LoggerUtils.error("Failed to get cell data at row " + rowNum + ", column " + columnName, e);
            return "";
        }
    }

    /**
     * Sets data in a specific cell.
     *
     * @param rowNum The row number (0-based)
     * @param colNum The column number (0-based)
     * @param data   The data to set
     */
    public void setCellData(int rowNum, int colNum, String data) {
        try {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }
            Cell cell = row.createCell(colNum);
            cell.setCellValue(data);
            LoggerUtils.debug("Set cell data at row " + rowNum + ", column " + colNum + ": " + data);
        } catch (Exception e) {
            LoggerUtils.error("Failed to set cell data at row " + rowNum + ", column " + colNum, e);
        }
    }

    /**
     * Sets data in a specific cell using column name.
     *
     * @param rowNum     The row number (0-based)
     * @param columnName The column name
     * @param data       The data to set
     */
    public void setCellData(int rowNum, String columnName, String data) {
        try {
            Integer colNum = COLUMN_INDEX_MAP.get(columnName.toLowerCase());
            if (colNum == null) {
                throw new IllegalArgumentException("Column not found: " + columnName);
            }
            setCellData(rowNum, colNum, data);
        } catch (Exception e) {
            LoggerUtils.error("Failed to set cell data at row " + rowNum + ", column " + columnName, e);
        }
    }

    /**
     * Gets all data from the sheet as a list of maps.
     *
     * @return List of maps containing row data
     */
    public List<Map<String, String>> getAllData() {
        List<Map<String, String>> data = new ArrayList<>();
        try {
            int rowCount = getRowCount();
            int colCount = getColumnCount();
            Row headerRow = sheet.getRow(0);

            for (int i = 1; i < rowCount; i++) {
                Map<String, String> rowData = new HashMap<>();
                Row row = sheet.getRow(i);
                if (row != null) {
                    for (int j = 0; j < colCount; j++) {
                        String header = headerRow.getCell(j).getStringCellValue();
                        String value = getCellData(i, j);
                        rowData.put(header, value);
                    }
                    data.add(rowData);
                }
            }
            LoggerUtils.debug("Retrieved " + data.size() + " rows of data");
        } catch (Exception e) {
            LoggerUtils.error("Failed to get all data from sheet", e);
        }
        return data;
    }

    /**
     * Writes data to the Excel file.
     *
     * @throws IOException if file cannot be written
     */
    public void writeData() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            workbook.write(fos);
            LoggerUtils.debug("Data written to file: " + filePath);
        } catch (IOException e) {
            LoggerUtils.error("Failed to write data to file: " + filePath, e);
            throw e;
        }
    }

    /**
     * Gets the cell value as a string.
     *
     * @param cell The cell to get the value from
     * @return The cell value as a string
     */
    private String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf((int) cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }

    /**
     * Closes the workbook and releases resources.
     */
    public void close() {
        try {
            if (workbook != null) {
                workbook.close();
            }
            LoggerUtils.debug("Closed workbook: " + filePath);
        } catch (IOException e) {
            LoggerUtils.error("Failed to close workbook: " + filePath, e);
        }
    }
}
