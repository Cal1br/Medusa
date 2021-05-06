package utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
public class PairMaker extends JPanel {
    private JLabel label = null;
    private JTextField textField = null;

    public PairMaker(String columnName) {
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800,20));
        label = new JLabel(columnName);
        this.add(label);
        switch (columnName) {
            default:
                textField = new JTextField();
                this.add(textField);
        }
    }

    //todo ENUM ili pf nz nqkaksi da vidim date li e tova int li e long li e nz nz. Switch za vseki h2 data type??
}
