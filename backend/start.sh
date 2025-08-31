#!/bin/bash
# Start script for Spring Boot W3C Verifiable Credentials Backend

# Set Java home if not set
if [ -z "$JAVA_HOME" ]; then
    export JAVA_HOME="/usr/lib/jvm/java-17-openjdk-amd64"
fi

# Add Java to PATH
export PATH="$JAVA_HOME/bin:$PATH"

# Set working directory
cd /app/backend

# Build the application using Gradle if needed
if [ ! -f "build/libs/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar" ]; then
    echo "Building Spring Boot application with Gradle..."
    ./gradlew clean build -x test
fi

echo "Starting Spring Boot W3C Verifiable Credentials Backend..."
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Working Directory: $(pwd)"

# Run the Spring Boot application
java -jar build/libs/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar