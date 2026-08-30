public class Main {
    public static void main(String[] args) {
        OrderBook orderBook = new OrderBook();
        MatchingEngine engine = new MatchingEngine(orderBook);

        System.out.println("=========================================");
        System.out.println("=== 1. TESTE DE ORDENS BASE E LIVRO     ===");
        System.out.println("=========================================");

        // Inserindo ordens limite básicas
        engine.processOrder(new Order("o1", Order.Type.LIMIT, Order.Side.BUY, 10.0, 200));
        engine.processOrder(new Order("o2", Order.Type.LIMIT, Order.Side.SELL, 10.5, 100));

        System.out.println("\nLivro Inicial:");
        orderBook.printBook();

        System.out.println("\n=========================================");
        System.out.println("=== 2. TESTE DE ORDEM PEGGED (Req. 5)   ===");
        System.out.println("=========================================");

        System.out.println("Adicionando Pegged Buy Order ('peg1') de 150 (acompanha o melhor Bid atual: 10.0):");
        engine.processOrder(new Order("peg1", Order.Type.PEG, Order.Side.BUY, 0, 150));
        orderBook.printBook();

        System.out.println("\nAdicionando nova ordem: limit buy 10.1 300 (Altera o topo do Bid, o Pegged deve acompanhar):");
        engine.processOrder(new Order("o3", Order.Type.LIMIT, Order.Side.BUY, 10.1, 300));
        orderBook.printBook();

        System.out.println("\n=========================================");
        System.out.println("=== 3. TESTE DE CANCELAMENTO (Req. 3)   ===");
        System.out.println("=========================================");

        System.out.println("Cancelando a ordem 'o1'...");
        orderBook.cancelOrder("o1");

        System.out.println("\nLivro após o cancelamento:");
        orderBook.printBook();

        System.out.println("\n=========================================");
        System.out.println("=== 4. TESTE DE MODIFICAÇÃO (Req. 4)    ===");
        System.out.println("=========================================");

        System.out.println("Modificando a ordem 'o3' de 10.1 para 9.99 (deve perder prioridade e ir para o fim da fila):");
        orderBook.modifyOrder("o3", 9.99, 250, engine);

        System.out.println("\nLivro final após todas as operações:");
        orderBook.printBook();

        System.out.println("\n=========================================");
        System.out.println("=== FIM DOS TESTES COM SUCESSO!       ===");
        System.out.println("=========================================");
    }
}
