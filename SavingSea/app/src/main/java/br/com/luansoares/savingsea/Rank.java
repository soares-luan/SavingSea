package br.com.luansoares.savingsea;

/**
 * Created by luan on 20/01/2017.
 */
public class Rank {

    String nome,pais,pontos;

    public Rank(String nome, String pais, String pontos) {
        this.nome = nome;
        this.pais = pais;
        this.pontos = pontos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getPontos() {
        return pontos;
    }

    public void setPontos(String pontos) {
        this.pontos = pontos;
    }
}
