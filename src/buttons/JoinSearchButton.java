package buttons;

import tabs.JoinPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JoinSearchButton extends JButton {
    private JoinPanel origin;

    public JoinSearchButton(final String name,final JoinPanel origin) {
        super(name);
        this.origin = origin;
        this.addActionListener(new JoinSearchAction());
    }

    private class JoinSearchAction implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {

        }
    }
}
