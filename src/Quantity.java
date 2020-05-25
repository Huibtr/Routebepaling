public class Quantity {
    //Variabelen aanmaken
    private String StockItemName;
    private int StockItemID;

    public Quantity( int StockItemID, String StockItemName) {
        //Ingevoerde gegevens opslaan
        this.StockItemName = StockItemName;
        this.StockItemID = StockItemID;
    }

    //region Getters
    //Functies om de variabelen op te halen
    public String getStockItemName() {
        return StockItemName;
    }

    public int getStockItemID() {
        return StockItemID;
    }
    //endregion
}
