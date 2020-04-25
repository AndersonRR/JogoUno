package com.ander.jogouno.jogo;


import android.widget.ImageView;

/**
 *
 * @author ander
 */
public class Carta {

    //private Long codigo;
    //private String nome;
    // private Integer valor;
    public enum Cor {
        AMARELO, VERMELHO, VERDE, AZUL;
    }

    public enum Valor {
        ZERO, UM, DOIS, TRES, QUATRO, CINCO, SEIS, SETE, OITO, NOVE, CORINGA, MAIS_QUATRO, MAIS_DOIS, BLOQUEIA;
    }

    private String cor;
    private String valor;
    private String nome;
    private int imagemCarta;

    public Carta(String v, String c, String nome, int imagemCarta) {
        this.cor = c;
        this.valor = v;
        this.nome = nome;
        this.imagemCarta = imagemCarta;
    }

    public int getImagemCarta() {
        return imagemCarta;
    }


    public void setImagemCarta(int imagemCarta) {
        this.imagemCarta = imagemCarta;
    }

    public String mostrar() {
        return (nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getName() {
        return nome;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }


}
