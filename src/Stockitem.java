public class Stockitem {
    private int stockItemID;
    private String description;
    private int pickedQuantity;

    public Stockitem(int StockItemID, String Description, int PickedQuantity) {
        this.stockItemID = StockItemID;
        this.description = Description;
        this.pickedQuantity = PickedQuantity;
    }

    //region Getters
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
