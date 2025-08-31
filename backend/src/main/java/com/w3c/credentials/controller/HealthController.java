package com.w3c.credentials.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Health Check", description = "Application health and status endpoints")
public class HealthController {
    
    @Autowired
    private MongoTemplate mongoTemplate;
    
    /**
     * Basic health check endpoint
     */
    @GetMapping("/")
    @Operation(summary = "Health check", description = "Basic application health check")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "W3C Verifiable Credentials API is running!");
        response.put("timestamp", LocalDateTime.now());
        response.put("status", "healthy");
        response.put("version", "1.0.0");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    /**
     * Detailed health status
     */
    @GetMapping("/health")
    @Operation(summary = "Detailed health status", description = "Detailed application and database health status")
    public ResponseEntity<Map<String, Object>> detailedHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("application", "W3C Verifiable Credentials Backend");
        response.put("version", "1.0.0");
        
        // Check database connectivity
        Map<String, Object> database = new HashMap<>();
        try {
            // Attempt to ping MongoDB
            mongoTemplate.getCollection("verifiable_credentials").estimatedDocumentCount();
            database.put("status", "connected");
            database.put("type", "MongoDB");
        } catch (Exception e) {
            database.put("status", "disconnected");
            database.put("error", e.getMessage());
        }
        response.put("database", database);
        
        // Overall status
        boolean isHealthy = "connected".equals(database.get("status"));
        response.put("status", isHealthy ? "healthy" : "unhealthy");
        
        HttpStatus httpStatus = isHealthy ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE;
        return new ResponseEntity<>(response, httpStatus);
    }
    
    /**
     * API information
     */
    @GetMapping("/info")
    @Operation(summary = "API information", description = "Information about the API and its capabilities")
    public ResponseEntity<Map<String, Object>> apiInfo() {
        Map<String, Object> response = new HashMap<>();
        response.put("name", "W3C Verifiable Credentials API");
        response.put("description", "Spring Boot API for managing W3C Verifiable Credentials");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now());
        
        Map<String, String> endpoints = new HashMap<>();
        endpoints.put("issue", "POST /api/credentials/issue");
        endpoints.put("verify", "POST /api/credentials/verify");
        endpoints.put("list", "GET /api/credentials");
        endpoints.put("get", "GET /api/credentials/{id}");
        endpoints.put("search", "GET /api/credentials/search?name={name}");
        endpoints.put("swagger", "GET /swagger-ui/index.html");
        
        response.put("endpoints", endpoints);
        response.put("features", new String[]{
            "W3C Verifiable Credentials issuance",
            "Credential verification",
            "MongoDB persistence", 
            "RESTful API",
            "Swagger documentation"
        });
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}