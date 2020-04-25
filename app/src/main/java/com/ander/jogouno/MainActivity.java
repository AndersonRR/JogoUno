package com.ander.jogouno;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ander.jogouno.design.NossoAdapter;
import com.ander.jogouno.estrutura.PilhaObjeto;
import com.ander.jogouno.jogo.Baralho;
import com.ander.jogouno.jogo.Carta;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NossoAdapter adapter;
    private List<Carta> cartasList;
    private List<Carta> cartasBot1;
    private List<Carta> cartasBot2;
    private int qtdMaoJogador;
    private int qtdMaoBot1;
    private int qtdMaoBot2;
    private ImageView cartaNaMesa;
    private ImageView botaoOK;
    private ImageView botaoRecomecar;
    private ImageView ganhou;
    private ImageView comecar;
    private TextView relatorio;
    private TextView player;
    private TextView bot1;
    private TextView bot2;
    private TextView nomeJogador;
    private Carta cartaNaMesaNome;
    private Baralho baralho;
    private boolean compraOuJoga;
    private boolean botJogando;
    private boolean somenteNaipe;
    private boolean bloqueiaBot;
    private int cartasLargadasBot;
    private AlertDialog alerta;
    private ConstraintLayout main_layout;
    private String nomeJogadorPronto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Modo Full Screen
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);

        main_layout = findViewById(R.id.menuComeco);
        nomeJogador = findViewById(R.id.nomeJogador);
        comecar = findViewById(R.id.botaoComecar);

        recyclerView = findViewById(R.id.recyler);
        recyclerView.setHasFixedSize(true);
        cartaNaMesa = findViewById(R.id.cartaNaMesa);
        botaoOK = findViewById(R.id.botaoOK);
        botaoRecomecar = findViewById(R.id.botaoRecomecar);
        ganhou = findViewById(R.id.ganhou);
        relatorio = findViewById(R.id.relatorio);
        player = findViewById(R.id.jogador1);
        bot1 = findViewById(R.id.jogador2);
        bot2 = findViewById(R.id.jogador3);


        // Adicionar o arrastar para cima para excluir a carta
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(addArrastarItem());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarCartaNaMao(baralho.comprarCarta());
                adapter.notifyItemInserted(getItemCount());
                if (!botJogando) {
                    Toast.makeText(getBaseContext(), "Comprou uma Carta!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        comecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeJogadorPronto = nomeJogador.getText().toString();
                main_layout.setVisibility(View.INVISIBLE);
                iniciarPartida();
            }
        });

        botaoOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (compraOuJoga && !botJogando) {
                    iniciarBots();
                    //Toast.makeText(getBaseContext(), "Iniciando os bots!", Toast.LENGTH_SHORT).show();
                    compraOuJoga = false;
                    somenteNaipe = false;
                    botJogando = true;
                } else if (!compraOuJoga) {
                    Toast.makeText(getBaseContext(), "Compre ou Jogue uma Carta!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Não é a Sua vez!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        botaoRecomecar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exibirAlerta();
            }
        });
    }

    public void iniciarPartida() {
        baralho = new Baralho();
        baralho.iniciarBaralho();

        cartasList = new ArrayList<>();
        cartasBot1 = new ArrayList<>();
        cartasBot2 = new ArrayList<>();

        //iniciando as mãos
        qtdMaoBot1 = 0;
        qtdMaoBot2 = 0;
        qtdMaoJogador = 0;

        bloqueiaBot = true;
        int i;
        for (i = 0; i < 3; i++) {
            adicionarCartaNaMao(baralho.comprarCarta());
            cartasBot1 = adicionarCartaBot(baralho.comprarCarta(), cartasBot1);
            cartasBot2 = adicionarCartaBot(baralho.comprarCarta(), cartasBot2);
        }
        qtdMaoJogador = cartasList.size();
        qtdMaoBot1 = cartasBot1.size();
        qtdMaoBot2 = cartasBot2.size();

        bloqueiaBot = false;

        player.setText(nomeJogadorPronto + " = " +qtdMaoJogador);
        bot1.setText("Bot1 = " + qtdMaoBot1);
        //bot2.setText("Bot2 = "+qtdMaoBot2);

        botaoOK.setVisibility(View.VISIBLE);

        adapter = new NossoAdapter(cartasList, this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setVisibility(View.VISIBLE);
        ganhou.setVisibility(View.INVISIBLE);

        //iniciando o jogo, adicionando carta na mesa e atualizando os textos
        adicionarCartaNaMesa(baralho.comprarCarta());
        compraOuJoga = false;
        botJogando = false;
        relatorio.setText("O jogo Começou! Compre ou Jogue.");
        Toast.makeText(getBaseContext(), "Mesa: " + cartaNaMesaNome.getName().toString(), Toast.LENGTH_SHORT).show();
    }

    public void adicionarCartaNaMao(Carta carta) {
        if (botJogando) {
            Toast.makeText(getBaseContext(), "Não é Sua vez!", Toast.LENGTH_SHORT).show();
            return;
        }

        String nomeDaCarta = carta.getNome();
        Carta cartaLocal = carta;
        compraOuJoga = true;
        ++qtdMaoJogador;
        player.setText(nomeJogadorPronto + "=" +qtdMaoJogador);

        switch (nomeDaCarta) {
            case "ZERO_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.zeroazul));
                break;
            case "ZERO_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.zeroverde));
                break;
            case "ZERO_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.zeroamarelo));
                break;
            case "ZERO_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.zerovermelho));
                break;

            case "UM_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.umazul));
                break;
            case "UM_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.umverde));
                break;
            case "UM_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.umamarelo));
                break;
            case "UM_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.umvermelho));
                break;

            case "DOIS_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.doisazul));
                break;
            case "DOIS_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.doisverde));
                break;
            case "DOIS_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.doisamarelo));
                break;
            case "DOIS_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.doisvermelho));
                break;

            case "TRES_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.tresazul));
                break;
            case "TRES_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.tresverde));
                break;
            case "TRES_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.tresamarelo));
                break;
            case "TRES_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.tresvermelho));
                break;

            case "QUATRO_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.quatroazul));
                break;
            case "QUATRO_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.quatroverde));
                break;
            case "QUATRO_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.quatroamarelo));
                break;
            case "QUATRO_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.quatrovermelho));
                break;

            case "CINCO_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.cincoazul));
                break;
            case "CINCO_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.cincoverde));
                break;
            case "CINCO_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.cincoamarelo));
                break;
            case "CINCO_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.cincovermelho));
                break;

            case "SEIS_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seisazul));
                break;
            case "SEIS_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seisverde));
                break;
            case "SEIS_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seisamarelo));
                break;
            case "SEIS_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seisvermelho));
                break;

            case "SETE_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seteazul));
                break;
            case "SETE_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seteverde));
                break;
            case "SETE_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.seteamarelo));
                break;
            case "SETE_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.setevermelho));
                break;

            case "OITO_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.oitoazul));
                break;
            case "OITO_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.oitoverde));
                break;
            case "OITO_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.oitoamarelo));
                break;
            case "OITO_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.oitovermelho));
                break;

            case "NOVE_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.noveazul));
                break;
            case "NOVE_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.noveverde));
                break;
            case "NOVE_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.noveamarelo));
                break;
            case "NOVE_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.novevermelho));
                break;

            //Especiais
            case "CORINGA":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.coringa));
                break;
            case "MAIS_QUATRO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.maisquatro));
                break;
            case "BLOQUEIA_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.bloqueiaazul));
                break;
            case "BLOQUEIA_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.bloqueiaamarelo));
                break;
            case "BLOQUEIA_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.bloqueiaverde));
                break;
            case "BLOQUEIA_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.bloqueiavermelho));
                break;
            case "MAIS_DOIS_AZUL":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.maisdoisazul));
                break;
            case "MAIS_DOIS_AMARELO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.maisdoisamarelo));
                break;
            case "MAIS_DOIS_VERDE":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.maisdoisverde));
                break;
            case "MAIS_DOIS_VERMELHO":
                cartasList.add(new Carta(cartaLocal.getValor(), cartaLocal.getCor(), nomeDaCarta, R.drawable.maisdoisvermelho));
                break;
        }
    }

    public void adicionarCartaNaMesa(Carta carta) {
        String nomeDaCarta = carta.getNome();
        cartaNaMesaNome = carta;

        switch (nomeDaCarta) {
            case "ZERO_AZUL":
                cartaNaMesa.setImageResource(R.drawable.zeroazul);
                break;
            case "ZERO_VERDE":
                cartaNaMesa.setImageResource(R.drawable.zeroverde);
                break;
            case "ZERO_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.zeroamarelo);
                break;
            case "ZERO_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.zerovermelho);
                break;

            case "UM_AZUL":
                cartaNaMesa.setImageResource(R.drawable.umazul);
                break;
            case "UM_VERDE":
                cartaNaMesa.setImageResource(R.drawable.umverde);
                break;
            case "UM_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.umamarelo);
                break;
            case "UM_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.umvermelho);
                break;

            case "DOIS_AZUL":
                cartaNaMesa.setImageResource(R.drawable.doisazul);
                break;
            case "DOIS_VERDE":
                cartaNaMesa.setImageResource(R.drawable.doisverde);
                break;
            case "DOIS_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.doisamarelo);
                break;
            case "DOIS_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.doisvermelho);
                break;

            case "TRES_AZUL":
                cartaNaMesa.setImageResource(R.drawable.tresazul);
                break;
            case "TRES_VERDE":
                cartaNaMesa.setImageResource(R.drawable.tresverde);
                break;
            case "TRES_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.tresamarelo);
                break;
            case "TRES_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.tresvermelho);
                break;

            case "QUATRO_AZUL":
                cartaNaMesa.setImageResource(R.drawable.quatroazul);
                break;
            case "QUATRO_VERDE":
                cartaNaMesa.setImageResource(R.drawable.quatroverde);
                break;
            case "QUATRO_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.quatroamarelo);
                break;
            case "QUATRO_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.quatrovermelho);
                break;

            case "CINCO_AZUL":
                cartaNaMesa.setImageResource(R.drawable.cincoazul);
                break;
            case "CINCO_VERDE":
                cartaNaMesa.setImageResource(R.drawable.cincoverde);
                break;
            case "CINCO_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.cincoamarelo);
                break;
            case "CINCO_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.cincovermelho);
                break;

            case "SEIS_AZUL":
                cartaNaMesa.setImageResource(R.drawable.seisazul);
                break;
            case "SEIS_VERDE":
                cartaNaMesa.setImageResource(R.drawable.seisverde);
                break;
            case "SEIS_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.seisamarelo);
                break;
            case "SEIS_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.seisvermelho);
                break;

            case "SETE_AZUL":
                cartaNaMesa.setImageResource(R.drawable.seteazul);
                break;
            case "SETE_VERDE":
                cartaNaMesa.setImageResource(R.drawable.seteverde);
                break;
            case "SETE_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.seteamarelo);
                break;
            case "SETE_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.setevermelho);
                break;

            case "OITO_AZUL":
                cartaNaMesa.setImageResource(R.drawable.oitoazul);
                break;
            case "OITO_VERDE":
                cartaNaMesa.setImageResource(R.drawable.oitoverde);
                break;
            case "OITO_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.oitoamarelo);
                break;
            case "OITO_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.oitovermelho);
                break;

            case "NOVE_AZUL":
                cartaNaMesa.setImageResource(R.drawable.noveazul);
                break;
            case "NOVE_VERDE":
                cartaNaMesa.setImageResource(R.drawable.noveverde);
                break;
            case "NOVE_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.noveamarelo);
                break;
            case "NOVE_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.novevermelho);
                break;

            //Especiais
            case "CORINGA":
                cartaNaMesa.setImageResource(R.drawable.coringa);
                break;
            case "MAIS_QUATRO":
                cartaNaMesa.setImageResource(R.drawable.maisquatro);
                break;
            case "BLOQUEIA_AZUL":
                cartaNaMesa.setImageResource(R.drawable.bloqueiaazul);
                break;
            case "BLOQUEIA_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.bloqueiaamarelo);
                break;
            case "BLOQUEIA_VERDE":
                cartaNaMesa.setImageResource(R.drawable.bloqueiaverde);
                break;
            case "BLOQUEIA_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.bloqueiavermelho);
                break;
            case "MAIS_DOIS_AZUL":
                cartaNaMesa.setImageResource(R.drawable.maisdoisazul);
                break;
            case "MAIS_DOIS_AMARELO":
                cartaNaMesa.setImageResource(R.drawable.maisdoisamarelo);
                break;
            case "MAIS_DOIS_VERDE":
                cartaNaMesa.setImageResource(R.drawable.maisdoisverde);
                break;
            case "MAIS_DOIS_VERMELHO":
                cartaNaMesa.setImageResource(R.drawable.maisdoisvermelho);
                break;
        }
    }

    public List<Carta> adicionarCartaBot(Carta carta, List<Carta> maoBot) {
        maoBot.add(carta);
        if (!bloqueiaBot) {
            for (Carta c : maoBot) {
                if (c.getValor() == cartaNaMesaNome.getValor() || c.getCor() == cartaNaMesaNome.getValor() || cartaNaMesaNome.getValor() == "MAIS_QUATRO" ||
                        c.getValor() == "CORINGA" || c.getValor() == "MAIS_QUATRO" || cartaNaMesaNome.getValor() == "CORINGA") {
                    return (removerCartaBot(maoBot));
                    //se a carta que o bot comprou por último for igual a da mesa, ele joga e não fica com ela
                }
            }
        }
        relatorio.setText("Bot Comprou. Sua vez!");
        return maoBot;
    }

    public List<Carta> removerCartaBot(List<Carta> maoBotRecebida) {
        cartasLargadasBot = 0;
        if (maoBotRecebida.size() < 1) {
            bloqueiaBot = true;
            return maoBotRecebida;
        }
        List<Carta> maoBot = maoBotRecebida;
        for (Carta c : maoBot) {
            if (c.getValor() == cartaNaMesaNome.getValor() || c.getCor() == cartaNaMesaNome.getValor() || cartaNaMesaNome.getValor() == "MAIS_QUATRO" ||
                    c.getValor() == "CORINGA" || c.getValor() == "MAIS_QUATRO" || cartaNaMesaNome.getValor() == "CORINGA") {
                maoBot.remove(c);
                adicionarCartaNaMesa(c);
                cartasLargadasBot++;
                return maoBot;
                //return removerNaipeBot();
            }
        }
        return maoBot = adicionarCartaBot(baralho.comprarCarta(), maoBot);
    }

    public void removerCartaMao(int cartaRemovida) {
        if (botJogando) {
            Toast.makeText(getBaseContext(), "Não é Sua vez!", Toast.LENGTH_SHORT).show();
            cancelarRemocao(cartaRemovida);
            return;
        }

        if (somenteNaipe) {
            Carta cartaR = cartasList.get(cartaRemovida);
            if (cartaNaMesaNome.getValor() == cartaR.getValor()) {
                Toast.makeText(getBaseContext(), cartaR.getNome(), Toast.LENGTH_SHORT).show();
                --qtdMaoJogador;
                player.setText(nomeJogadorPronto + "=" +qtdMaoJogador);
                adicionarCartaNaMesa(cartasList.get(cartaRemovida));
                cartasList.remove(cartaRemovida);
                adapter.notifyItemRemoved(cartaRemovida);
                adapter.notifyItemRangeChanged(cartaRemovida, cartasList.size());

                return;
            } else {
                Toast.makeText(getBaseContext(), "Essa Carta não é Válida!", Toast.LENGTH_SHORT).show();
                cancelarRemocao(cartaRemovida);
            }
            return;
        }

        Carta cartaR = cartasList.get(cartaRemovida);
        if ((cartaNaMesaNome.getCor() == cartaR.getCor() || cartaNaMesaNome.getValor() == cartaR.getValor() || cartaR.getValor() == "CORINGA" ||
                cartaR.getValor() == "MAIS_QUATRO" || cartaNaMesaNome.getValor() == "CORINGA" || cartaNaMesaNome.getValor() == "MAIS_QUATRO")) {
            Toast.makeText(getBaseContext(), cartaR.getNome(), Toast.LENGTH_SHORT).show();
            compraOuJoga = true;
            --qtdMaoJogador;
            player.setText(nomeJogadorPronto + "=" +qtdMaoJogador);
            adicionarCartaNaMesa(cartasList.get(cartaRemovida));
            cartasList.remove(cartaRemovida);
            somenteNaipe = true;
            adapter.notifyItemRemoved(cartaRemovida);
            adapter.notifyItemRangeChanged(cartaRemovida, cartasList.size());
        } else {
            Toast.makeText(getBaseContext(), "Essa Carta não é Válida!", Toast.LENGTH_SHORT).show();
            cancelarRemocao(cartaRemovida);
        }
    }

    public void cancelarRemocao(int cartaRemovida) {
        adapter.notifyItemChanged(cartaRemovida);
    }

    public void iniciarBots() {
        new CountDownTimer(5000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                relatorio.setText("Bot 1 está jogando...");
            }

            @Override
            public void onFinish() {
                cartasBot1 = removerCartaBot(cartasBot1);
                if (cartasBot1.size() < 1) {
                    Toast.makeText(getBaseContext(), "Bot 1 GANHOU!", Toast.LENGTH_SHORT).show();
                    botaoOK.setVisibility(View.INVISIBLE);
                    relatorio.setText("O BOT GANHOU! HA HA");
                } else {
                    bot1.setText("Bot1 = " + cartasBot1.size());
                    botJogando = false;
                    Toast.makeText(getBaseContext(), "Sua vez!", Toast.LENGTH_SHORT).show();
                }
            }

        }.start();

        //relatorio.setText("Bot 1 está jogando...");
        //Thread.sleep(2000);
        //cartasBot1 = removerCartaBot(cartasBot1);
        //Thread.sleep(2000);
        //relatorio.setText("Bot 2 está jogando...");
        //Thread.sleep(2000);
        //cartasBot2 = removerCartaBot(cartasBot2);
        //Thread.sleep(2000);
        //relatorio.setText("Sua vez!");
        //botJogando = false;
    }

    public int getItemCount() {
        return cartasList != null ? cartasList.size() : 0;
    }

    public ItemTouchHelper.SimpleCallback addArrastarItem() {
        ItemTouchHelper.SimpleCallback deslizarItem = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Toast.makeText(getBaseContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                final int deleteViewID = viewHolder.getAdapterPosition();
                removerCartaMao(deleteViewID);

                if (cartasList.size() < 1) {
                    botaoOK.setVisibility(View.INVISIBLE);
                    ganhou.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    relatorio.setText("Você ganhou!");
                }


            }
        };
        return deslizarItem;
    }

    private void exibirAlerta() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Reiniciar");
        //define a mensagem
        builder.setMessage("Deseja reiniciar a Partida?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                iniciarPartida();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(MainActivity.this, "negativo=" + arg1, Toast.LENGTH_SHORT).show();
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private List<Carta> removerNaipeBot() {
        for (Carta c : cartasBot1) {
            if (c.getValor() == cartaNaMesaNome.getValor()) {
                adicionarCartaNaMesa(c);
                cartasBot1.remove(c); //remove da mão todas as cartas com o mesmo naipe da primeira que ele jogou
                cartasLargadasBot++;
            }
        }
        relatorio.setText("Bot Largou " + cartasLargadasBot + " carta(s). Sua vez!");
        return cartasBot1;
    }
}

