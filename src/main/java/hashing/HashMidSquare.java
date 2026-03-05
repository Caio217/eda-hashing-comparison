package hashing;

public class HashMidSquare<T> implements FuncaoHash<T> {

    @Override
    public int hash(T input, int capacidade) {

        if (input == null)
            throw new IllegalArgumentException("Chave não pode ser null");

        long valor = input.hashCode();

        long quadrado = valor * valor;

        quadrado = Math.abs(quadrado);

        String numero = String.valueOf(quadrado);

        int tamanho = numero.length();
        int meio = tamanho / 2;

        int inicio = Math.max(0, meio - 1);
        int fim = Math.min(tamanho, meio + 2);

        String parteCentral = numero.substring(inicio, fim);

        int valorCentral = Integer.parseInt(parteCentral);

        return valorCentral % capacidade;
    }
}