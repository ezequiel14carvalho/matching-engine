import java.util.Collections;
import java.util.HashMap;
import java.util.Queue;
import java.util.TreeMap;

public class OrderBook {
    // Compras: Ordenadas do maior para o menor
    private TreeMap<Double, Queue<Order>> bids;

    // Vendas: Ordenadas do menor para o maior (a inversão é feita no contructor)
    private TreeMap<Double, Queue<Order>> asks;

    // HashMap garante buscas rápidas de ordens O(1)
    private HashMap<String, Order> orderMap;

    public OrderBook() {
        this.bids = new TreeMap<>(Collections.reverseOrder());
        this.asks = new TreeMap<>();
        this.orderMap = new HashMap<>();
    }
    
}
