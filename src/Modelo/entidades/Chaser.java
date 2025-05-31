/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author 2373891
 */
public class Chaser extends Entidade implements Serializable { // 0xFF76428a

    protected String[] images = { "chaser.png", null, null, null, null }; 

    
    private boolean iDirectionV;
    private boolean iDirectionH;
    private boolean canMove = true;

    public Chaser(int faseAtual, Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        iDirectionV = true;
        iDirectionH = true;
        
        setPosicao(p);
        
        this.bTransponivel = false;
    }

    public void computeDirection(Posicao heroPos) {
        if (heroPos.getColuna() < this.getPosicao().getColuna()) {
            iDirectionH = true;
        } else if (heroPos.getColuna() > this.getPosicao().getColuna()) {
            iDirectionH = false;
        }
        if (heroPos.getLinha() < this.getPosicao().getLinha()) {
            iDirectionV = true;
        } else if (heroPos.getLinha() > this.getPosicao().getLinha()) {
            iDirectionV = false;
        }
    }
    
    @Override
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            return Desenho.acessoATelaDoJogo().ehPosicaoValida(this.getPosicao(), this);
        }
        return false;       
    }

    public void autoDesenho() {
        super.autoDesenho();
        if(canMove){
            if (iDirectionH) {
                this.moveLeft();
            } else {
                this.moveRight();
            }
            if (iDirectionV) {
                this.moveUp();
            } else {
                this.moveDown();
            }
        }
        canMove = !canMove;
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
