public class MatchingEngine {
    private OrderBook orderBook = new OrderBook();

    public MatchingEngine(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    // metodo para execução de ordem tipo market
    private void executeMarketOrder(Order marketOrder) {
        if (marketOrder.getSide() == Order.Side.BUY) {
            // Ele tentará comprar até que ele venda tudo ou o orderBook de vendas esteja vazio
            while (marketOrder.getQuantity() > 0 && !orderBook.getAsks().isEmpty()) {
                
            }
        }
    }
}
