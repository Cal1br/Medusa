package tabs;

import buttons.AddButton;
import buttons.DelButton;
import buttons.EditButton;
import buttons.SearchButton;
import listeners.SearchButtonListener;
import listeners.TableListener;
import models.Column;
import models.Key;
import models.KeyColumn;
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
    private final List<KeyColumn> foreignKeyColumns = new LinkedList<>();
    private final List<Column> columns = new LinkedList<>(); //only filtered columns
    private final List<Pair> pairs = new LinkedList<>();
    private final List<ForeignKeyComboPair> foreignPairs = new LinkedList<>(); // TODO major refactor така че FOREIGNKEYCOMBOPAIR да extendva ot pair
    private List<Key> keysList = null;
    private final JTable table = new JTable();
    private final MouseListener tableListener = new TableListener(this);
    private final JScrollPane scrollPlane = new JScrollPane(table);
    private final JComboBox<String> searchBar = new MemoryComboBox<>();
    private final JTextField searchText = new JTextField();
    private final JPanel searchPanel = new JPanel();
    //TODO трябва да намеря начин да заредя idtata тука от таблицата
    private List<Long> idList = null;
    //todo this is for a quick test
    private JPanel buttonHolder = null;
    private JButton editBtn = null;
    private JButton searchBtn = new JButton("Търси");
    private MouseListener searchButtonListener = null;
    private JButton addBtn = null;
    private JButton delBtn = null;
    private String tableName = "";
    private String idColumn;

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
        for (KeyColumn column : foreignKeyColumns) {
            final ForeignKeyComboPair keyComboPair = new ForeignKeyComboPair(column, column.getKey());
            this.add(keyComboPair);
            foreignPairs.add(keyComboPair);
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
        System.out.println(idColumn + " " + tableName);
        System.out.println(foreignKeyColumns.toString() + " " + tableName);
        this.add(scrollPlane);
        this.setVisible(true);
        updateModel();//пълним table първоначално
    }


    public List<ForeignKeyComboPair> getForeignPairs() {
        return foreignPairs;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> list) {
        this.idList = list;
        System.out.println(idList.toString() + " " + tableName);
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

    public JComboBox<String> getSearchBar() {
        return searchBar;
    }

    public JTextField getSearchText() {
        return searchText;
    }

    public String getIdColumn() {
        return idColumn;
    }

    /**
     * Gets and filters all column names to normal fields and key fields
     */
    private void filterColumnNamesAndDataTypes() {

        //final HashMap<String, Boolean> keys = DBTool.getInstance().getTableKeys(tableName);
        keysList = DBTool.getInstance().getTableKeys(tableName);
        HashMap<String,Key> map = new HashMap<>(); //hashmap is always the answer
        for(Key key : keysList){
            map.put(key.getColumnName(),key);
        }
        final List<Column> columnList = DBTool.getInstance().getColumnNamesAndType(tableName);
        for (final Column nextColumn : columnList) {
            final String columnName = nextColumn.getField();
            if (map.containsKey(columnName)) {
                keysList.add(map.get(columnName));
                if (map.get(columnName).isPrimaryKey()) {
                    idColumn = columnName;
                } else {
                    KeyColumn keyColumn = new KeyColumn(nextColumn,map.get(columnName));
                    foreignKeyColumns.add(keyColumn);
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
/*        List<Column> columnList = new LinkedList<>(columns);
        columnList.addAll(foreignIdColumns);*/
        //таблицата не показва външните ключове, те ще се правят с join заявка
        table.setModel(DBTool.getInstance().getModelForColumns(this));
    }

    public List<Pair> getPairs() {
        return pairs;
    }
    //TODO да се проверяват дали нещата не са null или трябва или не трябва да са null
    //TODO ADD TOOLTIPS FOR BUTTONS!
    //TODO combo box-a който е с foreign key-ovete трябва да се направи така че да чете от config кой файл да избира...
    //TODO aко няма config, да се създава
    //TODO LOAD ENUMS FROM CONFIG FILE?? FUCKING GENIUS omfg yesss

}
