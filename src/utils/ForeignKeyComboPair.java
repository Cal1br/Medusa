package utils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForeignKeyComboPair extends JPanel {
    private String foreignKey = "";
    private JLabel label = null;
    private JComboBox<String> comboBox = null;

    public ForeignKeyComboPair(String key) {
        foreignKey = key;
        this.setLayout(new GridLayout(1, 2));
        this.setMaximumSize(new Dimension(800,20));
        label = new JLabel(key.replace("_ID",""));
        this.add(label);
        UpdateComboBox();
        this.add(comboBox);
    }
    //TODO
    public void UpdateComboBox(){
        String tableName = FindTableName(foreignKey);
        List<String> names = FindNames(tableName);
        String sql = "select "+names.get(0)+" from "+tableName;
        try {
            Connection connection = DBTool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
/*            statement.setString(2,tableName);
            statement.setString(1,names.get(0));*/
            //todo see wtf is this?
            ResultSet set = statement.executeQuery();
            comboBox.removeAllItems();
            while (set.next()){
                comboBox.addItem(set.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        //todo get values for combo box
        //TODO combobox (editable taka че за search), с който да си свързвам нещата йоо
    }
    private static List<String> FindNames(String tableName){
        final List<String> columnNames = DBTool.getInstance().getColumnNames(tableName);
        String fNameColumn = "";
        int shortestHamming = Integer.MAX_VALUE;
        for (String name : columnNames){
            if(Tools.HammingDistance("fname",name.toLowerCase())<shortestHamming){
                fNameColumn = name;
            }
        }
        columnNames.add(fNameColumn);
        return columnNames;
    }
    private static String FindTableName(String key){
        String tableName = "";
        int shortestHamming = Integer.MAX_VALUE;
        List<String> list = DBTool.getInstance().getTableNames();
        for(String name : list){
            if(Tools.HammingDistance(key,name)<shortestHamming){
                tableName = name;
            }
        }
        return tableName;
    }


    //todo ENUM ili pf nz nqkaksi da vidim date li e tova int li e long li e nz nz. Switch za vseki h2 data type??
}
