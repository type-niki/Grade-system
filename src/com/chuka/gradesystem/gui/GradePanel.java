package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.*;
import com.chuka.gradesystem.service.*;
import com.chuka.gradesystem.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class GradePanel extends JPanel implements ActionListener {
    
    private DataManager dataManager;
    private GradeService gradeService;
    private JTable gradeTable;
    private DefaultTableModel tableModel;
    private JTextField studentIdField, courseCodeField, scoreField;
    private JComboBox<String> semesterCombo;
    private JLabel letterGradeLabel, gradePointLabel, gpaLabel;
    
    public GradePanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Input Panel - Simple 2-column grid
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Grade"));
        
        inputPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        inputPanel.add(studentIdField);
        
        inputPanel.add(new JLabel("Course Code:"));
        courseCodeField = new JTextField();
        inputPanel.add(courseCodeField);
        
        inputPanel.add(new JLabel("Score (0-100):"));
        scoreField = new JTextField();
        scoreField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { calculateGrade(); }
        });
        inputPanel.add(scoreField);
        
        inputPanel.add(new JLabel("Semester:"));
        semesterCombo = new JComboBox<>(Constants.SEMESTERS);
        inputPanel.add(semesterCombo);
        
        inputPanel.add(new JLabel("Letter Grade:"));
        letterGradeLabel = new JLabel("-");
        letterGradeLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        inputPanel.add(letterGradeLabel);
        
        inputPanel.add(new JLabel("Grade Point:"));
        gradePointLabel = new JLabel("-");
        gradePointLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        inputPanel.add(gradePointLabel);
        
        inputPanel.add(new JLabel("GPA:"));
        JPanel gpaPanel = new JPanel(new FlowLayout());
        gpaLabel = new JLabel("-");
        gpaLabel.setBorder(BorderFactory.createLineBorder(Color.BLUE));
        gpaPanel.add(gpaLabel);
        JButton gpaBtn = new JButton("Calc GPA");
        gpaBtn.addActionListener(e -> calculateGPA());
        gpaPanel.add(gpaBtn);
        inputPanel.add(gpaPanel);
        
        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("Add Grade");
        JButton deleteBtn = new JButton("Delete");
        JButton clearBtn = new JButton("Clear");
        
        addBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        clearBtn.addActionListener(this);
        
        buttonPanel.add(addBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(clearBtn);
        
        // Table
        tableModel = new DefaultTableModel(Constants.GRADE_COLUMNS, 0) {
            public boolean isCellEditable(int row, int column) { return false; }
        };
        gradeTable = new JTable(tableModel);
        gradeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Auto-fill when row selected
        gradeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && gradeTable.getSelectedRow() != -1) {
                loadSelectedGrade();
            }
        });
        
        JScrollPane tableScroll = new JScrollPane(gradeTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Grade Records"));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(inputPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        for (Grade grade : dataManager.loadAllGrades()) {
            if (grade.getStudent() != null && grade.getCourse() != null) {
                tableModel.addRow(new Object[]{
                    grade.getStudent().getStudentId(),
                    grade.getStudent().getName(),
                    grade.getCourse().getCourseCode(),
                    grade.getCourse().getCourseName(),
                    grade.getScore(),
                    grade.getLetterGrade(),
                    grade.getGradePoint(),
                    grade.getSemester()
                });
            }
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Add Grade")) addGrade();
        else if (cmd.equals("Delete")) deleteGrade();
        else if (cmd.equals("Clear")) clearForm();
    }
    
    private void addGrade() {
        if (!validateInput()) return;
        
        String studentId = studentIdField.getText().trim();
        String courseCode = courseCodeField.getText().trim();
        
        Student student = dataManager.findStudentById(studentId);
        Course course = dataManager.findCourseByCode(courseCode);
        
        if (student == null || course == null) {
            JOptionPane.showMessageDialog(this, "Student or Course not found!");
            return;
        }
        
        double score = Double.parseDouble(scoreField.getText());
        String semester = (String) semesterCombo.getSelectedItem();
        
        Grade grade = new Grade(student, course, score, semester);
        if (dataManager.saveGrade(grade)) {
            JOptionPane.showMessageDialog(this, "Grade added successfully!");
            refreshData();
            clearForm();
        }
    }
    
    private void deleteGrade() {
        int row = gradeTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a grade to delete");
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(row, 0);
        String courseCode = (String) tableModel.getValueAt(row, 2);
        
        Student student = dataManager.findStudentById(studentId);
        Course course = dataManager.findCourseByCode(courseCode);
        
        if (student != null && course != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete this grade?");
            if (confirm == JOptionPane.YES_OPTION && dataManager.deleteGrade(student, course)) {
                JOptionPane.showMessageDialog(this, "Grade deleted!");
                refreshData();
                clearForm();
            }
        }
    }
    
    private void calculateGrade() {
        try {
            double score = Double.parseDouble(scoreField.getText());
            if (score >= 0 && score <= 100) {
                String letter = gradeService.getLetterGrade(score);
                double points = gradeService.getGradePoint(letter);
                letterGradeLabel.setText(letter);
                gradePointLabel.setText(String.format("%.1f", points));
            }
        } catch (Exception e) {
            letterGradeLabel.setText("-");
            gradePointLabel.setText("-");
        }
    }
    
    private void calculateGPA() {
        String studentId = studentIdField.getText().trim();
        if (studentId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID");
            return;
        }
        
        Student student = dataManager.findStudentById(studentId);
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!");
            return;
        }
        
        double gpa = gradeService.calculateCGPA(student);
        gpaLabel.setText(String.format("%.2f", gpa));
        
        JOptionPane.showMessageDialog(this,
            "GPA: " + String.format("%.2f", gpa) +
            "\nClass: " + gradeService.getClassStanding(gpa) +
            "\nHonor Roll: " + (gradeService.isHonorRoll(student) ? "Yes" : "No"));
    }
    
    private void loadSelectedGrade() {
        int row = gradeTable.getSelectedRow();
        studentIdField.setText((String) tableModel.getValueAt(row, 0));
        courseCodeField.setText((String) tableModel.getValueAt(row, 2));
        calculateGPA(); // Auto-calculate GPA when student selected
    }
    
    private void clearForm() {
        studentIdField.setText("");
        courseCodeField.setText("");
        scoreField.setText("");
        letterGradeLabel.setText("-");
        gradePointLabel.setText("-");
        gpaLabel.setText("-");
        semesterCombo.setSelectedIndex(0);
        gradeTable.clearSelection();
    }
    
    private boolean validateInput() {
        if (studentIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Student ID");
            return false;
        }
        if (courseCodeField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter Course Code");
            return false;
        }
        try {
            double score = Double.parseDouble(scoreField.getText());
            if (score < 0 || score > 100) return false;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid score (0-100)");
            return false;
        }
        return true;
    }
}