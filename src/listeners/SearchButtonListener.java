package listeners;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SearchButtonListener implements MouseListener {
    private JPanel searchPanel = null;
    public SearchButtonListener(final JPanel searchPanel) {
        this.searchPanel = searchPanel;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if(e.getClickCount()==2){
            searchPanel.setVisible(false);
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
