package org.example.gui.frames;

import org.apache.log4j.Logger;
import org.example.connector.DataBaseConnection;
import org.example.entity.Client;
import org.example.entity.Employee;
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

import static org.example.gui.frames.MainFrame.*;


/**
 * window for filling in information
 * <p>
 * used when adding a new client, worker, or room. illuminated on the right side of the inner window
 * </p>
 */
public final class AddFrame extends JInternalFrame {
    private static final Logger logger = Logger.getLogger(AddFrame.class.getName());
    private static JTextField numberOfRoom;
    private static JTextField cost;
    private static JTextField size;
    private static JTextField name;
    private static JTextField surname;
    private static JTextField lengthOfStay;
    private static JTextField post;
    private static JTextField salary;
    private static JList<String> roomList;

    /**
     * adding frame's constructor
     * <p>
     * creates text fields filled with old data for adding
     * </p>
     *
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public AddFrame(String tableType) {

        Box panel = Box.createVerticalBox();

        setTitle("Add");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setClosable(true);
        setMaximizable(false);

        logger.info("AddFrame was created");

        if (tableType.equals(ROOM_TYPE)) {

            JButton saveRoom = new JButton("Save");
            numberOfRoom = new JTextField(15);
            cost = new JTextField(15);
            size = new JTextField(3);

            panel.add(new JLabel("Enter number of room:"));
            panel.add(numberOfRoom);
            panel.add(new JLabel("Enter cost of room:"));
            panel.add(cost);
            panel.add(new JLabel("Enter size of room (m'2):"));
            panel.add(size);
            panel.add(saveRoom);

            saveRoom.addActionListener(e -> {
                try {
                    readRoomFields();
                    dispose();
                } catch (EmptyFieldException | NumberStartsFromZeroException |
                         RepeatedValueException | NonDigitsInNumberException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                    throw new RuntimeException(ex);
                }
            });
        }

        if (tableType.equals(CLIENT_TYPE)) {

            DefaultListModel<String> roomListModel = new DefaultListModel<>();

            List<Room> rooms = DataBaseConnection.getAllRooms();
            for (Room room : rooms) if (!room.getIsOccupied()) roomListModel.addElement(room.getNumberOfRoom());

            roomList = new JList<>(roomListModel);
            roomList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JScrollPane scrollPane = new JScrollPane(roomList);

            JButton saveClient = new JButton("Save");
            name = new JTextField(15);
            surname = new JTextField(15);
            lengthOfStay = new JTextField(5);

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
                    readClientFields();
                    dispose();
                } catch (EmptyFieldException | NumberStartsFromZeroException | NonDigitsInNumberException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                    throw new RuntimeException(ex);
                }
            });
        }

        if (tableType.equals(EMPLOYEE_TYPE)) {

            JButton saveEmployee = new JButton("Save");
            name = new JTextField(15);
            surname = new JTextField(15);
            post = new JTextField(10);
            salary = new JTextField(5);

            panel.add(new JLabel("Enter name of employee:"));
            panel.add(name);
            panel.add(new JLabel("Enter surname of employee:"));
            panel.add(surname);
            panel.add(new JLabel("Enter post this human:"));
            panel.add(post);
            panel.add(new JLabel("Enter his salary:"));
            panel.add(salary);
            panel.add(saveEmployee);

            saveEmployee.addActionListener(e -> {
                try {
                    readEmployeeFields();
                    dispose();
                } catch (EmptyFieldException | NumberStartsFromZeroException | NonDigitsInNumberException ex) {
                    JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage());
                    throw new RuntimeException(ex);
                }
            });
        }

        this.add(panel);
        setVisible(true);
    }

    /**
     * read employee's adding frame's fields
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readEmployeeFields() throws EmptyFieldException, NumberStartsFromZeroException, NonDigitsInNumberException {
        checkField(name.getText(), "str");
        checkField(surname.getText(), "str");
        checkField(post.getText(), "str");
        checkField(salary.getText(), "int");

        DataBaseConnection.addEmployee(new Employee(name.getText(), surname.getText(),
                post.getText(), Integer.parseInt(salary.getText())));

        logger.info("AddEmployee fields were read");
    }

    /**
     * read client's adding frame's fields
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readClientFields() throws EmptyFieldException, NumberStartsFromZeroException, NonDigitsInNumberException {
        checkField(name.getText(), "str");
        checkField(surname.getText(), "str");
        String numberOfRoomClient = roomList.getSelectedValue();
        checkField(numberOfRoomClient, "str");
        checkField(lengthOfStay.getText(), "int");

        GregorianCalendar inData = new GregorianCalendar();
        GregorianCalendar outData = new GregorianCalendar();
        outData.add(Calendar.DAY_OF_MONTH, Integer.parseInt(lengthOfStay.getText()));

        DataBaseConnection.addClient(new Client(name.getText(), surname.getText(),
                DataBaseConnection.getRoom(numberOfRoomClient), inData, outData));

        DataBaseConnection.getRoom(numberOfRoomClient).setIsOccupied(true);

        logger.info("AddClient fields were read");
    }

    /**
     * read room's adding frame's fields
     *
     * @throws EmptyFieldException  if you input nothing of first symbol is null
     * @throws NumberStartsFromZeroException  if the number you entered contains a zero in place of the first digit
     * @throws NonDigitsInNumberException  if the number you entered contains anything other than digits
     */
    private void readRoomFields() throws EmptyFieldException, NumberStartsFromZeroException,
            NonDigitsInNumberException, RepeatedValueException {
        checkField(numberOfRoom.getText(), "str");
        checkNumberOfRoomValue(numberOfRoom.getText());
        checkField(cost.getText(), "int");
        checkField(size.getText(), "int");

        DataBaseConnection.addRoom(new Room(numberOfRoom.getText(), false,
                    Integer.parseInt(cost.getText()), Integer.parseInt(size.getText())));

        logger.info("AddRoom fields were read");
    }

    private static void checkNumberOfRoomValue(String numberOfRoom) throws RepeatedValueException {
        List<Room> rooms = DataBaseConnection.getAllRooms();
        for (Room room : rooms)
            if (room.getNumberOfRoom().equals(numberOfRoom)) throw new RepeatedValueException("Room with current number already exists");

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

    //Only for test!!!
    public static boolean publicIsNumber(String field) { return isOnlyDigits(field); }
    public static void publicCheckField(String field, String dataType) throws NonDigitsInNumberException,
            NumberStartsFromZeroException, EmptyFieldException { checkField(field, dataType); }

}
