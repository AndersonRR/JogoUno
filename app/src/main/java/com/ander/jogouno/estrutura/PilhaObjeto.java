package com.ander.jogouno.estrutura;


import com.ander.jogouno.jogo.Carta;

public class PilhaObjeto {
    private Carta item[];
    private int topo;
    private int tamanhoDaPilha; // Capacidade maxima


    public PilhaObjeto(int TamMax) { //Cria uma Pilha vazia
        this.tamanhoDaPilha = TamMax ;
        this.item = new Carta[TamMax];
        this.topo = -1;
    }

    public void empilha(Carta novo) {
        if (this.cheia())
        {
            //return null;
        }
        this.item[this.topo+1] = novo;
        topo = topo + 1;
        //return novo;
    }

    public Carta desempilha() {
        if (this.vazia()) //if (this.topo == -1)
        {
            return null;
        }
        Carta aux = this.item[this.topo];
        topo = topo-1;
        return aux;
    }

    public Carta doTopo() {
        if (this.vazia()) //if (this.topo == -1)
        {
            return null;
        }
        return this.item[this.topo];
    }

    public boolean vazia() {
        return (this.topo == -1);
    }

    public boolean cheia() {
        return (this.topo == (this.tamanhoDaPilha-1));
    }

    public int qtd() {
        return (this.topo + 1);
    }

    public int vagos() {
        return (this.tamanhoDaPilha - (this.topo+1));
    }
    public void mostrar (PilhaObjeto p){
        for (int i = 0; i <= p.topo; i++){
            System.out.println(p.item[i].getName());
        }
    }
}

