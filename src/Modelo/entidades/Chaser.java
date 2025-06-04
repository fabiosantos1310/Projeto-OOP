package Modelo.entidades; // Exemplo para Chaser

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream; // Importar ObjectInputStream
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Chaser extends Entidade implements Serializable { 

    protected String[] images = { null, "chaser.png", null, null, "chaser.png" };    
    public int chaserFaseAtualIndex;
    private int canMove = 0;
    private transient Tela tela;

    public Chaser(int faseAtual, Posicao p) {
        super(p);
        this.chaserFaseAtualIndex = faseAtual;
        this.tela = Desenho.acessoATelaDoJogo();

        String imageName = null;
        if (this.images != null && this.chaserFaseAtualIndex >=0 && this.chaserFaseAtualIndex < this.images.length) {
            imageName = this.images[this.chaserFaseAtualIndex];
        }

        if (imageName != null) {
            try{
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
                
        this.bTransponivel = false;
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta: Chaser - Desenho.acessoATelaDoJogo() nulo durante readObject.");
        }
        
        String imageNameForLoad = null;
        if (this.images != null && this.chaserFaseAtualIndex >=0 && this.chaserFaseAtualIndex < this.images.length) {
            imageNameForLoad = this.images[this.chaserFaseAtualIndex];
        }

        if (imageNameForLoad != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageNameForLoad);
            } catch (java.io.IOException e) {
                System.err.println("Erro ao recarregar imagem para Chaser: " + imageNameForLoad + " - " + e.getMessage());
            }
        } else {
            System.err.println("Alerta: Chaser - Nome da imagem não pôde ser determinado para recarregar iImage.");
        }
    }

    @Override
    public void setPosicao(Posicao p){
        if (this.pPosicao != null) {
            this.pPosicao.setPosicao(p);
        } else {
            this.pPosicao = p;
        }
    }

    public void autoDesenho() {
        if (this.tela == null || this.tela.current == null || this.tela.current.getHero() == null || this.pPosicao == null) {
            super.autoDesenho();
            return;
        }
        Fase faseCorrente = tela.current; // Use a referência 'tela' que foi re-linkada
        if(pPosicao.igual(faseCorrente.getHero().getPosicao())){
            faseCorrente.entidades.remove(this);
            faseCorrente.getHero().morrer();
        }
        super.autoDesenho();
        if(canMove == 15){
            this.setPosicao(faseCorrente.getHero().getPosicaoAntiga());
            canMove = 12;
        }
        if(canMove != 15)
            canMove++;
    }    
}