package hashing;

public class HashDivisao implements FuncaoHash {

    public int hash(long input, int capacidade) {
        long chavePositiva = Math.abs(input); 
        return (int) (chavePositiva % capacidade);
    }

    @Override
    public int hash(String input, int capacidade) {
        if(input == null)
            throw new IllegalArgumentException("Chave nao pode ser null!");

        long hashComoNumero = input.hashCode() & 0x7fffffff;

        return hash(hashComoNumero, capacidade);
    }
}
