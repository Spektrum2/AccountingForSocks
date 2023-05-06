package sky.pro.accountingforsocks.exception;

public class SocksNotFoundException extends RuntimeException{
    private final String color;
    private final int cottonPart;

    public SocksNotFoundException(String color, int cottonPart) {
        this.color = color;
        this.cottonPart = cottonPart;
    }

    public String getColor() {
        return color;
    }

    public int getCottonPart() {
        return cottonPart;
    }
}
