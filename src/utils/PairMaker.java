package utils;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;

public class PairMaker extends JPanel {
    private JLabel label = null;
    private JTextField textField = null;

    @Deprecated
    public PairMaker(String columnName) {
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        label = new JLabel(columnName);
        this.add(label);
        switch (columnName) {
            default:
                textField = new JTextField();
                this.add(textField);
        }
    }

    public PairMaker(final String columnName, final DataType type) {
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        label = new JLabel(columnName);
        this.add(label);
        JFormattedTextField formattedTextField = null;
        switch (type) {

            case INTEGER:
                formattedTextField = new JFormattedTextField();

                textField = formattedTextField;
                //formattedTextField.
                break;
            case DATE:
                formattedTextField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
                textField = formattedTextField;
                break;
            case TIMESTAMP:
                //todo no text field, just showing a time stamp
            default:
                textField = new JTextField();
                this.add(textField);
        }
    }

    //todo ENUM ili pf nz nqkaksi da vidim date li e tova int li e long li e nz nz. Switch za vseki h2 data type??
}
