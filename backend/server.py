#!/usr/bin/env python3
"""
Wrapper script to start the Spring Boot Gradle application.
This exists to maintain compatibility with the existing supervisor configuration.
"""
import subprocess
import os
import sys
import signal
import time

def main():
    # Change to backend directory
    os.chdir('/app/backend')
    
    # Set JAVA_HOME if not set
    if 'JAVA_HOME' not in os.environ:
        os.environ['JAVA_HOME'] = '/usr/lib/jvm/java-17-openjdk-arm64'
    
    # Add Java to PATH
    os.environ['PATH'] = f"{os.environ['JAVA_HOME']}/bin:{os.environ.get('PATH', '')}"
    
    print("Starting Spring Boot W3C Verifiable Credentials Backend...")
    print(f"Java Version: {subprocess.run(['java', '-version'], capture_output=True, text=True).stderr.split()[2]}")
    print(f"Working Directory: {os.getcwd()}")
    
    # Build if necessary
    jar_path = 'build/libs/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar'
    if not os.path.exists(jar_path):
        print("Building Spring Boot application with Gradle...")
        build_process = subprocess.run(['./gradlew', 'clean', 'build', '-x', 'test'], check=True)
    
    # Start the Spring Boot application
    java_process = subprocess.Popen(['java', '-jar', jar_path])
    
    # Handle shutdown gracefully
    def signal_handler(signum, frame):
        print(f"Received signal {signum}, terminating Spring Boot application...")
        java_process.terminate()
        java_process.wait()
        sys.exit(0)
    
    signal.signal(signal.SIGTERM, signal_handler)
    signal.signal(signal.SIGINT, signal_handler)
    
    try:
        java_process.wait()
    except KeyboardInterrupt:
        java_process.terminate()
        java_process.wait()

if __name__ == '__main__':
    main()