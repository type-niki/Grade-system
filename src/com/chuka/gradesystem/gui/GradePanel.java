package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.*;
import javax.swing.*;
import java.awt.*;

public class GradePanel extends JPanel {
    private DataManager dataManager;
    private GradeService gradeService;

    public GradePanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Grade Management Panel - To Be Implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JTextArea instructions = new JTextArea(
            "INSTRUCTIONS FOR STUDENTS:\n\n" +
            "1. Create a JTable to display grades\n" +
            "2. Create a form with:\n" +
            "   - JComboBox for Student selection (load from DataManager)\n" +
            "   - JComboBox for Course selection (load from DataManager)\n" +
            "   - JTextField for Score (0-100)\n" +
            "   - JComboBox for Semester\n" +
            "   - JLabel to display calculated Letter Grade\n" +
            "   - JLabel to display calculated Grade Point\n" +
            "3. When score is entered, automatically calculate letter grade using GradeService\n" +
            "4. Add buttons: Add Grade, Update, Delete, Calculate GPA\n" +
            "5. Implement validation for score (0-100)\n" +
            "6. Use GradeService.calculateGPA() to show student's GPA\n\n" +
            "This panel demonstrates COMPOSITION (has-a relationship) with services!"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
    }

    public void refreshData() {
        // TODO: Refresh grade table
    }
}