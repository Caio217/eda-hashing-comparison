package hashing;

public class HashMultiplicacao implements FuncaoHash {
    
    private static final double A = (Math.sqrt(5) - 1) / 2;

    @Override
    public int hash(long input, int capacidade) {
        long chavePositiva = Math.abs(input);
        
        double frac = (chavePositiva * A) - Math.floor(chavePositiva * A);
        
        return (int) Math.floor(frac * capacidade);
    }

    @Override
    public int hash(String input, int capacidade) {
        if (input == null)
            throw new IllegalArgumentException("A chave não pode ser nula");

        long hashComoNumero = converterStringParaInt(input);
        
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

