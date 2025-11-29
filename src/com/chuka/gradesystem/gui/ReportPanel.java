package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.*;
import javax.swing.*;
import java.awt.*;

public class ReportPanel extends JPanel {
    private DataManager dataManager;
    private GradeService gradeService;

    public ReportPanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Report Generation Panel - To Be Implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JTextArea instructions = new JTextArea(
            "INSTRUCTIONS FOR STUDENTS:\n\n" +
            "1. Create a JComboBox to select student\n" +
            "2. Add a 'Generate Transcript' button\n" +
            "3. Create a JTextArea to display the formatted report\n" +
            "4. Report should include:\n" +
            "   - Student details (using student.getDetails())\n" +
            "   - List of all courses and grades\n" +
            "   - GPA (using gradeService.calculateCGPA())\n" +
            "   - Class Standing (using gradeService.getClassStanding())\n" +
            "   - Grade statistics (using gradeService.getGradeStatistics())\n" +
            "5. Add 'Export to File' button to save report as .txt file\n" +
            "6. Use JFileChooser for file selection\n\n" +
            "This demonstrates FILE I/O and using service methods!"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
    }

    public void refreshData() {
        // TODO: Refresh student list
    }
}