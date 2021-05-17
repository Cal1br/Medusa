package utils;

import tabs.CRUDPanel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ForeignKeyComboPair extends JPanel {
    private String foreignKey = "";
    private JLabel label = null;
    private JComboBox<String> comboBox = new JComboBox<>();
    private List<String> names = new LinkedList<>();
    private String foreignTableName = "";
    private CRUDPanel origin;
    //todo optimize this too, get a reference sql
    public ForeignKeyComboPair(CRUDPanel origin, String key) {
        this.origin = origin;
        foreignTableName = findTableName(foreignKey);
        names = findNames(foreignTableName);
        foreignKey = key;
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        label = new JLabel(key.replace("_ID", ""));
        this.add(label);
        this.add(comboBox);
    }

    public String getForeignTableName() {
        return foreignTableName;
    }

    public List<String> getNames() {
        return names;
    }

    //намира името на таблицата с което се идентифицира обекта, например име на човек
    //todo edit?
    //todo add Cyrillic
    private List<String> findNames(String tableName) {
        final List<String> columnNames = DBTool.getInstance().getColumnNames(tableName);
        String fNameColumn = "";
        int shortestHamming = Integer.MAX_VALUE;
        for (String name : columnNames) {
            if (!name.toLowerCase().contains("name")) {
                continue;
            }
            //todo redundant?
            int distance = Tools.HammingDistance("name", name.toLowerCase());
            if (distance < shortestHamming) {
                shortestHamming = distance;
                fNameColumn = name;
            }
        }
        names.add(fNameColumn);
        return names;
    }

    //намира как се казва таблицата от ключа
    private String findTableName(String key) {
        int shortestHamming = Integer.MAX_VALUE;
        List<String> list = DBTool.getInstance().getTableNames();
        for (String name : list) {
            int distance = Tools.HammingDistance(key, name.toLowerCase());
            if (distance < shortestHamming) {
                shortestHamming = distance;
                foreignTableName = name; //todo add more than 1 foreign key
            }
        }
        return foreignTableName;
    }

    //TODO clean up
    public void updateComboBox() {
        DBTool.getInstance().updateComboBox(origin, this);

        //todo get values for combo box
        //TODO combobox (editable taka че за search), с който да си свързвам нещата йоо
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }
}
