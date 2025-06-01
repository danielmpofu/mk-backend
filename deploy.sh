#!/bin/bash

# Configuration - using relative paths since we're in the project dir
PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
GIT_REPO="https://github.com/danielmpofu/mk-backend.git"
GIT_BRANCH="main"
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_USER="github"
LOG_FILE="/var/log/mk-backend-deploy.log"
JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"

# Ensure script can only be run from project directory
if [ ! -f "$PROJECT_DIR/pom.xml" ]; then
    echo "❌ Error: This script must be run from the project root directory!"
    exit 1
fi

# Setup environment
export JAVA_HOME=$JAVA_HOME
export PATH=$JAVA_HOME/bin:$PATH

# Logging function
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" | sudo tee -a $LOG_FILE
}

# Error handling
error_exit() {
    log "❌ ERROR: $1"
    log "Check logs: sudo tail -n 20 $LOG_FILE"
    exit 1
}

# Verify Java
java -version >/dev/null 2>&1 || error_exit "Java 17 not found!"

# Git operations
log "🔄 Updating Git repository..."
git fetch origin || error_exit "Git fetch failed"
git checkout $GIT_BRANCH || error_exit "Checkout failed"
git reset --hard origin/$GIT_BRANCH || error_exit "Git reset failed"
log "✓ At commit: $(git rev-parse --short HEAD)"

# Build project
log "⚙️ Building with Maven..."
mvn clean package -DskipTests || error_exit "Maven build failed"

[ ! -f "$WAR_PATH" ] && error_exit "WAR file not found at $WAR_PATH"
log "✓ Built WAR: $(du -h $WAR_PATH | cut -f1)"

# Tomcat deployment
log "🛑 Stopping Tomcat..."
sudo systemctl stop tomcat || error_exit "Failed to stop Tomcat"
sleep 5

log "🧹 Cleaning old deployment..."
sudo rm -rf $TOMCAT_WEBAPPS/demo* || error_exit "Cleanup failed"

log "📦 Deploying new WAR..."
sudo cp $WAR_PATH $TOMCAT_WEBAPPS/ || error_exit "Copy failed"
sudo chown $TOMCAT_USER:$TOMCAT_USER $TOMCAT_WEBAPPS/$WAR_NAME

log "🚀 Starting Tomcat..."
sudo systemctl start tomcat || error_exit "Start failed"

# Verification
log "⏳ Waiting for deployment (max 30 seconds)..."
for i in {1..6}; do
    if curl -sSf http://localhost:8080/demo/ >/dev/null 2>&1; then
        log "✅ Deployment successful!"
        log "🌐 Application URL: http://$(curl -s ifconfig.me):8080/demo/"
        exit 0
    fi
    sleep 5
done

error_exit "Deployment timeout - check Tomcat logs: sudo journalctl -u tomcat -n 50"