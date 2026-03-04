package hashing;

import java.util.*;

public class TabelaHashEnderecamentoAberto<K, V> {

    private Entry<K, V>[] tabela;
    private int size;
    private double fatorDeCarga;
    private FuncaoHash<K> funcaoHash;

    public static final int CAPACIDADE_DEFAULT = 11;
    public static final double FATOR_DE_CARGA_DEFAULT = 0.75;

    private final Entry<K, V> APAGADO = new Entry<>(null, null);

    public TabelaHashEnderecamentoAberto(FuncaoHash<K> funcao) {
        this(CAPACIDADE_DEFAULT, FATOR_DE_CARGA_DEFAULT, funcao);
    }

    public TabelaHashEnderecamentoAberto(int capacidade, double fator, FuncaoHash<K> funcao){
        if (fator <= 0 || fator >= 1)
            throw new IllegalArgumentException("Fator inválido!");

        this.tabela = (Entry<K, V>[]) new Entry[capacidade];
        this.fatorDeCarga = fator;
        this.funcaoHash = funcao;
        this.size = 0;
    }

    private int hash(K chave){
        return funcaoHash.hash(chave, tabela.length);
    }

    public V get(K chave) {
        int sondagem = 0;
       
        while (sondagem < tabela.length) {
            int index = (hash(chave) + sondagem) % tabela.length;
            Entry<K, V> atual = tabela[index];

            if (atual == null)
                return null;
            if (atual != APAGADO && atual.chave.equals(chave))
                return atual.valor;
                
            sondagem++;
        }   

        return null;
    }  

    public void put(K chave, V valor) {
        if ((double) size / tabela.length >= fatorDeCarga) 
             resize();

        int sondagem = 0;

        while(sondagem < tabela.length){
            int index = (hash(chave) + sondagem) % tabela.length;
            Entry<K, V> atual = tabela[index];
    
            if(atual == null || atual == APAGADO){
                tabela[index] = new Entry<>(chave, valor);
                size++;
                return;
            }
            
            if(atual.chave.equals(chave)){
                tabela[index].valor = valor;
                return;
            }
   
            sondagem++;
        }
    }  
              
    public V remove(K chave) {
        int sondagem = 0;
        
        while (sondagem < tabela.length) {
            int index = (hash(chave) + sondagem) % tabela.length;
            Entry<K, V> atual = tabela[index];        
    
            if(atual == null) 
                return null;

            if (atual != APAGADO && atual.chave.equals(chave)) {
                V valor = atual.valor;
                tabela[index] = APAGADO;
                this.size--;
                return valor;
            } 
            sondagem++;
        
        }

        return null;
    }

    private void resize(){
        Entry<K, V>[] tabelaAntiga = tabela;
        int novaCapacidade = proximoPrimo(tabelaAntiga.length * 2);

        tabela = (Entry<K, V>[]) new Entry[novaCapacidade];
        size = 0;

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

    private static class Entry<K, V> {
        K chave;
        V valor;

        Entry(K chave, V valor) {
            this.chave = chave;
            this.valor = valor;
        }
    }
}
