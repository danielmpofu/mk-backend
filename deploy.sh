#!/bin/bash

# Configuration
PROJECT_DIR="/home/ubuntu/mk-backend"
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
DEPLOY_NAME="demo.war"  # Simplified name for deployment
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_BIN="/opt/tomcat/bin"
TOMCAT_USER="ubuntu"
TOMCAT_GROUP="ubuntu"
LOG_FILE="/var/log/mk-backend-deploy.log"

# Initialize logging
exec > >(sudo tee -a $LOG_FILE) 2>&1
echo "=== Deployment started at $(date) ==="

# Function to fail on error
die() {
    echo "❌ ERROR: $1" >&2
    exit 1
}

# Verify running as correct user
if [ "$(whoami)" != "ubuntu" ]; then
    die "Script must be run as ubuntu user"
fi

# Stop Tomcat safely
echo "🛑 Stopping Tomcat..."
if systemctl is-active --quiet tomcat; then
    sudo systemctl stop tomcat || die "Failed to stop Tomcat"
else
    echo "ℹ️ Tomcat was not running, skipping stop."
fi
sleep 5

# Kill any remaining Java processes
TOMCAT_PIDS=$(pgrep -f tomcat)
if [ -n "$TOMCAT_PIDS" ]; then
    echo "⚠️ Force killing leftover Tomcat processes: $TOMCAT_PIDS"
    sudo pkill -9 -f tomcat
    sleep 2
else
    echo "✅ No leftover Tomcat processes found."
fi

# Clean previous deployment
echo "🧹 Cleaning old deployment..."
sudo rm -rf "$TOMCAT_WEBAPPS/demo"* || die "Failed to clean old deployment"

# Build new version
echo "⚙️ Building application..."
cd "$PROJECT_DIR" || die "Failed to enter project directory"
mvn clean package -DskipTests || die "Maven build failed"

# Verify WAR file exists
[ ! -f "$WAR_PATH" ] && die "WAR file not found at $WAR_PATH"

# Deploy with correct permissions
echo "📦 Deploying new version..."
sudo install -o "$TOMCAT_USER" -g "$TOMCAT_GROUP" -m 750 \
    "$WAR_PATH" "$TOMCAT_WEBAPPS/$DEPLOY_NAME" || die "Failed to deploy WAR"

# Fix permissions on webapps directory
echo "🔒 Setting correct permissions..."
sudo chown -R "$TOMCAT_USER:$TOMCAT_GROUP" "$TOMCAT_WEBAPPS"
sudo find "$TOMCAT_WEBAPPS" -type d -exec chmod 750 {} \;
sudo find "$TOMCAT_WEBAPPS" -type f -exec chmod 640 {} \;

# Start Tomcat
echo "🚀 Starting Tomcat..."
sudo systemctl start tomcat || die "Failed to start Tomcat"

# Verify deployment
echo "⏳ Waiting for deployment to complete..."
DEPLOY_SUCCESS=false
for i in {1..10}; do
    if curl -sSf "http://localhost:8080/demo/actuator/health" >/dev/null 2>&1; then
        DEPLOY_SUCCESS=true
        break
    fi
    sleep 5
done

# Final status
if $DEPLOY_SUCCESS; then
    PUBLIC_IP=$(curl -s ifconfig.me)
    echo "✅ Deployment successful!"
    echo "🌐 Application URL: http://$PUBLIC_IP:8080/demo/"
else
    echo "⚠️ Deployment might have failed - checking logs..."
    sudo tail -n 50 /opt/tomcat/logs/catalina.out
    die "Application did not start properly"
fi