package com.chuka.gradesystem.gui;

import com.chuka.gradesystem.service.*;
import com.chuka.gradesystem.util.Constants;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * MainFrame - Main Application Window
 * 
 * This class demonstrates SWING GUI programming with:
 * - JFrame usage
 * - JTabbedPane for navigation
 * - Menu bar with menus
 * - Event handling
 * - Layout managers
 * 
 * @author Charles David
 * @course ACSC 223 - Object Oriented Programming (Java 1)
 * @institution Chuka University
 */
public class MainFrame extends JFrame implements ActionListener {
    
    // Service objects - COMPOSITION
    private DataManager dataManager;
    private GradeService gradeService;
    
    // GUI Components
    private JTabbedPane tabbedPane;
    private StudentPanel studentPanel;
    private CoursePanel coursePanel;
    private GradePanel gradePanel;
    private ReportPanel reportPanel;
    private DashboardPanel dashboardPanel;
    
    // Menu components
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, viewMenu, helpMenu;
    private JMenuItem exitItem, aboutItem, refreshItem;
    
    /**
     * Constructor - Initialize the main frame
     */
    public MainFrame() {
        // Initialize services
        dataManager = new FileDataManager();
        gradeService = new GradeService();
        
        // Set up the frame
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Custom close handling
        setLocationRelativeTo(null); // Center on screen
        
        // Add window listener for close operation
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        // Initialize GUI components
        initializeComponents();
        
        // Set visible
        setVisible(true);
    }
    
    /**
     * Initialize all GUI components
     */
    private void initializeComponents() {
        // Create menu bar
        createMenuBar();
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Create panels
        dashboardPanel = new DashboardPanel(dataManager, gradeService);
        studentPanel = new StudentPanel(dataManager);
        coursePanel = new CoursePanel(dataManager);
        gradePanel = new GradePanel(dataManager, gradeService);
        reportPanel = new ReportPanel(dataManager, gradeService);
        
        // Add panels to tabbed pane
        tabbedPane.addTab("Dashboard", new ImageIcon(), dashboardPanel, "Overview and Statistics");
        tabbedPane.addTab("Students", new ImageIcon(), studentPanel, "Manage Students");
        tabbedPane.addTab("Courses", new ImageIcon(), coursePanel, "Manage Courses");
        tabbedPane.addTab("Grades", new ImageIcon(), gradePanel, "Manage Grades");
        tabbedPane.addTab("Reports", new ImageIcon(), reportPanel, "Generate Reports");
        
        // Add change listener to refresh panels when switched
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            refreshCurrentPanel(selectedIndex);
        });
        
        // Add tabbed pane to frame
        // IMPORTANT: Use getContentPane() - never add directly to JFrame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Add status bar at bottom
        JPanel statusBar = createStatusBar();
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }
    
    /**
     * Create the menu bar
     */
    private void createMenuBar() {
        menuBar = new JMenuBar();
        
        // File Menu
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
        
        // View Menu
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
        
        // Help Menu
        helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        
        aboutItem = new JMenuItem("About", KeyEvent.VK_A);
        aboutItem.addActionListener(this);
        
        helpMenu.add(aboutItem);
        
        // Add menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        // Set menu bar
        setJMenuBar(menuBar);
    }
    
    /**
     * Create status bar
     * @return Status bar panel
     */
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel statusLabel = new JLabel(Constants.APP_NAME + " - Ready");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        
        statusBar.add(statusLabel);
        
        return statusBar;
    }
    
    /**
     * Refresh the current panel
     * @param index Panel index
     */
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
    
    /**
     * Handle menu item actions - ActionListener implementation
     * @param e Action event
     */
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
    
    /**
     * Exit application with confirmation
     */
    private void exitApplication() {
        int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to exit?\nAll data will be saved automatically.",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (choice == JOptionPane.YES_OPTION) {
            // Save all data before exiting
            dataManager.saveAllData();
            System.exit(0);
        }
    }
    
    /**
     * Show about dialog
     */
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
    
    
    /**
     * Main method - Application entry point
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Set look and feel to system default
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                // Create and show the main frame
                new MainFrame();
            }
        });
    }
}
