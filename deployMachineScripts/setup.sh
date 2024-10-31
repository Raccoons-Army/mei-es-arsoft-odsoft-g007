#!/bin/bash

# Update package lists
sudo apt update

# Install wget and unzip
sudo apt install wget unzip -y

# Install curl and git
sudo apt install -y curl git

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

# Download and install H2 Database
H2_VERSION="2.3.232"
H2_ZIP_NAME="h2-2024-08-11"
wget "https://github.com/h2database/h2database/releases/download/version-${H2_VERSION}/h2-${H2_ZIP_NAME}.zip"
unzip "h2-${H2_ZIP_NAME}.zip"
rm "h2-${H2_ZIP_NAME}.zip"  # Clean up the zip file after extraction

# Variables for H2 service
USERNAME="root"  # Replace with your actual username if different
H2_JAR_PATH="/root/h2/bin/h2-${H2_VERSION}.jar"  # Adjust to match extracted jar file name
SERVICE_FILE="/etc/systemd/system/h2.service"

# Create the H2 service file
sudo bash -c "cat > ${SERVICE_FILE} <<EOF
[Unit]
Description=H2 Database Server
After=network.target

[Service]
User=${USERNAME}
ExecStartPre=/bin/bash -c 'fuser -k 9092/tcp || true'  # Kill any process using port 9092
ExecStart=/root/.asdf/shims/java -jar ${H2_JAR_PATH} -tcp -tcpAllowOthers -tcpPort 9092
SuccessExitStatus=143
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF"

# Reload systemd to recognize the new service
sudo systemctl daemon-reload

# Enable and start the H2 service
sudo systemctl enable h2
sudo systemctl start h2

# Check H2 service status
sudo systemctl status h2

