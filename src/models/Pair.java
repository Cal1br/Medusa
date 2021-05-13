package models;

import utils.DataType;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

public class Pair extends JPanel {
    private Column column = null;
    private JLabel label = null;
    private JTextField textField = null;
    private boolean nullAllowed;

    public Pair(final Column column) {
        this.column = column;
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        final String columnName = column.getField();
        final DataType type = column.getType();
        label = new JLabel(columnName);
        this.add(label);
        JFormattedTextField formattedTextField = null;
        nullAllowed = column.isNullAllowed();
        boolean isInt = false;
        switch (type) {
            /*The JFormattedTextField knows what default formatter to use based on the value type passed in.
            If the value passed in through setValue() method is a Date time, it would use the default date formatter.
            (x) doubt

            The following code formats the text in it as a date using the current locale format:

            JFormattedTextField  dobField = new JFormattedTextField();
            dobField.setValue(new Date());

             */

            //todo check if negatives are allowed?
            case INTEGER:
                isInt = true;
            case BIGINT:
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
                NumberFormatter numberFormatter = new NumberFormatter(nf);
                if (isInt) numberFormatter.setMaximum(Integer.MAX_VALUE);
                else numberFormatter.setMaximum(Long.MAX_VALUE);
                formattedTextField = new JFormattedTextField(numberFormatter);
                textField = formattedTextField;
                //formattedTextField.
                break;
            case DECIMAL:
                //todo понеже decimal се задава като (общо,числа след знака) а h2 го връща само общо, трябва да има конфиг оф който да се взимат числата след запетаята, и да има default
                //MaskFormatter nf = new DecimalFormat(NumberFormat.getInstance());
                NumberFormat numFormat = new DecimalFormat("#0,000.00");
                numFormat.setMaximumFractionDigits(2);
                numFormat.setMaximumIntegerDigits(column.getTypeLen() - 2);
                NumberFormatter numFormatter = new NumberFormatter(numFormat);
                textField = new JFormattedTextField(numFormatter);
                break;
            case DATE:
                formattedTextField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
                textField = formattedTextField;
                break;
            case TIMESTAMP:
                //todo no text field, just showing a time stamp
            default:
                textField = new JTextField();
        }
        this.add(textField);
    }
//todo ENUM ili pf nz nqkaksi da vidim date li e tova int li e long li e nz nz. Switch za vseki h2 data type??

    public JLabel getLabel() {
        return label;
    }

    public JTextField getTextField() {
        return textField;
    }
    public String getFormattedTextFieldText(){
        String st = textField.getText();
        switch (column.getType()){
            case INTEGER:
            case BIGINT:
                return st.replaceAll(",","");
            default:
                st = "\'"+st+"\'";
        }
        return st;
    }
    public Column getColumn() {
        return column;
    }

    public boolean isNullAllowed() {
        return nullAllowed;
    }

}
