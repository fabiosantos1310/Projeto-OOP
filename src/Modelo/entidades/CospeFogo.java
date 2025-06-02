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
    Tela tela = Desenho.acessoATelaDoJogo();
    int faseAtual;
    
    protected String[] images = { "caveira.png",  null,  "canhao.png", "caveira.png", "canhao.png" };

    
    public CospeFogo(int faseAtual, Posicao p, int direcao) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.faseAtual = faseAtual;
        this.bTransponivel = false;
        bMortal = false;
        this.iContaIntervalos = 0;
        this.direcao = direcao;
        switch(this.direcao){
            case 0 -> this.iImage = this.girarImagem(this.iImage, 90);
            case 1 -> this.iImage = this.girarImagem(this.iImage, 180);
            case 2 -> this.iImage = this.girarImagem(this.iImage, 270);
        }
        setPosicao(p);
        iContaIntervalos = 0;
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        this.iContaIntervalos++;
        if(this.iContaIntervalos >= Consts.TIMER + 10){
            this.iContaIntervalos = 0;
            Fogo f;
            switch(direcao){
                case 1 -> f = new Fogo(faseAtual, new Posicao(pPosicao), direcao);
                case 2 -> f = new Fogo(faseAtual, new Posicao(pPosicao), direcao);
                case 3 -> f = new Fogo(faseAtual, new Posicao(pPosicao), direcao);
                default -> f = new Fogo(faseAtual, new Posicao(pPosicao), direcao);
            }
            
            tela.current.fogos.add(f);
        }
    }    
}
