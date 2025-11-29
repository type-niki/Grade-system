package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.model.Student;
import com.chuka.gradesystem.service.DataManager;
import com.chuka.gradesystem.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class StudentPanel extends JPanel implements ActionListener, KeyListener {
    private DataManager dataManager;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JScrollPane tableScrollPane;
    private JTextField idField, nameField, emailField, phoneField, studentIdField, searchField;
    private JComboBox<String> departmentCombo;
    private JSpinner yearSpinner;
    private JButton addButton, updateButton, deleteButton, clearButton, viewButton;

    public StudentPanel(DataManager dataManager) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initializeComponents();
        refreshData();
    }

    private void initializeComponents() {
        createTable();
        JPanel formPanel = createFormPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel searchPanel = createSearchPanel();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(tableScrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(formPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void createTable() {
        tableModel = new DefaultTableModel(Constants.STUDENT_COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Arial", Font.PLAIN, 12));
        studentTable.setRowHeight(25);
        studentTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
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

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        panel.add(new JLabel("ID:"));
        idField = new JTextField();
        panel.add(idField);
        panel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        panel.add(studentIdField);
        panel.add(new JLabel("Name:"));
        nameField = new JTextField();
        panel.add(nameField);
        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);
        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);
        panel.add(new JLabel("Department:"));
        departmentCombo = new JComboBox<>(Constants.DEPARTMENTS);
        panel.add(departmentCombo);
        panel.add(new JLabel("Year of Study:"));
        SpinnerNumberModel yearModel = new SpinnerNumberModel(1, 1, 6, 1);
        yearSpinner = new JSpinner(yearModel);
        panel.add(yearSpinner);
        panel.add(new JLabel(""));
        panel.add(new JLabel(""));
        return panel;
    }

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

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        panel.add(new JLabel("Search:"));
        searchField = new JTextField(30);
        searchField.addKeyListener(this);
        panel.add(searchField);
        return panel;
    }

    public void refreshData() {
        tableModel.setRowCount(0);
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

    private void clearForm() {
        idField.setText("");
        studentIdField.setText("");
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        departmentCombo.setSelectedIndex(0);
        yearSpinner.setValue(1);
    }

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