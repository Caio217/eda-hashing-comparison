package hashing;

public interface FuncaoHash {
    int hash(long key, int capacidade);
    int hash(String key, int capacidade);
}
