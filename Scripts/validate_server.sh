#!/bin/bash
echo "Validating that services are running..."
sleep 5
ps aux | grep java | grep -v grep
if [ $? -eq 0 ]; then
    echo "All services are running successfully."
else
    echo "Service validation failed. Please check logs."
    exit 1
fi
