package com.ander.jogouno.estrutura;


// ancora.ant :==> Aponta para o fim
// ancora.prox:==> Aponta para o inicio
//+------+-------+--------+        +------+------+--------+  +------+----+------+     +------+----+------+
//|<----*|Ancora |*---->  |    null|<----*| X1   | *----> |  |<----*| X2 |*---->| ... |<----*| Xn |*---->| null
//+------+-------+--------+        +------+------+--------+  +------+----+------+     +------+----+------+
//                                   Ant             prox      Ant         prox         Ant         prox
import com.ander.jogouno.jogo.*;
import com.ander.jogouno.jogo.Carta.Cor;
import com.ander.jogouno.jogo.Carta.Valor;
import java.util.Random;

//+-------+---+    +-------+---+    +-------+---+             +-------+---+
//  Inicio                                                       FIM
public class LDE_Cartas {

    private static class Celula {

        Carta item;
        Celula ant, prox;
    }
    private Celula ancora;
    private int qtd;

    public LDE_Cartas() { //Cria uma Lista vazia
        this.ancora = new Celula();
        this.ancora.ant = this.ancora.prox = null;
        this.qtd = 0;
    }

    public Celula posInserir(Carta elemento) {
        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora;
        // ant = this.inicio; // se estiver vazio vai retornar o INICIO
        // senao retorna o anterior, onde o elo prox. apontará
        // para o novo elemento que esta sendo inserido.

        while (pos.prox != null) {
            //if (pos.prox.item.maior((Carta) elemento)) {
            //return pos;
            //} else if (pos.prox.item.iguais((Carta) elemento)) {
            // return null;
            //}
            //    ant = pos; // Guardo o anterior
            pos = pos.prox;
        }
        return pos;
    }

    public Carta inserirFim(Carta novoItem) {// novoItem é a nova chave
        if (this.ancora.prox == null) {
            Celula novo = new Celula();
            if (novo == null) { // não tem mais memória
                return null;
            }
            novo.item = (Carta) novoItem;
            this.ancora.prox = novo;
            this.ancora.ant = novo;
            this.qtd++;
            return novoItem;
        }
        Celula novo = new Celula();
        if (novo == null) { // não tem mais memória
            return null;
        }
        novo.item = (Carta) novoItem;
        ancora.ant.prox = novo;
        novo.ant = ancora.ant;
        novo.prox = null;
        ancora.ant = novo;
        this.qtd++;
        return novoItem;
    }

    public Carta inserirInicio(Carta novoItem) {// novoItem é a nova chave
        if (this.ancora.prox == null) {
            Celula novo = new Celula();
            if (novo == null) { // não tem mais memória
                return null;
            }
            novo.item = (Carta) novoItem;
            this.ancora.prox = novo;
            this.ancora.ant = novo;
            this.qtd++;
            return novoItem;
        }
        Celula novo = new Celula();
        if (novo == null) { // não tem mais memória
            return null;
        }
        novo.item = (Carta) novoItem;
        ancora.prox.ant = novo;
        novo.prox = ancora.prox;
        novo.ant = null;
        ancora.prox = novo;
        this.qtd++;
        return novoItem;
    }

    public Carta inserirPos(Carta novoItem, int posicao) {// novoItem é a nova chave
        if ((posicao > (this.qtd() + 1)) || (posicao < 1)) {
            System.out.println("Solicitando Inserção acima o limite[1,..," + (this.qtd() + 1) + "]");
            return null;
        }
        Celula novo = new Celula();
        if (novo == null) { // não tem mais memória
            return null;
        }
        if (this.ancora.prox == null) {
            novo.item = (Carta) novoItem;
            this.ancora.prox = novo;
            this.ancora.ant = novo;
            this.qtd++;
            return novoItem;
        }
        // inserir no inicio
        if (posicao == 1) {
            novo.item = (Carta) novoItem;
            ancora.prox.ant = novo;
            novo.prox = ancora.prox;
            novo.ant = null;
            ancora.prox = novo;
            this.qtd++;
            return novoItem;
        }
        // inserir no final
        if (posicao >this.qtd()) {
            novo.item = (Carta) novoItem;
            ancora.ant.prox = novo;
            novo.ant = ancora.ant;
            novo.prox = null;
            ancora.ant = novo;
            this.qtd++;
            return novoItem;
        }
        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox.prox;
        // ant = this.inicio; // se estiver vazio vai retornar o INICIO
        // senao retorna o anterior, onde o elo prox. apontará
        // para o novo elemento que esta sendo inserido.
        int cont=2;
        while (cont<posicao) {
            cont++;
            pos = pos.prox;
        }
        //System.out.println("Será no lugar do: " + pos.item.getValor());
        novo.item = (Carta) novoItem;
        novo.prox = pos;
        novo.ant = pos.ant;
        novo.prox.ant = novo;
        novo.ant.prox = novo;
        this.qtd++;
        return novoItem;
    }

    public Carta inserir(Carta novoItem) {// novoItem é a nova chave
        if (this.ancora.prox == null) {
            Celula novo = new Celula();
            if (novo == null) { // não tem mais memória
                return null;
            }
            novo.item = (Carta) novoItem;
            this.ancora.prox = novo;
            this.ancora.ant = novo;
            this.qtd++;
            return novoItem;
        }

        Celula novo = new Celula();
        if (novo == null) { // não tem mais memória
            return null;
        }

        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox.prox;
        // ant = this.inicio; // se estiver vazio vai retornar o INICIO
        // senao retorna o anterior, onde o elo prox. apontará
        // para o novo elemento que esta sendo inserido.
        //System.out.println("codigo=> " + pos.item.getValor());
        novo.item = (Carta) novoItem;
        novo.prox = pos.prox;
        novo.ant = pos;
        novo.prox.ant = novo;
        novo.ant.prox = novo;
        this.qtd++;
        return novoItem;
    }

    public Carta pesquisar(Carta elemento) {
        if (this.vazia()) {
            return null;
        }
        Celula pos = new Celula();
        Celula ant = new Celula();
        pos = this.ancora.prox;
        //ant = this.ancora; // se estiver vazio vai retornar o INICIO
        // senao retorna o anterior, onde o elo prox. apontará
        // para o novo elemento que esta sendo inserido.
        if (pos == null) {
            return null;
        }
        return (Carta) pos.item;
    }

    public Carta retirarInicio() {
        if (this.vazia()) {
            return null;
        }
        Celula pos = new Celula();
        if (pos == null) { // não tem mais memória
            return null;
        }
        //Removendo ultimo
        if (this.ancora.prox == this.ancora.ant) {
            ancora.ant = null;
            ancora.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }
        pos = this.ancora.prox;
        ancora.prox.prox.ant = null;
        ancora.prox = ancora.prox.prox;
        this.qtd--;
        return (Carta) pos.item;
    }

    public Carta retirarFim() {
        if (this.vazia()) {
            return null;
        }

        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox;
        if (pos == null) { // não tem mais memória
            return null;
        }

        //Removendo ultimo
        if (this.ancora.prox == this.ancora.ant) {
            ancora.ant = null;
            ancora.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do final
        pos = this.ancora.ant;
        ancora.ant = ancora.ant.ant;
        ancora.ant.prox = null;
        this.qtd--;
        return (Carta) pos.item;
    }

    public Carta retirarPos(int posicao) {
        if (this.vazia()) {
            return null;
        }
        if (posicao > this.qtd()) {
            System.out.println("Solicitando retirada acima o limite Superior: " + this.qtd());
            return null;
        }
        if (posicao < 1) {
            System.out.println("Solicitando retirada abaixo o limite Infeiror: " + 0);
            return null;
        }

        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox;
        if (pos == null) { // não tem mais memória
            return null;
        }

        //Removendo ultimo
        if (this.qtd == 1) {
            ancora.ant = null;
            ancora.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do inicio
        if (posicao == 1) {
            ancora.prox.prox.ant = null;
            ancora.prox = ancora.prox.prox;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do final
        if (posicao == this.qtd()) {
            pos = this.ancora.ant;
            ancora.ant = ancora.ant.ant;
            ancora.ant.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo da posicao passada como paremtnro.
        pos = this.ancora.prox.prox;
        int cont = 2;
        while (cont != posicao) {
            pos = pos.prox;
            cont++;
        }
        pos.ant.prox = pos.prox;
        pos.prox.ant = pos.ant;
        this.qtd--;
        return (Carta) pos.item;
    }

    public Carta retirar(Valor codigo) {
        if (this.vazia()) {
            return null;
        }

        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox;
        if (pos == null) { // não tem mais memória
            return null;
        }

        //Removendo ultimo
        if ((this.ancora.prox.item.getValor().equals(codigo))
                && (this.ancora.prox == this.ancora.ant)) {
            ancora.ant = null;
            ancora.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do inicio
        if (this.ancora.prox.item.getValor().equals(codigo)) {
            ancora.prox.prox.ant = null;
            ancora.prox = ancora.prox.prox;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do final
        if (this.ancora.ant.item.getValor().equals(codigo)) {
            pos = this.ancora.ant;
            ancora.ant = ancora.ant.ant;
            ancora.ant.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do meio ou não existe.
        pos = this.ancora.prox;
        while ((pos.prox != null) && (!(pos.item.getValor().equals(codigo)))) {
            pos = pos.prox;
        }
        if (pos.prox == null) {
            return null;
        }
        pos.ant.prox = pos.prox;
        pos.prox.ant = pos.ant;
        this.qtd--;
        return (Carta) pos.item;
    }

    public Carta pesquisar(int daPosicao) {// Pesquisa uma peça em uma posição na sequencia da lista
        if (daPosicao > this.qtd) {
            return null;
        }
        Celula pos = new Celula();
        if (pos == null) { // não tem mais memória
            return null;
        }
        Celula ant = new Celula();
        if (ant == null) { // não tem mais memória
            return null;
        }
        pos = this.ancora.prox;
        ant = this.ancora; // se estiver vazio vai retornar o INICIO
        // senao retorna o anterior, onde o elo prox. apontará
        // para o novo elemento que esta sendo inserido.
        int i = 1;
        while (daPosicao != i) {
            ant = pos; // Guardo o anterior
            pos = pos.prox;
            i++;
        }
        if (pos == null) {
            return null;
        }
        return (Carta) pos.item;
    }

    //    public Carta retirarPos(int removerDaPosicao) {// Remove uma peça em uma posição na sequencia da lista
//        this.qtd--;
//        return (Carta) pos.item;
//    }
    public boolean vazia() {
        return (this.ancora.prox == null);
    }

    public int qtd() {
        return (this.qtd);
    }

    public void mostrar() { // Mostra do inicio até o fim
        Celula pos = new Celula();
        pos = this.ancora.prox;
        int cont = 0;
        int cont2 = 1;
        System.out.print("Cartas=[ ");
        while (pos != null) {
            System.out.print(pos.item.mostrar() +"("+cont2+")"+" | ");
            pos = pos.prox;
            cont++;
            cont2++;
            if (cont % 50 == 0) {
                System.out.println("");
            }
        }
        System.out.println("]   \nCARTAS NA MÃO: "+this.qtd());
        //return (true);
    }

    public boolean mostrarInverso() { // Mostra do inicio até o fim
        Celula pos = new Celula();
        pos = this.ancora.ant;
        int cont = 0;
        System.out.print("Lista Inversa=[ ");
        while (pos != null) {
            System.out.print(pos.item.mostrar() + " ");
            pos = pos.ant;
            cont++;
            if (cont % 50 == 0) {
                System.out.println("");
            }
        }
        System.out.println("]   \nCont= " + cont + "   Lista Qtd():"+this.qtd());
        return (true);
    }

    public void sorteia() {
    }

    public void ordenaCrescente() {
    }

    public void ordenaDecrescente() {
    }

    public boolean copia(LDE_Cartas lista) {
        return (true);
    }

    //adaptando o método para o caso
    public Carta retirarNome(String codigo) {
        if (this.vazia()) {
            return null;
        }

        Celula pos = new Celula();
        //  Celula ant = new Celula();
        pos = this.ancora.prox;
        if (pos == null) { // não tem mais memória
            return null;
        }

        //Removendo ultimo
        if ((this.ancora.prox.item.getName().equals(codigo))
                && (this.ancora.prox == this.ancora.ant)) {
            ancora.ant = null;
            ancora.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do inicio
        if (this.ancora.prox.item.getName().equals(codigo)) {
            ancora.prox.prox.ant = null;
            ancora.prox = ancora.prox.prox;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do final
        if (this.ancora.ant.item.getName().equals(codigo)) {
            pos = this.ancora.ant;
            ancora.ant = ancora.ant.ant;
            ancora.ant.prox = null;
            this.qtd--;
            return (Carta) pos.item;
        }

        //Removendo do meio ou não existe.
        pos = this.ancora.prox;
        while ((pos.prox != null) && (!(pos.item.getName().equals(codigo)))) {
            pos = pos.prox;
        }
        if (pos.prox == null) {
            return null;
        }
        pos.ant.prox = pos.prox;
        pos.prox.ant = pos.ant;
        this.qtd--;
        return (Carta) pos.item;
    }


}
