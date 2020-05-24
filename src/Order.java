public class Order {
    //Variabelen aanmaken
    private int CustomerID;
    private int OrderID;

    public Order(int CustomerID, int OrderID) {
        //Ingevoerde gegevens opslaan
        this.CustomerID = CustomerID;
        this.OrderID = OrderID;
    }

    //region Getters
    //Functies om de variabelen op te halen
    public int getCustomerID() {
        return CustomerID;
    }

    public int getOrderID() {
        return OrderID;
    }
    //endregion
}
