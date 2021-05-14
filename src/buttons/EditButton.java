package buttons;

import tabs.CRUDPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EditButton extends JButton {
    private CRUDPanel origin = null;

    public EditButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new EditAction());
    }

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            //editBtn
        }
    }
}
