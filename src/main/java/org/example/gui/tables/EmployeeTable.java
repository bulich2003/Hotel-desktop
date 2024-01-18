package org.example.gui.tables;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Employee;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

import static org.example.gui.frames.InnerFrame.getInnerFrame;

/**
 * table containing employee data
 */
public final class EmployeeTable {
    private static final Logger logger = Logger.getLogger(EmployeeTable.class.getName());
    private static JTable employeeTable;

    /**
     * employee's table constructor
     * <p>
     * creates a DefaultTableModel to which employee data is added,
     * then collects a table from the model, adds sorting and filtering objects
     * </p>
     */
    public EmployeeTable(String filterContent) {

        DefaultTableModel employeeTableModel = new DefaultTableModel(){
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

        List<Employee> employees = DataBaseConnection.getAllEmployees();

        employeeTableModel.addColumn("ID");
        employeeTableModel.addColumn("Name");
        employeeTableModel.addColumn("Surname");
        employeeTableModel.addColumn("Post");
        employeeTableModel.addColumn("Salary");

        int row = 0;
        for (Employee employee : employees) employeeTableModel.insertRow( row++,
                new Object[]{ employee.getIdPerson(), employee.getName(),
                        employee.getSurname(), employee.getPost(), employee.getSalary()});

        employeeTable = new JTable(employeeTableModel);
        employeeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        Renderer defaultTableCellRenderer = new Renderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        employeeTable.setDefaultRenderer(String.class, defaultTableCellRenderer);
        employeeTable.setDefaultRenderer(Integer.class, defaultTableCellRenderer);

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(employeeTableModel);
        employeeTable.setRowSorter(sorter);

        sorter.setRowFilter(RowFilter.regexFilter(filterContent));

        JScrollPane scrollPane = new JScrollPane(employeeTable);
        getInnerFrame().add(scrollPane, BorderLayout.CENTER);

        logger.info("EmployeeTable was created");
    }

    /**
     * used to get the employee table object in external classes
     *
     * @return employee table object
     * @see #employeeTable
     */
    public static JTable getEmployeeTable() { return employeeTable; }

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
            else { setBackground(Color.WHITE); setForeground(Color.BLACK); }

            return this;
        }
    }
}