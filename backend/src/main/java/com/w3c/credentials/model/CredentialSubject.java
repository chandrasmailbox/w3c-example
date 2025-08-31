package com.w3c.credentials.model;

import jakarta.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

public class CredentialSubject {
    
    @NotBlank(message = "Subject ID is required")
    private String id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    private String degree;
    private String university;
    private String graduationDate;
    
    // Additional dynamic properties
    private Map<String, Object> additionalProperties = new HashMap<>();
    
    // Constructors
    public CredentialSubject() {}
    
    public CredentialSubject(String id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public CredentialSubject(String id, String name, String degree, String university, String graduationDate) {
        this.id = id;
        this.name = name;
        this.degree = degree;
        this.university = university;
        this.graduationDate = graduationDate;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDegree() { return degree; }
    public void setDegree(String degree) { this.degree = degree; }
    
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
    
    public String getGraduationDate() { return graduationDate; }
    public void setGraduationDate(String graduationDate) { this.graduationDate = graduationDate; }
    
    public Map<String, Object> getAdditionalProperties() { return additionalProperties; }
    public void setAdditionalProperties(Map<String, Object> additionalProperties) { this.additionalProperties = additionalProperties; }
    
    public void addProperty(String key, Object value) {
        this.additionalProperties.put(key, value);
    }
}