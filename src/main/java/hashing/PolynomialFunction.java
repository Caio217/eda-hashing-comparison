public class PolynomialFunction implements FuncaoHash<String> {
    
    @Override
    public int hash(String key,int tableSize) {
        long hash = 0;
        int base = 31;
        long mod = 1000000009;
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * base + key.charAt(i)) % mod;
        }
        return (int) (hash % tableSize);
    }
}
