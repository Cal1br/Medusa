package models;

import utils.DataType;

public class Column {
    private String field;
    private DataType type;
    private int typeLen;
    private boolean nullAllowed;

    @Override
    public String toString() {
        return field +' '+type +' '+typeLen+ "| NULL ALLOWED = "+nullAllowed+'\n';
    }

    public Column(String field, String type, boolean nullAllowed) {
        this.field = field;
        String temp = type;
        this.nullAllowed = nullAllowed;
        temp = temp.replace('(',' ');
        String[] arr = temp.split(" ");
        this.type = DataType.valueOf(arr[0]);
        this.typeLen = Integer.parseInt(arr[1].replace(')',' ').trim());
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
