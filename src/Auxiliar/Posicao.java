package auxiliar;

import Auxiliar.Desenho;
import Controler.Tela;
import java.io.Serializable;

public class Posicao implements Serializable {
    private int linha;
    private int coluna;
    
    private int linhaAnterior;
    private int colunaAnterior;
    private Tela tela = Desenho.acessoATelaDoJogo();

    public Posicao(int linha, int coluna) {
        this.setPosicao(linha, coluna);
    }

    public boolean setPosicao(int linha, int coluna) {

        if (linha < 0 || linha >= tela.c.MUNDO_ALTURA)
            return false;
        linhaAnterior = this.linha;
        this.linha = linha;

        if (coluna < 0 || coluna >= tela.c.MUNDO_LARGURA)
            return false;
        colunaAnterior = this.coluna;
        this.coluna = coluna;

        return true;
    }
    
    public boolean setPosicao(Posicao p) {

        if (p.linha < 0 || p.linha >= tela.c.MUNDO_ALTURA)
            return false;
        linhaAnterior = this.linha;
        this.linha = p.linha;

        if (p.coluna < 0 || p.coluna >= tela.c.MUNDO_LARGURA)
            return false;
        colunaAnterior = this.coluna;
        this.coluna = p.coluna;

        return true;
    }

    public int getLinha() {
        return linha;
    }

    public boolean volta() {
        return this.setPosicao(linhaAnterior, colunaAnterior);
    }

    public int getColuna() {
        return coluna;
    }

    public boolean igual(Posicao posicao) {
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public boolean copia(Posicao posicao) {
        return this.setPosicao(posicao.getLinha(), posicao.getColuna());
    }

    public boolean moveUp() {
        return this.setPosicao(this.getLinha() - 1, this.getColuna());
    }

    public boolean moveDown() {
        return this.setPosicao(this.getLinha() + 1, this.getColuna());
    }

    public boolean moveRight() {
        return this.setPosicao(this.getLinha(), this.getColuna() + 1);
    }

    public boolean moveLeft() {
        return this.setPosicao(this.getLinha(), this.getColuna() - 1);
    }
    
    public double distancia(Posicao p){
        double dx = p.getColuna() - this.getColuna();
        double dy = p.getColuna() - this.getLinha();
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
