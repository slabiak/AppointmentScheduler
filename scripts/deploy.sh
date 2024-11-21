#!/bin/bash

# Stop the currently running application, if any
if pgrep -f "appointmentscheduler-1.0.5.jar" > /dev/null; then
    echo "Stopping existing application..."
    pkill -f "appointmentscheduler-1.0.5.jar"
fi

# Start the new application
echo "Starting new application..."
nohup java -jar /home/ec2-user/app/appointmentscheduler-1.0.5.jar > /home/ec2-user/app/application.log 2>&1 &