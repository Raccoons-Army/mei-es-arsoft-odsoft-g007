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
