import React from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from './ui/card';
import { Badge } from './ui/badge';
import { VerifiableCredential } from '../types/credentials';
import { Calendar, User, Building, Award } from 'lucide-react';

interface CredentialCardProps {
  credential: VerifiableCredential;
  onSelect?: (credential: VerifiableCredential) => void;
  isSelected?: boolean;
}

const CredentialCard: React.FC<CredentialCardProps> = ({ 
  credential, 
  onSelect, 
  isSelected = false 
}) => {
  const issuerName = typeof credential.issuer === 'string' 
    ? credential.issuer 
    : credential.issuer.name || credential.issuer.id;

  const handleClick = () => {
    onSelect?.(credential);
  };

  return (
    <Card 
      className={`cursor-pointer transition-all hover:shadow-md ${
        isSelected ? 'ring-2 ring-blue-500 shadow-md' : ''
      }`}
      onClick={handleClick}
    >
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-lg flex items-center gap-2">
            <Award className="h-5 w-5 text-blue-600" />
            {credential.credentialSubject.degree || 'Academic Credential'}
          </CardTitle>
          <Badge variant="secondary" className="text-xs">
            {credential.type[credential.type.length - 1]}
          </Badge>
        </div>
        <CardDescription>
          Verifiable Credential • ID: {credential.id.split(':').pop()?.substring(0, 8)}...
        </CardDescription>
      </CardHeader>
      
      <CardContent className="space-y-3">
        <div className="flex items-center gap-2 text-sm">
          <User className="h-4 w-4 text-gray-500" />
          <span className="font-medium">Holder:</span>
          <span>{credential.credentialSubject.name || 'Unknown'}</span>
        </div>
        
        <div className="flex items-center gap-2 text-sm">
          <Building className="h-4 w-4 text-gray-500" />
          <span className="font-medium">Issuer:</span>
          <span>{issuerName}</span>
        </div>
        
        <div className="flex items-center gap-2 text-sm">
          <Calendar className="h-4 w-4 text-gray-500" />
          <span className="font-medium">Issued:</span>
          <span>{new Date(credential.issuanceDate).toLocaleDateString()}</span>
        </div>
      </CardContent>
    </Card>
  );
};

export default CredentialCard;