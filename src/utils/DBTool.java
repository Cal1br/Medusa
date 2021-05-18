package utils;

import models.Column;
import models.ExperimentalModel;
import models.Key;
import models.Pair;
import tabs.CRUDPanel;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

        } catch (FileNotFoundException e) { //
            try {
                File file = new File("src/config/config.properties");
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                properties.setProperty("db_path","");
                properties.setProperty("db_driver","");
                properties.setProperty("db_username","");
                properties.setProperty("db_password","");
                properties.setProperty("combo_box_auto_configure","");
                properties.store(fileOutputStream,"properties");

            } catch (IOException exception) {
                exception.printStackTrace();
            }
            e.printStackTrace(); //TODO create config or show error|create default config
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DBTool getInstance() {
        if (reference == null) {
            reference = new DBTool();
        }
        return reference;
    }

    public List<String> getTableNames() {
        connection = getConnection();
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='TABLE'";
        List<String> tableNames = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            set = statement.executeQuery(sql);
            while (set.next()) {
                tableNames.add(set.getString(1));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return tableNames;

    }

    @Deprecated
    public List<String> getColumnNames(String tableName) {
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
            while (resultSet.next()) {
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

    /**
     * Returns primary and foreign keys. Boolean value is true if it is the primary key, and false if it is a
     * foreign key.
     *
     * @param tableName
     * @return HashMap
     */
    public List<Key> getTableKeys(final String tableName) {
        List<Key> list = new LinkedList<>();
        connection = getConnection();
        String sql = "select CONSTRAINT_TYPE, COLUMN_LIST,SQL from information_schema.constraints where table_name = ?";
        try {
            sqlStatement = connection.prepareStatement(sql);
            sqlStatement.setString(1, tableName);
            set = sqlStatement.executeQuery();
            while (set.next()) {
                if (set.getString(1).equals("PRIMARY KEY")) {
                    list.add(new Key(tableName,set.getString(2),true));
                } else if(set.getString(1).equals("REFERENTIAL")){
                    //a little hacky... I know
                    final String string = set.getString(3);
                    final int start = string.indexOf("REFERENCES");

                    final String substring = string.substring(start);
                    StringBuilder sb = new StringBuilder(substring);
                    sb.delete(0,20);
                    final String[] split = sb.toString().split("[()]");
                    list.add(new Key(tableName,set.getString(2),split[0],split[1]));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Column> getColumnNamesAndType(String tableName) {
        List<Column> list = new LinkedList<>();
        String sql = "SHOW COLUMNS FROM " + tableName;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            final ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                list.add(new Column(resultSet.getNString(1), resultSet.getNString(2), resultSet.getBoolean(3)));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return list;
    }

    public TableModel getModelForColumnsWhere(final String tableName, final String selectedColumn, final String text){
        AbstractTableModel model = null;
        //String sql = String.format("SELECT (%s) FROM "+tableName,columnNames.stream().collect(Collectors.joining(", ")));
        String sql = "SELECT * FROM " + tableName + " WHERE " + selectedColumn + " iLIKE '" + text + "%'"; //iLike = ignoreCaseLike
        try {
            connection = getConnection();
            sqlStatement = connection.prepareStatement(sql);
            set = sqlStatement.executeQuery();
            model = new ExperimentalModel(set);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;
    }

    public TableModel getModelForColumns(final CRUDPanel origin) {
        AbstractTableModel model = null;
        String tableName = origin.getTableName();
        final List<Column> columns = origin.getColumns();
        String sql = String.format("SELECT " + origin.getIdColumn() + ",%s FROM " + tableName, columns.stream().map(Column::getField).collect(Collectors.joining(","))); //YARE YARE DAZE
        try {
            connection = getConnection();
            sqlStatement = connection.prepareStatement(sql);
            set = sqlStatement.executeQuery();
            ExperimentalModel experimentalModel = new ExperimentalModel(set);
            origin.setIdList(experimentalModel.getIdList());
            model = experimentalModel;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return model;
        //String sql = "SELECT * FROM " + tableName;
    }

    public void deleteAt(final long selectedId, final String tableName, final String idColumnName) {
        String sql = "DELETE FROM " + tableName + " WHERE " + idColumnName + "=" + selectedId;
        try {
            connection = getConnection();
            sqlStatement = connection.prepareStatement(sql);
            sqlStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public void add(final CRUDPanel origin) throws SQLException {
        StringBuilder columnsSb = new StringBuilder("INSERT INTO " + origin.getTableName() + "(");
        StringBuilder valuesSb = new StringBuilder(" VALUES (");

        for (Pair pair : origin.getPairs()) {
            columnsSb.append(pair.getColumn().getField()).append(", ");
            valuesSb.append(pair.getFormattedTextFieldText()).append(", ");
        }
        for(ForeignKeyComboPair pair: origin.getForeignPairs()){
            columnsSb.append(pair.getForeignKey().getColumnName()).append(", ");
            valuesSb.append(pair.getSelectedId()).append(", ");
        }
        columnsSb.replace(columnsSb.length() - 2, columnsSb.length(), ")");
        valuesSb.replace(valuesSb.length() - 2, valuesSb.length(), ")");
        columnsSb.append(valuesSb);
        connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(columnsSb.toString());
        statement.execute();
    }

    public void updateAt(final long selectedId, final CRUDPanel origin) throws SQLException {
        StringBuilder sql = new StringBuilder("UPDATE " + origin.getTableName() + " SET ");
        for (Pair pair : origin.getPairs()) {
            sql.append(pair.getColumn().getField()).append(" = ");
            sql.append(pair.getFormattedTextFieldText()).append(", ");
        }
        System.out.println(sql);
        sql.deleteCharAt(sql.length()-1);
        sql.deleteCharAt(sql.length()-1);
        System.out.println(sql);
        sql.append(" WHERE ").append(origin.getIdColumn()).append(" = ").append(selectedId);
        connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql.toString());
        statement.execute();
    }

    public void updateComboBox(final ForeignKeyComboPair foreignKeyComboPair) {
        final String names = foreignKeyComboPair.getName();
        JComboBox<String> comboBox = foreignKeyComboPair.getComboBox();
        final List<Long> idList = foreignKeyComboPair.getIdList();
        String sql = "select "+foreignKeyComboPair.getForeignKey().getReferenceTableKeyColumn()+", " +  names + " from " + foreignKeyComboPair.getForeignTableName();
        try {
            Connection connection = DBTool.getInstance().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
/*            statement.setString(2,tableName);
            statement.setString(1,names.get(0));*/
            //todo see wtf is this?
            ResultSet set = statement.executeQuery();
            comboBox.removeAllItems();
            if(foreignKeyComboPair.getColumn().isNullAllowed()){
                idList.add(-1L);
                comboBox.addItem(null);
            }
            while (set.next()) {
                idList.add(set.getLong(1));
                comboBox.addItem(set.getString(2));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }
}