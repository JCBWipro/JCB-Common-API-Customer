#!/bin/bash
 
echo "Setting permissions for executing the JARs"
chmod +x /data5/JCB_MobileAPI_Artifacts/*.jar
echo "Permissions set successfully!"
 
# Starting the Service Registry
echo "Starting Service Registry..."
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Service-Registry-0.0.1-SNAPSHOT.jar > /data5/logs/service-registry.log 2>&1 &
echo "Service Registry started successfully!"
sleep 10
 
# Starting other services in parallel
echo "Starting other services..."
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-API-Gateway-0.0.1-SNAPSHOT.jar > /data5/logs/api-gateway.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Authentication-0.0.1-SNAPSHOT.jar > /data5/logs/authentication.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Mobile-Authentication-0.0.1-SNAPSHOT.jar > /data5/logs/mobile-authentication.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Machines-0.0.1-SNAPSHOT.jar > /data5/logs/machines.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Reports-0.0.1-SNAPSHOT.jar > /data5/logs/reports.log 2>&1 &
/data5/Softwares/jdk-17-linux/bin/java -jar /data5/JCB_MobileAPI_Artifacts/JCB-Alerts-0.0.1-SNAPSHOT.jar > /data5/logs/alerts.log 2>&1 &
 
# Wait for all services to start
echo "All services started successfully!"
