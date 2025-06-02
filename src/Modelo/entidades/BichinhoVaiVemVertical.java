package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;

public class BichinhoVaiVemVertical extends Entidade{ // 0xFFd77bba
    protected String[] images = { "robo.png", null, null, null, null };

    boolean bUp;
    public BichinhoVaiVemVertical(int faseAtual, Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        bUp = true;
        this.setPosicao(p);
        this.bTransponivel = false;

    }

    public void autoDesenho(){
        if(bUp && this.validaPosicao(pPosicao.getLinha()-1))
            this.setPosicao(pPosicao.getLinha()-1, pPosicao.getColuna());
        else if(this.validaPosicao(pPosicao.getLinha()+1))
            this.setPosicao(pPosicao.getLinha()+1, pPosicao.getColuna());           

        super.autoDesenho();
        bUp = !bUp;
    }  
    private boolean validaPosicao(int linha){
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(new Posicao(linha, pPosicao.getColuna()), this);       
    }
}
