package com.chuka.gradesystem.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Student extends Person implements Serializable {
    private String studentId;
    private String department;
    private int yearOfStudy;
    private List<Course> enrolledCourses;
    private List<Grade> grades;

    public Student() {
        super();
        this.enrolledCourses = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public Student(String id, String name, String email, String phone,
                   String studentId, String department, int yearOfStudy) {
        super(id, name, email, phone);
        this.studentId = studentId;
        this.department = department;
        this.yearOfStudy = yearOfStudy;
        this.enrolledCourses = new ArrayList<>();
        this.grades = new ArrayList<>();
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDepartment() {
        return department;
    }

    public int getYearOfStudy() {
        return yearOfStudy;
    }

    public List<Course> getEnrolledCourses() {
        return enrolledCourses;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setYearOfStudy(int yearOfStudy) {
        this.yearOfStudy = yearOfStudy;
    }

    public void setEnrolledCourses(List<Course> enrolledCourses) {
        this.enrolledCourses = enrolledCourses;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public void enrollInCourse(Course course) {
        if (!enrolledCourses.contains(course)) {
            enrolledCourses.add(course);
        }
    }

    public void addGrade(Grade grade) {
        grades.add(grade);
    }

    public double calculateGPA() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double totalPoints = 0.0;
        int totalCredits = 0;
        for (Grade grade : grades) {
            Course course = grade.getCourse();
            totalPoints += grade.getGradePoint() * course.getCredits();
            totalCredits += course.getCredits();
        }
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    public int getTotalCredits() {
        int total = 0;
        for (Course course : enrolledCourses) {
            total += course.getCredits();
        }
        return total;
    }

    @Override
    public String getDetails() {
        StringBuilder details = new StringBuilder();
        details.append("=== STUDENT DETAILS ===\n");
        details.append("Student ID: ").append(studentId).append("\n");
        details.append("Name: ").append(getName()).append("\n");
        details.append("Email: ").append(getEmail()).append("\n");
        details.append("Phone: ").append(getPhone()).append("\n");
        details.append("Department: ").append(department).append("\n");
        details.append("Year of Study: ").append(yearOfStudy).append("\n");
        details.append("Enrolled Courses: ").append(enrolledCourses.size()).append("\n");
        details.append("Current GPA: ").append(String.format("%.2f", calculateGPA())).append("\n");
        return details.toString();
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + getName() + '\'' +
                ", department='" + department + '\'' +
                ", year=" + yearOfStudy +
                ", GPA=" + String.format("%.2f", calculateGPA()) +
                '}';
    }
}