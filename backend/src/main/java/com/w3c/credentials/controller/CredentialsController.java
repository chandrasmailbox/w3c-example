package com.w3c.credentials.controller;

import com.w3c.credentials.dto.IssueCredentialRequest;
import com.w3c.credentials.dto.VerificationResult;
import com.w3c.credentials.model.VerifiableCredential;
import com.w3c.credentials.repository.VerifiableCredentialRepository;
import com.w3c.credentials.service.CredentialIssuerService;
import com.w3c.credentials.service.CredentialVerifierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/credentials")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Verifiable Credentials", description = "W3C Verifiable Credentials management API")
public class CredentialsController {
    
    @Autowired
    private CredentialIssuerService issuerService;
    
    @Autowired
    private CredentialVerifierService verifierService;
    
    @Autowired
    private VerifiableCredentialRepository credentialRepository;
    
    /**
     * Issue a new verifiable credential
     */
    @PostMapping("/issue")
    @Operation(summary = "Issue a new verifiable credential", 
               description = "Creates and issues a new W3C verifiable credential for a student")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Credential issued successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<VerifiableCredential> issueCredential(
            @Parameter(description = "Credential issuance request") 
            @Valid @RequestBody IssueCredentialRequest request) {
        
        try {
            // Issue the credential
            VerifiableCredential credential = issuerService.issueBachelorDegree(request);
            
            // Save to database
            VerifiableCredential savedCredential = credentialRepository.save(credential);
            
            return new ResponseEntity<>(savedCredential, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get all credentials
     */
    @GetMapping
    @Operation(summary = "Get all verifiable credentials", 
               description = "Retrieves all stored verifiable credentials")
    public ResponseEntity<List<VerifiableCredential>> getAllCredentials() {
        try {
            List<VerifiableCredential> credentials = credentialRepository.findAll();
            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get credential by ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get credential by ID", 
               description = "Retrieves a specific verifiable credential by its ID")
    public ResponseEntity<VerifiableCredential> getCredentialById(
            @Parameter(description = "Credential ID") 
            @PathVariable String id) {
        
        Optional<VerifiableCredential> credential = credentialRepository.findById(id);
        
        if (credential.isPresent()) {
            return new ResponseEntity<>(credential.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    /**
     * Get credentials by holder DID
     */
    @GetMapping("/holder/{holderDID}")
    @Operation(summary = "Get credentials by holder DID", 
               description = "Retrieves all credentials belonging to a specific holder")
    public ResponseEntity<List<VerifiableCredential>> getCredentialsByHolder(
            @Parameter(description = "Holder DID") 
            @PathVariable String holderDID) {
        
        try {
            List<VerifiableCredential> credentials = credentialRepository.findByCredentialSubjectId(holderDID);
            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Verify a verifiable credential
     */
    @PostMapping("/verify")
    @Operation(summary = "Verify a verifiable credential", 
               description = "Verifies the authenticity and validity of a verifiable credential")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verification completed"),
        @ApiResponse(responseCode = "400", description = "Invalid credential data")
    })
    public ResponseEntity<VerificationResult> verifyCredential(
            @Parameter(description = "Verifiable credential to verify") 
            @Valid @RequestBody VerifiableCredential credential) {
        
        try {
            VerificationResult result = verifierService.verifyCredential(credential);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            VerificationResult errorResult = new VerificationResult(
                false, 
                "❌ Verification process failed due to an error: " + e.getMessage(),
                new VerificationResult.VerificationDetails(false, false, false)
            );
            return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
        }
    }
    
    /**
     * Verify credential by ID
     */
    @PostMapping("/verify/{id}")
    @Operation(summary = "Verify credential by ID", 
               description = "Verifies a stored credential by its ID")
    public ResponseEntity<VerificationResult> verifyCredentialById(
            @Parameter(description = "Credential ID to verify") 
            @PathVariable String id) {
        
        Optional<VerifiableCredential> credentialOpt = credentialRepository.findById(id);
        
        if (credentialOpt.isEmpty()) {
            VerificationResult errorResult = new VerificationResult(
                false, 
                "❌ Credential not found",
                new VerificationResult.VerificationDetails(false, false, false)
            );
            return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
        }
        
        try {
            VerificationResult result = verifierService.verifyCredential(credentialOpt.get());
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            VerificationResult errorResult = new VerificationResult(
                false, 
                "❌ Verification process failed due to an error: " + e.getMessage(),
                new VerificationResult.VerificationDetails(false, false, false)
            );
            return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Delete credential by ID
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete credential by ID", 
               description = "Deletes a verifiable credential by its ID")
    public ResponseEntity<?> deleteCredential(
            @Parameter(description = "Credential ID to delete") 
            @PathVariable String id) {
        
        try {
            credentialRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Get credentials by type
     */
    @GetMapping("/type/{type}")
    @Operation(summary = "Get credentials by type", 
               description = "Retrieves credentials of a specific type")
    public ResponseEntity<List<VerifiableCredential>> getCredentialsByType(
            @Parameter(description = "Credential type") 
            @PathVariable String type) {
        
        try {
            List<VerifiableCredential> credentials = credentialRepository.findByType(type);
            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Search credentials by student name
     */
    @GetMapping("/search")
    @Operation(summary = "Search credentials by student name", 
               description = "Searches for credentials by student name (case-insensitive)")
    public ResponseEntity<List<VerifiableCredential>> searchCredentialsByName(
            @Parameter(description = "Student name to search") 
            @RequestParam String name) {
        
        try {
            List<VerifiableCredential> credentials = credentialRepository.findByStudentName(name);
            return new ResponseEntity<>(credentials, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}