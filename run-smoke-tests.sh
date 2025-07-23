#!/bin/bash

# eAmata Smoke Test Execution Script
# Optimized for fast feedback on critical functionality

echo "=========================================="
echo "Starting eAmata Smoke Test Execution"
echo "=========================================="

# Set environment variables for performance
export MAVEN_OPTS="-Xmx2048m -XX:+UseG1GC -XX:+UseStringDeduplication"

# Clean previous results
echo "Cleaning previous test results..."
mvn clean

# Run smoke tests with optimized settings
echo "Executing smoke tests..."
mvn test -Dsurefire.suiteXmlFiles=testng-smoke-simple.xml \
         -Dheadless=true \
         -Dfailsafe.rerunFailingTestsCount=1

# Check exit code
if [ $? -eq 0 ]; then
    echo "✅ Smoke tests completed successfully!"
    
    # Generate Allure report if available
    if command -v allure &> /dev/null; then
        echo "Generating Allure report..."
        allure generate target/allure-results --clean -o target/allure-report
        echo "📊 Allure report generated at: target/allure-report/index.html"
    fi
else
    echo "❌ Smoke tests failed!"
    exit 1
fi

echo "=========================================="
echo "Smoke Test Execution Completed"
echo "=========================================="