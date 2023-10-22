package com.example.lab2;

public class Student
{
    String firstName;
    String lastName;
    String middleName;
    String faculty;
    String speciality;
    String admissionDate;
    String email;
    String phone;
    String socialMedia;
    String image;
    int course;

    public Student
    (
        String firstName,
        String lastName,
        String middleName,
        String faculty,
        String speciality,
        String admissionDate,
        int courseID,
        String email,
        String phone,
        String socialMedia,
        String image
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.faculty = faculty;
        this.speciality = speciality;
        this.admissionDate = admissionDate;
        this.course = parseCourseID(courseID);
        this.email = email;
        this.phone = "80 " + phone;
        this.socialMedia = socialMedia;
        this.image = image;
    }

    public String getStudentInfo() {
        return lastName + " " + firstName.charAt(0) + ". " + middleName.charAt(0) + ". " + faculty + "-" + course + " " + speciality;
    }

    public int parseCourseID(int courseID) {
        switch (courseID) {
            case 2131231113:
                return 1;
            case 2131231114:
                return 2;
            case 2131231115:
                return 3;
            case 2131231116:
                return  4;
            case 2131231117:
                return 5;
            default:
                return -1;
        }
    }
}
