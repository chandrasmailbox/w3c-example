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

# Install Maven if needed and build the application
if [ ! -f "target/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar" ]; then
    echo "Building Spring Boot application..."
    ./mvnw clean package -DskipTests
fi

echo "Starting Spring Boot W3C Verifiable Credentials Backend..."
echo "Java Version: $(java -version 2>&1 | head -1)"
echo "Working Directory: $(pwd)"

# Run the Spring Boot application
java -jar target/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar