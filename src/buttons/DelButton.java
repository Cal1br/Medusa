package buttons;

import listeners.TableListener;
import tabs.CRUDPanel;
import utils.DBTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class DelButton extends JButton {
    private CRUDPanel origin = null;
    private long selectedId = -1;

    public DelButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new DeleteAction());

    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            TableListener listener = (TableListener) origin.getTableListener();
            final List<Long> idList = origin.getIdList();
            selectedId = idList.get(listener.getSelected());
            DBTool.getInstance().deleteAt(selectedId,origin.getTableName(),origin.getIdColumn());
            origin.updateModel();
            //  final ActionListener[] actionListeners = this.getActionListeners();
            //        delBtn.removeActionListener();
        }
    }
}
