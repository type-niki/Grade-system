package com.chuka.gradesystem.util;

public class Constants {
    // Application Information
    public static final String APP_NAME = "Student Grade Management System";
    public static final String APP_VERSION = "1.0.0";
    public static final String UNIVERSITY_NAME = "Chuka University";
    public static final String DEPARTMENT = "Department of Computer Science";

    // Grading System Constants
    public static final double GRADE_A_MIN = 70.0;
    public static final double GRADE_B_MIN = 60.0;
    public static final double GRADE_C_MIN = 50.0;
    public static final double GRADE_D_MIN = 40.0;
    public static final double GRADE_F_MIN = 0.0;

    public static final double GRADE_POINT_A = 4.0;
    public static final double GRADE_POINT_B = 3.0;
    public static final double GRADE_POINT_C = 2.0;
    public static final double GRADE_POINT_D = 1.0;
    public static final double GRADE_POINT_F = 0.0;

    // GPA Classifications
    public static final double FIRST_CLASS_MIN = 3.5;
    public static final double SECOND_CLASS_UPPER_MIN = 3.0;
    public static final double SECOND_CLASS_LOWER_MIN = 2.5;
    public static final double PASS_MIN = 2.0;

    // Honor Roll
    public static final double HONOR_ROLL_MIN = 3.5;

    // Year of Study Limits
    public static final int MIN_YEAR = 1;
    public static final int MAX_YEAR = 6;

    // Credits Limits
    public static final int MIN_CREDITS = 1;
    public static final int MAX_CREDITS = 10;

    // Score Limits
    public static final double MIN_SCORE = 0.0;
    public static final double MAX_SCORE = 100.0;

    // Departments
    public static final String[] DEPARTMENTS = {
        "Computer Science",
        "Mathematics",
        "Physics",
        "Chemistry",
        "Biology",
        "Business Administration",
        "Economics",
        "Education",
        "Engineering",
        "Medicine"
    };

    // Semesters
    public static final String[] SEMESTERS = {
        "Semester 1 - 2024/2025",
        "Semester 2 - 2024/2025",
        "Semester 1 - 2023/2024",
        "Semester 2 - 2023/2024"
    };

    // UI Constants
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 700;
    public static final String WINDOW_TITLE = APP_NAME + " - " + UNIVERSITY_NAME;

    // Table Column Names
    public static final String[] STUDENT_COLUMNS = {
        "Student ID", "Name", "Email", "Phone", "Department", "Year", "GPA"
    };

    public static final String[] COURSE_COLUMNS = {
        "Course Code", "Course Name", "Credits", "Instructor"
    };

    public static final String[] GRADE_COLUMNS = {
        "Student ID", "Student Name", "Course Code", "Course Name",
        "Score", "Letter Grade", "Grade Point", "Semester"
    };

    // Messages
    public static final String MSG_SUCCESS = "Operation completed successfully!";
    public static final String MSG_ERROR = "An error occurred. Please try again.";
    public static final String MSG_VALIDATION_ERROR = "Please check your input and try again.";
    public static final String MSG_DELETE_CONFIRM = "Are you sure you want to delete this record?";
    public static final String MSG_NO_SELECTION = "Please select a record first.";

    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}