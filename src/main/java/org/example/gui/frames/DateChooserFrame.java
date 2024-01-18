package org.example.gui.frames;

import org.example.Config;
import org.example.connector.DataBaseConnection;
import org.example.entity.Client;
import org.example.entity.Room;
import org.example.errors.EmptyFieldException;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

import static org.example.gui.frames.MainFrame.getMainFrame;
import org.example.tools.reportCreator.monthlyHotelWorkReport;

public class DateChooserFrame extends JFrame {
    private final JList<String> months;
    private final JList<Integer> years;
    private Map<Room, Integer> incomePerRoom;
    private Map<Room, Integer> lengthOfStayInRoom;
    private ArrayList<Client> chosenClients;
    public static Map<String, Integer> MONTHS = new HashMap<>();
    private int monthNumber;
    private int yearNumber;

    public DateChooserFrame() {

        MONTHS.put("January", 0);
        MONTHS.put("February", 1);
        MONTHS.put("March", 2);
        MONTHS.put("April", 3);
        MONTHS.put("May", 4);
        MONTHS.put("June", 5);
        MONTHS.put("July", 6);
        MONTHS.put("August", 7);
        MONTHS.put("September", 8);
        MONTHS.put("October", 9);
        MONTHS.put("November", 10);
        MONTHS.put("December", 11);

        setSize(Config.DATA_CHOOSER_FRAME_SIZE, Config.DATA_CHOOSER_FRAME_SIZE);
        setLocation(Config.X_MAINFRAME + Config.WIDTH, Config.Y_MAINFRAME);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("Choose period");

        JButton create = new JButton("Create");

        DefaultListModel<Integer> yearsListModel = new DefaultListModel<>();
        for (int i = Config.START_WORKING_YEAR; i <= new GregorianCalendar().get(Calendar.YEAR); ++i) yearsListModel.addElement(i);
        years = new JList<>(yearsListModel);
        years.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane rightJScrollPane = new JScrollPane(years);

        DefaultListModel<String> monthListModel = new DefaultListModel<>();
        for (String i : MONTHS.keySet()) monthListModel.addElement(i);
        months = new JList<>(monthListModel);
        months.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane leftJScrollPane = new JScrollPane(months);

        this.add(leftJScrollPane, BorderLayout.NORTH);
        this.add(rightJScrollPane, BorderLayout.CENTER);
        this.add(create, BorderLayout.SOUTH);

        create.addActionListener(e -> {
            try {
                readingFields();
                calculatingValues();

                int choice = JOptionPane.showConfirmDialog(this, "Values were calculated.\n" +
                        "Do you want create PDF report?", "Confirm creating", JOptionPane.YES_NO_OPTION);

                if (choice == JOptionPane.YES_OPTION) {
                    new monthlyHotelWorkReport(incomePerRoom, lengthOfStayInRoom, monthNumber + 1, yearNumber, chosenClients.size());
                    JOptionPane.showMessageDialog(getMainFrame(), "Monthly PDF report was created");
                    dispose();
                }

                else if (choice == JOptionPane.NO_OPTION) { JOptionPane.showMessageDialog(this,
                        "You refused to create PDF report"); dispose(); }
            }
            catch (EmptyFieldException ex) { JOptionPane.showMessageDialog(getMainFrame(), ex.getMessage()); }
        });

        setVisible(true);
    }

    private void readingFields() throws EmptyFieldException {
        String month = months.getSelectedValue();
        Integer year = years.getSelectedValue();

        if (month == null || year == null) throw new EmptyFieldException("Not all fields are filled");
    }

    private void calculatingValues() {
        monthNumber = MONTHS.get(months.getSelectedValue());
        yearNumber = years.getSelectedValue();

        incomePerRoom = new HashMap<>();
        chosenClients = new ArrayList<>();
        lengthOfStayInRoom = new HashMap<>();

        List<Client> clients = DataBaseConnection.getAllClients();

        for (Client client : clients)
            if (client.getCheckInDate().get(GregorianCalendar.YEAR) == yearNumber
                    ||
            client.getCheckOutDate().get(GregorianCalendar.YEAR) == yearNumber
                    ||
            (client.getCheckInDate().get(GregorianCalendar.YEAR) < yearNumber &&
            client.getCheckOutDate().get(GregorianCalendar.YEAR) >= yearNumber))

                if (client.getCheckInDate().get(GregorianCalendar.MONTH) == monthNumber
                        ||
                client.getCheckOutDate().get(GregorianCalendar.MONTH) == monthNumber
                        ||
                (client.getCheckInDate().get(GregorianCalendar.MONTH) < monthNumber &&
                client.getCheckOutDate().get(GregorianCalendar.MONTH) >= monthNumber)
                        ||
                (client.getCheckInDate().get(GregorianCalendar.YEAR) < yearNumber &&
                client.getCheckOutDate().get(GregorianCalendar.MONTH) >= monthNumber))
                {
                    chosenClients.add(client);
                    lengthOfStayInRoom.put(client.getClientRoom(), 0);
                    incomePerRoom.put(client.getClientRoom(), 0);
                }

        for (Client client : chosenClients)
            if (client.getCheckInDate().get(GregorianCalendar.MONTH) == monthNumber &&
                    client.getCheckOutDate().get(GregorianCalendar.MONTH) == monthNumber)
                        lengthOfStayInRoom.put(client.getClientRoom(),
                                lengthOfStayInRoom.get(client.getClientRoom()) +
                                client.getCheckOutDate().get(GregorianCalendar.DAY_OF_YEAR) -
                                client.getCheckInDate().get(GregorianCalendar.DAY_OF_YEAR));

            else if (client.getCheckInDate().get(GregorianCalendar.MONTH) != monthNumber &&
                    client.getCheckOutDate().get(GregorianCalendar.MONTH) == monthNumber)
                        lengthOfStayInRoom.put(client.getClientRoom(),
                                lengthOfStayInRoom.get(client.getClientRoom()) +
                                client.getCheckOutDate().get(GregorianCalendar.DAY_OF_MONTH));

            else if (client.getCheckInDate().get(GregorianCalendar.MONTH) == monthNumber &&
                    client.getCheckOutDate().get(GregorianCalendar.MONTH) != monthNumber)
                        lengthOfStayInRoom.put(client.getClientRoom(),
                                lengthOfStayInRoom.get(client.getClientRoom()) +
                                Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH) -
                                client.getCheckInDate().get(GregorianCalendar.DAY_OF_MONTH));

            else if (client.getCheckInDate().get(GregorianCalendar.MONTH) != monthNumber &&
                    client.getCheckOutDate().get(GregorianCalendar.MONTH) != monthNumber) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(yearNumber, monthNumber, 1);
                    lengthOfStayInRoom.put(client.getClientRoom(),
                        lengthOfStayInRoom.get(client.getClientRoom()) + calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            }

        incomePerRoom.replaceAll((room, value) -> lengthOfStayInRoom.get(room) * room.getCost());
    }
}

