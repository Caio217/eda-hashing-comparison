package util;

public class Objeto {

    private Integer id;
    private String descricao;

    public Aluno(Integer id, String descricao) {
        this.chave = id;
        this.descricao = descricao;
    }

    public Integer getChave() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public int hashCode() {
        return this.Chave;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Objeto))
            return false;

        Objeto test = (Objeto) obj;
        return test.getChave().equals(this.chave);
    }

    public String toString() {
        return id;
    }

}


