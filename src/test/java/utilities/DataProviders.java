package utilities;

import org.testng.annotations.DataProvider;
import java.io.IOException;

public class DataProviders {

    /**
     * Provides login data for testing from an Excel file.
     * @return A 2D array containing login data (e.g., username, password).
     * @throws IOException If there is an issue reading the Excel file.
     */
    @DataProvider(name = "LoginData")
    public String[][] getLoginData() throws IOException {
        String excelFilePath = ".\\testdata\\logindata.xlsx"; // Path to the Excel file
        ExcelUtility excelUtility = new ExcelUtility(excelFilePath);

        // Get total rows and columns in the Excel sheet
        int totalRows = excelUtility.getRowCount("Sheet1");
        int totalColumns = excelUtility.getColumnCount("Sheet1", 0); // Fetch the column count from the first row (header row)

        // Initialize the 2D array to store login data (skipping header row)
        String[][] loginData = new String[totalRows - 1][totalColumns];

        // Loop through rows and columns to fetch login data
        for (int rowIndex = 1; rowIndex < totalRows; rowIndex++) {
            for (int colIndex = 0; colIndex < totalColumns; colIndex++) {
                loginData[rowIndex - 1][colIndex] = excelUtility.getCellData("Sheet1", rowIndex, colIndex);
            }
        }
        return loginData; // Return the populated 2D array with login data
    }
}
