# Simple Order Matching Engine

Motor de cruzamento de ordens (Matching Engine) desenvolvido em **Java puro**, projetado para simular o funcionamento básico de uma exchange financeira em memória volátil, criado como parte de um processo seletivo para estágio em Engenharia de Software.

---

## 🚀 Decisões de Arquitetura e Engenharia

Para atender ao requisito de eficiência de algoritmos (com complexidade otimizada) sem persistência em banco de dados, o projeto foi estruturado utilizando as seguintes estruturas de dados do ecossistema Java:

*   **`TreeMap<Double, Queue<Order>>` (Bids e Asks):** 
    *   Utilizado para gerenciar os níveis de preço do livro de ofertas. 
    *   O `TreeMap` mantém as chaves (preços) **automaticamente ordenadas**. Para o lado da compra (`bids`), usamos ordem decrescente (`Collections.reverseOrder()`) para que o maior preço fique sempre no topo. Para o lado da venda (`asks`), usamos a ordem natural (crescente). A busca pelo melhor preço é feita em tempo constante **$O(1)$** via `firstKey()`.
*   **`Queue<Order>` (LinkedList):** 
    *   Armazenada como valor em cada nível de preço do `TreeMap`. Garante estritamente a **Prioridade de Tempo (FIFO - First In, First Out)**, assegurando que a primeira ordem a chegar seja a primeira a ser executada.
*   **`HashMap<String, Order>` (Order Map):** 
    *   Mapeia o ID da ordem diretamente ao objeto `Order`. Permite que operações de **cancelamento e modificação** sejam executadas com busca em tempo constante **$O(1)$**, evitando varreduras custosas no livro.

---

## 📦 Funcionalidades Implementadas

1. **Gestão de Ordens Limitadas e a Mercado (Limit & Market Orders):** Suporte completo a ordens passivas e agressoras. Limit orders que cruzam o spread executam imediatamente (comportamento de *Marketable Limit Order*), e o saldo remanescente é enfileirado no livro.
2. **Visualização do Livro (Order Book View):** Método formatado para exibir o livro de ofertas consolidados por preço em duas colunas (`Compra | Venda`).
3. **Cancelamento de Ordens:** Remoção rápida de ordens ativas do livro e do mapa de referência.
4. **Modificação/Alteração de Ordens:** Permite alterar preço e quantidade. Caso o preço seja modificado, a ordem perde prioridade na fila, sendo removida e reinserida corretamente.
5. **Ordens Pegged (Peg to the Bid):** Ordens dinâmicas que acompanham automaticamente o melhor preço de compra do mercado sempre que o topo do livro sofre alterações.

---

Estrutura de arquivos

├── Order.java          # Entidades, Enums (Side, Type) e propriedades da Ordem
├── OrderBook.java      # Gerenciamento do livro de ofertas, TreeMap, HashMap e lógica Pegged
├── MatchingEngine.java # Regras de negócio, cruzamento (Matching) e execução de trades
└── Main.java           # Cenários de teste integrados cobrindo todos os requisitos

---

## 🛠️ Como Compilar e Rodar

Certifique-se de ter o **JDK (Java Development Kit)** instalado em sua máquina.

1. Abra o terminal na pasta raiz onde estão os arquivos `.java` (`Order.java`, `OrderBook.java`, `MatchingEngine.java`, `Main.java`).
2. Compile todos os arquivos com o comando:
   ```bash
   javac *.java
