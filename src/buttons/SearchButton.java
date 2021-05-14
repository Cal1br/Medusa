package buttons;

import models.Column;
import tabs.CRUDPanel;
import utils.DBTool;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchButton extends JButton {
    private CRUDPanel origin = null;
    private JPanel searchPanel = null;
    private JComboBox<String> searchBar = null;
    private JTextField searchText = null;

    public SearchButton(final String str, final CRUDPanel crudPanel) {
        super(str);
        this.origin = crudPanel;
        this.addActionListener(new SearchAction());
        this.searchBar = origin.getSearchBar();
        this.searchText = origin.getSearchText();

    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (!searchPanel.isVisible()) {
                searchPanel.setVisible(true);
                for (Column column : origin.getColumns()) {
                    searchBar.addItem(column.getField());
                }
            } else {
                final String text = searchText.getText();
                final String selectedColumn = searchBar.getSelectedItem().toString();
                JTable table = origin.getTable();
                table.setModel(DBTool.getInstance().getModelWhere(origin.getTableName(), selectedColumn, text));
            }
        }
    }
}
