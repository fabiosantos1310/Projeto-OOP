package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class BichinhoVaiVemHorizontal extends Entidade implements Serializable { // #0xFF99e550

    protected String[] images = { "roboPink.png", null, null, null, null };
    private boolean bRight;
    int iContador;

    public BichinhoVaiVemHorizontal(int faseAtual, Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        bRight = true;
        iContador = 0;
        this.setPosicao(p);
    }

    public void autoDesenho() {
        if (iContador == 5) {
            iContador = 0;
            if (bRight && this.validaPosicao(pPosicao.getColuna() + 1)) {
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() + 1);
            } else if (this.validaPosicao(pPosicao.getColuna() - 1)){
                this.setPosicao(pPosicao.getLinha(), pPosicao.getColuna() - 1);
            }

            bRight = !bRight;
        }
        super.autoDesenho();
        iContador++;
    }
    private boolean validaPosicao(int coluna){
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(new Posicao(pPosicao.getLinha(), coluna), this);       
    }
}
