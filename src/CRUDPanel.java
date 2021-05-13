import models.Column;
import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;
import models.Pair;
import utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//todo update combo box on tab click
public class CRUDPanel extends JPanel {
    private String tableName = "";
    private String idColumn = "";
    private List<String> foreignIdColumns = new LinkedList<>();
    //TODO трябва да намеря начин да заредя idtata тука от таблицата
    private List<Long> idList = new LinkedList<>();
    private List<String> columnNames = new LinkedList<>();
    private List<Column> columns = new LinkedList<>();
    private List<Pair> pairs = new LinkedList<>();
    private JTable table = new JTable();
    private JScrollPane scrollPlane = new JScrollPane(table);
    private JButton addBtn = new JButton("Добавяне");
    private JButton delBtn = new JButton("Изтриване");
    private JButton editBtn = new JButton("Редактиране");
    private JButton searchBtn = new JButton("Търси");
    private JComboBox<String> searchBar = new MemoryComboBox<>();

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
        for (String name : foreignIdColumns) {
            this.add(new ForeignKeyComboPair(name));
        }
        //---Button panel
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.add(addBtn);
        buttonHolder.add(delBtn);        /*You should (always) be using setValue for JFormattedTextField and you should true setValue(null) to clear the field*/
        buttonHolder.add(editBtn);
        buttonHolder.add(searchBar);
        buttonHolder.add(searchBtn);
        //searchBar.setMaximumSize(new Dimension(150,searchBtn.getHeight()));
        searchBar.setMaximumSize(new Dimension(150,26));
        this.add(buttonHolder);
        addBtn.addActionListener(new AddAction());
        delBtn.addActionListener(new DeleteAction());
        editBtn.addActionListener(new EditAction());
        searchBtn.addActionListener(new SearchAction());
        //---Down Panel

        scrollPlane.setPreferredSize(new Dimension(450, 150));
        System.out.println(idColumn + " " + tableName);
        System.out.println(foreignIdColumns.toString() + " " + tableName);
        //пълним table първоначално
        table.setModel(DBTool.getInstance().getModelForColumns(columnNames, tableName));
        this.add(scrollPlane);
        this.setVisible(true);
    }

    //might add field and data value in hashmap for a switch statement - DONE
    //TODO LOAD ENUMS FROM CONFIG FILE?? FUCKING GENIUS omfg yesss


    //Gets and filters all column names to normal fields and key fields
    private void filterColumnNamesAndDataTypes() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж

        final HashMap<String, Boolean> keys = DBTool.getInstance().getTableKeys(tableName);
        final List<Column> columnList = DBTool.getInstance().getColumnNamesAndType(tableName);
        for (final Column nextColumn : columnList) {
            final String columnName = nextColumn.getField();
            if (keys.containsKey(columnName)) {
                if (keys.get(columnName)) {
                    idColumn = columnName;
                } else {
                    foreignIdColumns.add(columnName);
                }
            } else {
                columns.add(nextColumn);
            }
        }
    }

    //DONE I HAVE TO GET DATA TYPES BEFORE GETTING ANY FURTHER
    @Deprecated
    private void filterColumnNames() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж
        final List<String> resultSet = DBTool.getInstance().getColumnNames(tableName);
        for (String string : resultSet) {
            if (string.contains("ID")) { //should i check primary key with hamming distance? YES
                if (Tools.HammingDistance(string, tableName + "_ID") < 4) {
                    //boom primary key found
                    //реално тука мога да заредя primary key-vete обаче ако са 20000+++, да няма да седяд в ram-ta
                    idColumn = string;
                } else {
                    List<String> tableNames = DBTool.getInstance().getTableNames();
                    for (String name : tableNames) {
                        if (Tools.HammingDistance(name + "_ID", string) < 4) {
                            foreignIdColumns.add(string);
                            //ако не е primary проверяваме дали не е foreign отново с hamming distance и като сравняваме с имената на останалите таблици
                        }
                    }
                }
                //todo check foreign keys
            } else if (string.equalsIgnoreCase("id")) {
                idColumn = string;
            } else {
                columnNames.add(string);
            }

        }
    }

    private class AddAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }

    private class EditAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }

    private class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }
}
