package com.w3c.credentials.model;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class Proof {
    
    @NotBlank(message = "Proof type is required")
    private String type;
    
    private LocalDateTime created;
    
    @NotBlank(message = "Proof purpose is required")
    private String proofPurpose;
    
    @NotBlank(message = "Verification method is required")
    private String verificationMethod;
    
    private String jws;
    private String proofValue;
    
    // Constructors
    public Proof() {}
    
    public Proof(String type, LocalDateTime created, String proofPurpose, String verificationMethod) {
        this.type = type;
        this.created = created;
        this.proofPurpose = proofPurpose;
        this.verificationMethod = verificationMethod;
    }
    
    public Proof(String type, LocalDateTime created, String proofPurpose, String verificationMethod, String jws) {
        this(type, created, proofPurpose, verificationMethod);
        this.jws = jws;
    }
    
    // Getters and Setters
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public LocalDateTime getCreated() { return created; }
    public void setCreated(LocalDateTime created) { this.created = created; }
    
    public String getProofPurpose() { return proofPurpose; }
    public void setProofPurpose(String proofPurpose) { this.proofPurpose = proofPurpose; }
    
    public String getVerificationMethod() { return verificationMethod; }
    public void setVerificationMethod(String verificationMethod) { this.verificationMethod = verificationMethod; }
    
    public String getJws() { return jws; }
    public void setJws(String jws) { this.jws = jws; }
    
    public String getProofValue() { return proofValue; }
    public void setProofValue(String proofValue) { this.proofValue = proofValue; }
}