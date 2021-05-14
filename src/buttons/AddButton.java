package buttons;

import models.Pair;
import tabs.CRUDPanel;
import utils.DBTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AddButton extends JButton {
    private CRUDPanel origin = null;

    public AddButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new AddAction());

    }

    private class AddAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            StringBuilder columnsSb = new StringBuilder("INSERT INTO " + origin.getTableName() + "(");
            StringBuilder valuesSb = new StringBuilder(" VALUES (");

            for (Pair pair : origin.getPairs()) {
                columnsSb.append(pair.getColumn().getField()).append(", ");
                valuesSb.append(pair.getFormattedTextFieldText()).append(", ");
            }
            columnsSb.replace(columnsSb.length() - 2, columnsSb.length(), ")");
            valuesSb.replace(valuesSb.length() - 2, valuesSb.length(), ")");
            columnsSb.append(valuesSb);
            try {
                DBTool.getInstance().executeSql(columnsSb.toString());
                origin.updateModel();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                //todo add error message
            }

        }
    }
}
