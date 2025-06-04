package Controler;

import Modelo.entidades.Entidade;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Chaser;
import Modelo.entidades.BichinhoVaiVemHorizontal;
import Auxiliar.*;
import Modelo.entidades.BichinhoVaiVemVertical;
import Modelo.entidades.ZigueZague;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import Modelo.fases.Fase;
import Modelo.entidades.Hero;
import javax.swing.JLabel;


public class Tela extends javax.swing.JFrame implements MouseListener, KeyListener {

    private Hero hero;
    public  ControleDeJogo cj = new ControleDeJogo();
    private transient Graphics g2;
    private int cameraLinha = 0;
    private int cameraColuna = 0;
    public Consts c = new Consts();
    public Mundo mundo;
    public Fase current;

    public Tela() {
        current = Game.fases.get(Game.faseAtual);
        Desenho.setCenario(this);
        initComponents();
        this.addMouseListener(this);
        this.addKeyListener(this);
        this.setSize(Consts.RES * Consts.CELL_SIDE + getInsets().left + getInsets().right,
                Consts.RES * Consts.CELL_SIDE + getInsets().top + getInsets().bottom);
        mundo = new Mundo();
        
        mundo.carregaMundo(current, c);
        hero = current.getHero();
        
        if (hero != null) {
            this.atualizaCamera();
        } else {
            System.err.println("Erro: Herói não inicializado na fase de construção da Tela.");
        }   
    }

    public int getCameraLinha() {
        return cameraLinha;
    }

    public int getCameraColuna() {
        return cameraColuna;
    }

    public boolean ehPosicaoValida(Posicao p, Entidade e) {
        return cj.ehPosicaoValida(this.current, p, e);
    }

    public Graphics getGraphicsBuffer() {
        return g2;
    }

    public void paint(Graphics gOld) {
        if (this.getBufferStrategy() == null) {
            this.createBufferStrategy(2);
            return;
        }
        Graphics g = this.getBufferStrategy().getDrawGraphics();
        g2 = g.create(getInsets().left, getInsets().top, getWidth() - getInsets().right, getHeight() - getInsets().top);

        for (int i = 0; i < Consts.RES; i++) {
            for (int j = 0; j < Consts.RES; j++) {
                int mapaLinha = cameraLinha + i;
                int mapaColuna = cameraColuna + j;

                if (mapaLinha < c.MUNDO_ALTURA && mapaColuna < c.MUNDO_LARGURA) {
                    try {
                        Image newImage = Toolkit.getDefaultToolkit().getImage(
                                new java.io.File(".").getCanonicalPath() + Consts.PATH + "blackTile.png");
                        g2.drawImage(newImage,
                                j * Consts.CELL_SIDE, i * Consts.CELL_SIDE,
                                Consts.CELL_SIDE, Consts.CELL_SIDE, null);
                    } catch (IOException ex) {
                        Logger.getLogger(Tela.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (current != null && !current.entidades.isEmpty()) {
            this.cj.desenhaTudo(current);
            this.cj.processaTudo(current);
        }

        g.dispose();
        g2.dispose();
        if (!getBufferStrategy().contentsLost()) {
            getBufferStrategy().show();
        }
    }

    public void atualizaCamera() {
        if (hero == null) return;
        int linha = hero.getPosicao().getLinha();
        int coluna = hero.getPosicao().getColuna();

        cameraLinha = Math.max(0, Math.min(linha - Consts.RES / 2, c.MUNDO_ALTURA - Consts.RES));
        cameraColuna = Math.max(0, Math.min(coluna - Consts.RES / 2, c.MUNDO_LARGURA - Consts.RES));
    }

    public void go() {
        java.util.Timer timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run() {
               
                repaint();
            }
        };
        timer.schedule(task, 0, Consts.PERIOD);
    }
    
    public void keyPressed(KeyEvent e) {
        if (hero == null && e.getKeyCode() != KeyEvent.VK_L && e.getKeyCode() != KeyEvent.VK_S) { 
             System.err.println("Herói é nulo, ações de jogo desabilitadas exceto carregar ou salvar.");
             return;
        }
        
        if (hero == null && e.getKeyCode() == KeyEvent.VK_S) {
             System.out.println("Tecla S pressionada (Herói nulo) - Tentando salvar jogo...");
             Game.salvarJogo();
             return; 
        }
        
        if (hero == null && e.getKeyCode() == KeyEvent.VK_L) {
        } else if (hero == null) {
            System.err.println("Herói é nulo, ação de jogo '" + KeyEvent.getKeyText(e.getKeyCode()) + "' desabilitada.");
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_C) {
            if(this.current != null) this.current.entidades.clear();
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if(hero != null) {hero.moveUp();}
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if(hero != null) {hero.moveDown();}
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(hero != null) {hero.moveLeft();}
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(hero != null) {
                hero.moveRight();
            }
        }  
        if (e.getKeyCode() == KeyEvent.VK_S) {
            System.out.println("Tecla S pressionada - Salvando jogo...");
            Game.salvarJogo(); 
        } else if (e.getKeyCode() == KeyEvent.VK_L) {
            System.out.println("Tecla L pressionada - Carregando jogo...");
            if (Game.carregarJogo()) {
                this.current = Game.fases.get(Game.faseAtual);
                if (this.current != null) {
                    this.hero = this.current.getHero();
                    if (this.hero == null) {
                        System.err.println("Alerta: Herói não encontrado na fase carregada. Verifique o arquivo de save ou a lógica de getHero().");
                    }
                } else {
                     System.err.println("Erro: Fase carregada resultou em 'current' nulo para o índice " + Game.faseAtual);
                }
                System.out.println("Estado da Tela atualizado com a fase carregada.");
            } else {
                System.err.println("Falha ao carregar o jogo. Estado da tela não alterado.");
            }
        }

        if (this.hero != null) { 
            this.atualizaCamera();
            this.setTitle("-> Cell: " + (hero.getPosicao().getColuna()) + ", "
                    + (hero.getPosicao().getLinha()));
        } else {
             this.setTitle("Skooter - Herói não disponível"); 
        }
        repaint();
    }

    public void mousePressed(MouseEvent e) {
        
    }
    
    public void passarFase(){
        if (current == null) return;
        mundo.apagarMundo(current);
        cj.limparListas();
        if (Game.faseAtual < Game.fases.size()) {
            current = Game.fases.get(Game.faseAtual);
            reiniciar();
        } else {
            System.out.println("Todas as fases foram completadas!!");
        }
    }
    
    public void reiniciar() {
        if (current == null) {
            System.err.println("Não é possível reiniciar, fase atual ('current') é nula.");
            if (Game.faseAtual >= 0 && Game.faseAtual < Game.fases.size()) {
                this.current = Game.fases.get(Game.faseAtual);
                 if (this.current == null) {
                     System.err.println("Falha ao obter a fase " + Game.faseAtual + " da lista global de fases.");
                     return;
                 }
            } else {
                 System.err.println("Índice de fase atual " + Game.faseAtual + " é inválido.");
                 return;
            }
        }
        hero = null;
        mundo.recomecarFase(current, c);
        hero = current.getHero();
        if (hero != null) {
            this.atualizaCamera();
        } else {
            System.err.println("Erro: Herói não encontrado após reiniciar a fase.");
        }
        repaint();
    }
    


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("POO2023-1 - Skooter");
        setAlwaysOnTop(true);
        setAutoRequestFocus(false);
        setResizable(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 561, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    public void mouseMoved(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}
