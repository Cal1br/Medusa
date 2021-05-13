package utils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class ForeignKeyComboPair extends JPanel {
    private String foreignKey = "";
    private JLabel label = null;
    private JComboBox<String> comboBox = new JComboBox<>();
    private List<String> names = null;
    private String tableName = "";


    //todo optimize this too, get a reference sql
    public ForeignKeyComboPair(String key) {
        tableName = findTableName(foreignKey);
        names = findNames(tableName);
        foreignKey = key;
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800, 20));
        label = new JLabel(key.replace("_ID", ""));
        this.add(label);
        updateComboBox();
        this.add(comboBox);
    }

    //намира името на таблицата с което се идентифицира обекта, например име на човек
    //todo edit?
    //todo add Cyrillic
    private List<String> findNames(String tableName) {
        names = new LinkedList<String>();
        final List<String> columnNames = DBTool.getInstance().getColumnNames(tableName);
        String fNameColumn = "";
        int shortestHamming = Integer.MAX_VALUE;
        for (String name : columnNames) {
            if(!name.toLowerCase().contains("name")){
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
                tableName = name; //todo add more than 1 foreign key
            }
        }
        return tableName;
    }

    //TODO clean up
    public void updateComboBox() {
        String sql = "select " + names.get(0) + " from " + tableName;
        try {
            Connection connection = DBTool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
/*            statement.setString(2,tableName);
            statement.setString(1,names.get(0));*/
            //todo see wtf is this?
            ResultSet set = statement.executeQuery();
            comboBox.removeAllItems();
            while (set.next()) {
                comboBox.addItem(set.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        //todo get values for combo box
        //TODO combobox (editable taka че за search), с който да си свързвам нещата йоо
    }


    //todo ENUM ili pf nz nqkaksi da vidim date li e tova int li e long li e nz nz. Switch za vseki h2 data type??
}
