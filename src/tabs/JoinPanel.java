package tabs;

import buttons.JoinSearchButton;
import models.Column;
import utils.DBTool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.List;

public class JoinPanel extends JPanel {
    private final JoinSearchButton joinSearchButton = new JoinSearchButton("Search", this); // todo sus
    private final JTable table = new JTable();
    private final JScrollPane scrollPlane = new JScrollPane(table);
    private final ButtonGroup buttonGroup = new ButtonGroup();
    private JComboBox<String> leftTable = new JComboBox<>();
    private JComboBox<String> rightTable = new JComboBox<>();
    private SearchPanel leftSearchPanel = new SearchPanel(leftTable);
    private SearchPanel rightSearchPanel = new SearchPanel(rightTable);

    public JoinPanel() {
        //Select * from states s join water_body w on s.state_id=w.state_id where water_area > 5
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        leftTable.addItemListener(new SelectionListener(leftSearchPanel));
        rightTable.addItemListener(new SelectionListener(rightSearchPanel));
        JPanel topPanel = new JPanel();
        JPanel midPanel = new JPanel();
        JPanel searchPanelsHolder = new JPanel();
        JPanel left = new JPanel();
        JPanel middle = new JPanel();
        JPanel right = new JPanel();
        topPanel.setLayout(new GridLayout(1, 3));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        middle.setLayout(new GridLayout(2, 2));
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        searchPanelsHolder.setLayout(new BoxLayout(searchPanelsHolder, BoxLayout.X_AXIS));
        JRadioButton innerJoin = new JRadioButton("Inner Join");
        JRadioButton leftJoin = new JRadioButton("Left Join");
        JRadioButton rightJoin = new JRadioButton("Right Join");
        JRadioButton fullJoin = new JRadioButton("Full Join");
        left.add(leftTable);
        middle.add(leftJoin);
        middle.add(rightJoin);
        middle.add(innerJoin);
        middle.add(fullJoin);
        right.add(rightTable);
        buttonGroup.add(innerJoin);
        buttonGroup.add(leftJoin);
        buttonGroup.add(rightJoin);
        buttonGroup.add(fullJoin);
        searchPanelsHolder.add(leftSearchPanel);
        searchPanelsHolder.add(rightSearchPanel);

        topPanel.add(left);
        topPanel.add(middle);
        topPanel.add(right);
        topPanel.setMaximumSize(new Dimension(600, 35));
        midPanel.add(joinSearchButton);
        joinSearchButton.addActionListener(new SearchAction(this));
        this.add(topPanel);
        this.add(midPanel);
        this.add(searchPanelsHolder);
        this.add(scrollPlane);
        updateTableBoxes();
    }

    public JTable getTable() {
        return table;
    }

    public JScrollPane getScrollPlane() {
        return scrollPlane;
    }

    public ButtonGroup getButtonGroup() {
        return buttonGroup;
    }

    public JComboBox<String> getLeftTable() {
        return leftTable;
    }

    public JComboBox<String> getRightTable() {
        return rightTable;
    }

    public SearchPanel getLeftSearchPanel() {
        return leftSearchPanel;
    }

    public SearchPanel getRightSearchPanel() {
        return rightSearchPanel;
    }

    public void updateTableBoxes() {
        final List<String> names = DBTool.getInstance().getTableNames();
        for (String name : names) {
            leftTable.addItem(name);
            rightTable.addItem(name);
        }

    }

    public void updateModel() {
/*        List<Column> columnList = new LinkedList<>(columns);
        columnList.addAll(foreignIdColumns);*/
        //таблицата не показва външните ключове, те ще се правят с join заявка
        // table.setModel(DBTool.getInstance().getModelForColumns(this));
    }

    private class SearchPanel extends JPanel {
        boolean emptySelection = true;
        JPanel radioHolder = new JPanel();
        private JComboBox<String> columnComboBox = new JComboBox();
        private List<Column> columnList;
        private JTextField searchField = new JTextField();
        private ButtonGroup radioButtons = new ButtonGroup();
        private JComboBox<String> table;


        SearchPanel(JComboBox<String> table) {
            this.table = table;
            searchField.setPreferredSize(new Dimension(150, 30));
            columnComboBox.setPreferredSize(new Dimension(150, 30));
            columnComboBox.addItemListener(new selectionListener());
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.RED));
            JPanel queryHolder = new JPanel();
            radioHolder.setLayout(new BoxLayout(radioHolder, BoxLayout.Y_AXIS));
            queryHolder.setLayout(new GridLayout(1, 2));
            radioHolder.setMaximumSize(new Dimension(20, 35));
            queryHolder.setMaximumSize(new Dimension(300, 35));
            queryHolder.setMinimumSize(new Dimension(300, 35));
            this.setLayout(new GridBagLayout());
            //queryHolder.setLayout(new GridLayout(1,2));
            JRadioButton more = new JRadioButton(">");
            JRadioButton equal = new JRadioButton("=");
            JRadioButton less = new JRadioButton("<");
            radioButtons.add(more);
            radioButtons.add(equal);
            radioButtons.add(less);
            radioHolder.add(more);
            radioHolder.add(equal);
            radioHolder.add(less);
            queryHolder.add(columnComboBox);
            queryHolder.add(searchField);
            this.add(queryHolder);
            this.add(radioHolder);
        }

        JComboBox<String> getColumnComboBox() {
            return columnComboBox;
        }

        JTextField getSearchField() {
            return searchField;
        }

        ButtonGroup getRadioButtons() {
            return radioButtons;
        }

        public void updateComboBox() {
            columnComboBox.removeAllItems();
            String selectedTable = table.getSelectedItem().toString();
            columnList = DBTool.getInstance().getColumnNamesAndTypesWithoutKeys(selectedTable);
            columnComboBox.addItem("");
            for (Column column : columnList) {
                columnComboBox.addItem(column.getField());
            }
        }

        private class selectionListener implements java.awt.event.ItemListener {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (columnComboBox.getSelectedItem() != null && !columnComboBox.getSelectedItem().toString().equals("")) {
                    emptySelection = false;
                    Column selectedColumn = columnList.get(columnComboBox.getSelectedIndex() - 1);
                    switch (selectedColumn.getType()) {
                        case DECIMAL:
                        case DOUBLE:
                        case BIGINT:
                        case INTEGER:
                        case TINYINT:
                            radioHolder.setVisible(true);
                            emptySelection = false;
                            break;
                        default:
                            emptySelection = true;
                            radioHolder.setVisible(false);
                    }

                } else {
                    radioHolder.setVisible(false);
                    emptySelection = true;
                }
            }
        }
    }

    private class SelectionListener implements java.awt.event.ItemListener {
        private SearchPanel searchPanel;

        public SelectionListener(SearchPanel searchPanel) {
            this.searchPanel = searchPanel;
        }

        @Override
        public void itemStateChanged(final ItemEvent e) {
            this.searchPanel.updateComboBox();
        }
    }

    private class SearchAction implements ActionListener {
        JoinPanel origin;

        SearchAction(JoinPanel origin) {
            this.origin = origin;
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            ButtonModel buttonModel = origin.getButtonGroup().getSelection();
            origin.getButtonGroup().isSelected(buttonModel);
            buttonModel.
            if(origin.getButtonGroup().getSelection())
            System.out.println(origin.getButtonGroup().getSelection());
        }
    }
}
