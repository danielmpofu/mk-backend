#!/bin/bash

PROJECT_DIR="/home/ubuntu/mk-backend"
GIT_BRANCH="main"
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_BIN="/opt/tomcat/bin"

echo "🔄 Pulling latest code from Git..."
cd $PROJECT_DIR
git stash save --keep-index --quiet
git pull origin $GIT_BRANCH

if [ $? -ne 0 ]; then
    echo "❌ Git pull failed. Fix the issue before continuing."
    exit 1
fi

echo "⚙️ Building project with Maven..."
mvn clean package -DskipTests

if [ ! -f "$WAR_PATH" ]; then
    echo "❌ Build failed or $WAR_NAME not found. Exiting."
    exit 1
fi

echo "🛑 Stopping Tomcat..."
sudo $TOMCAT_BIN/shutdown.sh
sleep 5

echo "🧹 Cleaning old deployment..."
sudo rm -rf $TOMCAT_WEBAPPS/demo
sudo rm -f $TOMCAT_WEBAPPS/$WAR_NAME

echo "📦 Copying new WAR file to Tomcat..."
sudo cp $WAR_PATH $TOMCAT_WEBAPPS/

echo "🚀 Starting Tomcat..."
sudo $TOMCAT_BIN/startup.sh

echo "✅ Deployment complete. Visit: http://51.21.252.97:8080/"
