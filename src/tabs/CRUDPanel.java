package tabs;

import listeners.SearchButtonListener;
import models.Column;
import models.Pair;
import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;
import listeners.TableListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

//todo update combo box on tab click
//todo UUID
public class CRUDPanel extends JPanel {
    private final List<String> foreignIdColumns = new LinkedList<>();
    //TODO трябва да намеря начин да заредя idtata тука от таблицата
    private final List<Long> idList = new LinkedList<>();
    private final List<String> columnNames = new LinkedList<>();
    private final List<Column> columns = new LinkedList<>(); //only filtered columns
    private final List<Pair> pairs = new LinkedList<>();
    private final JTable table = new JTable();
    private final MouseListener tableListener = new TableListener(this);
    private MouseListener searchButtonListener = null;
    private final JScrollPane scrollPlane = new JScrollPane(table);
    private final JButton addBtn = new JButton("Добавяне");
    private final JButton delBtn = new JButton("Изтриване");
    private final JButton editBtn = new JButton("Редактиране");
    private final JButton searchBtn = new JButton("Търси");
    private final JComboBox<String> searchBar = new MemoryComboBox<>();
    private final JTextField searchText = new JTextField();
    private String tableName = "";
    private String idColumn = "";
    private final JPanel searchPanel = new JPanel();
    //todo this is for a quick test
    JPanel buttonHolder = null;
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
        buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.add(addBtn);
        buttonHolder.add(delBtn);        /*You should (always) be using setValue for JFormattedTextField and you should true setValue(null) to clear the field*/
        buttonHolder.add(editBtn);
        buttonHolder.add(searchBar);
        searchBar.setVisible(false);
        buttonHolder.add(searchBtn);
        searchBar.setMaximumSize(new Dimension(150, 26));
        this.add(buttonHolder);
        addBtn.addActionListener(new AddAction());
        delBtn.addActionListener(new DeleteAction());
        editBtn.addActionListener(new EditAction());
        searchBtn.addActionListener(new SearchAction());
        //---Search Panel
        searchPanel.setLayout(new BoxLayout(searchPanel,BoxLayout.X_AXIS));
        searchPanel.add(searchBar);
        searchBar.setMaximumSize(new Dimension(150,30));
        searchPanel.add(searchText);
        searchText.setMaximumSize(new Dimension(350,30));
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

    private void updateModel() {
        table.setModel(DBTool.getInstance().getModelForColumns(columnNames, tableName));
    }

    public List<Pair> getPairs() {
        return pairs;
    }
    //TODO ADD TOOLTIPS FOR BUTTONS!
    private class AddAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            StringBuilder columnsSb = new StringBuilder("INSERT INTO " + tableName + "(");
            StringBuilder valuesSb = new StringBuilder(" VALUES (");
            for (Pair pair : pairs) {
                columnsSb.append(pair.getColumn().getField()).append(", ");
                valuesSb.append(pair.getFormattedTextFieldText()).append(", ");
            }
            columnsSb.replace(columnsSb.length() - 2, columnsSb.length(), ")");
            valuesSb.replace(valuesSb.length() - 2, valuesSb.length(), ")");
            columnsSb.append(valuesSb);
            try {
                DBTool.getInstance().executeSql(columnsSb.toString());
                updateModel();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
                //todo add error message
            }

        }
    }

    private class DeleteAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
         //   buttonHolder.
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
            if(!searchPanel.isVisible()){
                searchPanel.setVisible(true);
                for(Column column : columns){
                    searchBar.addItem(column.getField());
                }
            }
            else {
                final String text = searchText.getText();
                final String selectedColumn = searchBar.getSelectedItem().toString();
                table.setModel(DBTool.getInstance().getModelWhere(tableName,selectedColumn,text));
            }
        }
    }

}
