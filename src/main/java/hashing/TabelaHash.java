package hashing;

import java.util.*;

/**
* Implementação de uma tabela hash utilizando endereçamento aberto
* com sondagem linear para resolução de colisões.
*
* A tabela armazena pares chave-valor e utiliza uma função hash
* configurável para calcular o índice inicial.
*
* Quando o fator de carga é atingido, a tabela é redimensionada
* para o próximo número primo maior que o dobro da capacidade atual.
*
* @param <K> tipo da chave
* @param <V> tipo do valor armazenado
*/
public class TabelaHash<K, V> {

    /**
    * Array que armazena os elementos da tabela hash.
    */
    private Entry<K, V>[] tabela;
    
    /**
    * Número atual de elementos armazenados na tabela.
    */
    private int size;
    
    /**
    * Fator de carga máximo permitido antes do redimensionamento.
    */
    private double fatorDeCarga;
    
    /**
    * Número de colisões ocorridas durante operações de inserção.
    */
    private int colisoesPut;
    
    /**
    * Número de colisões ocorridas durante operações de busca.
    */
    private int colisoesGet;
        
    /**
    * Número de colisões ocorridas durante operações de remoção.
    */
    private int colisoesRemove;    
    
    /**
    * Função hash utilizada para calcular os índices da tabela.
    */
    private FuncaoHash funcaoHash;

    /**
    * Capacidade inicial padrão da tabela hash (número primo)
    */
    public static final int CAPACIDADE_DEFAULT = 11;

    /**
    * Fator de carga padrão da tabela hash.
    */
    public static final double FATOR_DE_CARGA_DEFAULT = 0.75;
    
    /**
    * Entrada especial utilizada para marcar posições removidas na 
    * tabela.
    */
    private final Entry<K, V> APAGADO = new Entry<>(null, null);

    /**
    * Cria uma tabela hash utilizando capacidade e fator de carga padrão.
    *
    * @param funcao função hash utilizada para calcular os índices
    */
    public TabelaHash(FuncaoHash funcao) {
        this(CAPACIDADE_DEFAULT, FATOR_DE_CARGA_DEFAULT, funcao);
    }
    
    /**
    * Cria uma tabela hash com capacidade inicial e fator de carga definidos.
    *
    * @param capacidade tamanho inicial da tabela
    * @param fator fator de carga máximo permitido antes do redimensionamento
    * @param funcao função hash utilizada para calcular os índices
    * @throws IllegalArgumentException se o fator de carga for inválido
    */
    public TabelaHash(int capacidade, double fator, FuncaoHash funcao){
        if (fator <= 0 || fator >= 1)
            throw new IllegalArgumentException("Fator inválido!");

        this.tabela = (Entry<K, V>[]) new Entry[capacidade];
        this.fatorDeCarga = fator;
        this.funcaoHash = funcao;
        this.colisoesPut = 0;
        this.colisoesGet = 0;
        this.colisoesRemove = 0;
        this.size = 0;
    }
    /**
    * Calcula o índice da tabela hash para uma determinada chave.
    * O método verifica o tipo da chave e aplica a função hash apropriada.
    *
    * @param chave chave utilizada para calcular o índice
    * @return índice correspondente na tabela
    */
    private int hash(K chave) {
        if (chave instanceof Long) {
            return funcaoHash.hash((Long) chave, this.tabela.length);
        } 
        else if (chave instanceof String) {
            return funcaoHash.hash((String) chave, this.tabela.length);
        } 
        else if (chave instanceof Integer) {
            return funcaoHash.hash(((Integer) chave).longValue(), this.tabela.length);
        } 
        else {
            return funcaoHash.hash(chave.toString(), this.tabela.length);
        }
    }

    /**
    * Busca o valor associado a uma chave na tabela hash.
    *
    * O método utiliza sondagem linear para resolver colisões.
    *
    * @param chave chave cujo valor será buscado
    * @return valor associado à chave ou null se a chave não existir
    */
    public V get(K chave) {
        int sondagem = 0;
       
        while (sondagem < this.tabela.length) {
            int index = (hash(chave) + sondagem) % this.tabela.length;
            Entry<K, V> atual = this.tabela[index];

            if (atual == null)
                return null;
            if (atual != APAGADO && atual.chave.equals(chave))
                return atual.valor;
                
            sondagem++;
            this.colisoesGet++;
        }   

        return null;
    }  

    /**
    * Insere um novo par chave-valor na tabela hash ou atualiza
    * o valor caso a chave já exista.
    *
    * Caso o fator de carga seja atingido, a tabela será redimensionada.
    *
    * @param chave chave a ser inserida ou atualizada
    * @param valor valor associado à chave
    */
    public void put(K chave, V valor) {
        if ((double) size / this.tabela.length >= this.fatorDeCarga) 
             resize();

        int sondagem = 0;

        while(sondagem < this.tabela.length){
            int index = (hash(chave) + sondagem) % this.tabela.length;
            Entry<K, V> atual = this.tabela[index];
    
            if(atual == null || atual == APAGADO){
                this.tabela[index] = new Entry<>(chave, valor);
                size++;
                return;
            }
            
            if(atual.chave.equals(chave)){
                this.tabela[index].valor = valor;
                return;
            }
   
            sondagem++;
            this.colisoesPut++;
        }
    }  
    
    /**
    * Remove uma chave da tabela hash.
    *
    * A remoção é realizada utilizando marcação de posição apagada pela 
    * variável APAGADO para manter o funcionamento da sondagem linear.
    *
    * @param chave chave a ser removida
    * @return valor associado à chave removida ou null se a chave não existir
    */          
    public V remove(K chave) {
        int sondagem = 0;
        
        while (sondagem < this.tabela.length) {
            int index = (hash(chave) + sondagem) % this.tabela.length;
            Entry<K, V> atual = this.tabela[index];        
    
            if(atual == null) 
                return null;

            if (atual != APAGADO && atual.chave.equals(chave)) {
                V valor = atual.valor;
                this.tabela[index] = APAGADO;
                this.size--;
                return valor;
            } 

            sondagem++;
            this.colisoesRemove++;
        }

        return null;
    }
    
    /**
    * Redimensiona a tabela hash para uma nova capacidade.
    *
    * A nova capacidade é o próximo número primo maior que o
    * dobro da capacidade atual. Todos os elementos válidos
    * da tabela antiga são reinseridos na nova tabela.
    */
    private void resize(){
        Entry<K, V>[] tabelaAntiga = this.tabela;
        int novaCapacidade = proximoPrimo(tabelaAntiga.length * 2);

        this.tabela = (Entry<K, V>[]) new Entry[novaCapacidade];
        this.size = 0;

        for(Entry<K, V> entry : tabelaAntiga)
            if(entry != null && entry != APAGADO)
                put(entry.chave, entry.valor);
    }
    
    /**
    * Calcula o próximo número primo maior ou igual a n.
    *
    * @param n número inicial
    * @return próximo número primo
    */
    private int proximoPrimo(int n) {
        while (!ehPrimo(n)) 
            n++;
        
        return n;
    }

    /**
    * Verifica se um número é primo.
    *
    * @param n número a ser verificado
    * @return true se o número for primo, false caso contrário
    */
    private boolean ehPrimo(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0) return false;
        
        return true;
    }

    /**
    * Retorna a quantidade de elementos armazenados na tabela.
    *
    * @return número de pares chave-valor armazenados
    */
    public int size() {
        return this.size;
    }

    /**
    * Verifica se a tabela hash está vazia.
    *
    * @return true se não houver elementos armazenados
    */
    public boolean isEmpty(){
        return this.size == 0;
    }

    /**
    * Retorna o número de colisões ocorridas durante operações de busca.
    *
    * @return número de colisões em operações get
    */
    public int getColisoesGet() {
        return this.colisoesGet;
    }

    /**
    * Retorna o número de colisões ocorridas durante inserções.
    *
    * @return número de colisões em operações put
    */
    public int getColisoesPut() {
        return this.colisoesPut;
    }
    
    /**
    * Retorna o número de colisões ocorridas durante remoções.
    *
    * @return número de colisões em operações remove
    */
    public int getColisoesRemove() {
        return this.colisoesRemove;
    }

    /**
    * Retorna o número total de colisões registradas na tabela.
    *
    * @return soma das colisões de inserção, busca e remoção
    */
    public int getColisoesTotal() {
        return this.colisoesGet + this.colisoesPut + this.colisoesRemove;
    }
    
    /**
    * Retorna uma representação textual da tabela hash contendo
    * os índices ocupados e seus respectivos pares chave-valor.
    *
    * @return representação em String da tabela
    */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (int i = 0; i < this.tabela.length; i++)
            if(this.tabela[i] != null && this.tabela[i] != APAGADO)
                sb.append(i).append("= ").append(this.tabela[i].toString()).append(", ");
        
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }
    
    /**
    * Imprime estatísticas sobre o estado atual da tabela hash,
    * incluindo quantidade de buckets ocupados, vazios e apagados,
    * além da taxa de ocupação da tabela.
    */
    public void imprimirEstatisticas() {
        int ocupados = 0;
        int vazios = 0;
        int apagados = 0;

        for (int i = 0; i < this.tabela.length; i++) {
            if (this.tabela[i] == null) { 
                vazios++;
            } else if (this.tabela[i] == APAGADO) {
                apagados++;
            } else {
                ocupados++;
            }
        }

        System.out.println("\n--- ESTATÍSTICAS DA TABELA HASH ---");
        System.out.println("Capacidade Total (Buckets): " + tabela.length);
        System.out.println("Buckets Ocupados (Válidos): " + ocupados);
        System.out.println("Buckets Apagados (Fantasmas): " + apagados);
        System.out.println("Buckets Vazios (Null): " + vazios);
        System.out.println("--- COLISÕES / SALTOS (Sondagem Aberta) ---");
        System.out.println("Durante Inserções (Put): " + colisoesPut);
    
        double taxaOcupacaoFisica = ((double) (ocupados + apagados) / this.tabela.length) * 100;
        System.out.printf("Taxa de Ocupação Real (Física): %.2f%%\n", taxaOcupacaoFisica);
    }
    
    /**
    * Representa um par chave-valor armazenado na tabela hash.
    *
    * Cada objeto Entry mantém uma chave do tipo K e o valor correspondente do tipo V.
    * É utilizada pela TabelaHash para guardar elementos nos buckets.
    *
    * @param <K> tipo da chave
    * @param <V> tipo do valor
    */
    private static class Entry<K, V> {
        
        /**
        * Chave associada a este elemento da tabela hash.
        */
        K chave;
            
        
        /**
        * Valor armazenado associado à chave.
        */
        V valor;
        
        /**
        * Construtor da Entry.
        *
        * @param chave chave do elemento
        * @param valor valor associado à chave
        */
        Entry(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    
        /**
        * Retorna uma representação textual do par chave-valor.
        *
        * @return String contendo chave e valor
        */
        @Override
        public String toString() {
            return chave + ": " + valor;
        }
    }

}
