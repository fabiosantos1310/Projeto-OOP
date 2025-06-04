
package Auxiliar;

import Controler.Tela;
import Modelo.fases.Fase;
import java.util.ArrayList;
import Modelo.entidades.Entidade;
import Modelo.entidades.BichinhoVaiVemHorizontal;
import Modelo.entidades.BichinhoVaiVemVertical;
import auxiliar.Posicao;

public class Game {

    public static int faseAtual = 0;
    public static ArrayList<Fase> fases = new ArrayList<Fase>();
    public static Tela tTela;

    static class SavedGameState implements java.io.Serializable {
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

        public int getFaseAtualIndex() { return faseAtualIndex; }
        public Fase getFaseObject() { return faseObject; }
        public int getMundoLargura() { return mundoLargura; }
        public int getMundoAltura() { return mundoAltura; }
    }

    public static void main(String[] args) {
        fases.clear();
        for (int i = 0; i < 5; i++) {
            fases.add(new Fase("mapaFase" + (i + 1) + ".png", i));
        }
        faseAtual = 0;

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Game.tTela = new Tela();
                Desenho.setCenario(Game.tTela);
                Game.tTela.setVisible(true);

                try {
                    if (!Game.tTela.isDisplayable()) {
                        Game.tTela.addNotify();
                        Thread.sleep(100);
                    }
                    if (Game.tTela.isDisplayable()) {
                        Game.tTela.createBufferStrategy(2);
                    } else {
                        System.err.println("Tela não se tornou 'displayable' a tempo para createBufferStrategy.");
                    }
                } catch (IllegalStateException e) {
                    System.err.println("Erro ao criar BufferStrategy (IllegalStateException): " + e.getMessage());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread interrompida ao esperar por displayable state.");
                }
                Game.tTela.go();
            }
        });
    }

    public static void salvarJogo() {
        if (tTela == null || tTela.current == null || tTela.c == null) {
            System.err.println("Erro: Componentes da Tela (current Fase ou Consts c) não inicializados para salvar.");
            return;
        }

        System.out.println("DEBUG_PRE_SAVE: Iniciando salvamento para Fase " + tTela.current.indice);
        if (tTela.current.entidades != null) {
            System.out.println("DEBUG_PRE_SAVE: Total Entidades: " + tTela.current.entidades.size());
            int bvhCount = 0;
            for (Entidade ent : tTela.current.entidades) {
                if (ent instanceof BichinhoVaiVemHorizontal) {
                    bvhCount++;
                    Posicao p = ent.getPosicao();
                    String posStr = (p != null) ? ("(" + p.getLinha() + "," + p.getColuna() + ")") : "NULL_POS";
                    System.out.println("  DEBUG_PRE_SAVE: BVH ID: " + System.identityHashCode(ent) + " Pos: " + posStr);
                }
            }
            System.out.println("DEBUG_PRE_SAVE: Contagem BVH: " + bvhCount);
            int bvvCount = 0;
            for (Entidade ent : tTela.current.entidades) {
                if (ent instanceof BichinhoVaiVemVertical) {
                    bvvCount++;
                    Posicao p = ent.getPosicao();
                    String posStr = (p != null) ? ("(" + p.getLinha() + "," + p.getColuna() + ")") : "NULL_POS";
                    System.out.println("  DEBUG_PRE_SAVE: BVV ID: " + System.identityHashCode(ent) + " Pos: " + posStr);
                }
            }
            System.out.println("DEBUG_PRE_SAVE: Contagem BVV: " + bvvCount);
        } else {
            System.out.println("DEBUG_PRE_SAVE: tTela.current.entidades é null.");
        }

        try (java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(new java.io.FileOutputStream(Consts.SAVE_FILE_PATH))) {
            SavedGameState gameState = new SavedGameState(
                    faseAtual,
                    tTela.current,
                    tTela.c.MUNDO_LARGURA,
                    tTela.c.MUNDO_ALTURA);
            oos.writeObject(gameState);
            System.out.println("Jogo salvo em " + Consts.SAVE_FILE_PATH);
        } catch (java.io.IOException e) {
            System.err.println("Erro ao salvar o jogo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean carregarJogo() {
        if (tTela == null) {
            System.err.println("Erro: Instância Tela (tTela) é nula. Não é possível carregar.");
            return false;
        }
        if (tTela.c == null) {
            System.err.println("Erro: Objeto Consts 'c' da tela é nulo ao carregar.");
            return false;
        }

        try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(Consts.SAVE_FILE_PATH))) {
            SavedGameState loadedState = (SavedGameState) ois.readObject();

            faseAtual = loadedState.getFaseAtualIndex();
            Modelo.fases.Fase loadedFase = loadedState.getFaseObject();

            if (loadedFase == null) {
                System.err.println("Erro: Fase carregada é nula.");
                return false;
            }

            System.out.println("DEBUG_POST_LOAD: Jogo carregado. Fase desserializada: " + loadedFase.indice);
            if (loadedFase.entidades != null) {
                System.out.println("DEBUG_POST_LOAD: Total Entidades: " + loadedFase.entidades.size());
                int bvhCount = 0;
                for (Entidade ent : loadedFase.entidades) {
                    if (ent instanceof BichinhoVaiVemHorizontal) {
                        bvhCount++;
                        Posicao p = ent.getPosicao();
                        String posStr = (p != null) ? ("(" + p.getLinha() + "," + p.getColuna() + ")") : "NULL_POS";
                        System.out.println("  DEBUG_POST_LOAD: BVH ID: " + System.identityHashCode(ent) + " Pos: " + posStr);
                    }
                }
                System.out.println("DEBUG_POST_LOAD: Contagem BVH: " + bvhCount);
                int bvvCount = 0;
                for (Entidade ent : loadedFase.entidades) {
                    if (ent instanceof BichinhoVaiVemVertical) {
                        bvvCount++;
                        Posicao p = ent.getPosicao();
                        String posStr = (p != null) ? ("(" + p.getLinha() + "," + p.getColuna() + ")") : "NULL_POS";
                        System.out.println("  DEBUG_POST_LOAD: BVV ID: " + System.identityHashCode(ent) + " Pos: " + posStr);
                    }
                }
                System.out.println("DEBUG_POST_LOAD: Contagem BVV: " + bvvCount);
            } else {
                System.out.println("DEBUG_POST_LOAD: loadedFase.entidades é null.");
            }

            if (faseAtual < 0) {
                System.err.println("Erro: Índice de fase carregado (" + faseAtual + ") é inválido.");
                return false;
            }
            while (fases.size() <= faseAtual) {
                fases.add(null);
            }
            if (faseAtual >= fases.size()) {
                System.err.println("Erro: Índice de fase carregado (" + faseAtual + ") fora dos limites (tamanho: " + fases.size() + ").");
                return false;
            }

            fases.set(faseAtual, loadedFase);

            if (tTela.current != null && tTela.current.entidades != null) {
                tTela.current.entidades.clear();
            }

            tTela.current = loadedFase;
            tTela.c.setLargura(loadedState.getMundoLargura());
            tTela.c.setAltura(loadedState.getMundoAltura());

            System.out.println("Jogo carregado de " + Consts.SAVE_FILE_PATH + ". Fase: " + faseAtual);
            return true;
        } catch (java.io.IOException | ClassNotFoundException e) {
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

