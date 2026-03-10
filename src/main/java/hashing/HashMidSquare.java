package hashing;

public class HashMidSquare implements FuncaoHash {

    @Override
    public int hash(long input, int capacidade) {
        long quadrado = input * input;
        
        long meio = quadrado / 100000L; 
        
        return Math.abs((int) (meio % capacidade));
    }

    @Override
    public int hash(String key, int capacidade) {
        if (key == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        long hashComoNumero = converterStringParaInt(key);

        return hash(hashComoNumero, capacidade);
    }

    private int converterStringParaInt(String chave) {
        int soma = 0;
        for (int i = 0; i < chave.length(); i++) {
            soma += chave.charAt(i);
        }
        return Math.abs(soma); 
    }
}