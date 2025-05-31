package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
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
    
    protected String heroPNG = "robbo.png";
    protected boolean vivo = true;
    private Queue<Posicao> posicoes;
    Posicao ultimaPosicao;
    public boolean temChave = false;
    public int chaves = 0;
    
    public Hero(Posicao p) {
        posicoes = new LinkedList<>();
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.heroPNG);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        setPosicao(p);
        Posicao aux = new Posicao(p.getLinha(), p.getColuna());
        posicoes.add(aux);
        ultimaPosicao = p;
        this.bTransponivel = true;
    }

    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public void removeChave(){
        chaves--;
        if(chaves == 0)
            temChave = false;
    }
    
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao(), this)) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;       
    }

    /*TO-DO: este metodo pode ser interessante a todos os personagens que se movem*/
    private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao(), this)) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
    
    public void morrer(){
        if(vivo){
            JOptionPane.showMessageDialog(Desenho.acessoATelaDoJogo(), "Você morreu! A fase será reiniciada.", "Game Over", JOptionPane.INFORMATION_MESSAGE);

            Desenho.acessoATelaDoJogo().reiniciar();
            vivo = false;
        }
    }
    
    public Posicao getPosicaoAntiga(){
        if(!this.posicoes.isEmpty())
            return this.posicoes.poll();
        return ultimaPosicao;
    }
    
    public boolean moveUp() {
        Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
        posicoes.add(aux);

        if(super.moveUp()){
            return validaPosicao();       

        }
        return false;
    }

    public boolean moveDown() {
        Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
        posicoes.add(aux);

        if(super.moveDown())
            return validaPosicao();       

        return false;
    }

    public boolean moveRight() {
        Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
        posicoes.add(aux);        
        
        if(super.moveRight())
            return validaPosicao();       
        return false;
    }

    public boolean moveLeft() {
        Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
        posicoes.add(aux);

        if(super.moveLeft())
            return validaPosicao();
                      
        return false;
    }    
    
}
