#!/bin/bash
echo "Cleaning up old artifacts..."
rm -rf /data5/JCB_MobileAPI_Artifact/*
echo "Preparing deployment directory..."
mkdir -p /data5/JCB_MobileAPI_Artifacts
echo "Setting execute permissions for scripts..."
chmod +x /data5/JCB_MobileAPI_Artifacts/Scripts/*.sh
