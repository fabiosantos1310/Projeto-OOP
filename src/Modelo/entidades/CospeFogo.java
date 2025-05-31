package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class CospeFogo extends Entidade implements Serializable{//0xFFac3232
    public int iContaIntervalos;
    private int direcao;
    
    protected String[] images = { "caveira.png", null, null, null, null };

    
    public CospeFogo(int faseAtual, Posicao p, int direcao) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = false;
        bMortal = false;
        this.iContaIntervalos = 0;
        this.direcao = direcao;
        setPosicao(p);
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();

        if(this.iContaIntervalos == Consts.TIMER){
            this.iContaIntervalos = 0;
            Fogo f;
            switch(direcao){
                case 1 -> f = new Fogo(new Posicao(pPosicao.getLinha() + 1, pPosicao.getColuna()), direcao);
                case 2 -> f = new Fogo(new Posicao(pPosicao.getLinha(), pPosicao.getColuna()- 1), direcao);
                case 3 -> f = new Fogo(new Posicao(pPosicao.getLinha() - 1, pPosicao.getColuna()), direcao);
                default -> f = new Fogo(new Posicao(pPosicao.getLinha(), pPosicao.getColuna()+ 1), direcao);
            }
            
            Desenho.acessoATelaDoJogo().current.entidades.add(f);
        }
    }    
}
