package com.w3c.credentials.dto;

import jakarta.validation.constraints.NotBlank;

public class IssueCredentialRequest {
    
    @NotBlank(message = "Student name is required")
    private String studentName;
    
    @NotBlank(message = "Student DID is required")
    private String studentDID;
    
    @NotBlank(message = "Degree title is required")
    private String degreeTitle;
    
    private String university;
    
    // Constructors
    public IssueCredentialRequest() {}
    
    public IssueCredentialRequest(String studentName, String studentDID, String degreeTitle) {
        this.studentName = studentName;
        this.studentDID = studentDID;
        this.degreeTitle = degreeTitle;
    }
    
    public IssueCredentialRequest(String studentName, String studentDID, String degreeTitle, String university) {
        this(studentName, studentDID, degreeTitle);
        this.university = university;
    }
    
    // Getters and Setters
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    
    public String getStudentDID() { return studentDID; }
    public void setStudentDID(String studentDID) { this.studentDID = studentDID; }
    
    public String getDegreeTitle() { return degreeTitle; }
    public void setDegreeTitle(String degreeTitle) { this.degreeTitle = degreeTitle; }
    
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
}