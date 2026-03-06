package hashing;

public class HashMidSquare<T> implements FuncaoHash<T> {

    @Override
    public int hash(T input, int capacidade) {

        if (input == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        long valor = input.hashCode();

        long quadrado = Math.abs(valor * valor);

        String numero = String.valueOf(quadrado);
        int tamanho = numero.length();
        int meio = tamanho / 2;

        int tamanhoJanela = 8;
        int metadeJanela = tamanhoJanela / 2;

        int inicio = Math.max(0, meio - metadeJanela);
        int fim = Math.min(tamanho, meio + metadeJanela);

        String parteCentral = numero.substring(inicio, fim);

        long valorCentral = Long.parseLong(parteCentral);

        return (int) (valorCentral % capacidade);
    }
}