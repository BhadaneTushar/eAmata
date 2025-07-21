#!/bin/bash

# Find the latest ExtentReport in reports directory
LATEST_REPORT=$(find reports -name "ExtentReport_*.html" -type f -printf '%T+ %p\n' | sort -r | head -n 1 | awk '{print $2}')

if [ -z "$LATEST_REPORT" ]; then
    echo "No ExtentReport found in reports directory"
    exit 1
fi

echo "Opening report: $LATEST_REPORT"

# Open the report in the default browser
xdg-open "$LATEST_REPORT" 