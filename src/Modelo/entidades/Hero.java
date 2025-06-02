package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Game;
import Controler.ControleDeJogo;
import Controler.Tela;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class Hero extends Entidade implements Serializable{ //0xFFfbf236
    
    protected String[] images = { "robbo.png", "robbo.png", "donkeykong.png", "robbo.png", "diddykong.png" };
    protected String clonePng = "diddykong.png";
    private Queue<Posicao> posicoes;
    Posicao ultimaPosicao;
    public boolean temChave = false;
    public int chaves = 0;
    public int direcao = 0;
    public int moedas = 0;
    public boolean isClone;
    private Fase fase;
    private Tela tela = Desenho.acessoATelaDoJogo();
    
    public Hero(Posicao p, boolean isClone, int faseAtual) {
        posicoes = new LinkedList<>();
        this.isClone = isClone;
        fase = Game.fases.get(faseAtual);

        try{
            if(this.isClone)
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.clonePng);
            else
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        
        setPosicao(p);
        Posicao aux = new Posicao(p.getLinha(), p.getColuna());
        posicoes.add(aux);
        ultimaPosicao = p;
        this.bTransponivel = false;
        this.bMortal = true;
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public void removeChave(){
        chaves--;
        if(chaves == 0)
            temChave = false;
    }
    
    public void removeMoedas(int val){
        this.moedas -= val;
    }
    
    public void addMoeda(){
        this.moedas++;
    }
    
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!tela.ehPosicaoValida(this.getPosicao(), this)) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!tela.ehPosicaoValida(this.getPosicao(), this)) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public void morrer(){
       
        JOptionPane.showMessageDialog(tela, "Você morreu! A fase será reiniciada.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        tela.cj.limparListas();
        tela.reiniciar(); 
    }
    
    public Posicao getPosicaoAntiga(){
        if(!this.posicoes.isEmpty())
            return this.posicoes.poll();
        return ultimaPosicao;
    }
    
    public boolean moveUp() {
        direcao = 3;
        if(!this.isClone){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveUp()){
            if(fase.hasClone && !this.isClone)
                fase.getClone().moveUp();
            if(validaPosicao()){
                tela.atualizaCamera();
                return true;
            }       
        }
        return false;

    }

    public boolean moveDown() {
        direcao = 1;
        if(!this.isClone){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveDown()){
            if(fase.hasClone && !this.isClone)
                fase.getClone().moveDown();
            if(validaPosicao()){
                tela.atualizaCamera();
                return true;
            }        
        }

        return false;
    }

    public boolean moveRight() {
        direcao = 0;
        if(!this.isClone){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveRight()){
            if(fase.hasClone && !this.isClone)
                fase.getClone().moveLeft();
            if(validaPosicao()){
                tela.atualizaCamera();
                return true;
            }
        }
        return false;
    }

    public boolean moveLeft() {
        direcao = 2;
        if(!this.isClone){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveLeft()){
            if(fase.hasClone && !this.isClone)
                fase.getClone().moveRight();
            if(validaPosicao()){
                tela.atualizaCamera();
                return true;
            }     
        }
                      
        return false;
    }    
}
