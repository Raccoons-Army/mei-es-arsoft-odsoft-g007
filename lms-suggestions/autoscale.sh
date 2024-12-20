#!/bin/bash

# Thresholds for CPU and memory usage to scale the service
CPU_THRESHOLD=75  # CPU usage threshold to scale (percentage)
MEM_THRESHOLD=80  # Memory usage threshold to scale (percentage)

# Minimum and Maximum number of replicas to prevent over-scaling or under-scaling
MIN_REPLICAS=3
MAX_REPLICAS=5

# Function to get the current CPU usage of the service
get_cpu_usage() {
  docker stats --no-stream --format "{{.CPUPerc}}" $(docker ps -q -f "name=stack_lmssuggestions") | sed 's/%//'
}

# Function to get the current memory usage of the service
get_mem_usage() {
  docker stats --no-stream --format "{{.MemPerc}}" $(docker ps -q -f "name=stack_lmssuggestions") | sed 's/%//'
}

scale_db() {
  local replicas=$1
  echo "Scaling H2 Database to $replicas replicas..."
  docker service scale stack_h2=$replicas
}

# Function to scale the service up or down
scale_service() {
  local replicas=$1
  echo "Scaling Microservice LMS-Suggestions to $replicas replicas..."
  docker service scale stack_lmssuggestions=$replicas
}

# Get current CPU and memory usage
CPU_USAGE=$(get_cpu_usage)
MEM_USAGE=$(get_mem_usage)

echo "Current CPU Usage: $CPU_USAGE%"
echo "Current Memory Usage: $MEM_USAGE%"

# Calculate the desired number of replicas based on resource usage
# If CPU or memory usage exceeds the threshold, scale up
if [[ $(echo "$CPU_USAGE $CPU_THRESHOLD" | awk '{print ($1 > $2)}') -eq 1 ]] ||
   [[ $(echo "$MEM_USAGE $MEM_THRESHOLD" | awk '{print ($1 > $2)}') -eq 1 ]]; then
  echo "High resource usage detected. Scaling up..."
  DESIRED_REPLICAS=$((MAX_REPLICAS))

  scale_db $DESIRED_REPLICAS
  scale_service $DESIRED_REPLICAS
else
  echo "Resource usage is under control. Scaling down..."
  DESIRED_REPLICAS=$((MIN_REPLICAS))
  scale_db $DESIRED_REPLICAS
  scale_service $DESIRED_REPLICAS
fi