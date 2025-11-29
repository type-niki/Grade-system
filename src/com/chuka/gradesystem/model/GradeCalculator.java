package com.chuka.gradesystem.model;

import java.util.List;

public interface GradeCalculator {
    double calculateGPA(List<Grade> grades);
    double calculateCGPA(Student student);
    String getLetterGrade(double score);
    double getGradePoint(String letterGrade);
    int calculateTotalCredits(List<Grade> grades);
    boolean isHonorRoll(Student student);
    String getClassStanding(double gpa);
}