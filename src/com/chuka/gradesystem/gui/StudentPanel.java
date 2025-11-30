package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.Student;
import com.chuka.gradesystem.service.DataManager;
import com.chuka.gradesystem.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * StudentPanel - Student Management Interface
 * 
 * Demonstrates:
 * - JTable for displaying data
 * - Form components (JTextField, JComboBox, JSpinner)
 * - ActionListener for button clicks
 * - KeyListener for search functionality
 * - Layout managers (BorderLayout, GridLayout, FlowLayout)
 * - Input validation
 * 
 * @author Charles David
 * @course ACSC 223 - Object Oriented Programming (Java 1)
 */
public class StudentPanel extends JPanel implements ActionListener, KeyListener {
    
    private DataManager dataManager;
    
    // Table components
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    
    // Form components
    private JTextField idField, nameField, emailField, phoneField, studentIdField, searchField;
    private JComboBox<String> departmentCombo;
    private JSpinner yearSpinner;
    
    // Buttons
    private JButton addButton, updateButton, deleteButton, clearButton, viewButton;
    
    /**
     * Constructor
     * @param dataManager Data manager instance
     */
    public StudentPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        initializeComponents();
        refreshData();
    }
    
    /**
     * Initialize all components
     */
    private void initializeComponents() {
        // Create table
        createTable();
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Create search panel
        JPanel searchPanel = createSearchPanel();
        
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
    
    /**
     * Create the student table
     */
    private void createTable() {
        tableModel = new DefaultTableModel(Constants.STUDENT_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Add mouse listener for row selection
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadSelectedStudent();
                }
            }
        });
        
        tableScrollPane = new JScrollPane(studentTable);
        tableScrollPane.setBorder(BorderFactory.createTitledBorder("Student Records"));
    }
    
    /**
     * Create the form panel
     * @return Form panel
     */
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        
        // Row 1
        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);
        
        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        panel.add(studentIdField);
        
        // Row 2
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        
        // Row 3
        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);
        
        panel.add(new JLabel("Department:"));
        departmentCombo = new JComboBox<>(Constants.DEPARTMENTS);
        panel.add(departmentCombo);
        
        // Row 4
        panel.add(new JLabel("Year of Study:"));
        SpinnerNumberModel yearModel = new SpinnerNumberModel(1, 1, 6, 1);
        yearSpinner = new JSpinner(yearModel);
        panel.add(yearSpinner);
        
        panel.add(new JLabel("")); // Empty cell
        panel.add(new JLabel("")); // Empty cell
        
        return panel;
    }
    
    /**
     * Create the button panel
     * @return Button panel
     */
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        addButton = new JButton("Add Student");
        addButton.addActionListener(this);
        
        updateButton = new JButton("Update");
        updateButton.addActionListener(this);
        
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        
        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);
        
        viewButton = new JButton("View Details");
        viewButton.addActionListener(this);
        
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        panel.add(viewButton);
        
        return panel;
    }
    
    /**
     * Create the search panel
     * @return Search panel
     */
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        panel.add(new JLabel("Search:"));
        searchField = new JTextField(30);
        searchField.addKeyListener(this); // Add KeyListener for real-time search
        panel.add(searchField);
        
        return panel;
    }
    
    /**
     * Refresh table data
     */
    public void refreshData() {
        tableModel.setRowCount(0); // Clear table
        
        List<Student> students = dataManager.loadAllStudents();
        for (Student student : students) {
            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getEmail(),
                student.getPhone(),
                student.getDepartment(),
                student.getYearOfStudy(),
                String.format("%.2f", student.calculateGPA())
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Handle button clicks - ActionListener implementation
     * @param e Action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addStudent();
        } else if (e.getSource() == updateButton) {
            updateStudent();
        } else if (e.getSource() == deleteButton) {
            deleteStudent();
        } else if (e.getSource() == clearButton) {
            clearForm();
        } else if (e.getSource() == viewButton) {
            viewStudentDetails();
        }
    }
    
    /**
     * Add new student
     */
    private void addStudent() {
        if (!validateInput()) {
            return;
        }
        
        Student student = new Student(
            idField.getText().trim(),
            nameField.getText().trim(),
            emailField.getText().trim(),
            phoneField.getText().trim(),
            studentIdField.getText().trim(),
            (String) departmentCombo.getSelectedItem(),
            (Integer) yearSpinner.getValue()
        );
        
        if (dataManager.saveStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            refreshData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add student!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Update existing student
     */
    private void updateStudent() {
        if (!validateInput()) {
            return;
        }
        
        Student student = dataManager.findStudentById(studentIdField.getText().trim());
        if (student == null) {
            JOptionPane.showMessageDialog(this, "Student not found!", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        student.setId(idField.getText().trim());
        student.setName(nameField.getText().trim());
        student.setEmail(emailField.getText().trim());
        student.setPhone(phoneField.getText().trim());
        student.setDepartment((String) departmentCombo.getSelectedItem());
        student.setYearOfStudy((Integer) yearSpinner.getValue());
        
        if (dataManager.updateStudent(student)) {
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            refreshData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update student!", 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Delete selected student
     */
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Constants.MSG_NO_SELECTION, 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            Constants.MSG_DELETE_CONFIRM, 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (dataManager.deleteStudent(studentId)) {
                JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete student!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * View student details
     */
    private void viewStudentDetails() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, Constants.MSG_NO_SELECTION, 
                "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student != null) {
            JOptionPane.showMessageDialog(this, student.getDetails(), 
                "Student Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Load selected student into form
     */
    private void loadSelectedStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            return;
        }
        
        String studentId = (String) tableModel.getValueAt(selectedRow, 0);
        Student student = dataManager.findStudentById(studentId);
        
        if (student != null) {
            idField.setText(student.getId());
            studentIdField.setText(student.getStudentId());
            nameField.setText(student.getName());
            emailField.setText(student.getEmail());
            phoneField.setText(student.getPhone());
            departmentCombo.setSelectedItem(student.getDepartment());
            yearSpinner.setValue(student.getYearOfStudy());
        }
    }
    
    /**
     * Clear form fields
     */
    private void clearForm() {
        idField.setText("");
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        departmentCombo.setSelectedIndex(0);
        yearSpinner.setValue(1);
    }
    
    /**
     * Validate input fields
     * @return true if valid, false otherwise
     */
    private boolean validateInput() {
        if (ValidationUtil.isEmpty(idField.getText())) {
            JOptionPane.showMessageDialog(this, "Please enter ID!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtil.isValidStudentId(studentIdField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid Student ID format!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtil.isValidName(nameField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid name!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtil.isValidEmail(emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid email address!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (!ValidationUtil.isValidPhone(phoneField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid phone number!", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    /**
     * Search students - KeyListener implementation
     */
    private void searchStudents(String query) {
        tableModel.setRowCount(0);
        
        List<Student> students = dataManager.loadAllStudents();
        for (Student student : students) {
            if (student.getName().toLowerCase().contains(query.toLowerCase()) ||
                student.getStudentId().toLowerCase().contains(query.toLowerCase()) ||
                student.getEmail().toLowerCase().contains(query.toLowerCase())) {
                
                Object[] row = {
                    student.getStudentId(),
                    student.getName(),
                    student.getEmail(),
                    student.getPhone(),
                    student.getDepartment(),
                    student.getYearOfStudy(),
                    String.format("%.2f", student.calculateGPA())
                };
                tableModel.addRow(row);
            }
        }
    }
    
    // KeyListener implementation for search
    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyPressed(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            refreshData();
        } else {
            searchStudents(query);
        }
    }
}
