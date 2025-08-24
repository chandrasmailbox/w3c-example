import React, { useState } from 'react';
import './App.css';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './components/ui/card';
import { Button } from './components/ui/button';
import { Input } from './components/ui/input';
import { Label } from './components/ui/label';
import { Alert, AlertDescription } from './components/ui/alert';
import { Tabs, TabsContent, TabsList, TabsTrigger } from './components/ui/tabs';
import { Separator } from './components/ui/separator';
import CredentialCard from './components/CredentialCard';
import CredentialViewer from './components/CredentialViewer';
import { VerifiableCredential, VerificationResult } from './types/credentials';
import { CredentialIssuer, DigitalWallet, CredentialVerifier } from './utils/credentialUtils';
import { Award, Shield, Check, UserCheck, Building, Wallet } from 'lucide-react';

function App() {
  // Initialize services
  const [issuer] = useState(() => new CredentialIssuer());
  const [wallet] = useState(() => new DigitalWallet());
  const [verifier] = useState(() => new CredentialVerifier());

  // State management
  const [credentials, setCredentials] = useState<VerifiableCredential[]>([]);
  const [selectedCredential, setSelectedCredential] = useState<VerifiableCredential | null>(null);
  const [verificationResult, setVerificationResult] = useState<VerificationResult | null>(null);
  
  // Form state for issuing credentials
  const [studentName, setStudentName] = useState('John Doe');
  const [degreeTitle, setDegreeTitle] = useState('Bachelor of Computer Science');
  const [studentDID, setStudentDID] = useState('did:example:student:johndoe');

  // Issue Credential Function
  const handleIssueCredential = () => {
    try {
      // Issuer creates and signs the credential
      const newCredential = issuer.issueBachelorDegree(studentDID, studentName, degreeTitle);
      
      // Holder receives and stores credential in wallet
      wallet.addCredential(newCredential);
      
      // Update UI state
      setCredentials(wallet.getCredentials());
      setSelectedCredential(newCredential);
      setVerificationResult(null);
      
      console.log('✅ Credential issued successfully:', newCredential);
    } catch (error) {
      console.error('❌ Failed to issue credential:', error);
    }
  };

  // Verify Credential Function
  const handleVerifyCredential = () => {
    if (!selectedCredential) {
      alert('Please select a credential to verify');
      return;
    }

    try {
      // Verifier validates the credential
      const result = verifier.verifyCredential(selectedCredential);
      setVerificationResult(result);
      
      console.log('🔍 Verification completed:', result);
    } catch (error) {
      console.error('❌ Verification failed:', error);
      setVerificationResult({
        isValid: false,
        message: '❌ Verification process failed due to an error',
        details: {
          hasValidIssuer: false,
          hasValidProof: false,
          isNotExpired: false
        }
      });
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-indigo-50">
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Shield className="h-8 w-8 text-blue-600" />
            <h1 className="text-3xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
              W3C Verifiable Credentials Demo
            </h1>
          </div>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Experience the complete flow of issuing, storing, and verifying digital credentials using W3C standards
          </p>
        </div>

        {/* Main Content */}
        <Tabs defaultValue="issuer" className="space-y-6">
          <TabsList className="grid w-full grid-cols-3">
            <TabsTrigger value="issuer" className="flex items-center gap-2">
              <Building className="h-4 w-4" />
              Issuer
            </TabsTrigger>
            <TabsTrigger value="holder" className="flex items-center gap-2">
              <Wallet className="h-4 w-4" />
              Holder
            </TabsTrigger>
            <TabsTrigger value="verifier" className="flex items-center gap-2">
              <UserCheck className="h-4 w-4" />
              Verifier
            </TabsTrigger>
          </TabsList>

          {/* Issuer Tab */}
          <TabsContent value="issuer" className="space-y-6">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Award className="h-5 w-5 text-blue-600" />
                  Issue New Credential
                </CardTitle>
                <CardDescription>
                  As a trusted issuer (University), create and sign a new verifiable credential
                </CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                  <div>
                    <Label htmlFor="studentName">Student Name</Label>
                    <Input
                      id="studentName"
                      value={studentName}
                      onChange={(e) => setStudentName(e.target.value)}
                      placeholder="Enter student name"
                    />
                  </div>
                  <div>
                    <Label htmlFor="studentDID">Student DID</Label>
                    <Input
                      id="studentDID"
                      value={studentDID}
                      onChange={(e) => setStudentDID(e.target.value)}
                      placeholder="did:example:student:..."
                    />
                  </div>
                </div>
                <div>
                  <Label htmlFor="degreeTitle">Degree Title</Label>
                  <Input
                    id="degreeTitle"
                    value={degreeTitle}
                    onChange={(e) => setDegreeTitle(e.target.value)}
                    placeholder="Bachelor of Computer Science"
                  />
                </div>
                <Button 
                  onClick={handleIssueCredential}
                  className="w-full bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700"
                >
                  Issue Credential
                </Button>
              </CardContent>
            </Card>
          </TabsContent>

          {/* Holder Tab */}
          <TabsContent value="holder" className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Wallet className="h-5 w-5 text-green-600" />
                    Digital Wallet
                  </CardTitle>
                  <CardDescription>
                    Your stored verifiable credentials ({credentials.length} total)
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  {credentials.length === 0 ? (
                    <div className="text-center py-8 text-muted-foreground">
                      <Wallet className="h-12 w-12 mx-auto mb-4 opacity-50" />
                      <p>No credentials in wallet</p>
                      <p className="text-sm">Issue a credential to get started</p>
                    </div>
                  ) : (
                    <div className="space-y-3">
                      {credentials.map((credential) => (
                        <CredentialCard
                          key={credential.id}
                          credential={credential}
                          onSelect={setSelectedCredential}
                          isSelected={selectedCredential?.id === credential.id}
                        />
                      ))}
                    </div>
                  )}
                </CardContent>
              </Card>

              <CredentialViewer credential={selectedCredential} />
            </div>
          </TabsContent>

          {/* Verifier Tab */}
          <TabsContent value="verifier" className="space-y-6">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
              <Card>
                <CardHeader>
                  <CardTitle className="flex items-center gap-2">
                    <Shield className="h-5 w-5 text-purple-600" />
                    Credential Verification
                  </CardTitle>
                  <CardDescription>
                    Verify the authenticity and validity of a credential
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  {selectedCredential ? (
                    <div className="space-y-4">
                      <Alert>
                        <Check className="h-4 w-4" />
                        <AlertDescription>
                          Selected credential: {selectedCredential.credentialSubject.name}'s {selectedCredential.credentialSubject.degree}
                        </AlertDescription>
                      </Alert>
                      
                      <Button 
                        onClick={handleVerifyCredential}
                        className="w-full bg-gradient-to-r from-purple-600 to-pink-600 hover:from-purple-700 hover:to-pink-700"
                      >
                        Verify Credential
                      </Button>
                    </div>
                  ) : (
                    <Alert>
                      <AlertDescription>
                        Please select a credential from the Holder tab to verify
                      </AlertDescription>
                    </Alert>
                  )}

                  {verificationResult && (
                    <>
                      <Separator />
                      <Alert className={verificationResult.isValid ? "border-green-200 bg-green-50" : "border-red-200 bg-red-50"}>
                        <AlertDescription>
                          <p className="font-medium mb-2">{verificationResult.message}</p>
                          <div className="text-sm space-y-1">
                            <div className="flex items-center gap-2">
                              <span className={verificationResult.details.hasValidIssuer ? "text-green-600" : "text-red-600"}>
                                {verificationResult.details.hasValidIssuer ? "✅" : "❌"}
                              </span>
                              <span>Trusted Issuer</span>
                            </div>
                            <div className="flex items-center gap-2">
                              <span className={verificationResult.details.hasValidProof ? "text-green-600" : "text-red-600"}>
                                {verificationResult.details.hasValidProof ? "✅" : "❌"}
                              </span>
                              <span>Valid Proof</span>
                            </div>
                            <div className="flex items-center gap-2">
                              <span className={verificationResult.details.isNotExpired ? "text-green-600" : "text-red-600"}>
                                {verificationResult.details.isNotExpired ? "✅" : "❌"}
                              </span>
                              <span>Not Expired</span>
                            </div>
                          </div>
                        </AlertDescription>
                      </Alert>
                    </>
                  )}
                </CardContent>
              </Card>

              <CredentialViewer credential={selectedCredential} />
            </div>
          </TabsContent>
        </Tabs>
      </div>
    </div>
  );
}

export default App;