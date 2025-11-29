package com.chuka.gradesystem.model;

import java.io.Serializable;

public class Grade implements Serializable {
    private Student student;
    private Course course;
    private double score;
    private String letterGrade;
    private double gradePoint;
    private String semester;

    public Grade() {
    }

    public Grade(Student student, Course course, double score, String semester) {
        this.student = student;
        this.course = course;
        this.score = score;
        this.semester = semester;
        calculateLetterGrade();
        calculateGradePoint();
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public double getScore() {
        return score;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public double getGradePoint() {
        return gradePoint;
    }

    public String getSemester() {
        return semester;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.score = score;
        calculateLetterGrade();
        calculateGradePoint();
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    private void calculateLetterGrade() {
        if (score >= 70) {
            letterGrade = "A";
        } else if (score >= 60) {
            letterGrade = "B";
        } else if (score >= 50) {
            letterGrade = "C";
        } else if (score >= 40) {
            letterGrade = "D";
        } else {
            letterGrade = "F";
        }
    }

    private void calculateGradePoint() {
        switch (letterGrade) {
            case "A":
                gradePoint = 4.0;
                break;
            case "B":
                gradePoint = 3.0;
                break;
            case "C":
                gradePoint = 2.0;
                break;
            case "D":
                gradePoint = 1.0;
                break;
            case "F":
                gradePoint = 0.0;
                break;
            default:
                gradePoint = 0.0;
        }
    }

    public boolean isPassing() {
        return score >= 40;
    }

    public String getGradeStatus() {
        if (score >= 70) {
            return "Excellent";
        } else if (score >= 60) {
            return "Good";
        } else if (score >= 50) {
            return "Average";
        } else if (score >= 40) {
            return "Poor";
        } else {
            return "Fail";
        }
    }

    public String getDetails() {
        return String.format("Course: %s\nScore: %.2f\nLetter Grade: %s\nGrade Point: %.1f\nStatus: %s",
                course.getCourseName(), score, letterGrade, gradePoint, getGradeStatus());
    }

    @Override
    public String toString() {
        return String.format("%s: %.2f (%s)",
                course.getCourseCode(), score, letterGrade);
    }
}