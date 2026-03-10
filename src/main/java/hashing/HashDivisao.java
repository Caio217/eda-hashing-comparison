package hashing;

public class HashDivisao implements FuncaoHash {

    public int hash(long input, int capacidade) {
        long chavePositiva = Math.abs(input); 
        return (int) (chavePositiva % capacidade);
    }

    @Override
    public int hash(String input, int capacidade) {
        if(input == null)
            throw new IllegalArgumentException("Chave nao pode ser null!");

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
