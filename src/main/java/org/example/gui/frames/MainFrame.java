package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.Config;
import org.example.tools.reportCreator.priceListCreator;

import javax.swing.*;
import java.awt.*;

/**
 * main program window
 * <p>
 * creates the main window of the program, which contains the menu for navigation at the top,
 * an empty area for placing the inner window at the bottom
 * </p>
 */
public final class MainFrame {
    private static final Logger logger = Logger.getLogger(MainFrame.class.getName());
    /**
     * constant to designate the type of database table, in this case the client table
     */
    public static final String CLIENT_TYPE = "client";
    /**
     * constant to designate the type of database table, in this case the employee table
     */
    public static final String EMPLOYEE_TYPE = "employee";
    /**
     * constant to designate the type of database table, in this case the room table
     */
    public static final String ROOM_TYPE = "room";
    private static JFrame mainFrame;

    /**
     * Main frame constructor
     * <p>
     * sets dimensions, position, layout manager and required start elements of the main window
     * </p>
     */
    public MainFrame() {

        mainFrame = new JFrame();

        mainFrame.setSize(Config.WIDTH, Config.HEIGHT);
        mainFrame.setLocation(Config.X_MAINFRAME, Config.Y_MAINFRAME);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        logger.info("MainFrame was created");

        mainFrame.add(new MainMenu(), BorderLayout.NORTH);

        mainFrame.setVisible(true);
    }


    /**
     * used to get the main window object in external classes
     *
     * @return  main frame object
     * @see #mainFrame
     */
    public static JFrame getMainFrame() { return mainFrame; }

    /**
     * menu for navigation in program
     * <p>
     * contains the function of switching between tables, opening help, opening program logs
     * </p>
     */
    private static final class MainMenu extends JMenuBar {
        private static final Logger logger = Logger.getLogger(MainMenu.class.getName());

        /**
         * main menu constructor
         * <p>
         * creates a program menu, adds the necessary menu sections,
         * and also connects event handlers when they are clicked
         * </p>
         */
        public MainMenu() {
            logger.info("MainMenu was created");

            JMenu file = new JMenu("File");
            JMenu tools = new JMenu("Tools");
            JMenu settings = new JMenu("Settings");
            JMenu help = new JMenu("Help");

            JMenuItem clientTable = new JMenuItem("Open client's table");
            JMenuItem employeeTable = new JMenuItem("Open employee's table");
            JMenuItem roomTable = new JMenuItem("Open room's table");
            JMenuItem createMonthlyReport = new JMenuItem("Create monthly report");
            JMenuItem createPriceList = new JMenuItem("Create current price list");
            JMenuItem openLog = new JMenuItem("Open log");
            JMenuItem reference = new JMenuItem("Reference");

            file.add(clientTable);
            file.add(employeeTable);
            file.add(roomTable);
            tools.add(createMonthlyReport);
            tools.add(createPriceList);
            settings.add(openLog);
            help.add(reference);

            this.add(file);
            this.add(tools);
            this.add(settings);
            this.add(help);

            clientTable.addActionListener(e -> new InnerFrame(CLIENT_TYPE));
            employeeTable.addActionListener(e -> new InnerFrame(EMPLOYEE_TYPE));
            roomTable.addActionListener(e -> new InnerFrame(ROOM_TYPE));
            createMonthlyReport.addActionListener(e -> new DateChooserFrame());
            createPriceList.addActionListener(e -> {
                int choice = JOptionPane.showConfirmDialog(getMainFrame(), "Do you want create price list?", "Confirm creating",
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    new priceListCreator();
                    JOptionPane.showMessageDialog(getMainFrame(), "Price list was created");
                }
                else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(),
                        "You refused to create price list");
            });
            openLog.addActionListener(e -> new LogFrame());
        }
    }
}
