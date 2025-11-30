package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.*;
import com.chuka.gradesystem.service.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class ReportPanel extends JPanel implements ActionListener {
    
    private DataManager dataManager;
    private GradeService gradeService;
    private JComboBox<Student> studentCombo;
    private JTextArea reportArea;
    private JButton generateBtn, exportBtn;
    
    public ReportPanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout(10, 10));
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Top Panel with controls
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        topPanel.add(new JLabel("Select Student:"));
        studentCombo = new JComboBox<>();
        studentCombo.setPreferredSize(new Dimension(250, 25));
        topPanel.add(studentCombo);
        
        generateBtn = new JButton("Generate Transcript");
        generateBtn.addActionListener(this);
        topPanel.add(generateBtn);
        
        exportBtn = new JButton("Export to File");
        exportBtn.addActionListener(this);
        topPanel.add(exportBtn);
        
        // Report TextArea
        reportArea = new JTextArea(20, 70);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student Transcript"));
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public void refreshData() {
        studentCombo.removeAllItems();
        for (Student student : dataManager.loadAllStudents()) {
            studentCombo.addItem(student);
        }
    }
    
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateBtn) {
            generateTranscript();
        } else if (e.getSource() == exportBtn) {
            exportToFile();
        }
    }
    
    private void generateTranscript() {
        Student student = (Student) studentCombo.getSelectedItem();
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Please select a student first.");
            return;
        }
        
        StringBuilder report = new StringBuilder();
        
        // Student details using getDetails()
        report.append(student.getDetails()).append("\n\n");
        
        // List of all courses and grades
        List<Grade> grades = dataManager.getGradesByStudent(student.getStudentId());
        if (grades.isEmpty()) {
            report.append("No grades recorded.\n");
        } else {
            report.append("=== COURSES AND GRADES ===\n");
            report.append("Course Code\tCourse Name\t\tScore\tGrade\tPoints\tSemester\n");
            report.append("------------------------------------------------------------------------\n");
            
            for (Grade grade : grades) {
                Course course = grade.getCourse();
                report.append(String.format("%-12s\t%-20s\t%.1f\t%s\t%.1f\t%s\n",
                    course.getCourseCode(),
                    course.getCourseName(),
                    grade.getScore(),
                    grade.getLetterGrade(),
                    grade.getGradePoint(),
                    grade.getSemester()));
            }
            report.append("\n");
        }
        
        // Student's CGPA using gradeService.calculateCGPA()
        double cgpa = gradeService.calculateCGPA(student);
        report.append(String.format("Cumulative GPA (CGPA): %.2f\n", cgpa));
        
        // Class standing using gradeService.getClassStanding()
        String classStanding = gradeService.getClassStanding(cgpa);
        report.append(String.format("Class Standing: %s\n", classStanding));
        
        // Grade statistics using gradeService.getGradeStatistics()
        String gradeStats = gradeService.getGradeStatistics(grades);
        report.append("\n").append(gradeStats);
        
        reportArea.setText(report.toString());
    }
    
    private void exportToFile() {
        if (reportArea.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please generate a transcript first.");
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Transcript");
        fileChooser.setSelectedFile(new File("student_transcript.txt"));
        
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.write(reportArea.getText());
                JOptionPane.showMessageDialog(this, 
                    "Transcript exported successfully to:\n" + file.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                    "Error exporting file: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}