/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
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

    protected String[] images = { null, "chaser.png", null, null, "chaser.png" }; 

    
    private boolean iDirectionV;
    private boolean iDirectionH;
    private int canMove = 0;
    private Tela tela = Desenho.acessoATelaDoJogo();

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
        Fase fase = tela.current;
        if(pPosicao.igual(fase.getHero().getPosicao())){
            fase.entidades.remove(this);
            fase.getHero().morrer();
        }
        super.autoDesenho();
        if(canMove == 15){
            this.setPosicao(fase.getHero().getPosicaoAntiga());
            canMove = 12;
        }
        if(canMove != 15)
            canMove++;

    }   
}
