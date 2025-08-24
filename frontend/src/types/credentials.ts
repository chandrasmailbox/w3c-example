// W3C Verifiable Credentials Types
export interface VerifiableCredential {
  "@context": string[];
  id: string;
  type: string[];
  issuer: string | IssuerObject;
  issuanceDate: string;
  credentialSubject: CredentialSubject;
  proof: Proof;
}

export interface IssuerObject {
  id: string;
  name?: string;
}

export interface CredentialSubject {
  id: string;
  [key: string]: any;
}

export interface Proof {
  type: string;
  created: string;
  proofPurpose: string;
  verificationMethod: string;
  jws?: string;
  proofValue?: string;
}

// Application State Types
export interface WalletState {
  credentials: VerifiableCredential[];
}

export interface VerificationResult {
  isValid: boolean;
  message: string;
  details: {
    hasValidIssuer: boolean;
    hasValidProof: boolean;
    isNotExpired: boolean;
  };
}