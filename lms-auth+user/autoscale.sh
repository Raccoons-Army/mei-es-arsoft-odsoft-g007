#!/bin/bash

# Thresholds for CPU and memory usage to scale the service
CPU_UP_THRESHOLD=6000    # CPU usage percentage (scaled by 100) to scale up
CPU_DOWN_THRESHOLD=2000  # CPU usage percentage (scaled by 100) to scale down
MEM_UP_THRESHOLD=5000    # Memory usage percentage (scaled by 100) to scale up
MEM_DOWN_THRESHOLD=2000  # Memory usage percentage (scaled by 100) to scale down

# Minimum and Maximum number of replicas to prevent over-scaling or under-scaling
MIN_REPLICAS=2
MAX_REPLICAS=10

# Function to get the current CPU usage of the service
get_cpu_usage() {
  cpu_usage=$(docker stats --no-stream --format "{{.CPUPerc}}" $(docker ps -q -f "name=auth_stack_lmsauthusers") | sed 's/%//' | head -n 1)
  # Ensure the value is an integer (multiply by 100 if necessary)
  cpu_usage_int=$(echo "$cpu_usage" | awk '{print int($1 * 100)}')
  echo $cpu_usage_int
}

# Function to get the current memory usage of the service
get_mem_usage() {
  mem_usage=$(docker stats --no-stream --format "{{.MemPerc}}" $(docker ps -q -f "name=auth_stack_lmsauthusers") | sed 's/%//' | head -n 1)
  # Ensure the value is an integer (multiply by 100 if necessary)
  mem_usage_int=$(echo "$mem_usage" | awk '{print int($1 * 100)}')
  echo $mem_usage_int
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
  docker service scale auth_stack_h2=$replicas
}

# Function to scale the MS application service up or down
scale_service() {
  local replicas=$1
  echo "Scaling Microservice LMS-AuthUsers to $replicas replicas..."
  docker service scale auth_stack_lmsauthusers=$replicas
}

# Get current CPU and memory usage
CPU_USAGE=$(get_cpu_usage)
MEM_USAGE=$(get_mem_usage)

echo "Current CPU Usage: ${CPU_USAGE}%"
echo "Current Memory Usage: ${MEM_USAGE}%"

# Get current number of replicas
CURRENT_REPLICAS=$(get_replicas auth_stack_lmsauthusers)

# Initialize desired replicas with the current count
DESIRED_REPLICAS=$CURRENT_REPLICAS

# Scale Up Logic
if [[ $CPU_USAGE -gt $CPU_UP_THRESHOLD ]] || [[ $MEM_USAGE -gt $MEM_UP_THRESHOLD ]]; then
  echo "High resource usage detected. Scaling up..."
  DESIRED_REPLICAS=$((CURRENT_REPLICAS + 2))
fi

# Scale Down Logic
if [[ $CPU_USAGE -lt $CPU_DOWN_THRESHOLD ]] && [[ $MEM_USAGE -lt $MEM_DOWN_THRESHOLD ]]; then
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

if [[ $DESIRED_REPLICAS -ne $CURRENT_REPLICAS ]]; then
  echo "Scaling services from $CURRENT_REPLICAS to $DESIRED_REPLICAS replicas"

  # Scale the services
  scale_db $DESIRED_REPLICAS
  scale_service $DESIRED_REPLICAS
else
  echo "Desired replicas match current replicas ($CURRENT_REPLICAS). No scaling needed."
fi