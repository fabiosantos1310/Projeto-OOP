package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class CospeFogo extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    public int iContaIntervalos;
    private int direcao; 
    transient Tela tela;
    int faseAtual; 
    protected String[] images = { "caveira.png",  null,  "canhao.png", "caveira.png", "canhao.png" };
    
    public CospeFogo(int faseAtual, Posicao p, int direcao) {
        super();
        this.tela = Desenho.acessoATelaDoJogo();
        this.faseAtual = faseAtual; 
        this.direcao = direcao;

        String imageName = null;
        if (this.images != null && this.faseAtual >= 0 && this.faseAtual < this.images.length && this.images[this.faseAtual] != null) {
            imageName = this.images[this.faseAtual];
        }

        if (imageName != null) {
            try{
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        
        this.bTransponivel = false;
        bMortal = false;
        this.iContaIntervalos = 0;
        
        aplicarRotacaoDaImagem();
        setPosicao(p);
    }

    private void aplicarRotacaoDaImagem() {
        if (this.iImage == null) return;
        switch(this.direcao){
            case 0: this.iImage = this.girarImagem(this.iImage, 90); break;
            case 1: this.iImage = this.girarImagem(this.iImage, 180); break;
            case 2: this.iImage = this.girarImagem(this.iImage, 270); break;
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta em CospeFogo.readObject: Desenho.acessoATelaDoJogo() nulo.");
        }

        String imageName = null;
        if (this.images != null && this.faseAtual >= 0 && this.faseAtual < this.images.length && this.images[this.faseAtual] != null) {
            imageName = this.images[this.faseAtual];
        }
        if (imageName != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
                aplicarRotacaoDaImagem(); 
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para CospeFogo: " + e.getMessage());
            }
        }
    }

    @Override
    public void autoDesenho() {
        super.autoDesenho();
        this.iContaIntervalos++;
        if(this.iContaIntervalos >= Consts.TIMER + 10){
            this.iContaIntervalos = 0;
            Fogo f = null;
            if (pPosicao != null) {
                 f = new Fogo(faseAtual, new Posicao(pPosicao.getLinha(), pPosicao.getColuna()), direcao);
            } else {
                 f = new Fogo(faseAtual, new Posicao(0,0), direcao); // Posição padrão ou tratar erro
            }
            
            if (this.tela != null && this.tela.current != null && this.tela.current.fogos != null) {
                this.tela.current.fogos.add(f);
            }
        }
    }    
}