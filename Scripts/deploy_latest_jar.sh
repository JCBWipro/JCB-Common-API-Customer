#!/bin/bash
 
BUCKET_NAME="codepipeline-ap-south-1-633051940229/JCBMobileAPI_CodePip/BuildArtif"
DEST_DIR="/data5/JCB_MobileAPI_Artifacts"
 
echo "Fetching the latest jar file from S3 bucket..."
 
# List objects in the bucket and get the latest one
LATEST_FILE=$(aws s3 ls s3://$BUCKET_NAME/ --recursive | sort | tail -n 1 | awk '{print $4}')
 
if [ -z "$LATEST_FILE" ]; then
    echo "No files found in the bucket. Exiting."
    exit 1
fi
 
echo "Latest file found: $LATEST_FILE"
 
# Download the latest file
aws s3 cp s3://$BUCKET_NAME/$LATEST_FILE /tmp/
 
# Unzip the file
echo "Unzipping the file..."
unzip /tmp/$(basename "$LATEST_FILE") -d /tmp/
 
# Move the jar file to the target directory
echo "Deploying the jar to $DEST_DIR..."
mv /tmp/*.jar $DEST_DIR/
 
echo "Deployment of the latest jar completed successfully."
