package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.ControleDeJogo;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fogo extends Entidade implements Serializable{
    protected String[] images = { "fire.png",  null,  "barrel.png", null, null };
    protected int direcao;
    private int iDirectionV;
    private int iDirectionH;
    Tela tela = Desenho.acessoATelaDoJogo();
    
    
    public Fogo(int faseAtual, Posicao p, int direcao) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bMortal = true;
        this.bTransponivel = true;
        this.pPosicao = p;
        switch(direcao){
            case 0 -> { // direita
                this.iDirectionH = 1;
                this.iDirectionV = 2;
            }
            case 1 -> { // baixo
                this.iDirectionH = 2;
                this.iDirectionV = 1;
                this.iImage = this.girarImagem(this.iImage, 90);
            }
            case 2 -> { // esquerda
                this.iDirectionH = 0;
                this.iDirectionV = 2;
                this.iImage = this.girarImagem(this.iImage, 180);

            }
            case 3 -> { // cima
                this.iDirectionH = 2;
                this.iDirectionV = 0;
                this.iImage = this.girarImagem(this.iImage, 270);
            }              
        }
        tela.current.fogos.add(this);
    }

    public void autoDesenho() {
        super.autoDesenho();

        if (iDirectionH == 0) {
            if (podeMover(-1, 0))
                this.moveLeft();
            else{
                    tela.current.entidades.remove(this);
                    tela.current.fogos.remove(this);
            }

        } else if (iDirectionH == 1) {
            if (podeMover(1, 0))
                this.moveRight();
            else{
                    tela.current.entidades.remove(this);
                    tela.current.fogos.remove(this);
            }
        }

        if (iDirectionV == 0) {
            if (podeMover(0, -1))
                this.moveUp();
            else{
                    tela.current.entidades.remove(this);
                    tela.current.fogos.remove(this);
            }
            

        } else if (iDirectionV == 1) {
            if (podeMover(0, 1))
                this.moveDown();
            else{
                    tela.current.entidades.remove(this);
                    tela.current.fogos.remove(this);
            }
        }
    }

    // Exemplo de método auxiliar para prever o movimento
    private boolean podeMover(int dx, int dy) {
        // Salva posição atual
        int xOriginal = this.pPosicao.getColuna();
        int yOriginal = this.pPosicao.getLinha();

        // Move virtualmente
        this.pPosicao.setPosicao(dy + yOriginal, dx + xOriginal);
        boolean ok = verificaPos();
        // Volta para posição original
        this.pPosicao.setPosicao(yOriginal, xOriginal);


        return ok;
    }

    
    public boolean verificaPos(){
        return tela.cj.ehPosicaoValida(tela.current, pPosicao, this);
    }
    
     private boolean validaPosicao(Posicao p){
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this);       
    }
    
    public boolean canMoveUp() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha() - 1, this.pPosicao.getColuna()));
    }

    public boolean canMoveDown() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha() + 1, this.pPosicao.getColuna()));
    }

    public boolean canMoveRight() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() + 1));
    }

    public boolean canMoveLeft() {
        return validaPosicao(new Posicao(this.pPosicao.getLinha(), this.pPosicao.getColuna() - 1));
    }    

    @Override
    public boolean setPosicao(int linha, int coluna){
        Posicao p = new Posicao(linha, coluna);
        if(Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this)){
            return this.pPosicao.setPosicao(p);
        }
        return false;       
    }
}
