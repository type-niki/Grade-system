package com.chuka.gradesystem.service;

import com.chuka.gradesystem.model.*;
import java.util.List;

public interface DataManager {
    // STUDENT OPERATIONS
    boolean saveStudent(Student student);
    List<Student> loadAllStudents();
    Student findStudentById(String studentId);
    boolean updateStudent(Student student);
    boolean deleteStudent(String studentId);

    // COURSE OPERATIONS
    boolean saveCourse(Course course);
    List<Course> loadAllCourses();
    Course findCourseByCode(String courseCode);
    boolean updateCourse(Course course);
    boolean deleteCourse(String courseCode);

    // GRADE OPERATIONS
    boolean saveGrade(Grade grade);
    List<Grade> loadAllGrades();
    List<Grade> getGradesByStudent(String studentId);
    List<Grade> getGradesByCourse(String courseCode);
    boolean deleteGrade(Student student, Course course);

    // UTILITY OPERATIONS
    boolean saveAllData();
    boolean loadAllData();
    boolean clearAllData();
}