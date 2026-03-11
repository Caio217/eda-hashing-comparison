package hashing;

/**
* Implementação de uma função hash utilizando o método da divisão.
*
* Essa classe fornece uma função hash para chaves numéricas (long)
* e chaves do tipo String. Para String, a chave é convertida para
* um número somando os valores ASCII de cada caractere antes de aplicar
* a função hash por divisão.
*/
public class HashDivisao implements FuncaoHash {

    /**
    * Calcula o índice da tabela hash para uma chave numérica (long)
    * utilizando o método da divisão.
    *
    * O valor absoluto da chave é utilizado para evitar índices negativos.
    *
    * @param input chave numérica que será transformada em índice
    * @param capacidade tamanho da tabela hash
    * @return índice calculado na tabela hash
    */
    @Override
    public int hash(long input, int capacidade) {
        long chavePositiva = Math.abs(input); 
        return (int) (chavePositiva % capacidade);
    }

    /**
    * Calcula o índice da tabela hash para uma chave do tipo String.
    *
    * A chave é convertida para um valor numérico somando os valores ASCII
    * de cada caractere, e então aplica-se a função hash por divisão.
    *
    * @param input chave em formato de texto
    * @param capacidade tamanho da tabela hash
    * @return índice calculado na tabela hash
    * @throws IllegalArgumentException se a chave for null
    */
    @Override
    public int hash(String input, int capacidade) {
        if(input == null)
            throw new IllegalArgumentException("Chave nao pode ser null!");
        
        long hashComoNumero = converterStringParaInt(input);

        return hash(hashComoNumero, capacidade);
    }

    /**
    * Converte uma chave do tipo String para um valor inteiro positivo.
    *
    * A conversão é realizada somando os valores ASCII de cada caractere da String.
    *
    * @param chave String que será convertida para um valor numérico
    * @return valor inteiro positivo correspondente à soma dos caracteres
    */
    private int converterStringParaInt(String chave) {
        int soma = 0;
        for (int i = 0; i < chave.length(); i++) {
            soma += chave.charAt(i);
        }
        return Math.abs(soma); 
    }
}
