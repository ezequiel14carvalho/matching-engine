import java.util.Queue;

public class MatchingEngine {
    private OrderBook orderBook = new OrderBook();

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    public void processOrder(Order order) {
        if (order.getType() == Order.Type.MARKET) {
            matchMarket(order);
        } else {
            matchLimit(order);
        }
    }

    private void executeMatch(Order incoming, Queue<Order> queue, double price) {
        int tradedTotal = 0;
        while (incoming.getQuantity() > 0 && !queue.isEmpty()) {
            Order resting = queue.peek();

            // necessário pois há casos onde a quantidade de compras é diferente
            // da quantidade de vendas disponiveis, e vice-versa.
            int qty = Math.min(incoming.getQuantity(), resting.getQuantity());

            tradedTotal += qty;
            incoming.setQuantity(incoming.getQuantity() - qty);
            resting.setQuantity(resting.getQuantity() - qty);

            // se não restou nada na nossa ordem, ent~ao removemos ele do noss orderBook
            if (resting.getQuantity() == 0) {
                queue.poll();
                orderBook.removeOrder(resting.getId());
            }
        }
        System.out.println("Trade, price: " + price + ", qty: " + tradedTotal);
    }

    // metodo para execução de ordem tipo market
    private void matchMarket(Order order) {
        if (order.getSide() == Order.Side.BUY) {
            while (order.getQuantity() > 0 && !orderBook.getAsks().isEmpty()) {
                // a primeira chave de uma treemap é a o menor valor
                double bestPrice = orderBook.getAsks().firstKey();

                // agora que temos o melhor preçp/chave
                // vamos pegar a fila correspondente
                Queue<Order> queue = orderBook.getAsks().get(bestPrice);

                // agora executamos o trade
                executeMatch(order, queue, bestPrice);
                // caso a fila do preço/chave ficou vazia, limpamos
                if (queue.isEmpty()) orderBook.getAsks().remove(bestPrice);
            }
        } else {
            // agora a ordem é do tipo sell e atacaremos o bid
            // como invertemos na propia classe, podemos pegar a primeira key
            double bestPrice = orderBook.getBids().firstKey();

            Queue<Order> queue = orderBook.getBids().get(bestPrice);

            executeMatch(order, queue, bestPrice);
            if (queue.isEmpty()) orderBook.getBids().remove(bestPrice);
        }

    }

    private void matchLimit(Order order) {
        if (order.getSide() == Order.Side.BUY) {
            while (order.getQuantity() > 0 && !orderBook.getAsks().isEmpty() && order.getPrice() >= orderBook.getAsks().firstKey()) {
                // Daqui em diante segue a mesma lógica do matchMarket
                double bestPrice = orderBook.getBids().firstKey();
                Queue<Order> queue = orderBook.getBids().get(bestPrice);
                executeMatch(order, queue, bestPrice);
                if (queue.isEmpty()) orderBook.getBids().remove(bestPrice);
            }
        } else {
            while (order.getQuantity() > 0 && !orderBook.getBids().isEmpty() && order.getPrice() <= orderBook.getBids().firstKey()) {
                double bestPrice = orderBook.getBids().firstKey();
                Queue<Order> queue = orderBook.getBids().get(bestPrice);
                executeMatch(order, queue, bestPrice);
                if (queue.isEmpty()) orderBook.getBids().remove(bestPrice);
            }
        }

        // Caso a Order não conseguir um match, adicionamos ao nosso orderBook
        if (order.getQuantity() > 0) orderBook.addOrder(order);
    }
}

