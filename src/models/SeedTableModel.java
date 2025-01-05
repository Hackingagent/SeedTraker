package models;

import java.util.List;

import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;

import utils.UserService;

public class SeedTableModel extends AbstractTableModel {
    private List<Seed> seeds;
    private UserService userService;
    private String[] columnNames = {"ID", "Seed Type", "Registration Date", "Expiring Date", "Quantity Available", "Added By", "Edit", "Delete"};

    public SeedTableModel(List<Seed> seeds, UserService userService) {
        this.seeds = seeds;
        this.userService = userService;
    }

    @Override
    public int getRowCount() {
        return seeds.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Seed seed = seeds.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return seed.getSeedID();
            case 1:
                return seed.getSeedType();
            case 2:
                return seed.getRegistrationDate();
            case 3:
                return seed.getExpiringDate();
            case 4:
                return seed.getQuantityAvailable();
            case 5:
                return userService.getUsernameById(seed.getAddedBy().getId()); // Get username from UserService
            case 6:
                return "Edit";
            case 7:
                return "Delete";
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 6 || columnIndex == 7) {
            return JButton.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 6 || columnIndex == 7;
    }

    public void setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
        fireTableDataChanged();
    }


}
