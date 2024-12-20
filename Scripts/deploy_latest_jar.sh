#!/bin/bash
 
# Define variables
S3_BUCKET="s3://codepipeline-ap-south-1-633051940229/JCBMobileAPI_CodePip/BuildArtif/"
LOCAL_DIR="/data5/JCB_MobileAPI_Artifacts"
 
# Log the action
echo "Fetching the latest jar file from S3 bucket..."
 
# Fetch the latest file using AWS CLI
LATEST_JAR=$(aws s3 ls $S3_BUCKET --recursive | sort | tail -n 1 | awk '{print $4}')
 
# Check if a jar file exists
if [ -z "$LATEST_JAR" ]; then
  echo "No jar files found in S3 bucket. Exiting..."
  exit 1
fi
 
# Download the latest jar file to the local directory
echo "Downloading $LATEST_JAR..."
aws s3 cp "$S3_BUCKET$LATEST_JAR" "$LOCAL_DIR"
 
# Ensure the jar file has been downloaded
if [ $? -eq 0 ]; then
  echo "Successfully downloaded $LATEST_JAR to $LOCAL_DIR"
else
  echo "Failed to download $LATEST_JAR. Please check permissions and S3 bucket path."
  exit 1
fi
 
# Log completion
echo "Deploy latest jar script executed successfully."
