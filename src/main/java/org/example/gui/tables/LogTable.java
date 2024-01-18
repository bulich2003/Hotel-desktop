package org.example.gui.tables;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Log;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.example.gui.frames.LogFrame.getLogFrame;

/**
 * table containing log data
 */
public class LogTable {
    private static final Logger logger = Logger.getLogger(LogTable.class.getName());

    /**
     * log's table constructor
     * <p>
     * creates a DefaultTableModel to which log data is added,
     * then collects a table from the model, adds sorting and filtering objects
     * </p>
     */
    public LogTable() {

        DefaultTableModel logTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 1, 2, 3, 4, 5 -> String.class;
                    default -> Object.class;
                };
            }
        };

        List<Log> logs = DataBaseConnection.getAllLogs();

        logTableModel.addColumn("Date");
        logTableModel.addColumn("In class");
        logTableModel.addColumn("In method");
        logTableModel.addColumn("In string");
        logTableModel.addColumn("Level");
        logTableModel.addColumn("Message");

        int row = 0;
        for (Log log : logs) logTableModel.insertRow(row++, new Object[] {new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z").
                format(log.getDate().getTime()), log.getPackageClass(), log.getMethod(), log.getNumberOfString(),
                log.getLevel(), log.getMessage()});

        JTable logTable = new JTable(logTableModel);

        logTable.getColumnModel().getColumn(0).setMaxWidth(140);
        logTable.getColumnModel().getColumn(0).setMinWidth(140);
        logTable.getColumnModel().getColumn(1).setMinWidth(140);
        logTable.getColumnModel().getColumn(1).setMaxWidth(140);
        logTable.getColumnModel().getColumn(2).setMaxWidth(120);
        logTable.getColumnModel().getColumn(2).setMinWidth(120);
        logTable.getColumnModel().getColumn(3).setMaxWidth(60);
        logTable.getColumnModel().getColumn(4).setMaxWidth(60);
        logTable.getColumnModel().getColumn(5).setMinWidth(200);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(logTableModel);
        logTable.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(logTable);
        getLogFrame().add(scrollPane, BorderLayout.CENTER);

        logger.info("LogTableModel was created");
    }
}