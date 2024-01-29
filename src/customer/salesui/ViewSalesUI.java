package customer.salesui;

import customer.sales.CustomerSales;
import database.Database;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ViewSalesUI extends JDialog {

    private final Database database;
    private final DefaultListModel<CustomerSales> salesListModel;
    private final JTextField fromDateField;
    private final JTextField toDateField;

    public ViewSalesUI(JFrame parentFrame) {

        super(parentFrame, "Customer Sales Management", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setTitle("Customer Sales Management");
        setLayout(new GroupLayout(getContentPane()));
        setSize(600, 400);

        database = new Database();

        salesListModel = new DefaultListModel<>();
        JList<CustomerSales> salesList = new JList<>(salesListModel);
        JScrollPane scrollPane = new JScrollPane(salesList);

        JLabel fromDateLabel = new JLabel("From: ");
        JLabel toDateLabel = new JLabel("To: ");

        fromDateField = createDateField();
        fromDateField.setColumns(8);
        toDateField = createDateField();
        toDateField.setColumns(8);

        JButton filterButton = new JButton("Filter by Date");
        filterButton.addActionListener(e -> filterByDate());

        ArrayList<CustomerSales> sales = database.getCustomerSales();
        for (CustomerSales sale : sales) {
            salesListModel.addElement(sale);
        }

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                        .addComponent(fromDateLabel)
                        .addComponent(fromDateField)
                        .addComponent(toDateLabel)
                        .addComponent(toDateField)
                        .addComponent(filterButton))
                .addComponent(scrollPane)
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup()
                        .addComponent(fromDateLabel)
                        .addComponent(fromDateField)
                        .addComponent(toDateLabel)
                        .addComponent(toDateField)
                        .addComponent(filterButton))
                .addComponent(scrollPane)
        );

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void filterByDate() {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date fromDate = dateFormat.parse(fromDateField.getText());
            Date toDate = dateFormat.parse(toDateField.getText());

            java.sql.Date sqlFromDate = new java.sql.Date(fromDate.getTime());
            java.sql.Date sqlToDate = new java.sql.Date(toDate.getTime());

            ArrayList<CustomerSales> filteredSales = database.getFilteredSales(sqlFromDate, sqlToDate);
            salesListModel.clear();
            for (CustomerSales sale : filteredSales) {
                salesListModel.addElement(sale);
            }
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format, use dd/MM/yyyy format.");
        }
    }

    private JFormattedTextField createDateField() {
        JFormattedTextField dateField = null;
        try {
            MaskFormatter dateFormatter = new MaskFormatter("##/##/####");
            dateField = new JFormattedTextField(dateFormatter);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return dateField;
    }
}
