package project3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class OverdueDateDialog extends JDialog implements ActionListener {

    private JTextField txtDate;
    private JButton okButton;
    private JButton cancelButton;
    private int closeStatus;
    private GregorianCalendar returnDate;
    public static final int OK = 0;
    public static final int CANCEL = 1;


    public OverdueDateDialog(JFrame parent) {
        super (parent, true);

        setTitle("Enter a Date");
        closeStatus = CANCEL;
        setSize(300, 100);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        txtDate = new JTextField("MM/DD/YYY", 10);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new GridLayout(2,1));

        textPanel.add(new JLabel("Enter Date (e.g. 01/20/2020"));
        textPanel.add(txtDate);

        getContentPane().add(textPanel, BorderLayout.CENTER);
        // Instantiate and display two buttons
        okButton = new JButton("OK");
        cancelButton = new JButton("Cancel");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        okButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setVisible (true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton) e.getSource();
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");

        if (button == okButton) {
            closeStatus = OK;

            Date d1 = null;

            try {
                this.returnDate = new GregorianCalendar();
                d1 = df.parse(txtDate.getText());
                this.returnDate.setTime(d1);
            } catch (ParseException e1) {
                //Do something here
            }
        }
        dispose();
    }

    public GregorianCalendar getReturnDate() {
        return this.returnDate;
    }

    public int getCloseStatus() {
        return closeStatus;
    }
}
