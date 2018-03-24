package by.matrosov.desktop;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class MyApplication{

    private JFrame frame;
    private JPanel panel;

    //-----------------------
    private JButton marButton;
    private JButton egbButton;
    //-----------------------

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
    //-----------------------

    public MyApplication() {
        initGUI();
    }

    private void initGUI(){
        frame = new JFrame("TITLE HERE");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        panel = new JPanel(new GridLayout(4, 2));
        Border border = BorderFactory.createTitledBorder("Choose calculated odds");
        panel.setBorder(border);

        marButton = new JButton("Calculate mar bets");
        egbButton = new JButton("Calculate egb bets");
        //panel.add(marButton, BorderLayout.PAGE_END);
        //panel.add(egbButton, BorderLayout.AFTER_LINE_ENDS);


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

        panel.add(egb);
        panel.add(lootbet);
        panel.add(ggbet);
        panel.add(thunderpick);
        panel.add(csgopositive);
        panel.add(bet365);
        panel.add(bet188);
        panel.add(onexbet);
        panel.add(esporbet);
        panel.add(pinnacle);
        panel.add(xbet);
        //-----------------------------------------------

        Container container = frame.getContentPane();
        container.add(panel, BorderLayout.NORTH);

        container.add(marButton);
        container.add(egbButton);

        frame.setSize(300, 480);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyApplication::new);
    }
}
