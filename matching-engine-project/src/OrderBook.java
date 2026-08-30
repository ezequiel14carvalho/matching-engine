import java.util.*;

public class OrderBook {
    // Compras: Ordenadas do maior para o menor (a inversão é feita no constructor)
    private TreeMap<Double, Queue<Order>> bids;

    // Vendas: Ordenadas do menor para o maior
    private TreeMap<Double, Queue<Order>> asks;

    // HashMap garante buscas rápidas de ordens O(1)
    private HashMap<String, Order> orderMap;

    private List<Order> peggedOrders;

    public OrderBook() {
        this.bids = new TreeMap<>(Collections.reverseOrder());
        this.asks = new TreeMap<>();
        this.orderMap = new HashMap<>();
        this.peggedOrders = new ArrayList<>();
    }

    // Insere a ordem no orderMap (para cancelamento/alteração rápidos)
    // e na fila de bids ou asks, de acordo com o lado e preço da ordem
    public void addOrder(Order order) {
        // Salva no mapa para acesso O(1)
        orderMap.put(order.getId(), order);

        // Se for ordem Pegged, inicializa o preço com o topo atual do livro
        if (order.isPegged()) {
            peggedOrders.add(order);
            if (order.getSide() == Order.Side.BUY && !bids.isEmpty()) {
                order.setPrice(bids.firstKey());
            } else if (order.getSide() == Order.Side.SELL && !asks.isEmpty()) {
                order.setPrice(asks.firstKey());
            } else {
                order.setPrice(0.0);
            }
        }

        // Analisa a ordem e coloca no respectivo Side
        TreeMap<Double, Queue<Order>> bookSide = (order.getSide() == Order.Side.BUY) ? bids : asks;

        // Se a fila do preço não existir, cria uma nova fila com esse preço novo vazia.
        bookSide.putIfAbsent(order.getPrice(), new LinkedList<>());

        // Adiciona a ordem no final da sua respectiva fila, para garantir prioridade de tempo
        bookSide.get(order.getPrice()).add(order);
    }

    // Remoção de alguma ordem
    public void removeOrder(String orderId) {
        Order order = orderMap.remove(orderId);

        if (order != null) {
            if (order.isPegged()) {
                peggedOrders.remove(order);
            }

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

    // Requisito adicional 1: Implementar uma função/metodo para visualização do livro
    public void printBook() {
        System.out.println("Ordens de Compra    | Ordens de Venda");
        System.out.println("--------------------|-----------------");

        // O keySet().iterator() pega os preços na ordem que configuramos (Bids decrescente, Asks crescente).
        Iterator<Double> bidIterator = bids.keySet().iterator();
        Iterator<Double> askIterator = asks.keySet().iterator();

        while (bidIterator.hasNext() || askIterator.hasNext()) {
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

                askString = totalQty + " @ " + price;
            }

            System.out.println(bidString + "| " + askString);
        }
    }

    // Requisito adicional 3: Podemos reutilizar a removeOrder
    public void cancelOrder(String id) {
        Order order = orderMap.get(id);
        if (order != null) {
            removeOrder(id);
            System.out.println("Order cancelled");
        } else {
            System.out.println("Order not found");
        }
    }

    // Requisito adicional 4: Removemos a ordem, e adicionamos ela com as modificações
    public void modifyOrder(String id, double newPrice, int newQty, MatchingEngine engine) {
        Order order = orderMap.get(id);
        if (order != null) {
            removeOrder(id);

            order.setPrice(newPrice);
            order.setQuantity(newQty);

            engine.processOrder(order);
            System.out.println("Order modified");
        } else {
            System.out.println("Order not found");
        }
    }

    // Requisito 5: Atualiza o preço das ordens Pegged automaticamente quando o topo do livro muda
    public void updatePeggedPrices() {
        for (Order pOrder : new ArrayList<>(peggedOrders)) {
            if (pOrder.getSide() == Order.Side.BUY && !bids.isEmpty()) {
                double bestBid = bids.firstKey();
                if (pOrder.getPrice() != bestBid) {
                    removeOrder(pOrder.getId());
                    pOrder.setPrice(bestBid);
                    addOrder(pOrder);
                }
            } else if (pOrder.getSide() == Order.Side.SELL && !asks.isEmpty()) {
                double bestAsk = asks.firstKey();
                if (pOrder.getPrice() != bestAsk) {
                    removeOrder(pOrder.getId());
                    pOrder.setPrice(bestAsk);
                    addOrder(pOrder);
                }
            }
        }
    }

    // metodo auxiliar que pega o total das quantidades em uma ordem no mesmo nivel de preço
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
