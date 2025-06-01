#!/bin/bash

# Configuration - UPDATED VERSION
PROJECT_DIR="/home/ubuntu/mk-backend"
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
DEPLOY_NAME="backend#v1.war"  # Added version identifier
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_CONF="/opt/tomcat/conf"
TOMCAT_BIN="/opt/tomcat/bin"
TOMCAT_USER="tomcat"
TOMCAT_GROUP="tomcat"
LOG_FILE="/var/log/backend-deploy.log"
APP_URL="http://51.21.252.97:8080/backend/"
HEALTH_CHECK_PATH="/actuator/health"  # Make sure this matches your app

# Initialize logging
exec > >(sudo tee -a $LOG_FILE) 2>&1
echo "=== Deployment started at $(date) ==="

die() {
    echo "❌ ERROR: $1" >&2
    exit 1
}

# Verify running as correct user
[ "$(whoami)" != "ubuntu" ] && die "Script must be run as ubuntu user"

# Stop Tomcat safely
echo "🛑 Stopping Tomcat..."
if systemctl is-active --quiet tomcat; then
    sudo systemctl stop tomcat || die "Failed to stop Tomcat"
    sleep 5
fi

# Clean previous deployment COMPLETELY
echo "🧹 Cleaning old deployment..."
sudo rm -rf "$TOMCAT_WEBAPPS/backend"*
sudo rm -f "$TOMCAT_CONF/Catalina/localhost/backend.xml"

# Deploy new version with versioned WAR name
echo "📦 Deploying new version as '$DEPLOY_NAME'..."
[ ! -f "$WAR_PATH" ] && die "WAR file not found at $WAR_PATH"
sudo cp "$WAR_PATH" "$TOMCAT_WEBAPPS/$DEPLOY_NAME"
sudo chown "$TOMCAT_USER:$TOMCAT_GROUP" "$TOMCAT_WEBAPPS/$DEPLOY_NAME"

# Create ROOT context configuration for /backend path
echo "🔧 Configuring context path..."
sudo mkdir -p "$TOMCAT_CONF/Catalina/localhost"
sudo bash -c "cat > $TOMCAT_CONF/Catalina/localhost/backend.xml" <<EOF
<Context docBase="$TOMCAT_WEBAPPS/$DEPLOY_NAME"
         reloadable="true"
         sessionCookiePath="/">
    <WatchedResource>WEB-INF/web.xml</WatchedResource>
</Context>
EOF

# Set permissions
sudo chown "$TOMCAT_USER:$TOMCAT_GROUP" "$TOMCAT_CONF/Catalina/localhost/backend.xml"
sudo chmod 640 "$TOMCAT_CONF/Catalina/localhost/backend.xml"

# Start Tomcat
echo "🚀 Starting Tomcat..."
sudo systemctl start tomcat || die "Failed to start Tomcat"

# Extended verification with more debugging
echo "⏳ Waiting for deployment to complete (up to 60 seconds)..."
DEPLOY_SUCCESS=false
for i in {1..12}; do
    # Check both Tomcat status and application health
    if curl -sSf "http://localhost:8080/backend$HEALTH_CHECK_PATH" >/dev/null 2>&1; then
        DEPLOY_SUCCESS=true
        break
    fi
    echo "Attempt $i: Application not responding yet..."
    sleep 5
done

# Final check with more detailed logging
if $DEPLOY_SUCCESS; then
    echo "✅ Deployment successful!"
    echo "🌐 Application URL: $APP_URL"
else
    echo "⚠️ Deployment failed - checking detailed logs..."
    echo "=== Last 50 lines of catalina.out ==="
    sudo tail -n 50 /opt/tomcat/logs/catalina.out
    echo "=== Checking application logs ==="
    sudo ls -la "$TOMCAT_WEBAPPS/$DEPLOY_NAME/WEB-INF/logs/" 2>/dev/null || \
    echo "No application-specific logs found"
    die "Application failed to start"
fi