package hashing;

public class PolynomialFunction implements FuncaoHash<String> {
    private static final int BASE = 31;
    private static final long MOD = 1_000_000_007;

    @Override
    public int hash(String key,int tableSize) {
        long hash = 0;

        for (int i = 0; i < key.length(); i++) {
            hash = (hash * this.BASE + key.charAt(i)) % this.MOD;
        }
        return (int) (hash % tableSize);
    }
}
