package database;

import customer.Customer;
import customer.sales.CustomerSales;
import exceptions.SupplierOrderException;
import org.jetbrains.annotations.NotNull;
import supplier.Supplier;
import shoes.Shoes;
import supplier.orders.SupplierOrders;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class Database {

    private Connection connection;

    public Database() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Supplier> getSupplierDatabase() {
        ArrayList<Supplier> supplierList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "")) {
            PreparedStatement selectSupplierStatement = connection.prepareStatement("SELECT * FROM fornitori");
            ResultSet resultSet = selectSupplierStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String name = resultSet.getString("nome");

                Supplier supplier = new Supplier(id, name);
                supplierList.add(supplier);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return supplierList;
    }

    public ArrayList<Shoes> getShoesDatabase() {
        ArrayList<Shoes> shoesList = new ArrayList<>();

        int id, brandId, modelId, colorId, typeId, quantity;
        float size, price, purchasePrice, sellingPrice;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "")) {
            PreparedStatement selectShoesStatement = connection.prepareStatement("SELECT * FROM scarpe");
            ResultSet resultSet = selectShoesStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt("ID");
                brandId = resultSet.getInt("ID_marca");
                modelId = resultSet.getInt("ID_modello");
                size = resultSet.getFloat("taglia");
                colorId = resultSet.getInt("ID_colore");
                typeId = resultSet.getInt("ID_tipo");
                price = resultSet.getFloat("prezzo");
                quantity = resultSet.getInt("quantita");
                purchasePrice = resultSet.getFloat("prezzo_acquisto");
                sellingPrice = resultSet.getFloat("prezzo_vendita");

                Shoes shoes = new Shoes(id, brandId, modelId, size, colorId, typeId, price, quantity, purchasePrice, sellingPrice);
                shoesList.add(shoes);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return shoesList;
    }

    // Place an order from the supplier by inserting data into the ordersupplier table
    public void orderSupplier(SupplierOrders order, List<Shoes> shoesList) throws SupplierOrderException {

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement insertOrderStatement = connection.prepareStatement("INSERT INTO ordinifornitore (ID_fornitore, data_ordine) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertOrderDetailsStatement = connection.prepareStatement("INSERT INTO dettaglioordini (ID_ordine_fornitore, ID_scarpa, quantita) VALUES (?, ?, ?)");
             PreparedStatement getShoesQuantity = connection.prepareStatement("SELECT quantita FROM scarpe WHERE ID = ?");
             PreparedStatement updateShoesStatement = connection.prepareStatement("UPDATE scarpe SET quantita = ? WHERE ID = ?")) {

            int orderID = 0;

            connection.setAutoCommit(false);

            Set<Integer> supplierIdSet = new HashSet<>();
            String querySupplier = "SELECT ID FROM fornitori";
            try (PreparedStatement selectSupplierStatement = connection.prepareStatement(querySupplier);
                 ResultSet shoesResultSet = selectSupplierStatement.executeQuery()) {
                while (shoesResultSet.next()) {
                    int supplierId = shoesResultSet.getInt("ID");
                    supplierIdSet.add(supplierId);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            for (int supplierId : supplierIdSet) {
                if (supplierIdSet.contains(order.getId())) {
                    insertOrderStatement.setInt(1, order.getId());
                    insertOrderStatement.setDate(2, (Date) order.getOrderDate());

                    if (insertOrderStatement.executeUpdate() > 0) {
                        ResultSet generatedKeys = insertOrderStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            orderID = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Error getting the generated order ID.");
                        }
                    } else {
                        throw new SQLException("Error inserting the order.");
                    }
                }
            }

            Set<Integer> shoesIdSet = new HashSet<>();
            String queryShoes = "SELECT ID FROM scarpe";
            try (PreparedStatement selectShoesStatement = connection.prepareStatement(queryShoes);
                 ResultSet shoesResultSet = selectShoesStatement.executeQuery()) {
                while (shoesResultSet.next()) {
                    int shoesId = shoesResultSet.getInt("ID");
                    shoesIdSet.add(shoesId);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

            for (Shoes shoes : shoesList) {
                int shoesIdToInsert = shoes.getID();
                if (shoesIdSet.contains(shoesIdToInsert)) {
                    insertOrderDetailsStatement.setInt(1, orderID);
                    insertOrderDetailsStatement.setInt(2, shoesIdToInsert);
                    insertOrderDetailsStatement.setInt(3, shoes.getQuantity());
                    insertOrderDetailsStatement.addBatch();
                }
            }

            int[] rowsUpdated = insertOrderDetailsStatement.executeBatch();
            for (int rows : rowsUpdated) {
                if (rows <= 0) {
                    throw new SQLException("Error inserting order details.");
                }
            }

            for (Shoes shoes : shoesList) {
                int shoesIdInserted = shoes.getID();
                int orderedQuantity = shoes.getQuantity();

                if (shoesIdSet.contains(shoesIdInserted)) {
                    int currentQuantity;
                    getShoesQuantity.setInt(1, shoesIdInserted);
                    ResultSet resultSet = getShoesQuantity.executeQuery();
                    if (resultSet.next()) {
                        currentQuantity = resultSet.getInt("quantita");
                    } else {
                        throw new SupplierOrderException("Error getting the quantity");
                    }

                    int newQuantity = currentQuantity + orderedQuantity;
                    updateShoesStatement.setInt(1, newQuantity);
                    updateShoesStatement.setInt(2, shoesIdInserted);
                    updateShoesStatement.addBatch();
                }
            }

            int[] rowsUpdatedInventory = updateShoesStatement.executeBatch();
            for (int rows : rowsUpdatedInventory) {
                if (rows <= 0) {
                    throw new SQLException("Error updating inventory.");
                }
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                this.connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println(rollbackException.getMessage());
            }
            throw new SupplierOrderException("Error during the order.");

        } finally {
            close();
        }
    }

    public ArrayList<Customer> getCustomerDatabase() {

        ArrayList<Customer> customerList = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "")) {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM clienti");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int customerId = resultSet.getInt("ID");
                String firstName = resultSet.getString("nome");
                String lastName = resultSet.getString("cognome");
                int loyaltyMember = resultSet.getInt("tesserato");
                Customer customer = new Customer(customerId, firstName, lastName, loyaltyMember);

                customerList.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customerList;
    }

    public boolean registerCustomer(String firstName, String lastName, int loyaltyMember) {

        String insertNewCustomer = "INSERT INTO clienti(nome, cognome, tesserato) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement insertStatement = connection.prepareStatement(insertNewCustomer)) {

            insertStatement.setString(1, firstName);
            insertStatement.setString(2, lastName);
            insertStatement.setInt(3, loyaltyMember);
            insertStatement.addBatch();

            int[] rowsInserted = insertStatement.executeBatch();
            for (int rows : rowsInserted) {
                if (rows <= 0) {
                    throw new SQLException("Error inserting customer data");
                } else {
                    return true;
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
        return false;
    }

    // Process a customer sale and insert data into the table
    public boolean customerSales(@NotNull ArrayList<Shoes> soldShoesList, Customer customer, Date saleDate) throws SQLException {
        boolean success = false;

        String queryCheckQuantity = "SELECT quantita FROM scarpe WHERE ID = ?";
        String queryUpdateQuantity = "UPDATE scarpe SET quantita = ? WHERE id = ?";
        String queryInsertSale = "INSERT INTO venditeindividuali (prodotto_id, quantita, prezzo_totale, cliente_id) VALUES (?, ?, ?, ?)";
        String queryInsertCustomerSale = "INSERT INTO venditeclienti (cliente_id, data_vendita) VALUES (?,?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement checkQuantity = connection.prepareStatement(queryCheckQuantity);
             PreparedStatement updateQuantity = connection.prepareStatement(queryUpdateQuantity);
             PreparedStatement insertIndividualSale = connection.prepareStatement(queryInsertSale);
             PreparedStatement insertCustomerSale = connection.prepareStatement(queryInsertCustomerSale)) {

            connection.setAutoCommit(false);

            for (Shoes shoes : soldShoesList) {
                checkQuantity.setInt(1, shoes.getID());

                ResultSet resultSet = checkQuantity.executeQuery();
                int quantity;
                if (resultSet.next()) {
                    quantity = resultSet.getInt("quantita");

                    if (quantity <= 0) {
                        throw new SQLException("Quantity not available in the store");
                    } else {
                        int quantitySold = shoes.getQuantity();
                        int newQuantity = quantity - quantitySold;

                        updateQuantity.setInt(1, newQuantity);
                        updateQuantity.setInt(2, shoes.getID());
                        updateQuantity.addBatch();
                    }
                }
            }

            int[] rowsUpdatedQuantity = updateQuantity.executeBatch();
            for (int rows : rowsUpdatedQuantity) {
                if (rows <= 0) {
                    throw new SQLException("Error inserting quantity into the shoes table");
                } else {
                    success = true;
                    System.out.println("Insertion into the shoes table successful");
                }
            }

            for (Shoes shoes : soldShoesList) {
                insertIndividualSale.setInt(1, shoes.getID());
                insertIndividualSale.setInt(2, shoes.getQuantity());
                insertIndividualSale.setFloat(3, shoes.getSellingPrice());
                insertIndividualSale.setInt(4, customer.getId());
                insertIndividualSale.addBatch();
            }

            int[] rowsInsertedSale = insertIndividualSale.executeBatch();
            for (int rows : rowsInsertedSale) {
                if (rows <= 0) {
                    throw new SQLException("Error inserting individual sale data");
                }
            }

            insertCustomerSale.setInt(1, customer.getId());
            insertCustomerSale.setDate(2, new java.sql.Date(saleDate.getTime()));
            insertCustomerSale.addBatch();

            int[] rowsInsertedCustomerSale = insertCustomerSale.executeBatch();
            for (int rows : rowsInsertedCustomerSale) {
                if (rows <= 0) {
                    throw new SQLException("Error inserting customer sale data");
                }
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException rollbackException) {
                System.out.println(rollbackException.getMessage());
            }
            throw new SQLException("Error during the sale transaction.");
        } finally {
            close();
        }

        return success;
    }

    public ArrayList<String> getBrandShoeDB() {
        ArrayList<String> shoeBrandsList = new ArrayList<>();
        String query = "SELECT nomeMarca FROM marcascarpe";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String brand = resultSet.getString("nomeMarca");
                shoeBrandsList.add(brand);
            }

            if (shoeBrandsList.isEmpty()) {
                throw new SQLException("The list of shoe brands is empty");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return shoeBrandsList;
    }

    public ArrayList<String> getModelShoeDB() {
        ArrayList<String> shoeModelsList = new ArrayList<>();
        String query = "SELECT modello FROM modelloscarpe";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String model = resultSet.getString("modello");
                shoeModelsList.add(model);
            }

            if (shoeModelsList.isEmpty()) {
                throw new SQLException("The list of shoe models is empty");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return shoeModelsList;
    }

    public ArrayList<String> getTypeShoeDB() {
        ArrayList<String> shoeTypesList = new ArrayList<>();
        String query = "SELECT tipo FROM tiposcarpe";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String type = resultSet.getString("tipo");
                shoeTypesList.add(type);
            }

            if (shoeTypesList.isEmpty()) {
                throw new SQLException("The list of shoe types is empty");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return shoeTypesList;
    }

    public ArrayList<String> getColorShoeDB() {
        ArrayList<String> shoeColorsList = new ArrayList<>();
        String query = "SELECT colore FROM colorescarpe";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String color = resultSet.getString("colore");
                shoeColorsList.add(color);
            }

            if (shoeColorsList.isEmpty()) {
                throw new SQLException("The list of shoe colors is empty");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return shoeColorsList;
    }

    // This method registers a new shoe in the "scarpe" table, including entries in related tables
    public void registerNewShoe(String shoeBrand, String shoeModel, String shoeColor, String shoeType, float size, float price, float purchasePrice) {

        String queryBrandShoes = "INSERT INTO marcascarpe (nomemarca) VALUES (?)";
        String queryModelShoes = "INSERT INTO modelloscarpe (modello) VALUES (?)";
        String queryColorShoes = "INSERT INTO colorescarpe (colore) VALUES (?)";
        String queryTypeShoes = "INSERT INTO tiposcarpe (tipo) VALUES (?)";
        String query = "INSERT INTO scarpe (Id_marca, Id_modello, taglia, Id_colore, Id_tipo, prezzo, prezzo_acquisto) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement insertBrandShoes = connection.prepareStatement(queryBrandShoes, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertModelShoes = connection.prepareStatement(queryModelShoes, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertColorShoes = connection.prepareStatement(queryColorShoes, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertTypeShoes = connection.prepareStatement(queryTypeShoes, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement insertNewShoe = connection.prepareStatement(query)
        ) {

            int brandId, modelId, colorId, typeId;

            connection.setAutoCommit(false);

            // Insert brand into "shoebrands" table
            insertBrandShoes.setString(1, shoeBrand);
            if (insertBrandShoes.executeUpdate() > 0) {
                ResultSet generatedKeys = insertBrandShoes.getGeneratedKeys();
                if (generatedKeys.next()) {
                    brandId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    throw new SQLException("Error getting ID for -Shoe Brand-");
                }
            } else {
                throw new SQLException("Error inserting into -Shoe Brand- table");
            }

            // Insert model into "shoemodels" table
            insertModelShoes.setString(1, shoeModel);
            if (insertModelShoes.executeUpdate() > 0) {
                ResultSet generatedKeys = insertModelShoes.getGeneratedKeys();
                if (generatedKeys.next()) {
                    modelId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    throw new SQLException("Error getting ID for -Shoe Model-");
                }
            } else {
                throw new SQLException("Error inserting into -Shoe Model- table");
            }

            // Insert color into "shoecolors" table
            insertColorShoes.setString(1, shoeColor);
            if (insertColorShoes.executeUpdate() > 0) {
                ResultSet generatedKeys = insertColorShoes.getGeneratedKeys();
                if (generatedKeys.next()) {
                    colorId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    throw new SQLException("Error getting ID for -Shoe Color-");
                }
            } else {
                throw new SQLException("Error inserting into -Shoe Color- table");
            }

            // Insert type into "shoetypes" table
            insertTypeShoes.setString(1, shoeType);
            if (insertTypeShoes.executeUpdate() > 0) {
                ResultSet generatedKeys = insertTypeShoes.getGeneratedKeys();
                if (generatedKeys.next()) {
                    typeId = generatedKeys.getInt(1);
                } else {
                    connection.rollback();
                    throw new SQLException("Error getting ID for -Shoe Type-");
                }
            } else {
                throw new SQLException("Error inserting into -Shoe Type- table");
            }

            // Insert data into "shoes" table
            insertNewShoe.setInt(1, brandId);
            insertNewShoe.setInt(2, modelId);
            insertNewShoe.setFloat(3, size);
            insertNewShoe.setInt(4, colorId);
            insertNewShoe.setInt(5, typeId);
            insertNewShoe.setFloat(6, price);
            insertNewShoe.setFloat(7, purchasePrice);

            // Execute the batch; if it fails, roll back
            int rowsUpdatedNewShoe = insertNewShoe.executeUpdate();

            if (rowsUpdatedNewShoe > 0) {
                System.out.println("Successfully inserted into the -Shoes- table");
                connection.commit();
            } else {
                connection.rollback();
                throw new SQLException("Error inserting into the -New Shoe- in the -Shoes- table");
            }

            connection.commit();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }
    }


    // This method filters sales by passing two date parameters
    public ArrayList<CustomerSales> getFilteredSales(@NotNull Date fromDate, @NotNull Date toDate) {

        ArrayList<CustomerSales> filteredList = new ArrayList<>();
        String query = "SELECT * FROM venditeclienti WHERE data_vendita BETWEEN ? AND ?";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setDate(1, new java.sql.Date(fromDate.getTime()));
            preparedStatement.setDate(2, new java.sql.Date(toDate.getTime()));

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ArrayList<CustomerSales> customerSales = getCustomerSales();
                for (CustomerSales cs : customerSales) {
                    if (cs.getSaleDate().equals(resultSet.getDate("data_vendita"))) {
                        filteredList.add(cs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return filteredList;
    }

    // This method retrieves customer sales from the database, used by the getFilteredSales method to filter sales
    public ArrayList<CustomerSales> getCustomerSales() {
        ArrayList<CustomerSales> customerSalesList = new ArrayList<>();
        String query = "SELECT ID, prodotto_id, quantita, prezzo_totale, cliente_id FROM venditeindividuali";
        String queryGetData = "SELECT data_vendita FROM venditeclienti";
        Date saleDate;
        CustomerSales customerSale;
        Map<Integer, Date> idDateMap = new HashMap<>();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/negozioscarpe", "root", "");
             PreparedStatement statement = connection.prepareStatement(query);
             PreparedStatement preparedStatement = connection.prepareStatement(queryGetData);
             ResultSet resultSet = statement.executeQuery(query);
             ResultSet resultSetGetData = preparedStatement.executeQuery()) {

            while (resultSetGetData.next() & resultSet.next()) {
                int saleId = resultSet.getInt("ID");
                saleDate = resultSetGetData.getDate("data_vendita");
                idDateMap.put(saleId, saleDate);

                int productId = resultSet.getInt("prodotto_id");
                int quantity = resultSet.getInt("quantita");
                float totalPrice = resultSet.getFloat("prezzo_totale");
                int customerId = resultSet.getInt("cliente_id");

                saleDate = idDateMap.get(saleId);

                customerSale = new CustomerSales(saleId, productId, quantity, totalPrice, saleDate, customerId);
                customerSalesList.add(customerSale);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            close();
        }

        return customerSalesList;
    }

}