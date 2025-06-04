package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class BichinhoVaiVemVertical extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String[] images = { "robo.png", null, null, null, null };
    boolean bUp;
    private int entidadeFaseAtualIndex;

    public BichinhoVaiVemVertical(int faseAtual, Posicao p) {
        super();
        this.entidadeFaseAtualIndex = faseAtual;
        try{
            if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[this.entidadeFaseAtualIndex]);
            }
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        bUp = true;
        this.setPosicao(p);
        this.bTransponivel = false;
        this.bMortal = true; // Garante que o bicho é letal
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[this.entidadeFaseAtualIndex]);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para BichinhoVaiVemVertical: " + e.getMessage());
            }
        }
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
        if (Desenho.acessoATelaDoJogo() == null || pPosicao == null) return false;
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(new Posicao(linha, pPosicao.getColuna()), this);    
    }
}