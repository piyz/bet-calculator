package by.matrosov.desktop;

import javax.swing.*;

public class MyApplication {
    public static void main(String[] args) {
        JFrame frame = new JFrame("HelloWorldSwing");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Hello World");
        frame.add(label);

        frame.pack();
        frame.setVisible(true);
    }
}
