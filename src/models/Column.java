package models;

import utils.DataType;

public class Column {
    private String field;
    private DataType type;
    private int typeLen;
    private boolean nullAllowed;

    public Column(String field, String type, boolean nullAllowed) {
        this.field = field;
        this.nullAllowed = nullAllowed;
        String temp = type;
        temp = temp.replace('(', ' ');
        String[] arr = temp.split(" ");
        this.type = DataType.valueOf(arr[0]);
        this.typeLen = Integer.parseInt(arr[1].replace(')', ' ').trim());
    }

    public Column(final String field, final DataType type, final int typeLen, final boolean nullAllowed) {
        this.field = field;
        this.type = type;
        this.typeLen = typeLen;
        this.nullAllowed = nullAllowed;
    }

    @Override
    public String toString() {
        return field + ' ' + type + ' ' + typeLen + "| NULL ALLOWED = " + nullAllowed + '\n';
    }

    public String getField() {
        return field;
    }

    public int getTypeLen() {
        return typeLen;
    }

    public DataType getType() {
        return type;
    }

    public boolean isNullAllowed() {
        return nullAllowed;
    }
}
