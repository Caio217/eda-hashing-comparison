package hashing;

import java.util.*;

public class TabelaHash<K, V> {

    private Entry<K, V>[] tabela;
    private int size;
    private double fatorDeCarga;
    private int colisoesPut;
    private int colisoesGet;
    private int colisoesRemove;    
    private FuncaoHash funcaoHash;

    public static final int CAPACIDADE_DEFAULT = 11;
    public static final double FATOR_DE_CARGA_DEFAULT = 0.75;

    private final Entry<K, V> APAGADO = new Entry<>(null, null);

    public TabelaHash(FuncaoHash funcao) {
        this(CAPACIDADE_DEFAULT, FATOR_DE_CARGA_DEFAULT, funcao);
    }

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

    private void resize(){
        Entry<K, V>[] tabelaAntiga = this.tabela;
        int novaCapacidade = proximoPrimo(tabelaAntiga.length * 2);

        this.tabela = (Entry<K, V>[]) new Entry[novaCapacidade];
        this.size = 0;

        for(Entry<K, V> entry : tabelaAntiga)
            if(entry != null && entry != APAGADO)
                put(entry.chave, entry.valor);
    }

    private int proximoPrimo(int n) {
        while (!ehPrimo(n)) 
            n++;
        
        return n;
    }

    private boolean ehPrimo(int n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0) return false;
        
        return true;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty(){
        return this.size == 0;
    }

    public int getColisoesGet() {
        return this.colisoesGet;
    }

    public int getColisoesPut() {
        return this.colisoesPut;
    }

    public int getColisoesRemove() {
        return this.colisoesRemove;
    }

    public int getColisoesTotal() {
        return this.colisoesGet + this.colisoesPut + this.colisoesRemove;
    }

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

    private static class Entry<K, V> {
        K chave;
        V valor;

        Entry(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }

        @Override
        public String toString() {
            return chave + ": " + valor;
        }
    }

}
