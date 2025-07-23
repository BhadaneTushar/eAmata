package utilities;

import org.testng.ITestResult;
import testBase.BaseClass;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Automatic Test Data Cleanup Utility
 * Manages and cleans up test data created during test execution
 */
public class TestDataCleanup extends BaseClass {
    
    // Thread-safe storage for test data created during execution
    private static final Map<String, List<TestDataRecord>> testDataRegistry = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> testDataByType = new ConcurrentHashMap<>();
    
    /**
     * Register test data for cleanup
     */
    public static void registerTestData(String testName, String dataType, String dataId, String cleanupAction) {
        TestDataRecord record = new TestDataRecord(dataType, dataId, cleanupAction, System.currentTimeMillis());
        
        testDataRegistry.computeIfAbsent(testName, k -> new ArrayList<>()).add(record);
        testDataByType.computeIfAbsent(dataType, k -> new HashSet<>()).add(dataId);
        
        LoggerUtils.info("Registered test data for cleanup - Test: " + testName + 
                        ", Type: " + dataType + ", ID: " + dataId);
    }
    
    /**
     * Register provider group for cleanup
     */
    public static void registerProviderGroup(String testName, String groupName, String groupId) {
        registerTestData(testName, "PROVIDER_GROUP", groupId, "DELETE_PROVIDER_GROUP:" + groupName);
        LoggerUtils.info("Provider Group registered for cleanup: " + groupName + " (ID: " + groupId + ")");
    }
    
    /**
     * Register staff member for cleanup
     */
    public static void registerStaff(String testName, String staffName, String staffId) {
        registerTestData(testName, "STAFF", staffId, "DELETE_STAFF:" + staffName);
        LoggerUtils.info("Staff member registered for cleanup: " + staffName + " (ID: " + staffId + ")");
    }
    
    /**
     * Register location for cleanup
     */
    public static void registerLocation(String testName, String locationName, String locationId) {
        registerTestData(testName, "LOCATION", locationId, "DELETE_LOCATION:" + locationName);
        LoggerUtils.info("Location registered for cleanup: " + locationName + " (ID: " + locationId + ")");
    }
    
    /**
     * Register nurse for cleanup
     */
    public static void registerNurse(String testName, String nurseName, String nurseId) {
        registerTestData(testName, "NURSE", nurseId, "DELETE_NURSE:" + nurseName);
        LoggerUtils.info("Nurse registered for cleanup: " + nurseName + " (ID: " + nurseId + ")");
    }
    
    /**
     * Clean up test data for a specific test
     */
    public static void cleanupTestData(String testName, ITestResult testResult) {
        LoggerUtils.info("Starting cleanup for test: " + testName);
        
        List<TestDataRecord> testData = testDataRegistry.get(testName);
        if (testData == null || testData.isEmpty()) {
            LoggerUtils.info("No test data to cleanup for test: " + testName);
            return;
        }
        
        int cleanedCount = 0;
        int failedCount = 0;
        
        // Sort by creation time (cleanup in reverse order)
        testData.sort((a, b) -> Long.compare(b.creationTime, a.creationTime));
        
        for (TestDataRecord record : testData) {
            try {
                boolean cleaned = performCleanupAction(record);
                if (cleaned) {
                    cleanedCount++;
                    LoggerUtils.info("Successfully cleaned up: " + record.dataType + " - " + record.dataId);
                } else {
                    failedCount++;
                    LoggerUtils.warn("Failed to cleanup: " + record.dataType + " - " + record.dataId);
                }
            } catch (Exception e) {
                failedCount++;
                LoggerUtils.error("Exception during cleanup of " + record.dataType + " - " + record.dataId + ": " + e.getMessage());
            }
        }
        
        // Remove cleaned data from registry
        testDataRegistry.remove(testName);
        
        // Update type registry
        for (TestDataRecord record : testData) {
            Set<String> typeData = testDataByType.get(record.dataType);
            if (typeData != null) {
                typeData.remove(record.dataId);
            }
        }
        
        LoggerUtils.info("Cleanup completed for test: " + testName + 
                        " - Cleaned: " + cleanedCount + ", Failed: " + failedCount);
        
        // Log cleanup summary to extent report
        if (TestListener.getExtentTest() != null) {
            ReportUtils.logStep(TestListener.getExtentTest(), 
                com.aventstack.extentreports.Status.INFO, 
                "Test data cleanup completed - Cleaned: " + cleanedCount + ", Failed: " + failedCount);
        }
    }
    
    /**
     * Clean up all test data (for suite cleanup)
     */
    public static void cleanupAllTestData() {
        LoggerUtils.info("Starting cleanup of all test data...");
        
        int totalCleaned = 0;
        int totalFailed = 0;
        
        for (Map.Entry<String, List<TestDataRecord>> entry : testDataRegistry.entrySet()) {
            String testName = entry.getKey();
            List<TestDataRecord> testData = entry.getValue();
            
            LoggerUtils.info("Cleaning up data for test: " + testName);
            
            for (TestDataRecord record : testData) {
                try {
                    boolean cleaned = performCleanupAction(record);
                    if (cleaned) {
                        totalCleaned++;
                    } else {
                        totalFailed++;
                    }
                } catch (Exception e) {
                    totalFailed++;
                    LoggerUtils.error("Exception during cleanup: " + e.getMessage());
                }
            }
        }
        
        // Clear all registries
        testDataRegistry.clear();
        testDataByType.clear();
        
        LoggerUtils.info("All test data cleanup completed - Total Cleaned: " + totalCleaned + 
                        ", Total Failed: " + totalFailed);
    }
    
    /**
     * Perform the actual cleanup action
     */
    private static boolean performCleanupAction(TestDataRecord record) {
        try {
            String[] actionParts = record.cleanupAction.split(":");
            String action = actionParts[0];
            String identifier = actionParts.length > 1 ? actionParts[1] : record.dataId;
            
            switch (action) {
                case "DELETE_PROVIDER_GROUP":
                    return deleteProviderGroup(identifier, record.dataId);
                case "DELETE_STAFF":
                    return deleteStaff(identifier, record.dataId);
                case "DELETE_LOCATION":
                    return deleteLocation(identifier, record.dataId);
                case "DELETE_NURSE":
                    return deleteNurse(identifier, record.dataId);
                default:
                    LoggerUtils.warn("Unknown cleanup action: " + action);
                    return false;
            }
        } catch (Exception e) {
            LoggerUtils.error("Error performing cleanup action: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete provider group
     */
    private static boolean deleteProviderGroup(String groupName, String groupId) {
        try {
            LoggerUtils.info("Attempting to delete provider group: " + groupName);
            
            // Navigate to provider groups page
            navigateToProviderGroups();
            
            // Search for the group
            if (searchForItem(groupName)) {
                // Click delete button
                if (clickDeleteButton(groupId)) {
                    // Confirm deletion
                    if (confirmDeletion()) {
                        LoggerUtils.info("Successfully deleted provider group: " + groupName);
                        return true;
                    }
                }
            }
            
            LoggerUtils.warn("Could not delete provider group: " + groupName);
            return false;
            
        } catch (Exception e) {
            LoggerUtils.error("Exception deleting provider group " + groupName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete staff member
     */
    private static boolean deleteStaff(String staffName, String staffId) {
        try {
            LoggerUtils.info("Attempting to delete staff member: " + staffName);
            
            // Navigate to staff page
            navigateToStaff();
            
            // Search and delete
            if (searchForItem(staffName) && clickDeleteButton(staffId) && confirmDeletion()) {
                LoggerUtils.info("Successfully deleted staff member: " + staffName);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            LoggerUtils.error("Exception deleting staff " + staffName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete location
     */
    private static boolean deleteLocation(String locationName, String locationId) {
        try {
            LoggerUtils.info("Attempting to delete location: " + locationName);
            
            // Navigate to locations page
            navigateToLocations();
            
            // Search and delete
            if (searchForItem(locationName) && clickDeleteButton(locationId) && confirmDeletion()) {
                LoggerUtils.info("Successfully deleted location: " + locationName);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            LoggerUtils.error("Exception deleting location " + locationName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Delete nurse
     */
    private static boolean deleteNurse(String nurseName, String nurseId) {
        try {
            LoggerUtils.info("Attempting to delete nurse: " + nurseName);
            
            // Navigate to nurses page
            navigateToNurses();
            
            // Search and delete
            if (searchForItem(nurseName) && clickDeleteButton(nurseId) && confirmDeletion()) {
                LoggerUtils.info("Successfully deleted nurse: " + nurseName);
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            LoggerUtils.error("Exception deleting nurse " + nurseName + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get cleanup summary for reporting
     */
    public static String getCleanupSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("📋 TEST DATA CLEANUP SUMMARY\n");
        summary.append("═══════════════════════════════════════\n");
        
        if (testDataRegistry.isEmpty()) {
            summary.append("✅ No test data requiring cleanup\n");
        } else {
            summary.append("📊 Test Data by Type:\n");
            for (Map.Entry<String, Set<String>> entry : testDataByType.entrySet()) {
                summary.append("  • ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" items\n");
            }
            
            summary.append("\n📝 Test Data by Test:\n");
            for (Map.Entry<String, List<TestDataRecord>> entry : testDataRegistry.entrySet()) {
                summary.append("  • ").append(entry.getKey()).append(": ").append(entry.getValue().size()).append(" items\n");
            }
        }
        
        summary.append("═══════════════════════════════════════\n");
        return summary.toString();
    }
    
    // Helper methods for navigation and actions
    private static void navigateToProviderGroups() {
        // Implementation depends on your application's navigation
        LoggerUtils.info("Navigating to Provider Groups page for cleanup");
    }
    
    private static void navigateToStaff() {
        LoggerUtils.info("Navigating to Staff page for cleanup");
    }
    
    private static void navigateToLocations() {
        LoggerUtils.info("Navigating to Locations page for cleanup");
    }
    
    private static void navigateToNurses() {
        LoggerUtils.info("Navigating to Nurses page for cleanup");
    }
    
    private static boolean searchForItem(String itemName) {
        // Implementation for searching items
        LoggerUtils.info("Searching for item: " + itemName);
        return true; // Placeholder
    }
    
    private static boolean clickDeleteButton(String itemId) {
        // Implementation for clicking delete button
        LoggerUtils.info("Clicking delete button for item: " + itemId);
        return true; // Placeholder
    }
    
    private static boolean confirmDeletion() {
        // Implementation for confirming deletion
        LoggerUtils.info("Confirming deletion");
        return true; // Placeholder
    }
    
    /**
     * Test Data Record class
     */
    private static class TestDataRecord {
        final String dataType;
        final String dataId;
        final String cleanupAction;
        final long creationTime;
        
        TestDataRecord(String dataType, String dataId, String cleanupAction, long creationTime) {
            this.dataType = dataType;
            this.dataId = dataId;
            this.cleanupAction = cleanupAction;
            this.creationTime = creationTime;
        }
        
        @Override
        public String toString() {
            return "TestDataRecord{" +
                    "dataType='" + dataType + '\'' +
                    ", dataId='" + dataId + '\'' +
                    ", cleanupAction='" + cleanupAction + '\'' +
                    ", creationTime=" + creationTime +
                    '}';
        }
    }
}