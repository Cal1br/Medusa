package models;

public class Key {
    private String tableName; //таблицата към която принадлежи
    private String columnName; //как се казва в таблицата към която принадлежи
    private String referenceTable; //таблицата към която се обръща
    private String referenceTableKeyColumn; //как се казва в тази таблица
    private boolean isPrimaryKey = false;

    public Key(final String tableName, final String columnName, final String referenceTable, final String referenceTableKeyColumn) { //referential key constructor
        this.tableName = tableName;
        this.columnName = columnName;
        this.referenceTable = referenceTable.replaceAll("\"", "");
        this.referenceTableKeyColumn = referenceTableKeyColumn.replaceAll("\"", "");
    }

    public Key(final String tableName, final String columnName, final boolean isPrimaryKey) { //primary key constructor
        this.tableName = tableName;
        this.columnName = columnName;
        this.isPrimaryKey = isPrimaryKey;
    }

    @Override
    public String toString() {
        return "Key{" +
                "tableName='" + tableName + '\'' +
                ", columnName='" + columnName + '\'' +
                ", referenceTable='" + referenceTable + '\'' +
                ", referenceTableKeyColumn='" + referenceTableKeyColumn + '\'' +
                ", isPrimaryKey=" + isPrimaryKey +
                '}';
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getReferencedTable() {
        return referenceTable;
    }

    public String getReferenceTableKeyColumn() {
        return referenceTableKeyColumn;
    }
}
