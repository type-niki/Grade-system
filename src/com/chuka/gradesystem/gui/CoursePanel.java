package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.DataManager;
import javax.swing.*;
import java.awt.*;

public class CoursePanel extends JPanel {
    private DataManager dataManager;

    public CoursePanel(DataManager dataManager) {
        this.dataManager = dataManager;
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Course Management Panel - To Be Implemented", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);

        JTextArea instructions = new JTextArea(
            "INSTRUCTIONS FOR STUDENTS:\n\n" +
            "1. Create a JTable to display courses\n" +
            "2. Create a form with fields: Course Code, Course Name, Credits, Instructor\n" +
            "3. Add buttons: Add Course, Update, Delete, Clear\n" +
            "4. Implement ActionListener for button clicks\n" +
            "5. Add search functionality with KeyListener\n" +
            "6. Use ValidationUtil to validate course code and credits\n" +
            "7. Use DataManager to save/load/update/delete courses\n\n" +
            "Refer to StudentPanel.java as a complete example!"
        );
        instructions.setEditable(false);
        instructions.setFont(new Font("Monospaced", Font.PLAIN, 12));
        instructions.setMargin(new Insets(20, 20, 20, 20));
        add(new JScrollPane(instructions), BorderLayout.SOUTH);
    }

    public void refreshData() {
        // TODO: Refresh course table
    }
}