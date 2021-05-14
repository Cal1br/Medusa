package buttons;

import tabs.CRUDPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DelButton extends JButton {
    private CRUDPanel origin = null;

    public DelButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new DeleteAction());

    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            //  final ActionListener[] actionListeners = this.getActionListeners();
            //        delBtn.removeActionListener();
        }
    }
}
