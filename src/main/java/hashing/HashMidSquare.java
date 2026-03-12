package hashing;

/**
 * Implementação da função de hash utilizando o método Mid-Square.
 *
 * O método Mid-Square consiste em elevar a chave ao quadrado e
 * extrair os dígitos centrais do resultado para gerar o valor hash.
 * O valor final é então ajustado ao tamanho da tabela (capacidade).
 */
public class HashMidSquare implements FuncaoHash {

    /**
     * Calcula o hash de uma chave numérica utilizando o método Mid-Square.
     *
     * @param input chave numérica
     * @param capacidade tamanho da tabela hash
     * @return índice calculado dentro do intervalo da tabela
     */
    @Override
    public int hash(long input, int capacidade) {
        long quadrado = input * input;
        
        long meio = quadrado / 100000L; 
        
        return Math.abs((int) (meio % capacidade));
    }

    /**
     * Calcula o hash de uma chave String convertendo-a primeiro
     * para um valor numérico.
     *
     * @param input chave textual
     * @param capacidade tamanho da tabela hash
     * @return índice calculado dentro do intervalo da tabela
     * @throws IllegalArgumentException se a chave for null
     */
    @Override
    public int hash(String key, int capacidade) {
        if (key == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        long hashComoNumero = converterStringParaInt(key);

        return hash(hashComoNumero, capacidade);
    }

    private int converterStringParaInt(String chave) {
        int soma = 0;
        for (int i = 0; i < chave.length(); i++) {
            soma += chave.charAt(i);
        }
        return Math.abs(soma); 
    }
}