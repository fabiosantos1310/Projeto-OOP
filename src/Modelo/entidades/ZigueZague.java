package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import javax.swing.ImageIcon;

public class ZigueZague extends Entidade implements Serializable{ // 0xFF5fcde4
    protected String[] images = {"robo.png", null, null, null, null};

    public ZigueZague(int faseAtual, Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        setPosicao(p);
    }

    public void autoDesenho(){
        Random rand = new Random();
        int iDirecao = rand.nextInt(4);
        
        if(iDirecao == 1 && this.validaPosicao(new Posicao(pPosicao.getLinha(), pPosicao.getColuna()+1)))
            this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()+1);
        else if(iDirecao == 2 && this.validaPosicao(new Posicao(pPosicao.getLinha() + 1, pPosicao.getColuna())))
            this.setPosicao(pPosicao.getLinha()+1, pPosicao.getColuna());
        else if(iDirecao == 3 && this.validaPosicao(new Posicao(pPosicao.getLinha(), pPosicao.getColuna() - 1)))
            this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna()-1);
        else if(iDirecao == 4 && this.validaPosicao(new Posicao(pPosicao.getLinha() - 1, pPosicao.getColuna())))
            this.setPosicao(pPosicao.getLinha()-1, pPosicao.getColuna());
        
        super.autoDesenho();
    }  
    private boolean validaPosicao(Posicao p){
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this);       
    }
}
