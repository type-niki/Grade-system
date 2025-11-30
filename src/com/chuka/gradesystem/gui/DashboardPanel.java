package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.*;
import com.chuka.gradesystem.service.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {
    
    private DataManager dataManager;
    private GradeService gradeService;
    private JLabel studentsLabel, coursesLabel, gradesLabel, avgGpaLabel;
    private JList<String> honorRollList;
    private JButton refreshButton;
    
    public DashboardPanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        statsPanel.setBorder(BorderFactory.createTitledBorder("Application Statistics"));
        
        studentsLabel = createStatLabel("Total Students: 0");
        coursesLabel = createStatLabel("Total Courses: 0");
        gradesLabel = createStatLabel("Total Grades: 0");
        avgGpaLabel = createStatLabel("Average GPA: 0.00");
        
        statsPanel.add(studentsLabel);
        statsPanel.add(coursesLabel);
        statsPanel.add(gradesLabel);
        statsPanel.add(avgGpaLabel);
        
        // Honor Roll panel
        JPanel honorRollPanel = new JPanel(new BorderLayout());
        honorRollPanel.setBorder(BorderFactory.createTitledBorder("Honor Roll Students (GPA ≥ 3.5)"));
        honorRollList = new JList<>();
        honorRollPanel.add(new JScrollPane(honorRollList), BorderLayout.CENTER);
        
        // Refresh button
        refreshButton = new JButton("Refresh Statistics");
        refreshButton.addActionListener(e -> refreshData());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(refreshButton);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(statsPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(honorRollPanel, BorderLayout.CENTER);
    }
    
    private JLabel createStatLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setOpaque(true);
        label.setBackground(Color.LIGHT_GRAY);
        return label;
    }
    
    public void refreshData() {
        // Basic statistics
        List<Student> students = dataManager.loadAllStudents();
        List<Course> courses = dataManager.loadAllCourses();
        List<Grade> grades = dataManager.loadAllGrades();
        
        studentsLabel.setText("Total Students: " + students.size());
        coursesLabel.setText("Total Courses: " + courses.size());
        gradesLabel.setText("Total Grades: " + grades.size());
        
        // Average GPA
        double totalGPA = 0;
        int count = 0;
        for (Student student : students) {
            double gpa = gradeService.calculateCGPA(student);
            if (gpa > 0) {
                totalGPA += gpa;
                count++;
            }
        }
        double avgGPA = count > 0 ? totalGPA / count : 0;
        avgGpaLabel.setText(String.format("Average GPA: %.2f", avgGPA));
        
        // Honor Roll
        DefaultListModel<String> honorRollModel = new DefaultListModel<>();
        for (Student student : students) {
            if (gradeService.isHonorRoll(student)) {
                honorRollModel.addElement(String.format("%s - %s (GPA: %.2f)", 
                    student.getStudentId(), student.getName(), gradeService.calculateCGPA(student)));
            }
        }
        honorRollList.setModel(honorRollModel);
    }
}