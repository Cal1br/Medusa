package buttons;

import listeners.TableListener;
import tabs.CRUDPanel;
import utils.DBTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class EditButton extends JButton {
    private CRUDPanel origin = null;
    private long selectedId = -1;

    public EditButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new EditAction());
    }

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            TableListener listener = (TableListener) origin.getTableListener();
            final List<Long> idList = origin.getIdList();
            selectedId = idList.get(listener.getSelected());
            try {
                DBTool.getInstance().updateAt(selectedId,origin);
                origin.updateModel();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
        }
    }
}
