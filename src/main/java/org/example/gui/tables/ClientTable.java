package org.example.gui.tables;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

import static org.example.gui.frames.InnerFrame.getInnerFrame;

/**
 * table containing client data
 */
public final class ClientTable {
    private static final Logger logger = Logger.getLogger(ClientTable.class.getName());
    private static JTable clientTable;

    /**
     * client's table constructor
     * <p>
     * creates a DefaultTableModel to which client data is added,
     * then collects a table from the model, adds sorting and filtering objects
     * </p>
     */
    public ClientTable(String filterContent) {

        DefaultTableModel clientTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 4 -> Integer.class;
                    case 1, 2, 3 -> String.class;
                    default -> Object.class;
                };
            }
        };

        List<Client> clients = DataBaseConnection.getAllClients();

        clientTableModel.addColumn("ID");
        clientTableModel.addColumn("Name");
        clientTableModel.addColumn("Surname");
        clientTableModel.addColumn("Room");
        clientTableModel.addColumn("Left time (in days)");

        int row = 0;
        for (Client client : clients) clientTableModel.insertRow( row++,
                new Object[]{ client.getIdPerson(), client.getName(), client.getSurname(),
                        client.getClientRoom().getNumberOfRoom(), client.getLeftTime() });

        clientTable = new JTable(clientTableModel);
        clientTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Renderer defaultTableCellRenderer = new Renderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        clientTable.setDefaultRenderer(String.class, defaultTableCellRenderer);
        clientTable.setDefaultRenderer(Integer.class, defaultTableCellRenderer);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(clientTableModel);
        clientTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter(filterContent));

        JScrollPane scrollPane = new JScrollPane(clientTable);
        getInnerFrame().add(scrollPane, BorderLayout.CENTER);

        logger.info("ClientTable was created");
    }

    /**
     * used to get the client table object in external classes
     *
     * @return client table object
     * @see #clientTable
     */
    public static JTable getClientTable() { return clientTable; }

    private static class Renderer extends DefaultTableCellRenderer
    {
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            setText(value.toString());

            if (isSelected) { setBackground(Color.MAGENTA); setForeground(Color.BLACK); }
            else if ((Integer) table.getValueAt(row, 4) == 0) { setBackground(Color.YELLOW); setForeground(Color.BLACK); }
            else if ((Integer) table.getValueAt(row, 4) < 0) { setBackground(Color.RED); setForeground(Color.BLACK); }
            else { setBackground(Color.WHITE); setForeground(Color.BLACK); }

            return this;
        }
    }
}


