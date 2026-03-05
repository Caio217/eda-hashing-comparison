package dados;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class GeradorDeEntradas {

    private static final Random gerador = new Random();

    public static void main(String[] args) {

        int[] quantidades = {10000, 100000, 1000000};

        int[] tamanhosString = {10, 50, 100};
        int[] tamanhosInteiro = {6, 7, 9};

        for (int quantidade : quantidades) {

            //String
            for (int tamanho : tamanhosString) {
                try {
                    gerarArquivoStringUniforme(quantidade, tamanho);
                    gerarArquivoStringPadrao(quantidade, tamanho);
                } catch (IOException e) {
                    System.out.println("Erro ao gerar arquivo String: " + e.getMessage());
                }
            }

            //INTEIROS
            for (int tamanho : tamanhosInteiro) {
                try {
                    gerarArquivoInteiroUniforme(quantidade, tamanho);
                    gerarArquivoInteiroPadrao(quantidade, tamanho);
                } catch (IOException e) {
                    System.out.println("Erro ao gerar arquivo Inteiro: " + e.getMessage());
                }
            }
        }

        System.out.println("Todos os arquivos foram gerados com sucesso!");
    }

    // String
    private static void gerarArquivoStringUniforme(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = "string_uniforme_" + quantidade + "_" + tamanho + ".txt";
        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {
            escritor.write(gerarStringAleatoria(tamanho));
            escritor.newLine();
        }

        escritor.close();
    }

    private static void gerarArquivoStringPadrao(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = "string_padrao_" + quantidade + "_" + tamanho + ".txt";
        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {

            if (i % 2 == 0) {
                escritor.write(gerarStringSequencial(i, tamanho));
            } else {
                escritor.write(gerarStringAnagrama(tamanho));
            }

            escritor.newLine();
        }

        escritor.close();
    }

    private static String gerarStringAleatoria(int tamanho) {

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            texto.append(caracteres.charAt(gerador.nextInt(caracteres.length())));
        }

        return texto.toString();
    }

    private static String gerarStringSequencial(int indice, int tamanho) {

        String numeroFormatado = String.format("%0" + tamanho + "d", indice);
        return numeroFormatado.substring(0, tamanho);
    }

    private static String gerarStringAnagrama(int tamanho) {

        String original = gerarStringAleatoria(tamanho);
        char[] caracteres = original.toCharArray();

        // Embaralhamento simples (anagrama real)
        for (int i = 0; i < caracteres.length; i++) {
            int indiceAleatorio = gerador.nextInt(caracteres.length);

            char temp = caracteres[i];
            caracteres[i] = caracteres[indiceAleatorio];
            caracteres[indiceAleatorio] = temp;
        }

        return new String(caracteres);
    }

    // Inteiros

    private static void gerarArquivoInteiroUniforme(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = "inteiro_uniforme_" + quantidade + "_" + tamanho + ".txt";
        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {
            escritor.write(String.valueOf(gerarInteiroAleatorio(tamanho)));
            escritor.newLine();
        }

        escritor.close();
    }

    private static void gerarArquivoInteiroPadrao(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = "inteiro_padrao_" + quantidade + "_" + tamanho + ".txt";
        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {

            int numero;

            if (i % 2 == 0) {
                numero = gerarInteiroSequencial(i, tamanho);
            } else {
                numero = gerarInteiroRepetitivo(tamanho);
            }

            escritor.write(String.valueOf(numero));
            escritor.newLine();
        }

        escritor.close();
    }

    private static int gerarInteiroAleatorio(int tamanho) {

        int minimo = (int) Math.pow(10, tamanho - 1);
        int maximo = (int) Math.pow(10, tamanho) - 1;

        return minimo + gerador.nextInt(maximo - minimo + 1);
    }

    private static int gerarInteiroSequencial(int indice, int tamanho) {

        int limite = (int) Math.pow(10, tamanho);
        return indice % limite;
    }

    private static int gerarInteiroRepetitivo(int tamanho) {

        String base = "123456789";
        String numero = base.substring(0, tamanho);

        return Integer.parseInt(numero);
    }
}