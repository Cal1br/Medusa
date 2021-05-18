package tabs;

import utils.DBTool;

import javax.swing.*;
import java.awt.*;

public class JoinPanel extends JPanel {
    private JComboBox<String> leftTable = new JComboBox<>();
    private JComboBox<String> leftColumn = new JComboBox<>();
    private JComboBox<String> rightTable = new JComboBox<>();
    private JComboBox<String> rightColumn = new JComboBox<>();
    private final JTable table = new JTable();
    private final JScrollPane scrollPlane = new JScrollPane(table);


    public JoinPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1,3));
        JPanel left = new JPanel();
        JPanel middle = new JPanel();
        JPanel right = new JPanel();
        left.setLayout(new BoxLayout(left,BoxLayout.Y_AXIS));
        middle.setLayout(new BoxLayout(middle,BoxLayout.Y_AXIS));
        right.setLayout(new BoxLayout(right,BoxLayout.Y_AXIS));
        left.add(leftTable);
        left.add(leftColumn);
        right.add(rightTable);
        right.add(rightColumn);
        topPanel.add(left);
        topPanel.add(middle);
        topPanel.add(right);
        this.add(topPanel);

    }
    public void updateModel() {
/*        List<Column> columnList = new LinkedList<>(columns);
        columnList.addAll(foreignIdColumns);*/
        //таблицата не показва външните ключове, те ще се правят с join заявка
       // table.setModel(DBTool.getInstance().getModelForColumns(this));
    }
}
