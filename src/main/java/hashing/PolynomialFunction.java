package hashing;

/**
 * Implementação da Função Hash Polinomial.
 * Utiliza cada caractere da string como coeficiente de um polinômio.
 */
public class PolynomialFunction implements FuncaoHash {
    /** Base polinomial (número primo padrão). */
    private static final int BASE = 31;

    /** Módulo primo para evitar estouro numérico (overflow). */
    private static final long MOD = 1_000_000_007;

    /**
     * Calcula o índice de hash para uma chave do tipo String.
     *
     * @param key       A chave a ser mapeada.
     * @param tableSize O tamanho da Tabela Hash.
     * @return          O índice calculado (entre 0 e tableSize - 1).
     * @throws IllegalArgumentException Se a chave for nula.
     */
    @Override
    public int hash(String key, int tableSize) {
        if (key == null) {
            throw new IllegalArgumentException("Chave não pode ser nula");
        }
        
        long hash = 0;

        for (int i = 0; i < key.length(); i++) {
            hash = (hash * BASE + key.charAt(i)) % MOD;
        }
        
        return Math.abs((int) (hash % tableSize));
    }

    /**
     * Calcula o índice de hash para uma chave numérica.
     * Converte o número para String e aplica a lógica polinomial.
     *
     * @param key       A chave numérica a ser mapeada.
     * @param tableSize O tamanho da Tabela Hash.
     * @return          O índice calculado (entre 0 e tableSize - 1).
     */
    @Override
    public int hash(long key, int tableSize) {
        return hash(String.valueOf(key), tableSize);
    }
}
