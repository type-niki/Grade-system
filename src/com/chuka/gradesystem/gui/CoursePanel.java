package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.Course;
import com.chuka.gradesystem.service.DataManager;
import com.chuka.gradesystem.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CoursePanel extends JPanel implements ActionListener, KeyListener {
    
    private DataManager dataManager;
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTextField courseCodeField, courseNameField, instructorField, searchField;
    private JSpinner creditsSpinner;
    private JButton addButton, updateButton, deleteButton, clearButton;
    
    public CoursePanel(DataManager dataManager) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeComponents();
        refreshData();
    }
    
    private void initializeComponents() {
        // Table
        tableModel = new DefaultTableModel(Constants.COURSE_COLUMNS, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        courseTable = new JTable(tableModel);
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        courseTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) loadSelectedCourse();
            }
        });
        JScrollPane tableScrollPane = new JScrollPane(courseTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Course Records"));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Course Information"));
        
        formPanel.add(new JLabel("Course Code:")); courseCodeField = new JTextField(); formPanel.add(courseCodeField);
        formPanel.add(new JLabel("Course Name:")); courseNameField = new JTextField(); formPanel.add(courseNameField);
        formPanel.add(new JLabel("Credits:")); 
        creditsSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1)); formPanel.add(creditsSpinner);
        formPanel.add(new JLabel("Instructor:")); instructorField = new JTextField(); formPanel.add(instructorField);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        addButton = new JButton("Add"); addButton.addActionListener(this); buttonPanel.add(addButton);
        updateButton = new JButton("Update"); updateButton.addActionListener(this); buttonPanel.add(updateButton);
        deleteButton = new JButton("Delete"); deleteButton.addActionListener(this); buttonPanel.add(deleteButton);
        clearButton = new JButton("Clear"); clearButton.addActionListener(this); buttonPanel.add(clearButton);
        
        // Search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchField = new JTextField(25); searchField.addKeyListener(this); searchPanel.add(searchField);
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    public void refreshData() {
        tableModel.setRowCount(0);
        for (Course course : dataManager.loadAllCourses()) {
            tableModel.addRow(new Object[]{
                course.getCourseCode(), course.getCourseName(), 
                course.getCredits(), course.getInstructor()
            });
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) addCourse();
        else if (e.getSource() == updateButton) updateCourse();
        else if (e.getSource() == deleteButton) deleteCourse();
        else if (e.getSource() == clearButton) clearForm();
    }
    
    private void addCourse() {
        if (!validateInput()) return;
        Course course = new Course(courseCodeField.getText().trim(), courseNameField.getText().trim(),
                                  (Integer) creditsSpinner.getValue(), instructorField.getText().trim());
        if (dataManager.saveCourse(course)) {
            JOptionPane.showMessageDialog(this, "Course added successfully!");
            refreshData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add course!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateCourse() {
        if (!validateInput()) return;
        Course course = dataManager.findCourseByCode(courseCodeField.getText().trim());
        if (course == null) {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        course.setCourseName(courseNameField.getText().trim());
        course.setCredits((Integer) creditsSpinner.getValue());
        course.setInstructor(instructorField.getText().trim());
        if (dataManager.updateCourse(course)) {
            JOptionPane.showMessageDialog(this, "Course updated successfully!");
            refreshData(); clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update course!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Constants.MSG_NO_SELECTION, "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, Constants.MSG_DELETE_CONFIRM, "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION && dataManager.deleteCourse(courseCode)) {
            JOptionPane.showMessageDialog(this, "Course deleted successfully!");
            refreshData(); clearForm();
        }
    }
    
    private void loadSelectedCourse() {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) return;
        String courseCode = (String) tableModel.getValueAt(selectedRow, 0);
        Course course = dataManager.findCourseByCode(courseCode);
        if (course != null) {
            courseCodeField.setText(course.getCourseCode());
            courseNameField.setText(course.getCourseName());
            creditsSpinner.setValue(course.getCredits());
            instructorField.setText(course.getInstructor());
        }
    }
    
    private void clearForm() {
        courseCodeField.setText(""); courseNameField.setText(""); 
        creditsSpinner.setValue(3); instructorField.setText("");
    }
    
    private boolean validateInput() {
        if (!ValidationUtil.isValidCourseCode(courseCodeField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid course code!\nExample: ACSC 223", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (ValidationUtil.isEmpty(courseNameField.getText()) || ValidationUtil.isEmpty(instructorField.getText())) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    private void searchCourses(String query) {
        tableModel.setRowCount(0);
        for (Course course : dataManager.loadAllCourses()) {
            if (course.getCourseCode().toLowerCase().contains(query.toLowerCase()) ||
                course.getCourseName().toLowerCase().contains(query.toLowerCase()) ||
                course.getInstructor().toLowerCase().contains(query.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    course.getCourseCode(), course.getCourseName(), 
                    course.getCredits(), course.getInstructor()
                });
            }
        }
    }
    
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyPressed(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) refreshData(); else searchCourses(query);
    }
}