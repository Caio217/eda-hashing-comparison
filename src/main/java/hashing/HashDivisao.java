package hashing;

public class HashDivisao<T> implements FuncaoHash<T> {

    @Override
    public int hash(T input, int capacidade) {
        if(input == null)
            throw new IllegalArgumentException("Chave null!");

        int hash = input.hashCode();
        hash = hash & 0x7fffffff;

        return hash % capacidade;
    }
}
