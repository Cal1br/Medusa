import utils.DBTool;

import javax.swing.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TabFrame extends JFrame {
    DBTool dbTool = null;
    JTabbedPane tabbedPane = new JTabbedPane();
    List<JPanel> panels = new LinkedList<>();

    public TabFrame(DBTool dbTool) {
        this.dbTool = dbTool;
        this.setSize(750, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        FillPanels(dbTool.getTableNames());
        this.add(tabbedPane);
        this.setVisible(true);
        tabbedPane.setVisible(true);
    }
    private void FillPanels(List<String> tableNames){
        for (final String tableName : tableNames) {
            JPanel panel = new CRUDPanel(tableName);
            panels.add(panel);
            tabbedPane.add(panel, (String)tableName);
        }
    }
}
