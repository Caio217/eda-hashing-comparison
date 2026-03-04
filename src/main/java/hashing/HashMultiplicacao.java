package hashing;

public class HashMultiplicacao<T> implements FuncaoHash<T> {
    
    public static int hash(int chave, int tamanho){
        if(chave  == null){
            return trow new IllegalArgumentException("A chave não pode ser nula");
        }
        double A = (Math.sqrt(5) - 1) / 2; 
        double frac = (chave * A) - Math.floor(chave * A);
        return (int) Math.floor(frac * tamanho); 
    }
}

