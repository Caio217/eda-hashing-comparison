package hashing;

public class HashMidSquare implements FuncaoHash {

    @Override
    public int hash(long input, int capacidade) {
        long quadrado = input * input;
        
        long meio = quadrado / 100000L; 
        
        return Math.abs((int) (meio % capacidade));
    }

    @Override
    public int hash(String input, int capacidade) {
        if (input == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        long hashComoNumero = input.hashCode() & 0x7fffffff;

        return hash(hashComoNumero, capacidade);
    }
}