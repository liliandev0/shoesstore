package shoestore;

import customer.sales.CustomerSales;
import customer.salesui.ViewSalesUI;
import database.Database;
import supplier.ordersui.SupplierOrderUI;
import customer.salesui.CustomerSaleUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StoreManagementGUI extends JFrame {

    public JPanel mainPanel;
    public JPanel buttonPanel;
    public JPanel loginPanel;
    public JPanel textAreaPanel;
    private JTextArea summaryTextArea;
    private Database database;
    JSplitPane splitPanel;

    public StoreManagementGUI() {
        setTitle("Store Management Software");
        setSize(600, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        database = new Database();

        // Main panel
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Login panel
        loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);

        GroupLayout layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        JLabel nameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        JTextField nameField = new JTextField(10);
        JPasswordField passwordField = new JPasswordField(10);

        JButton loginButton = createStyledButton("Login", e -> performLogin(nameField.getText(), new String(passwordField.getPassword())));

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameLabel)
                        .addComponent(passwordLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(nameField)
                        .addComponent(passwordField)
                        .addComponent(loginButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(nameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField))
                .addComponent(loginButton)
        );

        mainPanel.add(loginPanel, BorderLayout.NORTH);

        // Panel with buttons
        buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        textAreaPanel = createSummaryPanel();

        JButton orderButton = createStyledButton("Place Supplier Order", e -> openSupplierOrderUI());
        JButton saleButton = createStyledButton("Enter Customer Sale", e -> openCustomerSaleUI());
        JButton viewSalesButton = createStyledButton("View Sales", e -> viewSales());

        splitPanel = new JSplitPane(SwingConstants.VERTICAL, buttonPanel, textAreaPanel);

        buttonPanel.add(orderButton);
        buttonPanel.add(saleButton);
        buttonPanel.add(viewSalesButton);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JButton createButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.addActionListener(listener);
        return button;
    }

    private void openSupplierOrderUI() {
        SupplierOrderUI supplierOrderUI = new SupplierOrderUI(this);
        supplierOrderUI.setVisible(true);
    }

    private void openCustomerSaleUI() {
        CustomerSaleUI customerSaleUI = new CustomerSaleUI(this);
        customerSaleUI.setVisible(true);
    }

    private void viewSales() {
        ViewSalesUI viewSalesUI = new ViewSalesUI(this);
        viewSalesUI.setVisible(true);
    }

    public static void main(String[] args) {
        StoreManagementGUI mainFrame = new StoreManagementGUI();
        mainFrame.setVisible(true);
    }

    private void performLogin(String username, String password) {
        if (isAuthenticationCorrect(username, password)) {
            mainPanel.remove(loginPanel);
            mainPanel.add(splitPanel);
            mainPanel.revalidate();
            mainPanel.repaint();
        } else {
            System.out.println(username);
            System.out.println(password);
            JOptionPane.showMessageDialog(this, "Incorrect username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isAuthenticationCorrect(String username, String password) {
        return username.equals("admin") && password.equals("admin");
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel tableLabel = new JLabel("Sales Summary");
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable salesTable = new JTable(tableModel);

        tableModel.addColumn("ID");
        tableModel.addColumn("Product ID");
        tableModel.addColumn("Quantity");
        tableModel.addColumn("Total Price (€)");
        tableModel.addColumn("Sale Date");
        tableModel.addColumn("Customer ID");

        ArrayList<CustomerSales> sales = database.getCustomerSales();
        for (CustomerSales sale : sales) {
            tableModel.addRow(new Object[]{
                    sale.getId(),
                    sale.getProductId(),
                    sale.getQuantity(),
                    String.format("%.2f", sale.getTotalAmount()),
                    sale.getSaleDate(),
                    sale.getCustomerId()
            });
        }

        JScrollPane scrollPane = new JScrollPane(salesTable);
        panel.add(tableLabel);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String label, ActionListener listener) {
        JButton button = new JButton(label);
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(listener);
        return button;
    }
}
