import React from 'react';
import { Card, CardContent, CardHeader, CardTitle } from './ui/card';
import { VerifiableCredential } from '../types/credentials';
import { Code2 } from 'lucide-react';

interface CredentialViewerProps {
  credential: VerifiableCredential | null;
}

const CredentialViewer: React.FC<CredentialViewerProps> = ({ credential }) => {
  if (!credential) {
    return (
      <Card className="h-full">
        <CardContent className="flex items-center justify-center h-64 text-muted-foreground">
          <div className="text-center">
            <Code2 className="h-12 w-12 mx-auto mb-4 opacity-50" />
            <p>Select a credential to view its JSON structure</p>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className="h-full">
      <CardHeader>
        <CardTitle className="flex items-center gap-2">
          <Code2 className="h-5 w-5" />
          Credential JSON
        </CardTitle>
      </CardHeader>
      <CardContent>
        <pre className="bg-gray-50 p-4 rounded-lg text-xs overflow-auto max-h-96 border">
          {JSON.stringify(credential, null, 2)}
        </pre>
      </CardContent>
    </Card>
  );
};

export default CredentialViewer;