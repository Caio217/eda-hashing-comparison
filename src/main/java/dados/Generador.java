import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Generador {
    public static void main(String[] args) {
        String nomeArquivo = "inteiros_10000_7.txt";
        
        try (PrintWriter out = new PrintWriter(new FileWriter(nomeArquivo))) {
            System.out.println("Gerando arquivo mock: " + nomeArquivo);
            // Começa do 1.000.000 para garantir que tem 7 dígitos únicos
            for (int i = 0; i < 10000; i++) {
                out.println(1000000 + i); 
            }
            System.out.println("Arquivo gerado com sucesso! Pode rodar o JMH.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
