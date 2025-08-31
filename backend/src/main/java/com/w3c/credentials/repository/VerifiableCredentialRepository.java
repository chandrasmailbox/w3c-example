package com.w3c.credentials.repository;

import com.w3c.credentials.model.VerifiableCredential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VerifiableCredentialRepository extends MongoRepository<VerifiableCredential, String> {
    
    /**
     * Find credentials by credential subject ID (holder DID)
     */
    @Query("{'credentialSubject.id': ?0}")
    List<VerifiableCredential> findByCredentialSubjectId(String subjectId);
    
    /**
     * Find credentials by issuer DID
     */
    @Query("{'issuer.id': ?0}")
    List<VerifiableCredential> findByIssuerDID(String issuerDID);
    
    /**
     * Find credentials issued after a specific date
     */
    List<VerifiableCredential> findByIssuanceDateAfter(LocalDateTime date);
    
    /**
     * Find credentials by type
     */
    @Query("{'type': {$in: [?0]}}")
    List<VerifiableCredential> findByType(String type);
    
    /**
     * Find credentials that are not expired
     */
    @Query("{'expirationDate': {$gt: ?0}}")
    List<VerifiableCredential> findNotExpiredCredentials(LocalDateTime currentTime);
    
    /**
     * Find credentials by student name (in credential subject)
     */
    @Query("{'credentialSubject.name': {$regex: ?0, $options: 'i'}}")
    List<VerifiableCredential> findByStudentName(String name);
}