import { VerifiableCredential, VerificationResult } from '../types/credentials';

// Mock Issuer service - simulates a University issuing credentials
export class CredentialIssuer {
  private issuerDID = "did:example:university:stanford";
  private issuerName = "Stanford University";

  // Issue a Bachelor Degree credential
  public issueBachelorDegree(studentDID: string, studentName: string, degree: string): VerifiableCredential {
    const currentDate = new Date().toISOString();
    
    return {
      "@context": [
        "https://www.w3.org/2018/credentials/v1",
        "https://www.w3.org/2018/credentials/examples/v1"
      ],
      id: `urn:uuid:${this.generateUUID()}`,
      type: ["VerifiableCredential", "UniversityDegreeCredential"],
      issuer: {
        id: this.issuerDID,
        name: this.issuerName
      },
      issuanceDate: currentDate,
      credentialSubject: {
        id: studentDID,
        name: studentName,
        degree: degree,
        university: this.issuerName,
        graduationDate: currentDate
      },
      proof: {
        type: "Ed25519Signature2018",
        created: currentDate,
        proofPurpose: "assertionMethod",
        verificationMethod: `${this.issuerDID}#keys-1`,
        jws: this.generateMockSignature()
      }
    };
  }

  private generateUUID(): string {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
      const r = Math.random() * 16 | 0;
      const v = c === 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }

  private generateMockSignature(): string {
    // Mock JWS signature - in real implementation this would be cryptographically signed
    return "eyJhbGciOiJFZERTQSIsImtpZCI6IiNrZXlzLTEifQ..mock_signature_" + Math.random().toString(36).substring(2);
  }
}

// Digital Wallet - manages credential storage
export class DigitalWallet {
  private credentials: VerifiableCredential[] = [];

  public addCredential(credential: VerifiableCredential): void {
    this.credentials.push(credential);
  }

  public getCredentials(): VerifiableCredential[] {
    return [...this.credentials];
  }

  public removeCredential(credentialId: string): void {
    this.credentials = this.credentials.filter(cred => cred.id !== credentialId);
  }

  public getCredentialById(id: string): VerifiableCredential | undefined {
    return this.credentials.find(cred => cred.id === id);
  }
}

// Verifier service - validates credentials
export class CredentialVerifier {
  private trustedIssuers = [
    "did:example:university:stanford",
    "did:example:university:mit",
    "did:example:university:harvard"
  ];

  public verifyCredential(credential: VerifiableCredential): VerificationResult {
    const issuerDID = typeof credential.issuer === 'string' 
      ? credential.issuer 
      : credential.issuer.id;

    const hasValidIssuer = this.trustedIssuers.includes(issuerDID);
    const hasValidProof = this.validateProof(credential.proof);
    const isNotExpired = this.checkExpiration(credential);

    const isValid = hasValidIssuer && hasValidProof && isNotExpired;

    let message = "";
    if (isValid) {
      message = "✅ Credential verification successful! This is a valid credential.";
    } else {
      const issues = [];
      if (!hasValidIssuer) issues.push("untrusted issuer");
      if (!hasValidProof) issues.push("invalid proof");
      if (!isNotExpired) issues.push("credential expired");
      message = `❌ Verification failed: ${issues.join(", ")}`;
    }

    return {
      isValid,
      message,
      details: {
        hasValidIssuer,
        hasValidProof,
        isNotExpired
      }
    };
  }

  private validateProof(proof: any): boolean {
    // Basic proof validation - check if required fields exist
    return !!(proof && proof.type && proof.created && proof.proofPurpose && proof.verificationMethod);
  }

  private checkExpiration(credential: VerifiableCredential): boolean {
    // For this demo, credentials are always valid (no expiration)
    // In real implementation, check expirationDate if present
    return true;
  }
}