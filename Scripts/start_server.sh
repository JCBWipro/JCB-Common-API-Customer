#!/bin/bash
echo "Setting Permission for executing the jars"
chmod +x /data5/JCB_MobileAPI_Artifacts/*.jar
echo "Permissions set Successfully"
echo "Starting Service Registry..."
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Service-Registry-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/service_registry.log 2>&1 &
sleep 10
 
echo "Starting other services..."
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-API-Gateway-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/api_gateway.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Authentication-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/authentication.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Mobile-Authentication-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/mobile_auth.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Machines-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/machines.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-User-0.0.1-SNAPSHOT.jar > /data5/JCB_MobileAPI_Artifact/user.log 2>&1 &
 
echo "All services started successfully."
