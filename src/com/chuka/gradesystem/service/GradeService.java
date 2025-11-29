package com.chuka.gradesystem.service;

import com.chuka.gradesystem.model.*;
import java.util.List;

public class GradeService implements GradeCalculator {

    @Override
    public double calculateGPA(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0.0;
        }
        double totalPoints = 0.0;
        int totalCredits = 0;
        for (Grade grade : grades) {
            Course course = grade.getCourse();
            if (course != null) {
                totalPoints += grade.getGradePoint() * course.getCredits();
                totalCredits += course.getCredits();
            }
        }
        return totalCredits > 0 ? totalPoints / totalCredits : 0.0;
    }

    @Override
    public double calculateCGPA(Student student) {
        if (student == null) {
            return 0.0;
        }
        return calculateGPA(student.getGrades());
    }

    @Override
    public String getLetterGrade(double score) {
        if (score >= 70) {
            return "A";
        } else if (score >= 60) {
            return "B";
        } else if (score >= 50) {
            return "C";
        } else if (score >= 40) {
            return "D";
        } else {
            return "F";
        }
    }

    @Override
    public double getGradePoint(String letterGrade) {
        if (letterGrade == null) {
            return 0.0;
        }
        switch (letterGrade.toUpperCase()) {
            case "A":
                return 4.0;
            case "B":
                return 3.0;
            case "C":
                return 2.0;
            case "D":
                return 1.0;
            case "F":
            default:
                return 0.0;
        }
    }

    @Override
    public int calculateTotalCredits(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return 0;
        }
        int total = 0;
        for (Grade grade : grades) {
            Course course = grade.getCourse();
            if (course != null) {
                total += course.getCredits();
            }
        }
        return total;
    }

    @Override
    public boolean isHonorRoll(Student student) {
        if (student == null) {
            return false;
        }
        double cgpa = calculateCGPA(student);
        return cgpa >= 3.5;
    }

    @Override
    public String getClassStanding(double gpa) {
        if (gpa >= 3.5) {
            return "First Class";
        } else if (gpa >= 3.0) {
            return "Second Class Upper";
        } else if (gpa >= 2.5) {
            return "Second Class Lower";
        } else if (gpa >= 2.0) {
            return "Pass";
        } else {
            return "Fail";
        }
    }

    public String getGradeStatistics(List<Grade> grades) {
        if (grades == null || grades.isEmpty()) {
            return "No grades available";
        }
        int countA = 0, countB = 0, countC = 0, countD = 0, countF = 0;
        double totalScore = 0.0;
        for (Grade grade : grades) {
            totalScore += grade.getScore();
            String letter = grade.getLetterGrade();
            switch (letter) {
                case "A": countA++; break;
                case "B": countB++; break;
                case "C": countC++; break;
                case "D": countD++; break;
                case "F": countF++; break;
            }
        }
        double averageScore = totalScore / grades.size();
        double gpa = calculateGPA(grades);
        StringBuilder stats = new StringBuilder();
        stats.append("=== GRADE STATISTICS ===\n");
        stats.append(String.format("Total Courses: %d\n", grades.size()));
        stats.append(String.format("Average Score: %.2f\n", averageScore));
        stats.append(String.format("GPA: %.2f\n", gpa));
        stats.append(String.format("Class Standing: %s\n", getClassStanding(gpa)));
        stats.append("\nGrade Distribution:\n");
        stats.append(String.format("A: %d\n", countA));
        stats.append(String.format("B: %d\n", countB));
        stats.append(String.format("C: %d\n", countC));
        stats.append(String.format("D: %d\n", countD));
        stats.append(String.format("F: %d\n", countF));
        return stats.toString();
    }
}