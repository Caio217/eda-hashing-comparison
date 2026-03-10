package hashing;

public class HashDJB2 implements FuncaoHash {

    @Override
    public int hash(String input, int capacidade) {
        if (input == null)
            throw new IllegalArgumentException("Chave nao pode ser null!");

        long hash = 5381;

        for (int i = 0; i < input.length(); i++) {
            hash = ((hash << 5) + hash) + input.charAt(i);
        }
        
        hash = hash & 0x7fffffff; 

        return (int) (hash % capacidade);
    }

    @Override
    public int hash(long input, int capacidade) {
        return hash(String.valueOf(input), capacidade);
    }
}

