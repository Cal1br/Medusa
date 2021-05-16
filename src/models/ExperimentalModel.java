package models;

import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExperimentalModel extends AbstractTableModel {
    private ResultSet resultSet;
    private ResultSetMetaData metaData;
    private int columnCount;
    private List<Object[]> objectList = new ArrayList<>();
    private List<Long> idList = new ArrayList<>();
    private int rowCount;
    private boolean idListPresent;

    /**
     * Модел който очаква да му бъдат дадени всичките данни които да се покажат в таблицата, обаче първата колона трябва винаги да съдържа id-tata
     */
    public ExperimentalModel(final ResultSet resultSet) throws SQLException {
        this.resultSet = resultSet;
        this.metaData = resultSet.getMetaData();
        columnCount = metaData.getColumnCount();
        this.rowCount = 0;
        while (resultSet.next()) {
            idList.add(resultSet.getLong(1));
            Object[] row = new Object[columnCount];
            /* почваме от 1 защото първата колона съдържа id-ta. Целта е да се вземе id-tata и другите данни от 1 Result Set,
               така че да е по-леко на процесора и данните ВИНАГИ да съвпадат с тези в таблицата*/
            for (int n = 1; n < columnCount; n++) {
                row[n - 1] = resultSet.getObject(n + 1);
            }
            objectList.add(row);
            rowCount++;
        }
    }

    public List<Long> getIdList() {
        return idList;
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount - 1;
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return objectList.get(rowIndex)[columnIndex];
    }

    @Override
    public String getColumnName(final int column) {
        try {
            return metaData.getColumnName(column + 2);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            return "SQL ERROR";
        }
    }
}
