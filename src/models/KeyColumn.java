package models;

public class KeyColumn extends Column{
    private Key key;
    public KeyColumn(final Column column,final Key key) {
        super(column.getField(), column.getType(),column.getTypeLen(), column.isNullAllowed());
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
}
