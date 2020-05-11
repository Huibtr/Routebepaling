public class Quantity {
    private String StockItemName;
    private int StockItemID;

    public Quantity( int StockItemID, String StockItemName) {
        this.StockItemName = StockItemName;
        this.StockItemID = StockItemID;
    }

    //region Getters
    public String getStockItemName() {
        return StockItemName;
    }

    public int getStockItemID() {
        return StockItemID;
    }
    //endregion
}
