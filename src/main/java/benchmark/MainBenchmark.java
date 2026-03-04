package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class MainBenchmark {

    @Param({"1000", "10000", "100000"})
    private int n;

    @Param({"0.5", "0.75", "0.9"})
    private double fatorCarga;

    @Param({""})
    public String tipoDado;

    @Param({""})
    public String tipoHash;


    private FuncaoHash hashing;
    private HashTable tabelaParaBusca;
    private List<Object> dataset;

    @Setup(Level.Trial)
    public void setup() {

        switch (tipoHash) {
            case "DIVISAO":
                //hashing = new DivisionFunction();
                break;
            case "MULTIPLICACAO":
                //hashing = new MultiplyFunction();
                break;
            case "POLINOMIAL":
                hashing = new PolynomialFunction();
                break;
            case "FOLDING":
                //hashing = new FoldingFunction();
                break;
            // Adicionar outras funções...
        }

        if ("INT".equals(tipoDado)) {
            // dataset = InputGenerator.generateIntegers(n);
        } else {
            // dataset = InputGenerator.generateStrings(n); 
        }

        // tabelaParaBusca = new HashTable(16, fatorCarga, hashing);
        // for (Object key : dataset) {
        //     tabelaParaBusca.put(key, "valor_setup");
        // }
    }

    @Benchmark
    public void testPut() {
        // HashTable tabelaTeste = new HashTable(16, fatorCarga, hashing);
        
        // for (Object key : dataset) {
        //     tabelaTeste.put(key, "valor");
        // }
    }

    @Benchmark
    public void testGetExistente() {
        for (Object key : dataset) {
            // tabelaParaBusca.get(key);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("===================================");
        System.out.println("   BENCHMARK TABELA HASH - GRUPO 6  ");
        System.out.println("===================================");
        System.out.println("Escolha o cenario de teste:");
        System.out.println("1 - Apenas Inteiros (Funcoes Matematicas)");
        System.out.println("2 - Apenas Strings (Funcoes de Texto)");
        System.out.println("3 - Teste Unificado (Todas as Funcoes)");
        System.out.print("Sua escolha: ");
        int escolha = sc.nextInt();

        OptionsBuilder opt = new OptionsBuilder()
                .include(MainBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementIterations(5);

        if (escolha == 1) {
            opt.param("tipoDado", "INT");
            opt.param("tipoHash", "DIVISAO", "MULTIPLICACAO", "MID-SQUARE"); // Adicione Folding depois?
            System.out.println("\nIniciando testes para Inteiros...");
            
        } else if (escolha == 2) {
            opt.param("tipoDado", "STR");
            opt.param("tipoHash", "POLINOMIAL"); // Adicione FOLDING depois?
            System.out.println("\nIniciando testes para Strings...");
            
        } else if (escolha == 3) {
            opt.param("tipoDado", "STR");
            opt.param("tipoHash", "DIVISAO", "POLINOMIAL"); // Adicione todas depois
            System.out.println("\nIniciando Teste Unificado (Stress Test)...");
        } else {
            System.out.println("Opção inválida. Encerrando.");
            return;
        }

        //Executa testes
        new Runner(opt.build()).run();
        
        //System.out.println("Gerando graficos...");
        //Runtime.getRuntime().exec("python gerar_graficos.py");
    }
}