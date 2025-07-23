#!/bin/bash

# eAmata Regression Test Execution Script
# Optimized for comprehensive testing with parallel execution

echo "=========================================="
echo "Starting eAmata Regression Test Execution"
echo "=========================================="

# Set environment variables for performance
export MAVEN_OPTS="-Xmx4096m -XX:+UseG1GC -XX:+UseStringDeduplication -XX:MaxGCPauseMillis=200"

# Clean previous results
echo "Cleaning previous test results..."
mvn clean

# Run regression tests with optimized settings
echo "Executing regression tests..."
mvn test -Dsurefire.suiteXmlFiles=testng-regression.xml \
         -Dheadless=true \
         -Dthreads=4 \
         -Dfailsafe.rerunFailingTestsCount=1 \
         -Dparallel=methods

# Check exit code
if [ $? -eq 0 ]; then
    echo "✅ Regression tests completed successfully!"
    
    # Generate reports
    echo "Generating test reports..."
    
    # Generate Allure report if available
    if command -v allure &> /dev/null; then
        echo "Generating Allure report..."
        allure generate target/allure-results --clean -o target/allure-report
        echo "📊 Allure report generated at: target/allure-report/index.html"
    fi
    
    # Open reports automatically if on desktop environment
    if [ -n "$DISPLAY" ]; then
        echo "Opening test reports..."
        if [ -f "target/allure-report/index.html" ]; then
            xdg-open target/allure-report/index.html 2>/dev/null &
        fi
        if [ -f "reports/ExtentReport_*.html" ]; then
            xdg-open reports/ExtentReport_*.html 2>/dev/null &
        fi
    fi
else
    echo "❌ Regression tests failed!"
    
    # Generate failure report even on failure
    if command -v allure &> /dev/null; then
        echo "Generating failure report..."
        allure generate target/allure-results --clean -o target/allure-report
    fi
    
    exit 1
fi

echo "=========================================="
echo "Regression Test Execution Completed"
echo "=========================================="