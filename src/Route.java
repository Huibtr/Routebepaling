public class Route {
    private String name;
    private String adress;
    private String stad;
    private int orderId;

    public Route(String name, String adress, String stad, int orderId){
        this.name = name;
        this.adress = adress;
        this.stad = stad;
        this.orderId = orderId;

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

    public int getOrderId() {
        return orderId;
    }
}
