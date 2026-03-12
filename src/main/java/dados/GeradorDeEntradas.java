package dados;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Classe responsável por gerar arquivos de entrada (datasets) contendo
 * diferentes tipos de dados para testes de algoritmos.
 *
 * São gerados arquivos com:
 * - Strings (uniforme, padrão e anagramas)
 * - Inteiros (uniforme e padrão)
 *
 * Os arquivos são armazenados automaticamente na pasta "dataset".
 */
public class GeradorDeEntradas {

    private static final Random gerador = new Random();
    private static final String PASTA_DATASET = "dataset/";

    /**
     * Executa o processo de geração de todos os arquivos de dataset.
     * As entradas variam em quantidade e tamanho.
     */
    public static void main(String[] args) {

        criarPastaDataset();

        int[] quantidades = {10000, 100000, 1000000};

        int[] tamanhosString = {10, 50, 100};
        int[] tamanhosInteiro = {6, 7, 9};

        for (int quantidade : quantidades) {

            // STRINGS
            for (int tamanho : tamanhosString) {
                try {
                    gerarArquivoStringUniforme(quantidade, tamanho);
                    gerarArquivoStringPadrao(quantidade, tamanho);
                    gerarArquivoStringAnagramas(quantidade, tamanho);
                } catch (IOException e) {
                    System.out.println("Erro ao gerar arquivo String: " + e.getMessage());
                }
            }

            // INTEIROS
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

    /**
     * Cria a pasta onde os arquivos de dataset serão armazenados,
     * caso ela ainda não exista.
     */
    private static void criarPastaDataset() {

        File pasta = new File(PASTA_DATASET);

        if (!pasta.exists()) {
            pasta.mkdirs();
        }
    }

    /**
     * Gera um arquivo contendo strings aleatórias (distribuição uniforme).
     *
     * @param quantidade número de strings geradas
     * @param tamanho tamanho de cada string
     */
    private static void gerarArquivoStringUniforme(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = PASTA_DATASET + "string_uniforme_" + quantidade + "_" + tamanho + ".txt";

        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {
            escritor.write(gerarStringAleatoria(tamanho));
            escritor.newLine();
        }

        escritor.close();
    }

    /**
     * Gera um arquivo contendo strings que são anagramas de uma
     * mesma palavra base.
     *
     * @param quantidade número de strings
     * @param tamanho tamanho das strings
     */
    private static void gerarArquivoStringAnagramas(int quantidade, int tamanho) throws IOException {
        String nomeArquivo = PASTA_DATASET + "string_anagramas_" + quantidade + "_" + tamanho + ".txt";
        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        String palavraBase = gerarStringAleatoria(tamanho);
        
        for (int i = 0; i < quantidade; i++) {
            escritor.write(embaralharString(palavraBase));
            escritor.newLine();
        }

        escritor.close();
    }

    /**
     * Embaralha os caracteres de uma string gerando um anagrama.
     *
     * @param original string original
     * @return string com caracteres embaralhados
     */
    private static String embaralharString(String original) {
        char[] caracteres = original.toCharArray();
        
        for (int i = caracteres.length - 1; i > 0; i--) {
            int indiceAleatorio = gerador.nextInt(i + 1);
            char temp = caracteres[i];
            caracteres[i] = caracteres[indiceAleatorio];
            caracteres[indiceAleatorio] = temp;
        }

        return new String(caracteres);
    }

    /**
     * Gera um arquivo contendo strings com padrão alternado
     * entre valores sequenciais e anagramas.
     *
     * @param quantidade número de linhas
     * @param tamanho tamanho das strings
     */
    private static void gerarArquivoStringPadrao(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = PASTA_DATASET + "string_padrao_" + quantidade + "_" + tamanho + ".txt";

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

    /**
     * Gera uma string aleatória composta por letras e números.
     *
     * @param tamanho tamanho da string
     * @return string gerada aleatoriamente
     */
    private static String gerarStringAleatoria(int tamanho) {

        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        StringBuilder texto = new StringBuilder();

        for (int i = 0; i < tamanho; i++) {
            texto.append(caracteres.charAt(gerador.nextInt(caracteres.length())));
        }

        return texto.toString();
    }

    /**
     * Gera uma string numérica sequencial baseada no índice.
     *
     * @param indice valor usado para gerar a sequência
     * @param tamanho tamanho da string
     * @return string sequencial formatada
     */
    private static String gerarStringSequencial(int indice, int tamanho) {

        String numeroFormatado = String.format("%0" + tamanho + "d", indice);
        return numeroFormatado.substring(0, tamanho);
    }

    /**
     * Gera um anagrama a partir de uma string aleatória.
     *
     * @param tamanho tamanho da string
     * @return string embaralhada
     */
    private static String gerarStringAnagrama(int tamanho) {

        String original = gerarStringAleatoria(tamanho);

        char[] caracteres = original.toCharArray();

        for (int i = 0; i < caracteres.length; i++) {

            int indiceAleatorio = gerador.nextInt(caracteres.length);

            char temp = caracteres[i];
            caracteres[i] = caracteres[indiceAleatorio];
            caracteres[indiceAleatorio] = temp;
        }

        return new String(caracteres);
    }

    /**
     * Gera um arquivo contendo números inteiros aleatórios.
     *
     * @param quantidade número de valores
     * @param tamanho quantidade de dígitos
     */
    private static void gerarArquivoInteiroUniforme(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = PASTA_DATASET + "inteiro_uniforme_" + quantidade + "_" + tamanho + ".txt";

        BufferedWriter escritor = new BufferedWriter(new FileWriter(nomeArquivo));

        for (int i = 0; i < quantidade; i++) {
            escritor.write(String.valueOf(gerarInteiroAleatorio(tamanho)));
            escritor.newLine();
        }

        escritor.close();
    }

    /**
     * Gera um arquivo de inteiros com padrão alternado entre
     * números sequenciais e números repetitivos.
     *
     * @param quantidade número de valores
     * @param tamanho número de dígitos
     */
    private static void gerarArquivoInteiroPadrao(int quantidade, int tamanho)
            throws IOException {

        String nomeArquivo = PASTA_DATASET + "inteiro_padrao_" + quantidade + "_" + tamanho + ".txt";

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

    /**
     * Gera um número inteiro aleatório com quantidade específica de dígitos.
     *
     * @param tamanho quantidade de dígitos
     * @return número inteiro gerado
     */
    private static int gerarInteiroAleatorio(int tamanho) {

        int minimo = (int) Math.pow(10, tamanho - 1);
        int maximo = (int) Math.pow(10, tamanho) - 1;

        return minimo + gerador.nextInt(maximo - minimo + 1);
    }

    /**
     * Gera um número sequencial baseado no índice informado.
     *
     * @param indice índice da sequência
     * @param tamanho número de dígitos
     * @return número sequencial
     */
    private static int gerarInteiroSequencial(int indice, int tamanho) {

        int limite = (int) Math.pow(10, tamanho);
        return indice % limite;
    }

    /**
     * Gera um número inteiro formado por uma sequência fixa de dígitos.
     *
     * @param tamanho quantidade de dígitos
     * @return número repetitivo gerado
     */
    private static int gerarInteiroRepetitivo(int tamanho) {

        String base = "123456789";
        String numero = base.substring(0, tamanho);

        return Integer.parseInt(numero);
    }
}