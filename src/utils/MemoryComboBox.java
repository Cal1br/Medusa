package utils;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

public class MemoryComboBox<E> extends JComboBox<E> {
    private List<Long> idList = new LinkedList<Long>();

    public MemoryComboBox() {
        super();
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(final List<Long> idList) {
        this.idList = idList;
    }

    public long getIdAt(final int selectedIndex) {
        return idList.get(selectedIndex);
    }
}
