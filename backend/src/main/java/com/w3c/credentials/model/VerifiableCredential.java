package com.w3c.credentials.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "verifiable_credentials")
public class VerifiableCredential {
    
    @Id
    private String id;
    
    @JsonProperty("@context")
    @NotEmpty(message = "Context is required")
    private List<String> context;
    
    @NotEmpty(message = "Type is required")
    private List<String> type;
    
    @NotNull(message = "Issuer is required")
    private Object issuer; // Can be String or IssuerObject
    
    @NotNull(message = "Issuance date is required")
    private LocalDateTime issuanceDate;
    
    @NotNull(message = "Credential subject is required")
    @Valid
    private CredentialSubject credentialSubject;
    
    @NotNull(message = "Proof is required")
    @Valid
    private Proof proof;
    
    private LocalDateTime expirationDate;
    
    // Constructors
    public VerifiableCredential() {}
    
    public VerifiableCredential(String id, List<String> context, List<String> type, 
                              Object issuer, LocalDateTime issuanceDate, 
                              CredentialSubject credentialSubject, Proof proof) {
        this.id = id;
        this.context = context;
        this.type = type;
        this.issuer = issuer;
        this.issuanceDate = issuanceDate;
        this.credentialSubject = credentialSubject;
        this.proof = proof;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public List<String> getContext() { return context; }
    public void setContext(List<String> context) { this.context = context; }
    
    public List<String> getType() { return type; }
    public void setType(List<String> type) { this.type = type; }
    
    public Object getIssuer() { return issuer; }
    public void setIssuer(Object issuer) { this.issuer = issuer; }
    
    public LocalDateTime getIssuanceDate() { return issuanceDate; }
    public void setIssuanceDate(LocalDateTime issuanceDate) { this.issuanceDate = issuanceDate; }
    
    public CredentialSubject getCredentialSubject() { return credentialSubject; }
    public void setCredentialSubject(CredentialSubject credentialSubject) { this.credentialSubject = credentialSubject; }
    
    public Proof getProof() { return proof; }
    public void setProof(Proof proof) { this.proof = proof; }
    
    public LocalDateTime getExpirationDate() { return expirationDate; }
    public void setExpirationDate(LocalDateTime expirationDate) { this.expirationDate = expirationDate; }
}