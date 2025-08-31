package com.w3c.credentials.dto;

public class VerificationResult {
    
    private boolean isValid;
    private String message;
    private VerificationDetails details;
    
    // Constructors
    public VerificationResult() {}
    
    public VerificationResult(boolean isValid, String message, VerificationDetails details) {
        this.isValid = isValid;
        this.message = message;
        this.details = details;
    }
    
    // Getters and Setters
    public boolean isValid() { return isValid; }
    public void setValid(boolean valid) { isValid = valid; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public VerificationDetails getDetails() { return details; }
    public void setDetails(VerificationDetails details) { this.details = details; }
    
    // Inner class for verification details
    public static class VerificationDetails {
        private boolean hasValidIssuer;
        private boolean hasValidProof;
        private boolean isNotExpired;
        
        public VerificationDetails() {}
        
        public VerificationDetails(boolean hasValidIssuer, boolean hasValidProof, boolean isNotExpired) {
            this.hasValidIssuer = hasValidIssuer;
            this.hasValidProof = hasValidProof;
            this.isNotExpired = isNotExpired;
        }
        
        // Getters and Setters
        public boolean isHasValidIssuer() { return hasValidIssuer; }
        public void setHasValidIssuer(boolean hasValidIssuer) { this.hasValidIssuer = hasValidIssuer; }
        
        public boolean isHasValidProof() { return hasValidProof; }
        public void setHasValidProof(boolean hasValidProof) { this.hasValidProof = hasValidProof; }
        
        public boolean isNotExpired() { return isNotExpired; }
        public void setNotExpired(boolean notExpired) { isNotExpired = notExpired; }
    }
}