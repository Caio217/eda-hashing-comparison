package benchmark;

import hashing.*;
import org.openjdk.jmh.annotations.*; 
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.results.format.ResultFormatType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


/**
 * Classe principal de automação de testes utilizando JMH (Java Microbenchmark Harness).
 * <p>Mede o tempo médio de execução (em nanossegundos) e contabiliza colisões
 * para as operações de inserção, busca e remoção em uma Tabela Hash, 
 * variando parâmetros como Função Hash, tamanho da entrada e fator de carga.</p>
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MainBenchmark {

    /** Quantidade de elementos (N) a serem processados no teste. */
    @Param({"10000"})
    private int n;

    /** Fator de carga utilizado para dimensionar a capacidade inicial da tabela. */
    @Param({"0.5"})
    private double fatorCarga;

    /** Tipo de dado avaliado (ex: INT, STR). */
    @Param({""})
    public String tipoDado;

    /** Algoritmo de hash a ser utilizado (ex: DIVISAO, DJB2). */
    @Param({""})
    public String tipoHash;

    /** Cenário de distribuição dos dados (ex: UNIFORME, PADRAO). */
    @Param({""})
    public String cenario;

    /** Tamanho em caracteres/dígitos dos elementos gerados. */
    @Param({"0"})
    public int tamanho;
    
    private FuncaoHash hashing;
    private TabelaHash tabelaParaBusca;
    private List<Object> dataset;
    private int capacidadeInicial;

    /**
     * Prepara o ambiente antes do início de cada bateria de testes do JMH.
     * <p>Instancia a função hash correta, carrega o dataset do disco e realiza 
     * uma passagem de diagnóstico silenciosa para isolar a contagem de colisões 
     * da medição de tempo oficial do benchmark.</p>
     */
    @Setup(Level.Trial)
    public void setup() {
        System.out.println("\n--- Setup: " + tipoHash + " | N: " + n + " | Tam: " + tamanho + " ---");

        switch (tipoHash) {
            case "DIVISAO":
                hashing = new HashDivisao();
                break;
            case "MULTIPLICACAO":
                hashing = new HashMultiplicacao();
                break;
            case "POLINOMIAL":
                hashing = new PolynomialFunction();
                break;
            case "MID-SQUARE":
                hashing = new HashMidSquare();
                break;
            case "DJB2":
                hashing = new HashDJB2();
                break;
            default:
                throw new IllegalArgumentException("Funcao Hash nao reconhecida: " + tipoHash);
        }

        dataset = new ArrayList<>(n);
        String nomeArquivo = "";
        String caminho = "dataset/";
        
        try {
            
            String tipoPrefixo = "INT".equals(tipoDado) ? "inteiro" : "string";
            
            String cenarioPrefixo = cenario.toLowerCase();
            if (cenarioPrefixo.equals("numerico")) {
                cenarioPrefixo = "uniforme";
            }

            nomeArquivo = tipoPrefixo + "_" + cenarioPrefixo + "_" + n + "_" + tamanho + ".txt";
            
            System.out.println("   -> Lendo arquivo: " + caminho + nomeArquivo);
            
            List<String> linhas = Files.readAllLines(Paths.get(caminho + nomeArquivo));
            
            if ("INT".equals(tipoDado)) {
                for (String linha : linhas) {
                    dataset.add(Long.parseLong(linha.trim()));
                }
            } else {
                dataset.addAll(linhas);
            }
            
        } catch (IOException e) {
            throw new RuntimeException("ERRO FATAL: Arquivo nao encontrado: " + caminho + nomeArquivo, e);
        }

        capacidadeInicial = (int) (n / fatorCarga) + 1;

        TabelaHash tabelaDiagnostico = new TabelaHash(capacidadeInicial, fatorCarga, hashing);
        
        for (Object key : dataset) { tabelaDiagnostico.put(key, "valor"); }
        int colisoesPutReais = tabelaDiagnostico.getColisoesPut();
        
        for (Object key : dataset) { tabelaDiagnostico.get(key); }
        int colisoesGetReais = tabelaDiagnostico.getColisoesGet();

        for (Object key : dataset) { tabelaDiagnostico.remove(key); }
        int colisoesRemoveReais = tabelaDiagnostico.getColisoesRemove();

        System.out.println("\n📊 DIAGNÓSTICO DE COLISÕES (1 Passagem - " + n + " itens):");
        System.out.println("   -> Colisões na Inserção (Put): " + colisoesPutReais);
        System.out.println("   -> Saltos na Busca (Get): " + colisoesGetReais);
        System.out.println("   -> Saltos na Remoção (Remove): " + colisoesRemoveReais);
        System.out.println("=========================================================\n");

        salvarEstatisticasCSV(colisoesPutReais, colisoesGetReais, colisoesRemoveReais);

        tabelaParaBusca = new TabelaHash(capacidadeInicial, fatorCarga, hashing);
        for (Object key : dataset) {
             tabelaParaBusca.put(key, "valor_setup");
        }
    }

    /**
     * Finaliza o ambiente após a execução da bateria de testes, 
     * exibindo as estatísticas gerais da Tabela Hash em memória.
     */
    @TearDown(Level.Trial)
    public void relatorioFinal() {
        this.tabelaParaBusca.imprimirEstatisticas();
    }

    /**
     * Avalia o desempenho computacional da operação de inserção (Put).
     *
     * @param bh Consome o objeto gerado para evitar o descarte por otimização da JVM.
     */
    @Benchmark
    public void testPut(Blackhole bh) {
        TabelaHash tabelaTeste = new TabelaHash(capacidadeInicial, fatorCarga, hashing);
        
        for (Object key : dataset) {
            tabelaTeste.put(key, "valor");
        }
        
        bh.consume(tabelaTeste); 
    }

    /**
     * Avalia o desempenho computacional da operação de busca (Get).
     * Opera sobre uma tabela previamente populada no método setup.
     *
     * @param bh Consome o resultado da busca para forçar a execução efetiva.
     */
    @Benchmark
    public void testGetExistente(Blackhole bh) {
        for (Object key : dataset) {
            bh.consume(tabelaParaBusca.get(key));
        }
    }

    /**
     * Avalia o desempenho computacional da operação de exclusão (Remove).
     * Popula uma tabela temporária e, em seguida, mede o custo de remover os elementos.
     *
     * @param bh Consome o retorno da operação de remoção.
     */
    @Benchmark
    public void testRemove(Blackhole bh) {
        TabelaHash tabelaTeste = new TabelaHash(capacidadeInicial, fatorCarga, hashing);
        
        for (Object key : dataset) {
            tabelaTeste.put(key, "valor");
        }
        
        for (Object key : dataset) {
            bh.consume(tabelaTeste.remove(key));
        }
    }

    /**
     * Grava os resultados de diagnóstico de colisões em um arquivo CSV.
     * Facilita a posterior geração de gráficos analíticos.
     *
     * @param colPut    Número de colisões ocorridas nas inserções.
     * @param colGet    Número de "saltos" necessários durante as buscas.
     * @param colRemove Número de "saltos" necessários durante as remoções.
     */
    private void salvarEstatisticasCSV(int colPut, int colGet, int colRemove) {
        String nomeArquivo = "resultados/estatisticas_colisoes.csv";
        boolean arquivoExiste = new File(nomeArquivo).exists();

        try (FileWriter fw = new FileWriter(nomeArquivo, true);
             PrintWriter pw = new PrintWriter(fw)) {

            if (!arquivoExiste) {
                pw.println("Cenario,TipoHash,FatorCarga,N,Colisoes_Put,Colisoes_Get,Colisoes_Remove");
            }

            pw.printf("%s,%s,%s,%d,%d,%d,%d\n",
                    cenario, tipoHash, fatorCarga, n, colPut, colGet, colRemove);

        } catch (IOException e) {
            System.err.println("Erro ao salvar estatísticas: " + e.getMessage());
        }
    }

    /**
     * Ponto de entrada (Main) para inicialização do Benchmark JMH.
     * Lê dinamicamente as configurações via arquivo properties e delega 
     * a execução das medições ao Runner do JMH.
     *
     * @param args Argumentos de linha de comando (não utilizados diretamente).
     * @throws Exception Caso ocorra erro crítico na leitura do arquivo ou na execução do JMH.
     */
    public static void main(String[] args) throws Exception {
    System.out.println("==========================================");
    System.out.println(" INICIANDO AUTOMACAO - BENCHMARK GRUPO 6 ");
    System.out.println("==========================================");

    Properties config = new Properties();
    try (FileInputStream fis = new FileInputStream("config.properties")) {
        config.load(fis);
        System.out.println("Arquivo config.properties lido com sucesso.");
    } catch (IOException e) {
        System.err.println("ERRO: Arquivo config.properties não encontrado na raiz!");
        return;
    }

    String tipoDadoConfig = config.getProperty("tipoDado", "INT").toUpperCase();
    String[] tiposHash = config.getProperty("tipoHash", "DIVISAO").split(",");
    String[] tamanhosN = config.getProperty("n", "10000").split(",");
    String[] fatoresCarga = config.getProperty("fatorCarga", "0.5").split(",");
    String[] tamanhosEntrada;
    String[] cenariosEntrada;

    if (tipoDadoConfig.equals("INT")) {
        tamanhosEntrada = config.getProperty("tamanhoInt", "7").split(",");
        cenariosEntrada = config.getProperty("cenarioDados", "UNIFORME,PADRAO").split(","); 
        System.out.println("Modo INTEIROS: " + String.join(", ", tamanhosEntrada) + " digitos | Cenários: " + String.join(", ", cenariosEntrada));
    } else {
        tamanhosEntrada = config.getProperty("tamanhoStr", "10,50,100").split(",");
        cenariosEntrada = config.getProperty("cenarioDados", "UNIFORME,PADRAO").split(",");
        System.out.println("Modo STRINGS: " + String.join(", ", cenariosEntrada));
    }

    String regexInclusao = MainBenchmark.class.getSimpleName();

    Options opt = new OptionsBuilder()
            .include(regexInclusao)
            .forks(1)
            .warmupIterations(2)
            .measurementIterations(3)
            .param("tipoDado", tipoDadoConfig)
            .param("tipoHash", tiposHash)
            .param("n", tamanhosN)
            .param("fatorCarga", fatoresCarga)
            .param("cenario", cenariosEntrada)
            .param("tamanho", tamanhosEntrada)
            .resultFormat(ResultFormatType.CSV)
            .result("resultados/resultados_benchmark.csv")
            .build();

        System.out.println("Iniciando bateria de testes do JMH...");
        new Runner(opt).run();
    }
}