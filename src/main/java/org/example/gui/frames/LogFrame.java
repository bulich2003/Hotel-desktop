package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.Config;
import org.example.gui.tables.LogTable;

import javax.swing.*;
import java.awt.*;

/**
 * window containing log data
 * <p>
 * creates a workspace for viewing and searching for the necessary program logs
 * </p>
 */
public class LogFrame {
    private static final Logger logger = Logger.getLogger(LogFrame.class.getName());
    private static JFrame logFrame;

    /**
     * log's frame constructor
     * <p>
     * sets dimensions, position, log window layout manager, also adds log data table
     * </p>
     * @see LogTable#LogTable()
     */
    LogFrame() {
        logFrame = new JFrame();

        logFrame.setSize(Config.WIDTH, Config.HEIGHT);
        logFrame.setLocation(Config.X_LOG_FRAME, Config.Y_LOG_FRAME);
        logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        logFrame.setTitle("Log frame");
        logFrame.setLayout(new BorderLayout());

        logger.info("LogFrame was created");

        logFrame.add(new LogMenu(), BorderLayout.NORTH);
        new LogTable();

        logFrame.setVisible(true);
    }

    public static JFrame getLogFrame() { return logFrame; }

    /**
     * menu for working with logs
     */
    private static final class LogMenu extends JMenuBar {
        private static final Logger logger = Logger.getLogger(LogMenu.class.getName());

        /**
         * log's menu constructor
         */
        LogMenu() {
            logger.info("LogMenu was created");

            JMenu menu = new JMenu("Menu");
            this.add(menu);
        }
    }


}
