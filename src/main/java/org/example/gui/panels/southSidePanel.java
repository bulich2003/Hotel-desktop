package org.example.gui.panels;

import javax.swing.*;

import java.awt.*;

import static org.example.gui.frames.InnerFrame.updateInnerFrame;

/**
 * creates the bottom panel of the inner window,
 * which contains elements for filtering information in the table
 */
public class southSidePanel extends JPanel {
    private static String filterContent = "";

    /**
     * south side panel constructor
     *
     * @param tableType  contains the type of output information: clients, workers, rooms
     */
    public southSidePanel(String tableType) {

        JPanel searchPanel = new JPanel();

        JTextField searchField = new JTextField(filterContent, 15);
        JButton search = new JButton("Search");

        searchPanel.add(searchField);
        searchPanel.add(search);

        add(searchPanel, BorderLayout.WEST);

        search.addActionListener(e -> {
            filterContent = searchField.getText();
            updateInnerFrame(tableType);
        });
    }

    /**
     * used to get the filter content in external classes
     *
     * @return filter content
     * @see #filterContent
     */
    public static String getFilterContent() { return filterContent; }
}
