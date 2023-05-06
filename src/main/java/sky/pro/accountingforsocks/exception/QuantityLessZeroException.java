package sky.pro.accountingforsocks.exception;

public class QuantityLessZeroException extends RuntimeException{
    private final int quantityNeed;
    private final int quantity;

    public QuantityLessZeroException(int quantityNeed, int quantity) {
        this.quantityNeed = quantityNeed;
        this.quantity = quantity;
    }

    public int getQuantityNeed() {
        return quantityNeed;
    }

    public int getQuantity() {
        return quantity;
    }
}
