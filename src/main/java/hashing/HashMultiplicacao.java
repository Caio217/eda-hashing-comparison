package hashing;

public class HashMultiplicacao<T> implements FuncaoHash<T> {
    
    public int hash(T chave, int tamanho){
//        if(chave  == null){
            //return trow new IllegalArgumentException("chave não pode ser nula");
//        }
        int hash = chave.hashCode();
        double A = (Math.sqrt(5) - 1) / 2; 
        double frac = (hash * A) - Math.floor(hash * A);
        return (int) Math.floor(frac * tamanho); 
    }
}

