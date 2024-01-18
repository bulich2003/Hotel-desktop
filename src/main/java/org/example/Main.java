package org.example;
import org.example.connector.DataBaseConnection;
import org.example.gui.frames.MainFrame;

public class Main {
    public static void main(String[] args) {
        init();
    }

    private static void init() {
        new MainFrame();
        new DataBaseConnection();
    }
}

