PROJECT_DIR="/home/ubuntu/mk-backend"  # 🔁 Replace with your actual path
WAR_NAME="demo-0.0.1-SNAPSHOT.war"
WAR_PATH="$PROJECT_DIR/target/$WAR_NAME"
TOMCAT_WEBAPPS="/opt/tomcat/webapps"
TOMCAT_BIN="/opt/tomcat/bin"

echo "🔥 Deploying $WAR_NAME to Tomcat..."

# Stop Tomcat
echo "🛑 Stopping Tomcat..."
sudo $TOMCAT_BIN/shutdown.sh
sleep 5

echo "🧹 Cleaning up old deployment..."
sudo rm -rf $TOMCAT_WEBAPPS/demo
sudo rm -f $TOMCAT_WEBAPPS/$WAR_NAME

# Copy the WAR to Tomcat's webapps directory
echo "📦 Copying $WAR_NAME to Tomcat webapps..."
sudo cp $WAR_PATH $TOMCAT_WEBAPPS/

# Start Tomcat
echo "🚀 Starting Tomcat..."
sudo $TOMCAT_BIN/startup.sh

#this ip has a tendency of changing check in aws console
echo "✅ Deployment complete. Access the app at: http://51.21.252.97:8080/"
