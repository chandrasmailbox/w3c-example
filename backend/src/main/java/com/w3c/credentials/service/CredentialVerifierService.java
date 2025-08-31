package com.w3c.credentials.service;

import com.w3c.credentials.dto.IssuerObject;
import com.w3c.credentials.dto.VerificationResult;
import com.w3c.credentials.model.Proof;
import com.w3c.credentials.model.VerifiableCredential;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class CredentialVerifierService {
    
    private static final List<String> TRUSTED_ISSUERS = Arrays.asList(
        "did:example:university:stanford",
        "did:example:university:mit", 
        "did:example:university:harvard"
    );
    
    /**
     * Verifies a verifiable credential
     */
    public VerificationResult verifyCredential(VerifiableCredential credential) {
        // Extract issuer DID
        String issuerDID = extractIssuerDID(credential.getIssuer());
        
        // Perform verification checks
        boolean hasValidIssuer = validateIssuer(issuerDID);
        boolean hasValidProof = validateProof(credential.getProof());
        boolean isNotExpired = checkExpiration(credential);
        
        // Overall validation result
        boolean isValid = hasValidIssuer && hasValidProof && isNotExpired;
        
        // Generate verification message
        String message = generateVerificationMessage(isValid, hasValidIssuer, hasValidProof, isNotExpired);
        
        // Create verification details
        VerificationResult.VerificationDetails details = new VerificationResult.VerificationDetails(
            hasValidIssuer, hasValidProof, isNotExpired
        );
        
        return new VerificationResult(isValid, message, details);
    }
    
    /**
     * Extracts issuer DID from issuer object or string
     */
    private String extractIssuerDID(Object issuer) {
        if (issuer instanceof String) {
            return (String) issuer;
        } else if (issuer instanceof IssuerObject) {
            return ((IssuerObject) issuer).getId();
        } else {
            // Handle generic object (from MongoDB deserialization)
            try {
                @SuppressWarnings("unchecked")
                var issuerMap = (java.util.Map<String, Object>) issuer;
                return (String) issuerMap.get("id");
            } catch (Exception e) {
                return issuer.toString();
            }
        }
    }
    
    /**
     * Validates if the issuer is trusted
     */
    private boolean validateIssuer(String issuerDID) {
        return TRUSTED_ISSUERS.contains(issuerDID);
    }
    
    /**
     * Validates the proof structure and content
     */
    private boolean validateProof(Proof proof) {
        if (proof == null) {
            return false;
        }
        
        // Check if required fields are present
        return proof.getType() != null && 
               proof.getCreated() != null && 
               proof.getProofPurpose() != null && 
               proof.getVerificationMethod() != null;
    }
    
    /**
     * Checks if the credential has expired
     * For this demo, credentials are always valid (no expiration logic)
     */
    private boolean checkExpiration(VerifiableCredential credential) {
        if (credential.getExpirationDate() == null) {
            return true; // No expiration date means it doesn't expire
        }
        return credential.getExpirationDate().isAfter(LocalDateTime.now());
    }
    
    /**
     * Generates a human-readable verification message
     */
    private String generateVerificationMessage(boolean isValid, boolean hasValidIssuer, 
                                             boolean hasValidProof, boolean isNotExpired) {
        if (isValid) {
            return "✅ Credential verification successful! This is a valid credential.";
        } else {
            StringBuilder message = new StringBuilder("❌ Verification failed: ");
            boolean first = true;
            
            if (!hasValidIssuer) {
                message.append("untrusted issuer");
                first = false;
            }
            
            if (!hasValidProof) {
                if (!first) message.append(", ");
                message.append("invalid proof");
                first = false;
            }
            
            if (!isNotExpired) {
                if (!first) message.append(", ");
                message.append("credential expired");
            }
            
            return message.toString();
        }
    }
    
    /**
     * Gets the list of trusted issuers
     */
    public List<String> getTrustedIssuers() {
        return TRUSTED_ISSUERS;
    }
}