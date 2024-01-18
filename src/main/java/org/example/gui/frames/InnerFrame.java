package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.entity.Client;
import org.example.entity.Employee;

import org.example.entity.Room;
import org.example.gui.panels.southSidePanel;
import org.example.gui.tables.ClientTable;
import org.example.gui.tables.EmployeeTable;
import org.example.gui.tables.RoomTable;
import org.example.tools.xmlConverter;
import org.example.tools.reportCreator.*;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyVetoException;

import static org.example.connector.DataBaseConnection.*;
import static org.example.gui.frames.MainFrame.*;
import static org.example.gui.panels.southSidePanel.getFilterContent;
import static org.example.gui.tables.ClientTable.getClientTable;
import static org.example.gui.tables.EmployeeTable.getEmployeeTable;
import static org.example.gui.tables.RoomTable.getRoomTable;

/**
 * window containing a workspace with tables of rooms, clients or employees
 * <p>
 * creates a workspace that includes: buttons for working with table data on the left side,
 * a table for presenting data in the center, a window for adding, changing data on the right side
 * (appears when performing a certain action by clicking on the corresponding button),
 * bottom filter panel
 * </p>
 */
public final class InnerFrame {
    private static final Logger logger = Logger.getLogger(InnerFrame.class.getName());
    /**
     * stores one of the constants: CLIENT_TYPE, EMPLOYEE_TYPE, ROOM_TYPE
     * @see MainFrame#CLIENT_TYPE
     * @see MainFrame#EMPLOYEE_TYPE
     * @see MainFrame#ROOM_TYPE
     */
    private static String tableType;
    private static JInternalFrame innerFrame;
    /**
     * constant for storing the mode of working with XML files for a specific operation
     */
    public static final String RECORDING_MODE = "recording";
    /**
     * constant for storing the mode of working with XML files for a specific operation
     */
    public static final String READING_MODE = "reading";

    /**
     * Inner frame's constructor
     * <p>
     * sets the properties of the inner window, creates the necessary elements:
     * on the left is a panel with buttons, on the bottom is a panel with filters,
     * on the right is a panel with windows for working with tabular information.
     * sets button listeners
     * </p>
     * @param tableType  contains the type of output information: clients, workers, rooms
     * @see southSidePanel#southSidePanel(String)
     * @see ClientTable#ClientTable(String)
     * @see EmployeeTable#EmployeeTable(String)
     * @see RoomTable#RoomTable(String)
     */
    public InnerFrame(String tableType) {

        innerFrame = new JInternalFrame();
        InnerFrame.tableType = tableType;
        JDesktopPane desktopPane = new JDesktopPane();

        innerFrame.setTitle(tableType);
        innerFrame.setResizable(false);
        innerFrame.setClosable(true);
        innerFrame.setMaximizable(false);
        innerFrame.setIconifiable(false);
        innerFrame.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
        innerFrame.setVisible(true);

        logger.info("InnerFrame " + tableType + " was created");

        JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
        JButton add = new JButton(new ImageIcon("./img/add.png"));
        JButton delete = new JButton(new ImageIcon("./img/delete.png"));
        JButton edit = new JButton(new ImageIcon("./img/edit.png"));
        JButton update = new JButton(new ImageIcon("./img/update.png"));
        JButton exportXML = new JButton(new ImageIcon("./img/exportXML.png"));
        JButton importXML = new JButton(new ImageIcon("./img/importXML.png"));
        JButton createPDF = new JButton(new ImageIcon("./img/createPDF.png"));
        add.setToolTipText("Add new string");
        delete.setToolTipText("Delete marked string");
        edit.setToolTipText("Edit marked string");
        update.setToolTipText("Update table");
        exportXML.setToolTipText("Export to XML");
        importXML.setToolTipText("Import from XML");
        if (tableType.equals(EMPLOYEE_TYPE)) createPDF.setToolTipText("Create Employees report in PDF");
        if (tableType.equals(CLIENT_TYPE)) createPDF.setToolTipText("Create Current Residents report in PDF");
        if (tableType.equals(ROOM_TYPE)) createPDF.setToolTipText("Create Free Rooms report in PDF");

        toolBar.add(add);
        toolBar.add(delete);
        toolBar.add(edit);
        toolBar.add(update);
        toolBar.add(exportXML);
        toolBar.add(importXML);
        toolBar.add(createPDF);

        JPanel westSidePanel = new JPanel();
        JPanel eastSidePanel = new JPanel();

        westSidePanel.add(toolBar);
        innerFrame.add(eastSidePanel, BorderLayout.EAST);
        innerFrame.add(westSidePanel, BorderLayout.WEST);
        innerFrame.add(new southSidePanel(tableType), BorderLayout.SOUTH);

        desktopPane.add(innerFrame, BorderLayout.CENTER);
        getMainFrame().add(desktopPane, BorderLayout.CENTER);

        if (InnerFrame.tableType.equals(ROOM_TYPE)) { new RoomTable(getFilterContent()); }
        if (InnerFrame.tableType.equals(CLIENT_TYPE)) { new ClientTable(getFilterContent()); }
        if (InnerFrame.tableType.equals(EMPLOYEE_TYPE)) { new EmployeeTable(getFilterContent()); }

        add.addActionListener(e -> eastSidePanel.add(new AddFrame(tableType)));
        delete.addActionListener(e -> deleteObject(tableType));
        edit.addActionListener(e -> {
            if (tableType.equals(EMPLOYEE_TYPE) && getEmployeeTable().getSelectedRowCount() != 0)
                eastSidePanel.add(new EditFrame(tableType, getEmployeeTable().getSelectedRow()));
            else if (tableType.equals(ROOM_TYPE) && getRoomTable().getSelectedRowCount() != 0)
                eastSidePanel.add(new EditFrame(tableType, getRoomTable().getSelectedRow()));
            else if (tableType.equals(CLIENT_TYPE) && getClientTable().getSelectedRowCount() != 0)
                eastSidePanel.add(new EditFrame(tableType, getClientTable().getSelectedRow()));
            else JOptionPane.showMessageDialog(getMainFrame(), "No line selected");
        });
        update.addActionListener(e -> updateInnerFrame(InnerFrame.tableType));
        exportXML.addActionListener(e -> workWithXML(RECORDING_MODE, tableType));
        importXML.addActionListener(e -> workWithXML(READING_MODE, tableType));
        createPDF.addActionListener(e -> createReportInPDF(tableType));

        try { innerFrame.setMaximum(true); }
        catch (PropertyVetoException ex) { throw new RuntimeException(ex); }
    }

    /**
     * used to get the inner frame object in external classes
     *
     * @return inner frame object
     * @see #innerFrame
     */
    public static JInternalFrame getInnerFrame() { return innerFrame; }

    /**
     * helper function to enable the operation of working with the XML file
     *
     * @param mode  contains the type of operation being performed - import or export
     * @param tableType  contains the type of output information: clients, workers, rooms
     * @see xmlConverter#fromTableToXML(String, String)
     * @see xmlConverter#fromXMLToTable(String, String)
     * @see #RECORDING_MODE
     * @see #READING_MODE
     */
    private void workWithXML(String mode, String tableType) {
        try {
            new FileChooserFrame(mode);
            if (mode.equals(READING_MODE)) xmlConverter.fromXMLToTable(FileChooserFrame.getPath(), tableType);
            if (mode.equals(RECORDING_MODE)) xmlConverter.fromTableToXML(FileChooserFrame.getPath(), tableType);
            JOptionPane.showMessageDialog(getMainFrame(),
                    "File " + FileChooserFrame.getPath() + ".xml was reading and data were recording");
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(getMainFrame(), ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * helper function for the possibility of carrying out the operation of creating a PDF report
     *
     * @param tableType  contains the type of output information: clients, workers, rooms
     * @see org.example.tools.reportCreator.employeesReportCreator
     */
    private void createReportInPDF(String tableType) {
        int choice = JOptionPane.showConfirmDialog(getMainFrame(), "Do you want create PDF report?", "Confirm creating",
                JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            if (tableType.equals(EMPLOYEE_TYPE)) new employeesReportCreator();
            if (tableType.equals(CLIENT_TYPE)) new residentsReportCreator();
            if (tableType.equals(ROOM_TYPE)) new freeRoomsReportCreator();
            JOptionPane.showMessageDialog(getMainFrame(), "Employee's PDF report was created");
        }
        else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(),
                "You refused to create PDF report");
    }

    /**
     * updates the information in the table after the actions performed on it
     *
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public static void updateInnerFrame(String tableType) {
        innerFrame.dispose();
        logger.info("InnerFrame " + tableType + " was updated");
        new InnerFrame(tableType);
    }

    /**
     * helper function to delete table/database objects
     * <p>
     * removes a table/database field when a row is selected.
     * before that, displays an informational message that notifies the user
     * about the properties of the object being deleted
     * </p>
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    private void deleteObject(String tableType) {
        if (tableType.equals(EMPLOYEE_TYPE) && getEmployeeTable().getSelectedRowCount() != 0) {
            Employee employee = getEmployee(Integer.parseInt(String.valueOf(getEmployeeTable().
                    getValueAt(getEmployeeTable().getSelectedRow(), 0))));
            int choice = JOptionPane.showConfirmDialog(getMainFrame(),
                    "Do you want delete the next employee?\nName: " + employee.getName() +
                            "\nSurname: " + employee.getSurname() + "\nPost: " + employee.getPost() +
                            "\nSalary: " + employee.getSalary(), "Confirm remove",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteEmployee(employee);
                JOptionPane.showMessageDialog(getMainFrame(), "Employee was deleted");
            }
            else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(), "You refused to remove");
        }

        else if (tableType.equals(ROOM_TYPE) && getRoomTable().getSelectedRowCount() != 0) {
            Room room = getRoom(Integer.parseInt(String.valueOf(getRoomTable().
                    getValueAt(getRoomTable().getSelectedRow(), 0))));
            int choice = JOptionPane.showConfirmDialog(getMainFrame(),
                    "Do you want delete the next room?\nNumber: " + room.getNumberOfRoom() +
                            "\nOccupied: : " + room.getIsOccupied() +
                            "\nSize: " + room.getSize() + "\nCost: " + room.getCost() +
                            "\n" + room.getClients().size() +
                            " clients will be deleted from the\nclient's list along with the room",
                    "Confirm remove",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteRoom(room);
                JOptionPane.showMessageDialog(getMainFrame(), "Room was deleted");
            }
            else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(), "You refused to remove");
        }

        else if (tableType.equals(CLIENT_TYPE) && getClientTable().getSelectedRowCount() != 0) {
            Client client = getClient(Integer.parseInt(String.valueOf(getClientTable().
                    getValueAt(getClientTable().getSelectedRow(), 0))));
            int choice = JOptionPane.showConfirmDialog(getMainFrame(),
                    "Do you want delete the next client?\nName: " + client.getName() +
                            "\nSurname: " + client.getSurname() + "\nRoom: " +
                            client.getClientRoom().getNumberOfRoom() + "\nLeft time: " + client.getLeftTime() +
                            " days", "Confirm remove",
                    JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                deleteClient(client);
                JOptionPane.showMessageDialog(getMainFrame(), "Client was deleted");
            }
            else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(),
                    "You refused to remove");
        }

        else JOptionPane.showMessageDialog(getMainFrame(), "No line selected");
    }
}
