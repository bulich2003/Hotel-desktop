package org.example.gui.tables;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

import static org.example.gui.frames.InnerFrame.getInnerFrame;

/**
 * table containing room data
 */
public final class RoomTable {
    private static final Logger logger = Logger.getLogger(RoomTable.class.getName());
    private static JTable roomTable;

    /**
     * room's table constructor
     * <p>
     * creates a DefaultTableModel to which room data is added,
     * then collects a table from the model, adds sorting and filtering objects
     * </p>
     */
    public RoomTable(String filterContent) {

        DefaultTableModel roomTableModel = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int column) {
                return switch (column) {
                    case 0, 3, 4 -> Integer.class;
                    case 1 -> String.class;
                    case 2 -> Boolean.class;
                    default -> Object.class;
                };
            }
        };

        List<Room> rooms = DataBaseConnection.getAllRooms();

        roomTableModel.addColumn("ID");
        roomTableModel.addColumn("Room number");
        roomTableModel.addColumn("Occupied");
        roomTableModel.addColumn("Cost");
        roomTableModel.addColumn("Size");

        int row = 0;
        for (Room room : rooms) roomTableModel.insertRow( row++,
                new Object[]{ room.getIdRoom(), room.getNumberOfRoom(), room.getIsOccupied(),
                        room.getCost(), room.getSize()});

        roomTable = new JTable(roomTableModel);
        roomTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Renderer defaultTableCellRenderer = new Renderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        roomTable.setDefaultRenderer(String.class, defaultTableCellRenderer);
        roomTable.setDefaultRenderer(Integer.class, defaultTableCellRenderer);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(roomTableModel);
        roomTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter(filterContent));

        JScrollPane scrollPane = new JScrollPane(roomTable);
        getInnerFrame().add(scrollPane, BorderLayout.CENTER);

        logger.info("RoomTable was created");
    }

    /**
     * used to get the room table object in external classes
     *
     * @return room table object
     * @see #roomTable
     */
    public static JTable getRoomTable() { return roomTable; }

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
            else if (!((Boolean) table.getValueAt(row, 2))) { setBackground(Color.GREEN); setForeground(Color.BLACK); }
            else if ((Boolean) table.getValueAt(row, 2)) { setBackground(Color.RED); setForeground(Color.BLACK); }
            else { setBackground(Color.WHITE); setForeground(Color.BLACK); }

            return this;
        }
    }
}
