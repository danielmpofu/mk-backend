#!/bin/bash

# Configuration
PROJECT_DIR="/home/ubuntu/mk-backend"
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
DEPLOY_NAME="backend.war"  # Changed to backend.war for URL mapping
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_CONF="/opt/tomcat/conf"
TOMCAT_BIN="/opt/tomcat/bin"
TOMCAT_USER="tomcat"  # Changed to tomcat user for security
TOMCAT_GROUP="tomcat"
LOG_FILE="/var/log/backend-deploy.log"
APP_URL="http://51.21.252.97:8080/backend/"

# Initialize logging
exec > >(sudo tee -a $LOG_FILE) 2>&1
echo "=== Deployment started at $(date) ==="
echo "Target URL: $APP_URL"

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
sudo rm -rf "$TOMCAT_WEBAPPS/backend"* || die "Failed to clean old deployment"

# Build new version (if needed)
echo "⚙️ Building application..."
cd "$PROJECT_DIR" || die "Failed to enter project directory"
mvn clean package -DskipTests || die "Maven build failed"

# Verify WAR file exists
[ ! -f "$WAR_PATH" ] && die "WAR file not found at $WAR_PATH"

# Deploy with correct permissions and renamed
echo "📦 Deploying new version as 'backend.war'..."
sudo install -o "$TOMCAT_USER" -g "$TOMCAT_GROUP" -m 750 \
    "$WAR_PATH" "$TOMCAT_WEBAPPS/$DEPLOY_NAME" || die "Failed to deploy WAR"

# Create context configuration for URL mapping
echo "🔧 Configuring context path..."
sudo mkdir -p "$TOMCAT_CONF/Catalina/localhost"
sudo bash -c "cat > $TOMCAT_CONF/Catalina/localhost/backend.xml" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<Context
    path="/backend"
    docBase="$TOMCAT_WEBAPPS/backend.war"
    reloadable="true"
    privileged="true"
    antiResourceLocking="false"
    antiJARLocking="false">
</Context>
EOF

# Set proper permissions on context file
sudo chown "$TOMCAT_USER:$TOMCAT_GROUP" "$TOMCAT_CONF/Catalina/localhost/backend.xml"
sudo chmod 640 "$TOMCAT_CONF/Catalina/localhost/backend.xml"

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
    if curl -sSf "http://localhost:8080/backend/actuator/health" >/dev/null 2>&1; then
        DEPLOY_SUCCESS=true
        break
    fi
    sleep 5
done

# Final status
if $DEPLOY_SUCCESS; then
    echo "✅ Deployment successful!"
    echo "🌐 Application URL: $APP_URL"
    echo "🔄 Deployment completed at $(date)"
else
    echo "⚠️ Deployment might have failed - checking logs..."
    sudo tail -n 50 /opt/tomcat/logs/catalina.out
    die "Application did not start properly"
fi