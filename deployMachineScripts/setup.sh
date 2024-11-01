#!/bin/bash

# Update package lists
sudo apt update

# Install wget and unzip
sudo apt install wget unzip -y

# Install curl and git
sudo apt install -y curl git

# Install screen
sudo apt-get install screen -y

# Install asdf
echo "Installing asdf version manager..."
git clone https://github.com/asdf-vm/asdf.git ~/.asdf --branch v0.14.1

# Add asdf to bash profile
echo -e '\n. $HOME/.asdf/asdf.sh' >> ~/.bashrc
echo -e '\n. $HOME/.asdf/completions/asdf.bash' >> ~/.bashrc
source ~/.bashrc

# Install java and maven plugins with asdf
asdf plugin add java
asdf plugin add maven

# Install JDK 17 and Maven 3.9
asdf install java openjdk-17
asdf install maven 3.9.9

# Set global versions for Java and Maven
asdf global java openjdk-17
asdf global maven 3.9.9


# Create a script to start the Java application
START_SCRIPT="startApp.sh"
echo "Creating the application start script: $START_SCRIPT"

cat > "$START_SCRIPT" << 'EOF'
#!/bin/bash

# Default values for the arguments
PORT=2224
ENV_FILE="cfg/myCfg.env"
JAR_PATH="deploy/*.jar"
CONFIG_LOCATION="cfg/"

# Parse arguments
while [[ "$#" -gt 0 ]]; do
    case $1 in
        --port) PORT="$2"; shift ;;
        --env-file) ENV_FILE="$2"; shift ;;
        --jar-path) JAR_PATH="$2"; shift ;;
        --config-location) CONFIG_LOCATION="$2"; shift ;;
        *) echo "Unknown parameter passed: $1"; exit 1 ;;
    esac
    shift
done

# Kill any existing App processes
fuser -k $PORT/tcp || true

# Source environment variables
if [ -f "$ENV_FILE" ]; then
  export $(grep -v '^#' $ENV_FILE | xargs)
  source "$ENV_FILE"
else
  echo "Environment file $ENV_FILE not found!"
  exit 1
fi

# Start the Java application in the background, redirecting output to app.log
nohup java -jar "$JAR_PATH" --server.port="$PORT" --spring.config.location="$CONFIG_LOCATION" > app.log 2>&1 &

# Provide feedback
echo "Application started on port $PORT with environment file $ENV_FILE and config location $CONFIG_LOCATION. Logs are in app.log."

# Example of how to run the script
# ./start_app.sh --port 8080 --env-file "/path/to/env/file" --jar-path "/path/to/app.jar" --config-location "/custom/config/location/"

EOF

# Make the start script executable
chmod +x "$START_SCRIPT"
