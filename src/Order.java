public class Order {
    private int CustomerID;
    private int OrderID;

    public Order(int CustomerID, int OrderID) {
        this.CustomerID = CustomerID;
        this.OrderID = OrderID;
    }

    //region Getters
    public int getCustomerID() {
        return CustomerID;
    }

    public int getOrderID() {
        return OrderID;
    }
    //endregion
}
