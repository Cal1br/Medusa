package utils;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class DBTool {
    //init метод който зарежда configa в strings
    //ще е хубаво да е singleton или да е static само, обаче при singleton не може да се избегне init метода. По-ООП е
    private static DBTool reference = null;
    private static Connection connection = null;
    private static PreparedStatement sqlStatement = null;
    private static ResultSet set = null;
    private String DBPath = "";
    private String driverPath = "";
    private String DBUsername = "";
    private String DBPassword = "";


    public static DBTool getInstance() {
        if (reference == null) {
            reference = new DBTool();
        }
        return reference;
    }

    private DBTool() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("src/config/config.properties"); //TODO това за сега върши рабора, обаче като е jar?
            properties.load(inputStream);
            DBPath = properties.getProperty("db_path");
            driverPath = properties.getProperty("db_driver");
            DBUsername = properties.getProperty("db_username");
            DBPassword = properties.getProperty("db_password");

        } catch (FileNotFoundException e) {
            e.printStackTrace(); //TODO create config or show error|create default config
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getTableNames() {
        connection = getConnection();
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE'";
        List<String> tableNames = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()){
                tableNames.add(set.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return tableNames;

    }
    public List<String> getColumnNames(String tableName){
        List<String> list = new LinkedList<>();
        String sql = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? " +
                "ORDER BY ORDINAL_POSITION";
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, tableName); //todo this kinda sus
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                list.add(resultSet.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return list;
    }

    public java.sql.Connection getConnection() {
        try {
            Class.forName(driverPath);
            connection = java.sql.DriverManager.getConnection
                     (DBPath, DBUsername, DBPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }


    public void FillCombo(JComboBox<String> combo, String columnName, String tableName) {

        connection = getConnection();
        String sql = "select " + columnName + " from " + tableName;
        try {
            sqlStatement = connection.prepareStatement(sql, java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE, java.sql.ResultSet.CONCUR_READ_ONLY);
            set = sqlStatement.executeQuery();
            combo.removeAllItems(); // виж setModel в документацията
            if (combo instanceof MemoryComboBox) {
                MemoryComboBox memory = (MemoryComboBox) combo;
                java.util.List list = memory.getIdList();
                list.clear();
                while (set.next()) {
                    list.add(set.getLong(1));
                }
                memory.setIdList(list);
                System.out.println(list.toString());
                set.beforeFirst();
            }

            while (set.next()) {
                combo.addItem(set.getString(2));
            }
        } catch (java.sql.SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public TableModel getModelForColumns(final List<String> columnNames, String tableName) {
        MyModel model = null;
        //String sql = String.format("SELECT (%s) FROM "+tableName,columnNames.stream().collect(Collectors.joining(", ")));
        String sql ="SELECT * FROM "+tableName;
        try {
            connection = getConnection();
            sqlStatement = connection.prepareStatement(sql);
            set = sqlStatement.executeQuery();
            model = new MyModel(set);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;
    }

    /*public MyModel getAllData(String tableName) {
        connection = getConnection();
        MyModel model = null;
        try {
            //Заявките които връщат нещо (set) се изпълняват с executeQuery.
            sqlStatement = connection.prepareStatement("SELECT * FROM " + tableName);
            set = sqlStatement.executeQuery();
            model = new MyModel(set);
        } catch (java.sql.SQLException sqlException) {
            sqlException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }*/
}