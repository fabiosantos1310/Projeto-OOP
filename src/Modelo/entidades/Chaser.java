/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author 2373891
 */
public class Chaser extends Entidade implements Serializable { // 0xFF76428a

    protected String[] images = { "chaser.png", "chaser.png", null, null, null }; 

    
    private boolean iDirectionV;
    private boolean iDirectionH;
    private int canMove = 0;

    public Chaser(int faseAtual, Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        iDirectionV = true;
        iDirectionH = true;
        
        this.pPosicao = p;
        
        this.bTransponivel = false;
    }
    
    @Override
    public void setPosicao(Posicao p){
        this.pPosicao.setPosicao(p);
       
    }

    public void autoDesenho() {
        Fase fase = Desenho.acessoATelaDoJogo().current;
        if(pPosicao.igual(fase.getHero().getPosicao())){
            fase.entidades.remove(this);
            fase.getHero().morrer();
        }
        super.autoDesenho();
        if(canMove == 15){
            this.setPosicao(fase.getHero().getPosicaoAntiga());
            canMove = 15;
        }
        if(canMove != 15)
            canMove++;

    }

     private boolean validaPosicao(){
        if (!Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao(), this)) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;       
    }
     
    public void voltaAUltimaPosicao(){
        this.pPosicao.volta();
    }
    
    public boolean moveUp() {
        if(super.moveUp())
            return validaPosicao();
        return false;
    }

    public boolean moveDown() {
        if(super.moveDown())
            return validaPosicao();
        return false;
    }

    public boolean moveRight() {
        if(super.moveRight())
            return validaPosicao();
        return false;
    }

    public boolean moveLeft() {
        if(super.moveLeft())
            return validaPosicao();
        return false;
    }    
}
