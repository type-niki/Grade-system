package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.*;
import com.chuka.gradesystem.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements ActionListener {
    private DataManager dataManager;
    private GradeService gradeService;
    private JTabbedPane tabbedPane;
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private GradePanel gradePanel;
    private ReportPanel reportPanel;
    private DashboardPanel dashboardPanel;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, viewMenu, helpMenu;
    private JMenuItem exitItem, aboutItem, refreshItem;

    public MainFrame() {
        dataManager = new FileDataManager();
        gradeService = new GradeService();
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        initializeComponents();
        setVisible(true);
    }

    private void initializeComponents() {
        createMenuBar();
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        dashboardPanel = new DashboardPanel(dataManager, gradeService);
        studentPanel = new StudentPanel(dataManager);
        coursePanel = new CoursePanel(dataManager);
        gradePanel = new GradePanel(dataManager, gradeService);
        reportPanel = new ReportPanel(dataManager, gradeService);
        tabbedPane.addTab("Dashboard", new ImageIcon(), dashboardPanel, "Overview and Statistics");
        tabbedPane.addTab("Students", new ImageIcon(), studentPanel, "Manage Students");
        tabbedPane.addTab("Courses", new ImageIcon(), coursePanel, "Manage Courses");
        tabbedPane.addTab("Grades", new ImageIcon(), gradePanel, "Manage Grades");
        tabbedPane.addTab("Reports", new ImageIcon(), reportPanel, "Generate Reports");
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            refreshCurrentPanel(selectedIndex);
        });
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        JPanel statusBar = createStatusBar();
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        refreshItem = new JMenuItem("Refresh", KeyEvent.VK_R);
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        refreshItem.addActionListener(this);
        exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(this);
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        viewMenu = new JMenu("View");
        viewMenu.setMnemonic(KeyEvent.VK_V);
        JMenuItem dashboardItem = new JMenuItem("Dashboard");
        dashboardItem.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        JMenuItem studentsItem = new JMenuItem("Students");
        studentsItem.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        JMenuItem coursesItem = new JMenuItem("Courses");
        coursesItem.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        JMenuItem gradesItem = new JMenuItem("Grades");
        gradesItem.addActionListener(e -> tabbedPane.setSelectedIndex(3));
        JMenuItem reportsItem = new JMenuItem("Reports");
        reportsItem.addActionListener(e -> tabbedPane.setSelectedIndex(4));
        viewMenu.add(dashboardItem);
        viewMenu.add(studentsItem);
        viewMenu.add(coursesItem);
        viewMenu.add(gradesItem);
        viewMenu.add(reportsItem);
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(this);
        helpMenu.add(aboutItem);
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel(Constants.APP_NAME + " - Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        statusBar.add(statusLabel);
        return statusBar;
    }

    private void refreshCurrentPanel(int index) {
        switch (index) {
            case 0:
                dashboardPanel.refreshData();
                break;
            case 1:
                studentPanel.refreshData();
                break;
            case 2:
                coursePanel.refreshData();
                break;
            case 3:
                gradePanel.refreshData();
                break;
            case 4:
                reportPanel.refreshData();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitItem) {
            exitApplication();
        } else if (e.getSource() == aboutItem) {
            showAboutDialog();
        } else if (e.getSource() == refreshItem) {
            refreshCurrentPanel(tabbedPane.getSelectedIndex());
            JOptionPane.showMessageDialog(this, "Data refreshed successfully!");
        }
    }

    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?\nAll data will be saved automatically.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (choice == JOptionPane.YES_OPTION) {
            dataManager.saveAllData();
            System.exit(0);
        }
    }

    private void showAboutDialog() {
        String message = Constants.APP_NAME + "\n" +
            "Version: " + Constants.APP_VERSION + "\n\n" +
            Constants.UNIVERSITY_NAME + "\n" +
            Constants.DEPARTMENT + "\n\n" +
            "Course: ACSC 223 - Object Oriented Programming (Java 1)\n" +
            "Instructor: Charles David\n\n" +
            "© 2024 Chuka University";
        JOptionPane.showMessageDialog(
            this,
            message,
            "About " + Constants.APP_NAME,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new MainFrame();
            }
        });
    }
}