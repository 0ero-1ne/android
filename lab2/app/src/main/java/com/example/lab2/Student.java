package com.example.lab2;

public class Student
{
    String firstName;
    String lastName;
    String middleName;
    String faculty;
    String speciality;
    String admissionDate;
    int course;

    public Student
    (
        String firstName,
        String lastName,
        String middleName,
        String faculty,
        String speciality,
        String admissionDate,
        int course
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.faculty = faculty;
        this.speciality = speciality;
        this.admissionDate = admissionDate;
        this.course = course;
    }

    public String getStudentInfo() {
        return lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + ". " + faculty + "-" + course + " " + speciality;
    }
}
