package supplier.orders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SupplierOrders {

    private int id;
    private Date orderDate;

    public SupplierOrders(int id, String orderDate) {
        setId(id);
        setOrderDate(orderDate);
    }

    public Date getOrderDate() {
        return this.orderDate;
    }

    public void setOrderDate(String orderDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            java.util.Date date = dateFormat.parse(orderDate);
            this.orderDate = new java.sql.Date(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String toString() {
        return "SupplierOrder [OrderDate=" + orderDate + "]";
    }
}
