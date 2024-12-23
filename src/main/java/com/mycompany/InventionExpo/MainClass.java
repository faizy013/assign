package com.mycompany.InventionExpo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainClass {
    private DbHelper dbHelper;
    private JFrame frame;
    private JTextField nameField, scoreField;
    private JTable table;
    private DefaultTableModel tableModel;

    public MainClass() {
        try {
            dbHelper = new DbHelper("assets/BC230410238.db"); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database connection failed: " + e.getMessage());
            System.exit(0);
        }
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Invention Expo");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(2, 2));
        JLabel nameLabel = new JLabel("Name:");
        JLabel scoreLabel = new JLabel("Score:");
        nameField = new JTextField();
        scoreField = new JTextField();
        topPanel.add(nameLabel);
        topPanel.add(nameField);
        topPanel.add(scoreLabel);
        topPanel.add(scoreField);

        JButton addButton = new JButton("Add Inventor");
        addButton.addActionListener(e -> addInventor());
        JButton loadButton = new JButton("Load Data");
        loadButton.addActionListener(e -> loadData());
        JButton deleteButton = new JButton("Delete All");
        deleteButton.addActionListener(e -> deleteAll());
        JButton viewWinnerButton = new JButton("View Winner");
        viewWinnerButton.addActionListener(e -> viewWinners());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(loadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewWinnerButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Score"}, 0);
        table = new JTable(tableModel);

        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(600, 400);
        frame.setVisible(true);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(frame, "Thanks for using Invention Expo.\nDeveloped by [Your Name]");
                System.exit(0);
            }
        });
    }

    private void addInventor() {
        try {
            String name = nameField.getText().trim();
            int score = Integer.parseInt(scoreField.getText().trim());
            if (name.isEmpty() || score <= 0 || score > 100) {
                JOptionPane.showMessageDialog(frame, "Invalid input!");
                return;
            }
            if (dbHelper.addInventor(name, score)) {
                JOptionPane.showMessageDialog(frame, "Inventor added successfully!");
                nameField.setText("");
                scoreField.setText("");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void loadData() {
        try {
            tableModel.setRowCount(0);
            ArrayList<Inventor> inventors = dbHelper.fetchAllInventors();
            for (Inventor inventor : inventors) {
                tableModel.addRow(new Object[]{inventor.getId(), inventor.getName(), inventor.getScore()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void deleteAll() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to delete all data?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                dbHelper.deleteAllInventors();
                tableModel.setRowCount(0);
                JOptionPane.showMessageDialog(frame, "All data deleted!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        }
    }

    private void viewWinners() {
        try {
            tableModel.setRowCount(0);
            ArrayList<Inventor> winners = dbHelper.fetchTopInventors();
            for (Inventor winner : winners) {
                tableModel.addRow(new Object[]{winner.getId(), winner.getName(), winner.getScore()});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new MainClass();
    }
}
