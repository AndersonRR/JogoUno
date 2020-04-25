package com.ander.jogouno.jogo;

import com.ander.jogouno.estrutura.*;
import com.ander.jogouno.jogo.Carta.Cor;
import com.ander.jogouno.jogo.Carta.Valor;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author ander
 */
public class Baralho {

    public static PilhaObjeto baralho = new PilhaObjeto(100000);
    private static LDE_Cartas listaCartas = new LDE_Cartas();
    ArrayList<String> valor = new ArrayList<String>();
    ArrayList<String> cor = new ArrayList<String>();

    public void iniciarBaralho() {
        cor.add("AZUL");cor.add("VERDE");cor.add("AMARELO");cor.add("VERMELHO");
        valor.add("ZERO");valor.add("UM");valor.add("DOIS");valor.add("TRES");valor.add("QUATRO");valor.add("CINCO");valor.add("SEIS");valor.add("SETE");
        valor.add("OITO");valor.add("NOVE");valor.add("CORINGA");valor.add("MAIS_QUATRO");valor.add("MAIS_DOIS");valor.add("BLOQUEIA");

        Random aleatorio = new Random();
        for (String corDaCarta : cor) {
            for (int j = 0; j < 2; j++) { //pra fazer duas vezes por cor...
                for (String naipeDaCarta : valor) {
                    if ((j == 1 && naipeDaCarta == "MAIS_QUATRO") || (j == 1 && naipeDaCarta == "CORINGA")) {
                        continue; //se for a segunda vez, ele não adiciona mais quatro e nem coringa.
                                  //baralho contém apenas 4 de cada um

                    } else if ((naipeDaCarta == "MAIS_QUATRO") || naipeDaCarta == "CORINGA") {

                        Carta carta = new Carta(naipeDaCarta, "",naipeDaCarta,0);
                        int posicao = aleatorio.nextInt(listaCartas.qtd() + 1) + 1;
                        if (posicao > listaCartas.qtd()) {
                            //System.out.println("");
                            listaCartas.inserirFim(carta);
                        } else {
                            listaCartas.inserirPos(carta, posicao);
                        }

                    } else {

                        Carta carta = new Carta(naipeDaCarta, corDaCarta,naipeDaCarta+"_"+corDaCarta,0);
                        int posicao = aleatorio.nextInt(listaCartas.qtd() + 1) + 1;
                        if (posicao > listaCartas.qtd()) {
                            //System.out.println("");
                            listaCartas.inserirFim(carta);
                        } else {
                            listaCartas.inserirPos(carta, posicao);
                        }
                    }

                }
            }
        }

        while (!listaCartas.vazia()){
            baralho.empilha(listaCartas.retirarFim());
            //coloca todas as cartas embaralhadas no baralho !
        }
        //return baralho;
    }

    public Carta comprarCarta(){
        return baralho.desempilha();
    }
}

