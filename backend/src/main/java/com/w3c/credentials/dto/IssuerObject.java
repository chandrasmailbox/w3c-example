package com.w3c.credentials.dto;

public class IssuerObject {
    
    private String id;
    private String name;
    
    // Constructors
    public IssuerObject() {}
    
    public IssuerObject(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}