#!/bin/bash

# eAmata Complete Test Suite Execution Script
# Runs all 20 test methods across 5 test classes

echo "=========================================="
echo "Starting Complete eAmata Test Execution"
echo "=========================================="
echo "📋 Test Classes to Execute:"
echo "   • TC001_SuperAdminLogin (3 tests)"
echo "   • TC002_AddProviderGroup (4 tests)"
echo "   • TC003_AddStaff (6 tests)"
echo "   • TC004_AddLocation (6 tests)"
echo "   • TC005_AddEamataNurse (1 test)"
echo "📊 Total: 20 test methods"
echo "=========================================="

# Set environment variables for performance
export MAVEN_OPTS="-Xmx4096m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=200"

# Clean previous results
echo "🧹 Cleaning previous test results..."
mvn clean

# Run all tests
echo "🚀 Executing all eAmata tests..."
mvn test -Dsurefire.suiteXmlFiles=testng-all-tests.xml \
         -Dheadless=true \
         -Dfailsafe.rerunFailingTestsCount=1

# Check exit code
if [ $? -eq 0 ]; then
    echo ""
    echo "=========================================="
    echo "✅ ALL TESTS COMPLETED SUCCESSFULLY!"
    echo "=========================================="
    
    # Show test summary
    echo "📊 Test Execution Summary:"
    if [ -f "target/surefire-reports/testng-results.xml" ]; then
        echo "   📄 TestNG Results: target/surefire-reports/testng-results.xml"
    fi
    
    # Show reports location
    echo "📋 Generated Reports:"
    echo "   • Extent Report: reports/ExtentReport_*.html"
    echo "   • Surefire Reports: target/surefire-reports/"
    
    # Generate Allure report if available
    if command -v allure &> /dev/null; then
        echo "📊 Generating Allure report..."
        allure generate target/allure-results --clean -o target/allure-report
        echo "   • Allure Report: target/allure-report/index.html"
    fi
    
    # Generate performance dashboard
    echo "📈 Performance metrics available in console output"
    
    # Open reports automatically if on desktop environment
    if [ -n "$DISPLAY" ]; then
        echo "🌐 Opening test reports..."
        if [ -f "target/allure-report/index.html" ]; then
            xdg-open target/allure-report/index.html 2>/dev/null &
        fi
        if [ -f "reports/ExtentReport_"*.html ]; then
            xdg-open reports/ExtentReport_*.html 2>/dev/null &
        fi
    fi
    
else
    echo ""
    echo "=========================================="
    echo "❌ SOME TESTS FAILED!"
    echo "=========================================="
    
    # Show failure information
    echo "🔍 Check the following for details:"
    echo "   • Console output above"
    echo "   • Surefire reports: target/surefire-reports/"
    echo "   • Extent report: reports/ExtentReport_*.html"
    
    # Generate failure report even on failure
    if command -v allure &> /dev/null; then
        echo "📊 Generating failure analysis report..."
        allure generate target/allure-results --clean -o target/allure-report
        echo "   • Failure Analysis: target/allure-report/index.html"
    fi
    
    exit 1
fi

echo ""
echo "=========================================="
echo "Complete Test Execution Finished"
echo "=========================================="