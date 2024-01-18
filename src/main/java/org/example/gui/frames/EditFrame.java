package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Room;
import org.example.errors.EmptyFieldException;
import org.example.errors.NonDigitsInNumberException;
import org.example.errors.NumberStartsFromZeroException;
import org.example.errors.RepeatedValueException;

import javax.swing.*;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import static org.example.connector.DataBaseConnection.getClient;
import static org.example.connector.DataBaseConnection.getRoom;
import static org.example.gui.frames.MainFrame.*;
import static org.example.gui.tables.ClientTable.getClientTable;
import static org.example.gui.tables.EmployeeTable.getEmployeeTable;
import static org.example.gui.tables.RoomTable.getRoomTable;

/**
 * window for filling in information
 * <p>
 * used when editing a new client, worker, or room. illuminated on the right side of the inner window
 * </p>
 */
public class EditFrame extends JInternalFrame {
    private static final Logger logger = Logger.getLogger(EditFrame.class.getName());
    private static JTextField numberOfRoom;
    private static boolean isOccupied;
    private static JTextField cost;
    private static JTextField size;
    private static JTextField name;
    private static JTextField surname;
    private static JTextField lengthOfStay;
    private static JTextField post;
    private static JTextField salary;
    private static JList<String> roomList;

    /**
     * editing frame's constructor
     * <p>
     * creates text fields filled with old data for editing
     * </p>
     *
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public EditFrame(String tableType, int numberOfSelectedRow) {
        Box panel = Box.createVerticalBox();

        setTitle("Edit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setClosable(true);
        setMaximizable(false);

        logger.info("EditFrame was created");

        if (tableType.equals(ROOM_TYPE)) {

            JButton saveRoom = new JButton("Save");

            numberOfRoom = new JTextField((String) getRoomTable().getValueAt(numberOfSelectedRow, 1));
            isOccupied = (boolean) getRoomTable().getValueAt(numberOfSelectedRow, 2);
            cost = new JTextField(String.valueOf(getRoomTable().getValueAt(numberOfSelectedRow, 3)));
            size = new JTextField(String.valueOf(getRoomTable().getValueAt(numberOfSelectedRow, 4)));

            JCheckBox checkBox = new JCheckBox("Occupied", isOccupied);
            checkBox.setHorizontalTextPosition(JCheckBox.LEFT);

            panel.add(new JLabel("Enter number of room:"));
            panel.add(numberOfRoom);
            panel.add(checkBox);
            panel.add(new JLabel("Enter cost of room:"));
            panel.add(cost);
            panel.add(new JLabel("Enter size of room (m'2):"));
            panel.add(size);
            panel.add(saveRoom);

            saveRoom.addActionListener(e -> {
                try {
                    readRoomFields((Integer) getRoomTable().getValueAt(numberOfSelectedRow, 0));
                } catch (EmptyFieldException | NumberStartsFromZeroException |
                         NonDigitsInNumberException | RepeatedValueException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                }
            });

            checkBox.addItemListener(e -> updateIsOccupied());
        }

        if (tableType.equals(CLIENT_TYPE)) {

            DefaultListModel<String> roomListModel = new DefaultListModel<>();

            List<Room> rooms = DataBaseConnection.getAllRooms();
            for (Room room : rooms) if (!room.getIsOccupied() || Objects.equals(room.getNumberOfRoom(), getClient(
                    (Integer) getClientTable().getValueAt(numberOfSelectedRow, 0)).getClientRoom().getNumberOfRoom()))
                        roomListModel.addElement(room.getNumberOfRoom());

            roomList = new JList<>(roomListModel);
            roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(roomList);

            JButton saveClient = new JButton("Save");
            name = new JTextField((String) getClientTable().getValueAt(numberOfSelectedRow, 1));
            surname = new JTextField((String) getClientTable().getValueAt(numberOfSelectedRow, 2));
            lengthOfStay = new JTextField(String.valueOf(getClient(
                    (Integer) getClientTable().getValueAt(numberOfSelectedRow, 0)).getLengthOfStay()));

            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Enter name of client:"));
            panel.add(name);
            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Enter surname of client:"));
            panel.add(surname);
            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Enter number of his room: "));
            panel.add(scrollPane);
            panel.add(Box.createVerticalStrut(15));
            panel.add(new JLabel("Enter duration his stay (days):"), BorderLayout.NORTH);
            panel.add(lengthOfStay);
            panel.add(Box.createVerticalStrut(15));
            panel.add(saveClient);

            saveClient.addActionListener(e -> {
                try {
                    readClientFields((Integer) getClientTable().getValueAt(numberOfSelectedRow, 0));
                } catch (EmptyFieldException | NumberStartsFromZeroException | NonDigitsInNumberException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                }
            });
        }

        if (tableType.equals(EMPLOYEE_TYPE)) {

            JButton saveClient = new JButton("Save");
            name = new JTextField((String) getEmployeeTable().getValueAt(numberOfSelectedRow, 1));
            surname = new JTextField((String) getEmployeeTable().getValueAt(numberOfSelectedRow, 2));
            post = new JTextField((String) getEmployeeTable().getValueAt(numberOfSelectedRow, 3));
            salary = new JTextField(String.valueOf(getEmployeeTable().getValueAt(numberOfSelectedRow, 4)));

            panel.add(new JLabel("Enter name of employee:"));
            panel.add(name);
            panel.add(new JLabel("Enter surname of employee:"));
            panel.add(surname);
            panel.add(new JLabel("Enter post this human:"));
            panel.add(post);
            panel.add(new JLabel("Enter his salary:"));
            panel.add(salary);
            panel.add(saveClient);

            saveClient.addActionListener(e -> {
                try {
                    readEmployeeFields((Integer) getEmployeeTable().getValueAt(numberOfSelectedRow, 0));
                } catch (EmptyFieldException | NumberStartsFromZeroException | NonDigitsInNumberException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                }
            });
        }

        this.add(panel);
        setVisible(true);
    }

    /**
     * changes room occupancy
     * <p>
     * inverts the boolean variable that is responsible for the occupancy of the room when the checkbox is clicked
     * </p>
     */
    private void updateIsOccupied() { isOccupied = !isOccupied; }

    /**
     * read employee's editing frame's fields
     * <p>
     *  reads and validates user input.
     *  If the verification is successful, it notifies the user about the changes made,
     *  displaying an information window with a choice
     * </p>
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readEmployeeFields(int employeeId) throws EmptyFieldException, NumberStartsFromZeroException, NonDigitsInNumberException {
        checkField(name.getText(), "str");
        checkField(surname.getText(), "str");
        checkField(post.getText(), "str");
        checkField(salary.getText(), "int");

        int choice = JOptionPane.showConfirmDialog(getMainFrame(), "Save changes?\nName: " + name.getText() +
                "\nSurname: " + surname.getText() + "\nPost: " + post.getText() + "\nSalary: " + salary.getText(), "Confirm changes", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) { DataBaseConnection.editEmployee(employeeId, name.getText(), surname.getText(),
                post.getText(), Integer.parseInt(salary.getText())); dispose(); }
        else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(), "You refused to change");

        logger.info("EditEmployee fields were read");
    }

    /**
     * read client's editing frame's fields
     * <p>
     *  reads and validates user input.
     *  If the verification is successful, it notifies the user about the changes made,
     *  displaying an information window with a choice
     * </p>
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readClientFields(int clientId) throws EmptyFieldException, NumberStartsFromZeroException, NonDigitsInNumberException {
        checkField(name.getText(), "str");
        checkField(surname.getText(), "str");
        String numberOfRoomClient = roomList.getSelectedValue();
        checkField(lengthOfStay.getText(), "int");

        GregorianCalendar inDate = (GregorianCalendar) getClient(clientId).getCheckInDate();
        GregorianCalendar outDate = (GregorianCalendar) getClient(clientId).getCheckInDate().clone();
        outDate.add(Calendar.DAY_OF_MONTH, Integer.parseInt(lengthOfStay.getText()));

        int choice = JOptionPane.showConfirmDialog(getMainFrame(), "Save changes?\nName: " + name.getText() +
                "\nSurname: " + surname.getText() + "\nNumber of room: " + numberOfRoomClient + "\nLength of stay "
                + lengthOfStay.getText(), TITLE_PROPERTY, JOptionPane.YES_NO_OPTION);

        DataBaseConnection.getClient(clientId).getClientRoom().setIsOccupied(false);

        if (choice == JOptionPane.YES_OPTION) { DataBaseConnection.editClient(clientId, name.getText(), surname.getText(),
                getRoom(numberOfRoomClient), inDate, outDate); dispose(); }
        else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(), "You refused to change");

        DataBaseConnection.getClient(clientId).getClientRoom().setIsOccupied(true);

        logger.info("EditClient fields were read");
    }

    /**
     * read room's editing frame's fields
     * <p>
     *  reads and validates user input.
     *  If the verification is successful, it notifies the user about the changes made,
     *  displaying an information window with a choice
     * </p>
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readRoomFields(int roomId) throws EmptyFieldException, NumberStartsFromZeroException,
            NonDigitsInNumberException, RepeatedValueException {
        checkField(numberOfRoom.getText(), "str");
        checkNumberOfRoomValue(numberOfRoom.getText(), roomId);
        checkField(cost.getText(), "int");
        checkField(size.getText(), "int");

        int choice = JOptionPane.showConfirmDialog(getMainFrame(), "Save changes?\nNumber of room: " +
                numberOfRoom.getText() + "\nOccupied: " + isOccupied + "\nCost: " +
                cost.getText() + "\nSize: " + size.getText(), TITLE_PROPERTY, JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) { DataBaseConnection.editRoom(roomId, numberOfRoom.getText(),
                isOccupied, Integer.parseInt(cost.getText()), Integer.parseInt(size.getText())); dispose(); }
        else if (choice == JOptionPane.NO_OPTION) JOptionPane.showMessageDialog(getMainFrame(), "You refused to change");

        logger.info("EditRoom fields were read");
    }

    private static void checkNumberOfRoomValue(String numberOfRoom, int id) throws RepeatedValueException {
        List<Room> rooms = DataBaseConnection.getAllRooms();
        for (Room room : rooms)
            if (room.getNumberOfRoom().equals(numberOfRoom) && !getRoom(id).getNumberOfRoom().equals(numberOfRoom)) throw new RepeatedValueException("Room with current number already exists");

    }

    /**
     * checks the text field for type matching
     *
     * @param field  contains field information in text form
     * @param dataType  contains the data type (string or integer)
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private static void checkField(String field, String dataType) throws EmptyFieldException,
            NumberStartsFromZeroException, NonDigitsInNumberException {
        if (field == null) throw new EmptyFieldException("Not all fields are filled");
        if (field.isEmpty() || field.indexOf(" ") == 0) throw new EmptyFieldException("Field isn't filled or first symbol is NULL: " + field);
        if (dataType.equals("int") && field.indexOf('0') == 0) throw new NumberStartsFromZeroException("Zero can't be the first digit: " + field);
        if (dataType.equals("int") && !isOnlyDigits(field)) throw new NonDigitsInNumberException("The number contains an wrong character: " + field);

        logger.info(dataType + " " + field + " was checked");
    }

    /**
     * checks for extraneous characters (not numbers) in the field
     *
     * @param field  contains field information in text form
     * @return  if contains only digits - true, else - false
     */
    private static boolean isOnlyDigits(String field) {
        if (field.length() == 0) return false;
        for (int i = 0; i < field.length(); ++i) if (!Character.isDigit(field.charAt(i))) return false;
        return true;
    }
}
