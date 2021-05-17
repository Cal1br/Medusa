package utils;

import models.Key;
import tabs.CRUDPanel;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ForeignKeyComboPair extends JPanel {
    private Key foreignKey;
    private JLabel label;
    private JComboBox<String> comboBox = new JComboBox<>();
    private List<String> names = new LinkedList<>();

    public String getForeignTableName() {
        return foreignKey.getReferencedTable();
    }

    private CRUDPanel origin;


    //todo optimize this too, get a reference sql
    public ForeignKeyComboPair(CRUDPanel origin, Key foreignKey) {
        this.foreignKey=foreignKey;
        label = new JLabel(foreignKey.getColumnName().replace("_ID",""));
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        names = findNames(foreignKey.getReferencedTable());
        this.add(label);
        this.add(comboBox);
/*        this.origin = origin;
        foreignKey = key;
        foreignTableName = findTableName(foreignKey);
        names = findNames(foreignTableName);
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        label = new JLabel(key.replace("_ID", ""));
        this.add(label);
        this.add(comboBox);*/
    }

    public List<String> getNames() {
        return names;
    }

    //намира името на таблицата с което се идентифицира обекта, например име на човек
    //todo тука ще се направи така че да се вижда от Config как да се казва
    @Deprecated
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
