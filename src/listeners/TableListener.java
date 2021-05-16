package listeners;

import models.Pair;
import tabs.CRUDPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;

//най-доброто място където да storevam кои ключове съм взимал е таблицата
public class TableListener implements MouseListener {
    private CRUDPanel originTable = null;
    private int selected = -1;
    private int selectedId = -1;

    public TableListener(CRUDPanel table) {
        super();
        this.originTable = table;
    }

    public int getSelected() {
        return selected;
    }

    public int getSelectedId() { //todo да връща селектираното ID
        return selectedId;
    }

    @Override
    //ТОВА ПЪЛНИ ТАБЛИЦАТА КАТО СЕ НАТИСНЕ 2 ПЪТИ БЪРЗО
    public void mouseClicked(final MouseEvent e) {
        Component component = e.getComponent();
        JTable jTable = (JTable) component;
        selected = jTable.getSelectedRow();
        //при 2 бързи натискания
        if (e.getClickCount() == 2) { //реално мога да направя заявка към базата данни с id-to ама няма смисъл за излишен товар...
            final List<Pair> pairs = originTable.getPairs();
            HashMap<String, Object> columnNameToValue = new HashMap<>();
            for (int j = 0; j < jTable.getColumnCount(); j++) {
                columnNameToValue.put(jTable.getColumnName(j), jTable.getValueAt(selected, j));
            }
            for (Pair pair : pairs) {
                pair.getTextField().setText(columnNameToValue.get(pair.getColumn().getField()).toString()); //bravo na men
            }
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {

    }

    @Override
    public void mouseReleased(final MouseEvent e) {

    }

    @Override
    public void mouseEntered(final MouseEvent e) {

    }

    @Override
    public void mouseExited(final MouseEvent e) {

    }
}

