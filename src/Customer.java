public class Customer {
    private int customerID;
    private String customerName;
    private String cityName;
    private String deliveryAddressLine2;
    private String deliveryPostalCode;
    private String phoneNumber;

    public Customer (int customerID, String customerName,  String cityName, String deliveryAddressLine2, String deliveryPostalCode, String phoneNumber) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.cityName = cityName;
        this.deliveryAddressLine2 = deliveryAddressLine2;
        this.deliveryPostalCode = deliveryPostalCode;
        this.phoneNumber = phoneNumber;
    }

    //region Getters
    public int getCustomerID() {
        return customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCityName() {
        return cityName;
    }

    public String getDeliveryAddressLine2() {
        return deliveryAddressLine2;
    }


    public String getDeliveryPostalCode() {
        return deliveryPostalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    //endregion
}
