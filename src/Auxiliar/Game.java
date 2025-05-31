package Auxiliar;

import Controler.Tela;
import Modelo.fases.Fase;
import java.util.ArrayList;

public class Game {

    public static int faseAtual = 0;
    public static ArrayList<Fase> fases = new ArrayList<Fase>();

    public static void main(String[] args) {
        for(int i = 0; i < 5; i++){
            fases.add(new Fase("mapaFase" + (i+1) + ".png", i));
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Tela tTela = new Tela();
                tTela.setVisible(true);
                tTela.createBufferStrategy(2);
                tTela.go();
            }
        });
    }
}

