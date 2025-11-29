package com.chuka.gradesystem.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    
    private static final Pattern PHONE_PATTERN =
        Pattern.compile("^[0-9]{10}$|^\\+[0-9]{12}$");
    
    private static final Pattern STUDENT_ID_PATTERN =
        Pattern.compile("^[A-Z]{2,4}[0-9]{3,6}$");
    
    private static final Pattern COURSE_CODE_PATTERN =
        Pattern.compile("^[A-Z]{3,4}\\s?[0-9]{3}$");

    private ValidationUtil() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }

    public static boolean isValidStudentId(String studentId) {
        if (studentId == null || studentId.trim().isEmpty()) {
            return false;
        }
        return STUDENT_ID_PATTERN.matcher(studentId.trim()).matches();
    }

    public static boolean isValidCourseCode(String courseCode) {
        if (courseCode == null || courseCode.trim().isEmpty()) {
            return false;
        }
        return COURSE_CODE_PATTERN.matcher(courseCode.trim()).matches();
    }

    public static boolean isValidScore(double score) {
        return score >= 0 && score <= 100;
    }

    public static boolean isValidCredits(int credits) {
        return credits > 0 && credits <= 10;
    }

    public static boolean isValidYearOfStudy(int year) {
        return year >= 1 && year <= 6;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return name.trim().matches("^[a-zA-Z\\s]+$");
    }

    public static String sanitize(String input) {
        if (input == null) {
            return "";
        }
        return input.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}