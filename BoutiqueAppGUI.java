import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class BoutiqueAppGUI {
    static ArrayList<Dress> Dresses = new ArrayList<>();
    static JTable table;
    static DefaultTableModel tableModel;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Boutique Record Keeper");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Dress and Boutique Record Keeper", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));  // Blue
        frame.add(titleLabel, BorderLayout.NORTH);

        String[] columnHeaders = {"Dress Name", "Size", "Color", "Price"};
        tableModel = new DefaultTableModel(columnHeaders, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addBtn = new JButton("Add Dress");
        JButton viewBtn = new JButton("View All");
        JButton searchBtn = new JButton("Search");
        JButton deleteBtn = new JButton("Delete");
        JButton editBtn = new JButton("Edit");
        JButton exitBtn = new JButton("Exit");

        buttonPanel.add(addBtn);
        buttonPanel.add(viewBtn);
        buttonPanel.add(searchBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(exitBtn);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addDress(frame));
        viewBtn.addActionListener(e -> refreshTable());
        searchBtn.addActionListener(e -> searchDress(frame));
        deleteBtn.addActionListener(e -> deleteDress(frame));
        editBtn.addActionListener(e -> editDress(frame));
        exitBtn.addActionListener(e -> System.exit(0));

        loadFromFile();
        refreshTable();

        frame.setVisible(true);
    }

    static void addDress(JFrame frame) {
        JTextField nameField = new JTextField(10);
        JTextField sizeField = new JTextField(10);
        JTextField colorField = new JTextField(10);
        JTextField priceField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.add(new JLabel("Dress Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Size (S/M/L/XL):"));
        panel.add(sizeField);
        panel.add(new JLabel("Color:"));
        panel.add(colorField);
        panel.add(new JLabel("Price:"));
        panel.add(priceField);

        int result = JOptionPane.showConfirmDialog(frame, panel, "Add New Dress", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText().trim();
                String size = sizeField.getText().trim();
                String color = colorField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());

                Dress dress = new Dress(name, size, color, price);
                Dresses.add(dress);
                refreshTable();
                saveToFile();
                JOptionPane.showMessageDialog(frame, "Dress added:\n" + dress);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid price entered!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    static void refreshTable() {
        tableModel.setRowCount(0);
        for (Dress d : Dresses) {
            tableModel.addRow(new Object[]{d.name, d.size, d.color, d.price});
        }
    }

    static void searchDress(JFrame frame) {
        String[] options = {"Color", "Size"};
        int choice = JOptionPane.showOptionDialog(frame, "Search by:", "Search",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            String color = JOptionPane.showInputDialog(frame, "Enter color:");
            boolean found = false;
            if (color != null) {
                tableModel.setRowCount(0);
                for (Dress d : Dresses) {
                    if (d.color.equalsIgnoreCase(color.trim())) {
                        tableModel.addRow(new Object[]{d.name, d.size, d.color, d.price});
                        found = true;
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(frame, "No Dresses found With This Color");
            }
        } else if (choice == 1) {
            String size = JOptionPane.showInputDialog(frame, "Enter size (S/M/L/XL):");
            boolean found = false;
            if (size != null) {
                tableModel.setRowCount(0);
                for (Dress d : Dresses) {
                    if (d.size.equalsIgnoreCase(size.trim())) {
                        tableModel.addRow(new Object[]{d.name, d.size, d.color, d.price});
                        found = true;
                    }
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(frame, "No Dresses found With This Size");
            }
        }
    }

    static void deleteDress(JFrame frame) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Dresses.remove(row);
            refreshTable();
            saveToFile();
            JOptionPane.showMessageDialog(frame, "Dress deleted.");
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a dress to delete.", "No selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    static void editDress(JFrame frame) {
        int row = table.getSelectedRow();
        if (row >= 0) {
            Dress dress = Dresses.get(row);

            JTextField nameField = new JTextField(dress.name, 10);
            JTextField sizeField = new JTextField(dress.size, 10);
            JTextField colorField = new JTextField(dress.color, 10);
            JTextField priceField = new JTextField(String.valueOf(dress.price), 10);

            JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
            panel.add(new JLabel("Dress Name:"));
            panel.add(nameField);
            panel.add(new JLabel("Size (S/M/L/XL):"));
            panel.add(sizeField);
            panel.add(new JLabel("Color:"));
            panel.add(colorField);
            panel.add(new JLabel("Price:"));
            panel.add(priceField);

            int result = JOptionPane.showConfirmDialog(frame, panel, "Edit Dress", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    dress.name = nameField.getText().trim();
                    dress.size = sizeField.getText().trim();
                    dress.color = colorField.getText().trim();
                    dress.price = Double.parseDouble(priceField.getText().trim());
                    refreshTable();
                    saveToFile();
                    JOptionPane.showMessageDialog(frame, "Dress updated.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid price entered!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a dress to edit.", "No selection", JOptionPane.WARNING_MESSAGE);
        }
    }

    static void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("dresses.txt"))) {
            for (Dress d : Dresses) {
                writer.write(d.name + "," + d.size + "," + d.color + "," + d.price);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    static void loadFromFile() {
        Dresses.clear();
        File file = new File("dresses.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String name = parts[0];
                    String size = parts[1];
                    String color = parts[2];
                    double price = Double.parseDouble(parts[3]);
                    Dresses.add(new Dress(name, size, color, price));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }

    static class Dress {
        String name;
        String size;
        String color;
        double price;

        Dress(String name, String size, String color, double price) {
            this.name = name;
            this.size = size;
            this.color = color;
            this.price = price;
        }

        @Override
        public String toString() {
            return "Name: " + name + " | Size: " + size + " | Color: " + color + " | Price: Rs. " + price;
        }
    }
}
