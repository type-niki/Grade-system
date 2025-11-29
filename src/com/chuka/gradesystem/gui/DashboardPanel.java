package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.*;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private DataManager dataManager;
    private GradeService gradeService;

    public DashboardPanel(DataManager dataManager, GradeService gradeService) {
        this.dataManager = dataManager;
        this.gradeService = gradeService;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Dashboard Panel - To Be Implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JTextArea instructions = new JTextArea(
            "INSTRUCTIONS FOR STUDENTS:\n\n" +
            "1. Create a GridLayout with panels showing:\n" +
            "   - Total Students (use dataManager.loadAllStudents().size())\n" +
            "   - Total Courses (use dataManager.loadAllCourses().size())\n" +
            "   - Total Grades (use dataManager.loadAllGrades().size())\n" +
            "   - Average GPA across all students\n" +
            "2. Create a panel showing Honor Roll students:\n" +
            "   - Use gradeService.isHonorRoll() to filter students\n" +
            "   - Display in a JList or JTable\n" +
            "3. ADVANCED: Create a custom JPanel that overrides paintComponent()\n" +
            "   - Use Graphics2D to draw a bar chart of grade distribution\n" +
            "   - This demonstrates CUSTOM PAINTING\n" +
            "4. Add a 'Refresh' button to update statistics\n\n" +
            "This panel demonstrates data aggregation and visualization!"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
    }

    public void refreshData() {
        // TODO: Refresh statistics
    }
}