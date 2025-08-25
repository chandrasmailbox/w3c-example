# W3C Verifiable Credentials Demo

A complete TypeScript React application demonstrating the W3C Verifiable Credentials (VC) flow with Issuer, Holder, and Verifier roles.

## 🎯 Overview

This application showcases the complete lifecycle of verifiable credentials:
- **Issuer**: Creates and signs digital credentials (University issuing degrees)
- **Holder**: Stores credentials in a digital wallet
- **Verifier**: Validates credential authenticity and integrity

## 🚀 Features

- ✅ Complete W3C Verifiable Credentials workflow
- ✅ TypeScript implementation with strict typing
- ✅ Modern React with hooks and functional components
- ✅ Beautiful UI with Tailwind CSS and shadcn/ui components
- ✅ Responsive design (mobile, tablet, desktop)
- ✅ Real-time credential JSON viewer
- ✅ Mock cryptographic signatures and verification
- ✅ Multiple credential support in wallet

## 🛠 Technology Stack

- **Frontend**: React 19, TypeScript, Tailwind CSS
- **UI Components**: shadcn/ui, Radix UI, Lucide React icons
- **Backend**: FastAPI, Python (for future API integration)
- **Database**: MongoDB (ready for persistence)
- **Build Tools**: Create React App, Craco

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Node.js** (v16 or higher)
- **Yarn** package manager
- **Python** (v3.8 or higher) - for backend
- **MongoDB** (optional - for data persistence)

## 🔧 Installation & Setup

### 1. Clone or Download the Project

```bash
# If using git
git clone <repository-url>
cd w3c-verifiable-credentials-demo

# Or if you have the project files
cd /path/to/project
```

### 2. Install Frontend Dependencies

```bash
cd frontend
yarn install
```

### 3. Install Backend Dependencies (Optional)

```bash
cd backend
pip install -r requirements.txt
```

## 🚀 Running the Application

### Option 1: Frontend Only (Recommended for Demo)

The application currently works entirely in the frontend with mock data:

```bash
cd frontend
yarn start
```

The application will open at `http://localhost:3000`

### Option 2: Full Stack (Frontend + Backend)

If you want to run the complete stack:

1. **Start MongoDB** (if using persistence):
```bash
# Using Docker
docker run -d -p 27017:27017 --name mongodb mongo

# Or using local MongoDB installation
mongod
```

2. **Start Backend**:
```bash
cd backend
uvicorn server:app --host 0.0.0.0 --port 8001 --reload
```

3. **Start Frontend**:
```bash
cd frontend
yarn start
```

## 📱 Using the Application

### 1. Issuer Role
- Navigate to the "Issuer" tab
- Fill in student details:
  - Student Name (e.g., "John Doe")
  - Student DID (e.g., "did:example:student:johndoe")
  - Degree Title (e.g., "Bachelor of Computer Science")
- Click "Issue Credential" to create a new verifiable credential

### 2. Holder Role
- Navigate to the "Holder" tab
- View all issued credentials in the digital wallet
- Click on any credential card to select it
- View the complete JSON structure in the credential viewer

### 3. Verifier Role
- Navigate to the "Verifier" tab
- Ensure a credential is selected from the Holder tab
- Click "Verify Credential" to validate authenticity
- View detailed verification results:
  - ✅ Trusted Issuer verification
  - ✅ Valid Proof validation
  - ✅ Expiration status check

## 📁 Project Structure

```
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   │   ├── ui/           # shadcn/ui components
│   │   │   ├── CredentialCard.tsx
│   │   │   └── CredentialViewer.tsx
│   │   ├── types/
│   │   │   └── credentials.ts # TypeScript interfaces
│   │   ├── utils/
│   │   │   └── credentialUtils.ts # Business logic
│   │   ├── App.tsx           # Main application
│   │   └── index.tsx
│   ├── public/
│   ├── package.json
│   └── tsconfig.json
├── backend/
│   ├── server.py            # FastAPI server
│   ├── requirements.txt
│   └── .env
└── README.md
```

## 🔒 Security Notes

⚠️ **Important**: This is a demonstration application with mock implementations:

- **Signatures**: Uses mock JWS signatures (not cryptographically secure)
- **Verification**: Simulated validation logic
- **Storage**: Credentials stored in browser memory only
- **Production Use**: Requires real cryptographic implementations

## 🛠 Development

### Adding New Features

1. **New Credential Types**: Modify `credentialUtils.ts`
2. **UI Components**: Add to `components/` directory
3. **Styling**: Update Tailwind classes or `index.css`
4. **Types**: Extend interfaces in `types/credentials.ts`

### Building for Production

```bash
cd frontend
yarn build
```

Built files will be in the `build/` directory.

## 🐛 Troubleshooting

### Common Issues

1. **TypeScript Errors**:
```bash
# Remove node_modules and reinstall
rm -rf node_modules yarn.lock
yarn install
```

2. **Port Already in Use**:
```bash
# Kill process on port 3000
lsof -ti:3000 | xargs kill -9
```

3. **Build Failures**:
```bash
# Clear Create React App cache
yarn start --reset-cache
```

### Environment Variables

Create `.env` files if needed:

**Frontend (.env)**:
```env
REACT_APP_BACKEND_URL=http://localhost:8001
```

**Backend (.env)**:
```env
MONGO_URL=mongodb://localhost:27017/verifiable_credentials
DB_NAME=verifiable_credentials
```

## 📚 W3C Standards Compliance

This application implements key W3C Verifiable Credentials concepts:

- **@context**: Proper JSON-LD context
- **Credential Types**: `VerifiableCredential`, `UniversityDegreeCredential`
- **Issuer DIDs**: Decentralized identifiers for issuers
- **Proof Structure**: Mock Ed25519Signature2018 proofs
- **Credential Subject**: Student information and claims

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## 📄 License

This project is for educational and demonstration purposes.

## 🔗 Useful Links

- [W3C Verifiable Credentials Specification](https://www.w3.org/TR/vc-data-model/)
- [DID Specification](https://www.w3.org/TR/did-core/)
- [React Documentation](https://react.dev/)
- [TypeScript Handbook](https://www.typescriptlang.org/docs/)
- [Tailwind CSS](https://tailwindcss.com/)

---

Made with ❤️ using React, TypeScript, and W3C Standards
