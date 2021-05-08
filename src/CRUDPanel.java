import models.Column;
import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;
import utils.PairMaker;
import utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.List;

//todo update combo box on tab click
public class CRUDPanel extends JPanel {
    private String tableName = "";
    private String idColumnName = "";
    private List<String> foreignIdColumnNames = new LinkedList<>();
    //TODO трябва да намеря начин да заредя idtata тука от таблицата
    private List<Long> idList = new LinkedList<>();
    private List<String> columnNames = new LinkedList<>();
    private List<Column> columns = new LinkedList<>();
    private JTable table = new JTable();
    private JScrollPane scrollPlane = new JScrollPane(table);
    private JComboBox<String> searchCombo = new MemoryComboBox<>();
    private JButton addBtn = new JButton("Добавяне");
    private JButton delBtn = new JButton("Изтриване");
    private JButton editBtn = new JButton("Редактиране");
    private JButton searchBtn = new JButton("Търси");
    private JComboBox<String> searchBar = new MemoryComboBox<>();

    public CRUDPanel(String tableName) {
        this.tableName = tableName;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       // filterColumnNames();
        filterColumnNamesAndDataTypes();
        System.out.println(columns);
        for (Column column : columns) {
            this.add(new PairMaker(column.getField(),column.getType()));
        }
        for (String name : foreignIdColumnNames) {
            this.add(new ForeignKeyComboPair(name));
        }
        //---Button panel
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.add(addBtn);
        buttonHolder.add(delBtn);
        buttonHolder.add(editBtn);
        buttonHolder.add(searchBtn);
        this.add(buttonHolder);
        addBtn.addActionListener(new AddAction());
        //---Down Panel

        scrollPlane.setPreferredSize(new Dimension(450, 150));
        System.out.println(idColumnName + " " + tableName);
        System.out.println(foreignIdColumnNames.toString() + " " + tableName);
        //пълним table първоначално
        table.setModel(DBTool.getInstance().getModelForColumns(columnNames, tableName));
        this.add(scrollPlane);
        this.setVisible(true);
    }

    //Gets and filters all column names to normal fields and key fields
    //might add field and data value in hashmap for a switch statement
    //TODO LOAD ENUMS FROM CONFIG FILE?? FUCKING GENIUS omfg yesss

    @Deprecated
    private void filterColumnNames() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж
        final List<String> resultSet = DBTool.getInstance().getColumnNames(tableName);
        for (String string : resultSet) {
            if (string.contains("ID")) { //should i check primary key with hamming distance? YES
                if (Tools.HammingDistance(string, tableName + "_ID") < 4) {
                    //boom primary key found
                    //реално тука мога да заредя primary key-vete обаче ако са 20000+++, да няма да седяд в ram-ta
                    idColumnName = string;
                } else {
                    List<String> tableNames = DBTool.getInstance().getTableNames();
                    for (String name : tableNames) {
                        if (Tools.HammingDistance(name + "_ID", string) < 4) {
                            foreignIdColumnNames.add(string);
                            //ако не е primary проверяваме дали не е foreign отново с hamming distance и като сравняваме с имената на останалите таблици
                        }
                    }
                }
                //todo check foreign keys
            } else if (string.equalsIgnoreCase("id")) {
                idColumnName = string;
            } else {
                columnNames.add(string);
            }

        }
    }

    private void filterColumnNamesAndDataTypes() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж

        //TODO COMPLETE REWRITE С
        /*select CONSTRAINT_TYPE, COLUMN_LIST from information_schema.constraints
          where table_name = 'SUPERVISOR'
          така имам всичките нужни данни да ги филтрирам, йес ве
        *  */

        final List<Column> resultSet = DBTool.getInstance().getColumnNamesAndType(tableName);
        for (Column column : resultSet) {
            String field = column.getField();
            if (field.contains("ID")) { //should i check primary key with hamming distance? YES
                if (Tools.HammingDistance(field, tableName + "_ID") < 4) {
                    //boom primary key found
                    //реално тука мога да заредя primary key-vete обаче ако са 20000+++, да няма да седят в ram-ta
                    idColumnName = field;
                } else {
                    //ако не е primary проверяваме дали не е foreign отново с hamming distance и като сравняваме с имената на останалите таблици
                    List<String> tableNames = DBTool.getInstance().getTableNames();
                    for (String name : tableNames) {
                        if (Tools.HammingDistance(name + "_ID", field) < 4) {
                            foreignIdColumnNames.add(field);
                            continue;
                        }
                    }
                }
                //todo check foreign keys
            } else if (field.equalsIgnoreCase("id")) {
                idColumnName = field;
            } else {
                columns.add(column);
            }

        }
    }

    //TODO I HAVE TO GET DATA TYPES BEFORE GETTING ANY FURTHER
    private class AddAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }

}
