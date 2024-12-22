#!/bin/bash

# Thresholds for CPU and memory usage to scale the service
CPU_UP_THRESHOLD=75    # CPU usage percentage to scale up
CPU_DOWN_THRESHOLD=20  # CPU usage percentage to scale down
MEM_UP_THRESHOLD=80    # Memory usage percentage to scale up
MEM_DOWN_THRESHOLD=30  # Memory usage percentage to scale down

# Minimum and Maximum number of replicas to prevent over-scaling or under-scaling
MIN_REPLICAS=2
MAX_REPLICAS=10

# Function to get the current CPU usage of the service
get_cpu_usage() {
  docker stats --no-stream --format "{{.CPUPerc}}" $(docker ps -q -f "name=recommendations_stack_lmsrecommendations") | sed 's/%//'
}

# Function to get the current memory usage of the service
get_mem_usage() {
  docker stats --no-stream --format "{{.MemPerc}}" $(docker ps -q -f "name=recommendations_stack_lmsrecommendations") | sed 's/%//'
}

# Function to get the current number of replicas for a service
get_replicas() {
  local service_name=$1
  docker service ls --format "{{.Name}} {{.Replicas}}" | grep "^${service_name}" | awk '{print $2}' | awk -F'/' '{print $1}'
}

# Function to scale the H2 database service up or down
scale_db() {
  local replicas=$1
  echo "Scaling H2 Database to $replicas replicas..."
  docker service scale recommendations_stack_h2=$replicas
}

# Function to scale the MS application service up or down
scale_service() {
  local replicas=$1
  echo "Scaling Microservice LMS-Recommendations to $replicas replicas..."
  docker service scale recommendations_stack_lmsrecommendations=$replicas
}

# Get current CPU and memory usage
CPU_USAGE=$(get_cpu_usage)
MEM_USAGE=$(get_mem_usage)

echo "Current CPU Usage: $CPU_USAGE%"
echo "Current Memory Usage: $MEM_USAGE%"

# Get current number of replicas
CURRENT_REPLICAS=$(get_replicas recommendations_stack_lmsrecommendations)

# Initialize desired replicas with the current count
DESIRED_REPLICAS=$CURRENT_REPLICAS

# Scale Up Logic
if [[ $(echo "$CPU_USAGE $CPU_UP_THRESHOLD" | awk '{print ($1 > $2)}') -eq 1 ]] ||
   [[ $(echo "$MEM_USAGE $MEM_UP_THRESHOLD" | awk '{print ($1 > $2)}') -eq 1 ]]; then
  echo "High resource usage detected. Scaling up..."
  DESIRED_REPLICAS=$((CURRENT_REPLICAS + 1))
fi

# Scale Down Logic
if [[ $(echo "$CPU_USAGE $CPU_DOWN_THRESHOLD" | awk '{print ($1 < $2)}') -eq 1 ]] &&
   [[ $(echo "$MEM_USAGE $MEM_DOWN_THRESHOLD" | awk '{print ($1 < $2)}') -eq 1 ]]; then
  echo "Low resource usage detected. Scaling down..."
  DESIRED_REPLICAS=$((CURRENT_REPLICAS - 1))
fi

# Ensure the desired replicas stay within the minimum and maximum bounds
if [[ $DESIRED_REPLICAS -lt $MIN_REPLICAS ]]; then
  DESIRED_REPLICAS=$MIN_REPLICAS
elif [[ $DESIRED_REPLICAS -gt $MAX_REPLICAS ]]; then
  DESIRED_REPLICAS=$MAX_REPLICAS
fi

echo "Adjusted desired replicas: $DESIRED_REPLICAS"

# Scale the services
scale_db $DESIRED_REPLICAS
scale_service $DESIRED_REPLICAS