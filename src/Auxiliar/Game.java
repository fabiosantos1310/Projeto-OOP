package Auxiliar;

import Controler.Tela;
import Modelo.fases.Fase;
import java.util.ArrayList;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Game {

    public static int faseAtual = 0;
    public static ArrayList<Fase> fases = new ArrayList<Fase>();
    public static Tela tTela; 

    static class SavedGameState implements Serializable {
        private static final long serialVersionUID = 1L; 
        public int faseAtualIndex;
        public Fase faseObject;
        public int mundoLargura;
        public int mundoAltura;

        public SavedGameState(int faseAtualIndex, Fase faseObject, int mundoLargura, int mundoAltura) {
            this.faseAtualIndex = faseAtualIndex;
            this.faseObject = faseObject;
            this.mundoLargura = mundoLargura;
            this.mundoAltura = mundoAltura;
        }

        public int getFaseAtualIndex() {
            return faseAtualIndex;
        }

        public Fase getFaseObject() {
            return faseObject;
        }

        public int getMundoLargura() {
            return mundoLargura;
        }

        public int getMundoAltura() {
            return mundoAltura;
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            fases.add(new Fase("mapaFase" + (i + 1) + ".png", i));
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Game.tTela = new Tela(); 
                Desenho.setCenario(Game.tTela); 
                Game.tTela.setVisible(true);
                if (Game.tTela.isDisplayable()) {
                    Game.tTela.createBufferStrategy(2);
                }
                Game.tTela.go();
            }
        });
    }

    public static void salvarJogo() {
        if (tTela == null || tTela.current == null || tTela.c == null) {
            System.err.println("Erro: Componentes da Tela (current Fase ou Consts c) não inicializados ou acessíveis para salvar.");
            return;
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Consts.SAVE_FILE_PATH))) {
            SavedGameState gameState = new SavedGameState(
                    faseAtual,
                    tTela.current,
                    tTela.c.MUNDO_LARGURA,
                    tTela.c.MUNDO_ALTURA);
            oos.writeObject(gameState);
            System.out.println("Jogo salvo em " + Consts.SAVE_FILE_PATH);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean carregarJogo() {
        if (tTela == null) {
            System.err.println("Erro: Tela não inicializada para carregar o jogo.");
            return false;
        }
        if (tTela.c == null) {
            System.err.println("Erro: Objeto Consts 'c' da tela é nulo ao carregar.");
            return false;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Consts.SAVE_FILE_PATH))) {
            SavedGameState loadedState = (SavedGameState) ois.readObject();

            faseAtual = loadedState.getFaseAtualIndex();
            Fase loadedFase = loadedState.getFaseObject();

            if (loadedFase == null) {
                System.err.println("Erro: Fase carregada é nula.");
                return false;
            }
            if (faseAtual < 0) {
                 System.err.println("Erro: Índice de fase carregado é inválido: " + faseAtual);
                 return false;
            }

            while (fases.size() <= faseAtual) {
                fases.add(null);
            }
            fases.set(faseAtual, loadedFase);
            
            tTela.current = loadedFase;

            tTela.c.setLargura(loadedState.getMundoLargura()); 
            tTela.c.setAltura(loadedState.getMundoAltura());
            
            System.out.println("Jogo carregado de " + Consts.SAVE_FILE_PATH + ". Fase: " + faseAtual);
            return true;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Falha ao carregar o jogo: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (ClassCastException e) {
            System.err.println("Falha ao carregar o jogo: Formato do arquivo de save incompatível. " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}