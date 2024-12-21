#!/bin/bash
 
# Ensure the correct PATH is set
export PATH=$PATH:/usr/local/bin
 
# Define the bucket and folder structure
BUCKET_NAME="codepipeline-ap-south-1-633051940229"
FOLDER_NAME="JCBMobileAPI_CodePip/BuildArtif"
DEST_DIR="/data5/JCB_MobileAPI_Artifacts"
 
echo "Fetching the latest artifact from S3 and deploying..."
 
# Fetch the latest JAR file from the specified S3 bucket
echo "Finding the latest file in S3 bucket $BUCKET_NAME under $FOLDER_NAME..."
LATEST_FILE=$(aws s3 ls s3://$BUCKET_NAME/$FOLDER_NAME/ --recursive | sort | tail -n 1 | awk '{print $4}')
 
# Check if a file was found
if [ -z "$LATEST_FILE" ]; then
  echo "No latest file found in the S3 bucket! Exiting with failure."
  exit 1
fi
 
echo "Latest file identified: $LATEST_FILE"
 
# Downloading the latest file from S3
echo "Downloading the latest file..."
aws s3 cp s3://$BUCKET_NAME/$LATEST_FILE /tmp/latest_artifact.zip
 
# Extracting the file if it's a ZIP
echo "Extracting downloaded artifact..."
unzip -o /tmp/latest_artifact.zip -d /tmp/extracted_artifact/
 
# Deploying the extracted artifact to the target directory
echo "Deploying the artifact to $DEST_DIR..."
mv /tmp/extracted_artifact/*.jar $DEST_DIR/
 
echo "Deployment of the latest jar completed successfully."
