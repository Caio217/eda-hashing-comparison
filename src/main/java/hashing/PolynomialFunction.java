package hashing;

public class PolynomialFunction implements FuncaoHash {
    private static final int BASE = 31;
    private static final long MOD = 1_000_000_007;

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

    @Override
    public int hash(long key, int tableSize) {
        return hash(String.valueOf(key), tableSize);
    }
}
