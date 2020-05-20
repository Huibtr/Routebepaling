public class Route {
    private String name;
    private String adress;
    private String stad;
    private int orderId;
    private int customerID;

    public Route(String name, String adress, String stad, int customerID){
        this.name = name;
        this.adress = adress;
        this.stad = stad;
        this.customerID = customerID;

    }

    public String getName() {
        return name;
    }

    public String getAdress() {
        return adress;
    }

    public String getStad() {
        return stad;
    }

    public int getCustomerID() {
        return customerID;
    }
}
