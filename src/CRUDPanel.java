import utils.DBTool;
import utils.ForeignKeyComboPair;
import utils.MemoryComboBox;
import utils.PairMaker;
import utils.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class CRUDPanel extends JPanel {
    private String tableName = "";
    private String idColumnName = "";
    private List<String> foreignIdColumnNames = new LinkedList<>();
    private List<Long> idList = new LinkedList<>();
    private List<String> columnNames = new LinkedList<>();
    private JTable table = new JTable();
    private JScrollPane scrollPlane = new JScrollPane(table);

    private JComboBox<String> searchCombo = new MemoryComboBox<>();
    private JButton searchBtn = new JButton("Търси");
    private JButton addBtn = new JButton("Добавяне");
    private JButton delBtn = new JButton("Изтриване");
    private JButton editBtn = new JButton("Редактиране");

    public CRUDPanel(String tableName) {
        this.tableName = tableName;
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        filterColumnNames();
        for (String name : columnNames) {
            this.add(new PairMaker(name));
        }
        for(String name : foreignIdColumnNames){
            this.add(new ForeignKeyComboPair(name));
        }
        //---Middle Panel
        //по default панелите имат flow layout
/*        midPanel.add(addBtn);
        midPanel.add(delBtn);
        midPanel.add(editBtn);
        midPanel.add(searchCombo);
        midPanel.add(searchBtn);

        this.add(midPanel);*/


        //---Down Panel
        //scrollPlane.setPreferredSize(new Dimension(450, 150));
        scrollPlane.setMinimumSize(new Dimension(450, 150));
        // botPanel.add(scrollPlane);
        //scrollPlane
        //  this.add(botPanel);
        System.out.println(idColumnName + " " + tableName);
        System.out.println(foreignIdColumnNames.toString() + " " + tableName);
        this.setVisible(true);
    }

    private void filterColumnNames() { //малко ми изглежда тежко, така че ще е хубаво само да се изпълнява веднъж
        /*String sql = "SELECT COLUMN_NAME " +
                "FROM INFORMATION_SCHEMA.COLUMNS " +
                "WHERE TABLE_NAME = ? " +
                "ORDER BY ORDINAL_POSITION";
        Connection connection = DBTool.getInstance().getConnection();
        PreparedStatement statement = null;*/


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
