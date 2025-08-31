#!/usr/bin/env python3
"""
FastAPI wrapper that starts the Spring Boot application in the background.
This maintains compatibility with the existing supervisor uvicorn configuration.
"""
import subprocess
import os
import threading
import time
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
import httpx
import uvicorn

# Initialize the FastAPI app
app = FastAPI(title="W3C Verifiable Credentials Proxy", version="1.0.0")

# Add CORS middleware
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Global variable to track the Spring Boot process
spring_boot_process = None

def start_spring_boot():
    """Start the Spring Boot application in the background."""
    global spring_boot_process
    
    # Change to backend directory
    os.chdir('/app/backend')
    
    # Set JAVA_HOME if not set
    if 'JAVA_HOME' not in os.environ:
        os.environ['JAVA_HOME'] = '/usr/lib/jvm/java-17-openjdk-arm64'
    
    # Add Java to PATH
    os.environ['PATH'] = f"{os.environ['JAVA_HOME']}/bin:{os.environ.get('PATH', '')}"
    
    print("Starting Spring Boot W3C Verifiable Credentials Backend...")
    
    # Build if necessary
    jar_path = 'build/libs/verifiable-credentials-backend-0.0.1-SNAPSHOT.jar'
    if not os.path.exists(jar_path):
        print("Building Spring Boot application with Gradle...")
        subprocess.run(['./gradlew', 'clean', 'build', '-x', 'test'], check=True)
    
    # Start the Spring Boot application on port 8002 (to avoid conflict)
    spring_boot_process = subprocess.Popen([
        'java', '-jar', jar_path, '--server.port=8002'
    ])
    
    # Wait a bit for Spring Boot to start
    time.sleep(10)

# Start Spring Boot when the module is imported
threading.Thread(target=start_spring_boot, daemon=True).start()

@app.get("/api/health")
async def health_check():
    """Health check endpoint."""
    return {"status": "healthy", "service": "W3C Verifiable Credentials Proxy"}

@app.post("/api/issue")
async def issue_credential(request_data: dict):
    """Proxy request to Spring Boot issue endpoint."""
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(
                "http://localhost:8002/api/issue",
                json=request_data,
                timeout=30.0
            )
            return response.json()
        except Exception as e:
            return {"error": f"Failed to connect to Spring Boot backend: {str(e)}"}

@app.post("/api/verify")
async def verify_credential(request_data: dict):
    """Proxy request to Spring Boot verify endpoint."""
    async with httpx.AsyncClient() as client:
        try:
            response = await client.post(
                "http://localhost:8002/api/verify",
                json=request_data,
                timeout=30.0
            )
            return response.json()
        except Exception as e:
            return {"error": f"Failed to connect to Spring Boot backend: {str(e)}"}

@app.on_event("shutdown")
async def shutdown_event():
    """Gracefully shutdown Spring Boot when FastAPI shuts down."""
    global spring_boot_process
    if spring_boot_process:
        spring_boot_process.terminate()
        spring_boot_process.wait()

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8001)