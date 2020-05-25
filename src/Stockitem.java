public class Stockitem {
    //Variabelen aanmaken
    private int stockItemID;
    private String description;
    private int pickedQuantity;

    public Stockitem(int StockItemID, String Description, int PickedQuantity) {
        //Ingevoerde gegevens opslaan
        this.stockItemID = StockItemID;
        this.description = Description;
        this.pickedQuantity = PickedQuantity;
    }

    //region Getters
    //Functies om de variabelen op te halen
    public int getStockItemID() {
        return stockItemID;
    }

    public String getDescription() {
        return description;
    }

    public int getPickedQuantity() {
        return pickedQuantity;
    }
    //endregion
}
