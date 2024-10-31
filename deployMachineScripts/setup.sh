# Update
sudo apt update

# Install command wget and unzip
sudo apt install wget -y
sudo apt install unzip -y

# Install git
sudo apt install -y curl git

# Install asdf
echo "Installing asdf version manager..."
git clone https://github.com/asdf-vm/asdf.git ~/.asdf --branch v0.14.1
# in case you use ~/.bashrc change the 2 lines below to ~/.bashrc
echo -e '\n. $HOME/.asdf/asdf.sh' >> ~/.bashrc
echo -e '\n. $HOME/.asdf/completions/asdf.bash' >> ~/.bashrc
source ~/.bashrc

# asdf add java and maven plugins
asdf plugin add java
asdf plugin add maven

# Install jdk 17 and maven 3.9
asdf install java openjdk-17
asdf install maven 3.9.9
# Set global versions
asdf global java openjdk-17
asdf global maven 3.9.9

# Install and boot h2 
wget https://github.com/h2database/h2database/releases/download/version-2.3.232/h2-2024-08-11.zip
unzip h2-2024-08-11.zip
#java -jar h2/bin/h2*.jar -tcp -tcpAllowOthers -tcpPort 9092

# add h2 as a service
# Variables
USERNAME="root"  # Replace with your actual username
H2_JAR_PATH="/root/h2/bin/h2-2.3.232.jar"  # Replace with the actual path to H2 jar
SERVICE_FILE="/etc/systemd/system/h2.service"

# Create the H2 service file
sudo bash -c "cat > ${SERVICE_FILE} <<EOF
[Unit]
Description=H2 Database Server
After=network.target

[Service]
User=${USERNAME}
ExecStartPre=/bin/bash -c 'fuser -k 9092/tcp || true'  # Kill any process using port 9092
ExecStart=/${USERNAME}/.asdf/shims/java -jar ${H2_JAR_PATH} -tcp -tcpAllowOthers -tcpPort 9092
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


