package hashing;

public class HashDJB2<T> implements FuncaoHash<T> {

    @Override
    public int hash(T input, int capacidade) {
        if (input == null)
            throw new IllegalArgumentException("Chave null!");

        String chave = input.toString();
        long hash = 5381;

        for (int i = 0; i < chave.length(); i++) {
            hash = ((hash << 5) + hash) + chave.charAt(i);
        }
        //pra garantir que seja positivo
        hash = hash & 0x7fffffff; 

        return (int) (hash % capacidade);
    }
}
