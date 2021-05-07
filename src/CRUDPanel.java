import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;
import utils.PairMaker;
import utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

//todo update combo box on tab click
public class CRUDPanel extends JPanel {
    private String tableName = "";
    private String idColumnName = "";
    private List<String> foreignIdColumnNames = new LinkedList<>();
    private List<Long> idList = new LinkedList<>();
    private List<String> columnNames = new LinkedList<>();
    private JTable table = new JTable();
    private JScrollPane scrollPlane = new JScrollPane(table);
    private JComboBox<String> searchCombo = new MemoryComboBox<>();
    private JButton addBtn = new JButton("Добавяне");
    private JButton delBtn = new JButton("Изтриване");
    private JButton editBtn = new JButton("Редактиране");
    private JButton searchBtn = new JButton("Търси");
    private JComboBox<String> searchBar = new MemoryComboBox<>();

    public CRUDPanel(String tableName) {
        this.tableName = tableName;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        filterColumnNames();
        for (String name : columnNames) {
            this.add(new PairMaker(name));
        }
        for (String name : foreignIdColumnNames) {
            this.add(new ForeignKeyComboPair(name));
        }
        //---Button panel
        JPanel buttonHolder = new JPanel();
        buttonHolder.setLayout(new BoxLayout(buttonHolder, BoxLayout.X_AXIS));
        buttonHolder.add(addBtn);
        buttonHolder.add(delBtn);
        buttonHolder.add(editBtn);
        buttonHolder.add(searchBtn);
        this.add(buttonHolder);
        //---Down Panel

        scrollPlane.setPreferredSize(new Dimension(450, 150));
        System.out.println(idColumnName + " " + tableName);
        System.out.println(foreignIdColumnNames.toString() + " " + tableName);
        //пълним table първоначално
        table.setModel(DBTool.getInstance().getModelForColumns(columnNames, tableName));
        this.add(scrollPlane);
        this.setVisible(true);
    }

    private void filterColumnNames() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж

        final List<String> resultSet = DBTool.getInstance().getColumnNames(tableName);
        for (String string : resultSet) {
            if (string.contains("ID")) { //should i check primary key with hamming distance? YES
                if (Tools.HammingDistance(string, tableName + "_ID") < 4) {
                    //boom primary key found
                    //реално тука мога да заредя primary key-vete обаче ако са 20000+++, да няма да седяд в ram-ta
                    idColumnName = string;
                } else {
                    List<String> tableNames = DBTool.getInstance().getTableNames();
                    for (String name : tableNames) {
                        if (Tools.HammingDistance(name + "_ID", string) < 4) {
                            foreignIdColumnNames.add(string);
                            //ако не е primary проверяваме дали не е foreign отново с hamming distance и като сравняваме с имената на останалите таблици
                        }
                    }
                }
                //todo check foreign keys
            } else if (string.equalsIgnoreCase("id")) {
                idColumnName = string;
            } else {
                columnNames.add(string);
            }

        }
    }
    //todo a get all column names

}
