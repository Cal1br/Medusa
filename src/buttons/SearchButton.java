package buttons;

import models.Column;
import tabs.CRUDPanel;
import utils.DBTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButton extends JButton {
    private CRUDPanel origin;
    private JPanel searchPanel;
    private JComboBox<String> searchBar;
    private JTextField searchText;

    public SearchButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new SearchAction());
        this.searchBar = origin.getSearchBar();
        this.searchText = origin.getSearchText();
        this.searchPanel = origin.getSearchPanel();

    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            //Populatevame Search bar-a
            if (!searchPanel.isVisible()) {
                searchPanel.setVisible(true);
                searchBar.removeAll();
                for (Column column : origin.getColumns()) {
                    searchBar.addItem(column.getField());
                }
            } else {
                final String text = searchText.getText();
                final String selectedColumn = searchBar.getSelectedItem().toString();
                JTable table = origin.getTable();
                table.setModel(DBTool.getInstance().getModelForColumnsWhere(origin.getTableName(), selectedColumn, text));
            }
        }
    }
}
