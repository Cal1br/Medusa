package buttons;

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
            try {
                DBTool.getInstance().add(origin);
                origin.updateModel();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                //todo add error message
            }

        }
    }
}
