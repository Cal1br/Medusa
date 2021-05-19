import tabs.CRUDPanel;
import tabs.JoinPanel;
import utils.DBTool;
import utils.ForeignKeyComboPair;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.LinkedList;
import java.util.List;

public class TabFrame extends JFrame {
    DBTool dbTool;
    JTabbedPane tabbedPane = new JTabbedPane();
    List<JPanel> panels = new LinkedList<>();

    public TabFrame(DBTool dbTool) {
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(final ChangeEvent e) {
                if(tabbedPane.getSelectedComponent().getClass()==CRUDPanel.class){
                    CRUDPanel focusedPanel = (CRUDPanel) tabbedPane.getSelectedComponent();
                    focusedPanel.updateModel();
                    final List<ForeignKeyComboPair> foreignPairs = focusedPanel.getForeignPairs();
                    for(ForeignKeyComboPair foreign : foreignPairs){
                        foreign.updateComboBox();
                    }
                }
            }
        });
        this.setTitle("Medusa");
        ImageIcon icon = new ImageIcon("src/medusa.png");
        this.setIconImage(icon.getImage());
        this.dbTool = dbTool;
        this.setSize(750, 600);
        this.setResizable(false);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FillPanels(dbTool.getTableNames());
        this.add(tabbedPane);
        this.setVisible(true);
        tabbedPane.setVisible(true);
    }

    private void FillPanels(List<String> tableNames) {
        for (final String tableName : tableNames) {
            JPanel panel = new CRUDPanel(tableName);
            panels.add(panel);
            tabbedPane.add(panel, tableName);
        }
        tabbedPane.add(new JoinPanel(),"Join заявки");
    }
}
