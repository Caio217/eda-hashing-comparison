package hashing;

/**
* Interface que define uma função hash para diferentes tipos de chave.
* Implementações devem fornecer métodos para calcular o índice de uma chave
* considerando a capacidade da tabela hash.
*/
public interface FuncaoHash {
        
    /**
    * Calcula o índice da tabela hash para uma chave numérica (long).
    *
    * @param key chave numérica a ser processada
    * @param capacidade tamanho da tabela hash
    * @return índice calculado na tabela hash
    */
    int hash(long key, int capacidade);
    
    /**
    * Calcula o índice da tabela hash para uma chave do tipo String.
    *
    * @param key chave em formato de texto
    * @param capacidade tamanho da tabela hash
    * @return índice calculado na tabela hash
    */    
    int hash(String key, int capacidade);
}
