package com.w3c.credentials.service;

import com.w3c.credentials.dto.IssueCredentialRequest;
import com.w3c.credentials.dto.IssuerObject;
import com.w3c.credentials.model.CredentialSubject;
import com.w3c.credentials.model.Proof;
import com.w3c.credentials.model.VerifiableCredential;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.UUID;

@Service
public class CredentialIssuerService {
    
    private static final String ISSUER_DID = "did:example:university:stanford";
    private static final String ISSUER_NAME = "Stanford University";
    
    /**
     * Issues a Bachelor Degree credential for a student
     */
    public VerifiableCredential issueBachelorDegree(IssueCredentialRequest request) {
        LocalDateTime currentTime = LocalDateTime.now();
        String credentialId = "urn:uuid:" + UUID.randomUUID().toString();
        
        // Create issuer object
        IssuerObject issuer = new IssuerObject(ISSUER_DID, ISSUER_NAME);
        
        // Create credential subject
        CredentialSubject credentialSubject = new CredentialSubject(
            request.getStudentDID(),
            request.getStudentName(),
            request.getDegreeTitle(),
            request.getUniversity() != null ? request.getUniversity() : ISSUER_NAME,
            currentTime.toString()
        );
        
        // Create proof (mock signature)
        Proof proof = new Proof(
            "Ed25519Signature2018",
            currentTime,
            "assertionMethod",
            ISSUER_DID + "#keys-1",
            generateMockSignature()
        );
        
        // Create verifiable credential
        VerifiableCredential credential = new VerifiableCredential(
            credentialId,
            Arrays.asList(
                "https://www.w3.org/2018/credentials/v1",
                "https://www.w3.org/2018/credentials/examples/v1"
            ),
            Arrays.asList("VerifiableCredential", "UniversityDegreeCredential"),
            issuer,
            currentTime,
            credentialSubject,
            proof
        );
        
        return credential;
    }
    
    /**
     * Generates a mock JWS signature for demonstration purposes
     * In production, this would be replaced with actual cryptographic signing
     */
    private String generateMockSignature() {
        return "eyJhbGciOiJFZERTQSIsImtpZCI6IiNrZXlzLTEifQ..mock_signature_" + 
               UUID.randomUUID().toString().substring(0, 8);
    }
    
    /**
     * Gets the issuer DID
     */
    public String getIssuerDID() {
        return ISSUER_DID;
    }
    
    /**
     * Gets the issuer name
     */
    public String getIssuerName() {
        return ISSUER_NAME;
    }
}