package utils;

import models.Column;
import models.Key;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public class ForeignKeyComboPair extends JPanel {
    private Key foreignKey;
    private JLabel label;
    private JComboBox<String> comboBox = new JComboBox<>();
    private String identifierName;
    private List<Long> idList = new LinkedList<>();
    private Column column;

    //todo optimize this too, get a reference sql
    public ForeignKeyComboPair(Column column, Key foreignKey) {
        this.column = column;
        this.foreignKey = foreignKey;
        label = new JLabel(foreignKey.getColumnName().replace("_ID", ""));
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        identifierName = findName(foreignKey.getReferencedTable());
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

    public Column getColumn() {
        return column;
    }

    public String getForeignTableName() {
        return foreignKey.getReferencedTable();
    }

    public String getName() {
        return identifierName;
    }

    //намира името на таблицата с което се идентифицира обекта, например име на човек
    //todo тука ще се направи така че да се вижда от Config как да се казва
    private String findName(String tableName) {
        boolean autoSearch = false;
        Properties prop = new Properties();
        try {
            InputStream stream = new FileInputStream("src/config/config.properties");
            prop.load(stream);
            autoSearch = Boolean.parseBoolean(prop.getProperty("combo_box_auto_configure"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!autoSearch) {
            return prop.getProperty(foreignKey.getReferencedTable().toLowerCase());
        }
        final List<String> columnNames = DBTool.getInstance().getColumnNames(tableName);
        String name = "";
        int shortestHamming = Integer.MAX_VALUE;
        for (String str : columnNames) {
            if (!str.toLowerCase().contains("name")) {
                continue;
            }
            //todo redundant?
            int distance = Tools.HammingDistance("name", str.toLowerCase());
            if (distance < shortestHamming) {
                shortestHamming = distance;
                name = str;
            }
        }
        return name;
    }

    //TODO clean up
    public void updateComboBox() {
        DBTool.getInstance().updateComboBox(this);

        //todo get values for combo box
        //TODO combobox (editable taka че за search), с който да си свързвам нещата йоо
    }

    public JComboBox<String> getComboBox() {
        return comboBox;
    }

    public Key getForeignKey() {
        return foreignKey;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public long getSelectedId() {
        return idList.get(comboBox.getSelectedIndex());
    }
}
