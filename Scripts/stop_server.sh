#!/bin/bash
echo "Stopping currently running Java processes..."
ps aux | grep java | grep -v grep | awk '{print $2}' | xargs -r kill -9
echo "All Java processes stopped successfully."
