import java.util.*;

public class OrderBook {
    // Compras: Ordenadas do maior para o menor (a inversão é feita no contructor)
    private TreeMap<Double, Queue<Order>> bids;

    // Vendas: Ordenadas do menor para o maior
    private TreeMap<Double, Queue<Order>> asks;

    // HashMap garante buscas rápidas de ordens O(1)
    private HashMap<String, Order> orderMap;

    public OrderBook() {
        this.bids = new TreeMap<>(Collections.reverseOrder());
        this.asks = new TreeMap<>();
        this.orderMap = new HashMap<>();
    }

    // Insere a ordem no orderMap (para cancelamento/alteração rápidos)
    // e na fila de bids ou asks, de acordo com o lado e preço da ordem
    public void addOrder(Order order) {
        // Salva no mapa para acesso O(1)
        orderMap.put(order.getId(), order);

        // Analisa a ordem e coloca no respectivo Side
        TreeMap<Double, Queue<Order>> bookSide = (order.getSide() == Order.Side.BUY) ? bids : asks;

        // Se a fila do preço não existir, cria uma nova fila com esse preço novo vaia.
        bookSide.putIfAbsent(order.getPrice(), new LinkedList<>());

        // Adiciona a ordem no final da sua respectiva fila, para garantir prioridade de tempo
        bookSide.get(order.getPrice()).add(order);

        // Remoção de alguma ordem
        public void removeOrder(String orderId) {
            Order order = oderMap.remove(orderId);

        }
    }
}
