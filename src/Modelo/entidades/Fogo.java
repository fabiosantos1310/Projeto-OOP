package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fogo extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String[] images = { "fire.png",  null,  "barrel.png", "fire.png", "barrel.png" };
    protected int direcaoFogo;
    private int iDirectionV;
    private int iDirectionH;
    transient Tela tela;
    private int entidadeFaseAtualIndex; 
    
    public Fogo(int faseAtual, Posicao p, int direcao) {
        super();
        this.tela = Desenho.acessoATelaDoJogo();
        this.entidadeFaseAtualIndex = faseAtual;
        this.direcaoFogo = direcao;

        String imageName = null;
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            imageName = this.images[this.entidadeFaseAtualIndex];
        }
        
        if (imageName != null) {
            try{
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        
        this.bMortal = true;
        this.bTransponivel = true;
        this.pPosicao = p; 
        
        aplicarRotacaoDaImagem();
        configurarDirecoesDeMovimento();
    }

    private void aplicarRotacaoDaImagem() {
        if (this.iImage == null) return;
        switch(this.direcaoFogo){
            case 1: this.iImage = this.girarImagem(this.iImage, 90); break;  
            case 2: this.iImage = this.girarImagem(this.iImage, 180); break; 
            case 3: this.iImage = this.girarImagem(this.iImage, 270); break; 
        }
    }
    
    private void configurarDirecoesDeMovimento() {
        switch(this.direcaoFogo){ 
            case 0: 
                this.iDirectionH = 1;
                this.iDirectionV = 2; 
                break;
            case 1: 
                this.iDirectionH = 2; 
                this.iDirectionV = 1;
                break;
            case 2: 
                this.iDirectionH = 0;
                this.iDirectionV = 2; 
                break;
            case 3: 
                this.iDirectionH = 2; 
                this.iDirectionV = 0;
                break;            
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta em Fogo.readObject: Desenho.acessoATelaDoJogo() nulo.");
        }

        String imageName = null;
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            imageName = this.images[this.entidadeFaseAtualIndex];
        }
        if (imageName != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
                aplicarRotacaoDaImagem();
                configurarDirecoesDeMovimento(); 
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para Fogo: " + e.getMessage());
            }
        }
    }

    public void autoDesenho() {
        boolean moveu = false;
        if (this.tela == null || this.tela.current == null || this.tela.current.fogos == null) {
            super.autoDesenho(); // Ainda tenta desenhar se a imagem existir
            return;
        }

        if (iDirectionH == 0) { 
            if (podeMover(-1, 0)) {
                this.moveLeft();
                moveu = true;
            }
        } else if (iDirectionH == 1) {
            if (podeMover(1, 0)) {
                this.moveRight();
                moveu = true;
            }
        }

        if (!moveu) { 
            if (iDirectionV == 0) {
                if (podeMover(0, -1)) {
                    this.moveUp();
                    moveu = true;
                }
            } else if (iDirectionV == 1) {
                if (podeMover(0, 1)) {
                    this.moveDown();
                    moveu = true;
                }
            }
        }
        
        if (!moveu) {
             this.tela.current.fogos.remove(this);
        }
        super.autoDesenho();
    }

    private boolean podeMover(int dx, int dy) {
        if (this.pPosicao == null || this.tela == null || this.tela.current == null) return false;
        
        Posicao proximaPosicao = new Posicao(this.pPosicao.getLinha() + dy, this.pPosicao.getColuna() + dx);
        
        if (proximaPosicao.getLinha() < 0 || proximaPosicao.getLinha() >= this.tela.c.MUNDO_ALTURA ||
            proximaPosicao.getColuna() < 0 || proximaPosicao.getColuna() >= this.tela.c.MUNDO_LARGURA) {
            return false; 
        }
        
        // Salva a posição original para restaurar após a verificação
        int linhaOriginal = this.pPosicao.getLinha();
        int colunaOriginal = this.pPosicao.getColuna();
        
        // Move temporariamente para a próxima posição para verificar com ehPosicaoValida
        this.pPosicao.setPosicao(proximaPosicao.getLinha(), proximaPosicao.getColuna());
        boolean ok = this.tela.cj.ehPosicaoValida(this.tela.current, this.pPosicao, this);
        
        // Restaura a posição original
        this.pPosicao.setPosicao(linhaOriginal, colunaOriginal);
        
        return ok;
    }
    
    public boolean verificaPos(){
        if (this.tela == null || this.tela.cj == null || this.tela.current == null || this.pPosicao == null) return false;
        // A chamada original para this.tela.cj.verificaFogo(...) foi removida.
        return this.tela.cj.ehPosicaoValida(this.tela.current, pPosicao, this);
    }
    
    private boolean validaPosicao(Posicao p){
         if (Desenho.acessoATelaDoJogo() == null) return false;
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this);    
    }
    
    @Override
    public boolean setPosicao(int linha, int coluna){
        if (this.pPosicao == null) { 
            this.pPosicao = new Posicao(linha, coluna);
            return true;
        }
        Posicao p = new Posicao(linha, coluna);
        if(this.validaPosicao(p)){ 
            return this.pPosicao.setPosicao(p);
        }
        return false;        
    }
}