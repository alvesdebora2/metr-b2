package Atividade;

public class Analista {
    private String nome;
    private String username;

    Analista() {
    }

    public Analista(String nome, String username) {
        this.nome = nome;
        this.username = username;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
