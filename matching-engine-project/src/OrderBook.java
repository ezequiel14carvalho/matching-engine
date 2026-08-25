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
    }


    public void removeOrder(String orderId) {
        Order order = orderMap.remove(orderId);

        if (order != null) {
            // Analisa o Side e pega a fila de ordens
            TreeMap<Double, Queue<Order>> bookSide = (order.getSide() == Order.Side.BUY) ? bids : asks;
            Queue<Order> priceQueue = bookSide.get(order.getPrice());

            if (priceQueue != null) {
                // Remove a ordem da fila
                priceQueue.remove(order);
                // Se a fila daquele nível de preço ficar vazia, removemos o preço do livro
                if (priceQueue.isEmpty()) {
                    bookSide.remove(order.getPrice());
                }
            }
        }
    }

    // Getters para a Matching Engine acessar o estado do livro
    public TreeMap<Double, Queue<Order>> getBids() { return bids; }
    public TreeMap<Double, Queue<Order>> getAsks() { return asks; }
    public Order getOrderById(String id) { return orderMap.get(id); }


    // Requisito adicional 1:  Implementar uma função/metodo para visualização do livro
    public void printBook() {
        System.out.println("Ordens de Compra    | Ordens de Venda");
        System.out.println("--------------------|-----------------");

        // O keySet().iterator() pega os preços na ordem que configuramos (Bids descrente, Asks crescente).
        Iterator<Double> bidIterator = bids.keySet().iterator();
        Iterator<Double> askIterator = asks.keySet().iterator();

        while (bidIterator.hasNext() | askIterator.hasNext()) {
            String bidString = "";
            String askString = "";

            if (bidIterator.hasNext()) {
                double price = bidIterator.next();
                int totalQty = getTotalQuantity(bids.get(price));

                // String.format nos garante alinhamento
                bidString = String.format("%-20s", totalQty + " @ " + price);
            } else {
                // Espaço em branco se não tiver mais bids
                bidString = String.format("%-20s", "");
            }

            if (askIterator.hasNext()) {
                double price = askIterator.next();
                int totalQty = getTotalQuantity(asks.get(price));

                // Note que como ele está a diretia, não precisa de formatação
                askString = totalQty + " @ " + price;
            }

            System.out.println(bidString + "| " + askString);
        }
    }

    // metodo auxiliar que pega o total das quantidades em uma ordem nomesmo nivel de preço
    private int getTotalQuantity(Queue<Order> priceQueue) {
        if (priceQueue == null) {
            return 0;
        }
        int total = 0;
        for (Order order : priceQueue) {
            total += order.getQuantity();
        }
        return total;
    }
}
