package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fogo extends Entidade implements Serializable{
    protected String image =  "fire.png";
    protected int direcao;
    private int iDirectionV;
    private int iDirectionH;
    
    public Fogo(Posicao p, int direcao) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bMortal = false;
        this.pPosicao = p;
        switch(direcao){
            case 0 -> {
                this.iDirectionH = 1;
                this.iDirectionV = 2;
            }
            case 1 -> {
                this.iDirectionH = 2;
                this.iDirectionV = 1;
            }
            case 2 -> {
                this.iDirectionH = 0;
                this.iDirectionV = 2;
            }
            case 3 -> {
                this.iDirectionH = 2;
                this.iDirectionV = 0;
            }              
        }
    }

    public void autoDesenho() {
        super.autoDesenho();
        
        if (iDirectionH == 0) {
            if(!this.canMoveLeft())
                Desenho.acessoATelaDoJogo().current.entidades.remove(this);
            else
                this.moveLeft();
        } else if(iDirectionH == 1 && this.pPosicao.getLinha() < 20) {
            System.out.println(this.pPosicao.getColuna());
            if(!this.canMoveRight())
                Desenho.acessoATelaDoJogo().current.entidades.remove(this);
            else
                this.moveRight();
            System.out.println(this.pPosicao.getColuna());
        } 
        if (iDirectionV == 0) {
            if(!this.canMoveUp())
                Desenho.acessoATelaDoJogo().current.entidades.remove(this);
            else
                this.moveUp();
        } else  if(iDirectionV == 1){
            if(!this.canMoveDown())
                Desenho.acessoATelaDoJogo().current.entidades.remove(this);
            else
                this.moveDown();
        }

    }
    
     private boolean validaPosicao(Posicao p){
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this);       
    }
    
    public boolean canMoveUp() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha() - 1, this.pPosicao.getColuna()));
    }

    public boolean canMoveDown() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha() + 1, this.pPosicao.getColuna()));
    }

    public boolean canMoveRight() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() + 1));
    }

    public boolean canMoveLeft() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() - 1));
    }    

    @Override
    public boolean setPosicao(int linha, int coluna){
        Posicao p = new Posicao(linha, coluna);
        if(Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this)){
            return this.pPosicao.setPosicao(p);
        }
        return false;       
    }
}
