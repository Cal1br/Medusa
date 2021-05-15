package tabs;

import buttons.AddButton;
import buttons.DelButton;
import buttons.EditButton;
import buttons.SearchButton;
import listeners.SearchButtonListener;
import listeners.TableListener;
import models.Column;
import models.Pair;
import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//todo update combo box on tab click
//todo UUID
public class CRUDPanel extends JPanel {
    private final List<Column> foreignIdColumns = new LinkedList<>();
    //TODO трябва да намеря начин да заредя idtata тука от таблицата
    private final List<Long> idList = new LinkedList<>();
    private final List<Column> columns = new LinkedList<>(); //only filtered columns
    private final List<Pair> pairs = new LinkedList<>();
    private final JTable table = new JTable();
    private final MouseListener tableListener = new TableListener(this);
    private final JScrollPane scrollPlane = new JScrollPane(table);
    private final JComboBox<String> searchBar = new MemoryComboBox<>();
    private final JTextField searchText = new JTextField();
    private final JPanel searchPanel = new JPanel();
    //todo this is for a quick test
    private JPanel buttonHolder = null;
    private JButton editBtn = null;
    private JButton searchBtn = new JButton("Търси");
    private MouseListener searchButtonListener = null;
    private JButton addBtn = null;
    private JButton delBtn = null;
    private String tableName = "";
    private String idColumn = "";

    public CRUDPanel(String tableName) {
        //Label&TextField Panel
        this.tableName = tableName;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        filterColumnNamesAndDataTypes();
        for (Column column : columns) {
            Pair pair = new Pair(column);
            pairs.add(pair);
            this.add(pair);
        }
        for (Column column : foreignIdColumns) {
            this.add(new ForeignKeyComboPair(column.getField()));
        }


        //---Button panel
        buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        addBtn = new AddButton("Добавяне", this);
        delBtn = new DelButton("Изтриване", this);
        editBtn = new EditButton("Редактиране", this);
        searchBtn = new SearchButton("Търсене", this);
        buttonHolder.add(addBtn);
        buttonHolder.add(delBtn);        /*You should (always) be using setValue for JFormattedTextField and you should true setValue(null) to clear the field*/
        buttonHolder.add(editBtn);
        buttonHolder.add(searchBtn);
        this.add(buttonHolder);
        //---Search Panel
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
        searchPanel.add(searchBar);

        searchBar.setMaximumSize(new Dimension(150, 30));
        searchPanel.add(searchText);
        searchText.setMaximumSize(new Dimension(350, 30));
        this.add(searchPanel);
        searchBar.setVisible(true);
        searchText.setVisible(true);
        searchPanel.setVisible(false);
        //---Down Panel
        table.addMouseListener(tableListener);
        searchButtonListener = new SearchButtonListener(searchPanel);
        searchBtn.addMouseListener(searchButtonListener);
        scrollPlane.setPreferredSize(new Dimension(450, 150));
        System.out.println(idColumn + " " + tableName);
        System.out.println(foreignIdColumns.toString() + " " + tableName);
        updateModel();//пълним table първоначално
        this.add(scrollPlane);
        this.setVisible(true);
    }

    public MouseListener getTableListener() {
        return tableListener;
    }

    public String getTableName() {
        return tableName;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public JPanel getSearchPanel() {
        return searchPanel;
    }
//might add field and data value in hashmap for a switch statement - DONE
    //TODO LOAD ENUMS FROM CONFIG FILE?? FUCKING GENIUS omfg yesss

    public JComboBox<String> getSearchBar() {
        return searchBar;
    }

    public JTextField getSearchText() {
        return searchText;
    }

    /**
     * Gets and filters all column names to normal fields and key fields
     */
    private void filterColumnNamesAndDataTypes() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж

        final HashMap<String, Boolean> keys = DBTool.getInstance().getTableKeys(tableName);
        final List<Column> columnList = DBTool.getInstance().getColumnNamesAndType(tableName);
        for (final Column nextColumn : columnList) {
            final String columnName = nextColumn.getField();
            if (keys.containsKey(columnName)) {
                if (keys.get(columnName)) {
                    idColumn = columnName;
                } else {
                    foreignIdColumns.add(nextColumn);
                }
            } else {
                columns.add(nextColumn);
            }
        }
    }

    public JTable getTable() {
        return table;
    }

    public void updateModel() {
        table.setModel(DBTool.getInstance().getModelForColumns(columns, tableName));
    }

    public List<Pair> getPairs() {
        return pairs;
    }
    //TODO ADD TOOLTIPS FOR BUTTONS!


}
