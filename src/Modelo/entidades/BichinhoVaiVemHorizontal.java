package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class BichinhoVaiVemHorizontal extends Entidade implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String[] images = { "roboPink.png", null, null, null, null };
    private boolean bRight;
    int iContador;
    private int entidadeFaseAtualIndex;

    public BichinhoVaiVemHorizontal(int faseAtual, Posicao p) {
        super();
        this.entidadeFaseAtualIndex = faseAtual;
        try{
            if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[this.entidadeFaseAtualIndex]);
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        bRight = true;
        iContador = 0;
        this.bTransponivel = false;
        this.bMortal = true;
        this.setPosicao(p);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[this.entidadeFaseAtualIndex]);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para BichinhoVaiVemHorizontal: " + e.getMessage());
            }
        }
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
        if (Desenho.acessoATelaDoJogo() == null || pPosicao == null) return false;
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(new Posicao(pPosicao.getLinha(), coluna), this);    
    }
}