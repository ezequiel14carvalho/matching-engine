public class Order {
    // Esses enums servem para gantir que não haja erros de digitação
    public enum Side { BUY, SELL }
    public enum Type { LIMIT, MARKET, PEG }

    private String id;
    private Type type;
    private Side side;
    private double price;
    private int quantity;
    private boolean isPegged;

    // Construtor
    public Order(String id, Type type, Side side, double price, int quantity) {
        this.id = id;
        this.type = type;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.isPegged = (type == Type.PEG);
    }

    // Getters
    public String getId() { return id; }
    public Type getType() { return type; }
    public Side getSide() { return side; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public boolean isPegged() { return isPegged; }

    // Setters (serão úteis para as ordens Pegged e parciais)
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setPrice(double price) { this.price = price; }

    // Sobrescrevi por motivos de vizualização clara no terminal
    @Override
    public String toString() {
        return quantity + " @ " + price;
    }
}
