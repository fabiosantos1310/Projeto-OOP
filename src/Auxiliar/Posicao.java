package auxiliar;

import Auxiliar.Desenho;
import Controler.Tela;
import java.io.Serializable;
import java.io.IOException;
import java.io.ObjectInputStream;

public class Posicao implements Serializable {
    private int linha;
    private int coluna;
    
    private int linhaAnterior;
    private int colunaAnterior;
    private transient Tela tela; 
    private static final long serialVersionUID = 1L;

    public Posicao(int linha, int coluna) {
        relinkTela(); 
        this.setPosicao(linha, coluna);
    }

    public void relinkTela() {
        this.tela = Desenho.acessoATelaDoJogo();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        relinkTela();
    }
    

    public boolean setPosicao(int linha, int coluna) {
        if (this.tela == null) {
            relinkTela();
        }
        if (this.tela == null || this.tela.c == null) {
             this.linhaAnterior = this.linha;
             this.linha = linha;
             this.colunaAnterior = this.coluna;
             this.coluna = coluna;
             return true;
        }

        if (linha < 0 || linha >= this.tela.c.MUNDO_ALTURA || coluna < 0 || coluna >= this.tela.c.MUNDO_LARGURA) {
            return false;
        }
        this.linhaAnterior = this.linha;
        this.colunaAnterior = this.coluna;
        this.linha = linha;
        this.coluna = coluna;
        return true;
    }
    
    public boolean setPosicao(Posicao p) {
        if (p == null) return false;
         if (this.tela == null) relinkTela();
        return this.setPosicao(p.getLinha(), p.getColuna());
    }

    public int getLinha() {
        return linha;
    }

    public boolean volta() {
        if (this.tela == null) relinkTela(); 
        return this.setPosicao(linhaAnterior, colunaAnterior);
    }

    public int getColuna() {
        return coluna;
    }

    public boolean igual(Posicao posicao) {
        if (posicao == null) return false;
        return (linha == posicao.getLinha() && coluna == posicao.getColuna());
    }

    public boolean copia(Posicao posicao) {
        if (posicao == null) return false;
        if (this.tela == null) relinkTela();
        return this.setPosicao(posicao.getLinha(), posicao.getColuna());
    }

    public boolean moveUp() {
        if (this.tela == null) relinkTela();
        return this.setPosicao(this.getLinha() - 1, this.getColuna());
    }

    public boolean moveDown() {
        if (this.tela == null) relinkTela();
        return this.setPosicao(this.getLinha() + 1, this.getColuna());
    }

    public boolean moveRight() {
        if (this.tela == null) relinkTela();
        return this.setPosicao(this.getLinha(), this.getColuna() + 1);
    }

    public boolean moveLeft() {
        if (this.tela == null) relinkTela();
        return this.setPosicao(this.getLinha(), this.getColuna() - 1);
    }
    
    public double distancia(Posicao p){
        if (p == null) return Double.NaN;
        double dx = p.getColuna() - this.getColuna();
        double dy = p.getLinha() - this.getLinha(); 
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
