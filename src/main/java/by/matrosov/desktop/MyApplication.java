package by.matrosov.desktop;

import by.matrosov.appl.HltvParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyApplication{

    private JFrame frame;

    //--------------------------
    private JPanel checkboxPanel;
    private JPanel buttonPanel;

    //-----------------------
    private JButton marButton;
    private JButton egbButton;
    private JButton thirdButton;
    private JButton fourthButton;
    private JButton fifthButton;
    private JButton sixthButton;

    //----------------------
    private JCheckBox egb;
    private JCheckBox lootbet;
    private JCheckBox ggbet;
    private JCheckBox thunderpick;
    private JCheckBox csgopositive;
    private JCheckBox bet365;
    private JCheckBox bet188;
    private JCheckBox onexbet;
    private JCheckBox esporbet;
    private JCheckBox pinnacle;
    private JCheckBox xbet;

    public MyApplication() {
        initGUI();
    }

    private void initGUI(){
        frame = new JFrame("TITLE HERE");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        checkboxPanel = new JPanel(new GridLayout(4, 2));
        Border border = BorderFactory.createTitledBorder("Choose calculated odds");
        checkboxPanel.setBorder(border);

        marButton = new JButton("Calculate mar bets");

        egbButton = new JButton("Calculate egb bets");

        thirdButton = new JButton("Third button");
        thirdButton.addActionListener(e -> Toolkit.getDefaultToolkit().beep());
        fourthButton = new JButton("Fourth button");
        fourthButton.addActionListener(e -> Toolkit.getDefaultToolkit().beep());
        fifthButton = new JButton("Fifth button");
        fifthButton.addActionListener(e -> Toolkit.getDefaultToolkit().beep());
        sixthButton = new JButton("Sixth button");
        sixthButton.addActionListener(e -> Toolkit.getDefaultToolkit().beep());


        buttonPanel = new JPanel(new GridLayout(4,2));
        buttonPanel.add(marButton);
        buttonPanel.add(egbButton);
        buttonPanel.add(thirdButton);
        buttonPanel.add(fourthButton);
        buttonPanel.add(fifthButton);
        buttonPanel.add(sixthButton);


        //----------cho cho cho-----------------------------
        egb = new JCheckBox("egb");
        egb.setSelected(true);

        lootbet = new JCheckBox("lootbet");
        lootbet.setSelected(true);

        ggbet = new JCheckBox("ggbet");
        ggbet.setSelected(true);

        thunderpick = new JCheckBox("thunderpick");
        thunderpick.setSelected(false);

        csgopositive = new JCheckBox("csgopositive");
        csgopositive.setSelected(false);

        bet365 = new JCheckBox("bet365");
        bet365.setSelected(true);

        bet188 = new JCheckBox("bet188");
        bet188.setSelected(true);

        onexbet = new JCheckBox("onexbet");
        onexbet.setSelected(true);

        esporbet = new JCheckBox("esporbet");
        esporbet.setSelected(true);

        pinnacle = new JCheckBox("pinnacle");
        pinnacle.setSelected(true);

        xbet = new JCheckBox("xbet");
        xbet.setSelected(true);

        checkboxPanel.add(egb);
        checkboxPanel.add(lootbet);
        checkboxPanel.add(ggbet);
        checkboxPanel.add(thunderpick);
        checkboxPanel.add(csgopositive);
        checkboxPanel.add(bet365);
        checkboxPanel.add(bet188);
        checkboxPanel.add(onexbet);
        checkboxPanel.add(esporbet);
        checkboxPanel.add(pinnacle);
        checkboxPanel.add(xbet);
        //-----------------------------------------------

        Container container = frame.getContentPane();
        container.add(checkboxPanel, BorderLayout.NORTH);
        container.add(buttonPanel);


        frame.setSize(380, 270);
        //frame.setMaximumSize(new Dimension(300, 480));
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApplication::new);
    }
}
