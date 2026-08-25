public class Main {
    public static void main(String[] args) {
        System.out.println("=========== Teste ===========");

        OrderBook orderBook = new OrderBook();

        Order buy1 = new Order("ord_1", Order.Type.LIMIT, Order.Side.BUY, 10.4, 100);
        Order buy2 = new Order("ord_2", Order.Type.LIMIT, Order.Side.BUY, 9.99, 150);
        Order sell1 = new Order("ord_3", Order.Type.LIMIT, Order.Side.SELL, 8.99, 80);

        orderBook.addOrder(buy1);
        orderBook.addOrder(buy2);
        orderBook.addOrder(sell1);

        orderBook.printBook();
    }
}
