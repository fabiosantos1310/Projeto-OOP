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
        // It's good practice to initialize transient fields that have default access logic
        relinkTela(); 
        this.setPosicao(linha, coluna); // setPosicao will use the (now hopefully linked) tela
    }

    public void relinkTela() {
        this.tela = Desenho.acessoATelaDoJogo();
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject(); // Reads non-transient fields (linha, coluna, etc.)
        relinkTela(); // Re-initialize transient 'tela' field after deserialization
    }
    

    public boolean setPosicao(int linha, int coluna) {
        if (this.tela == null) {
            relinkTela();
        }
        // It's possible 'tela' or 'tela.c' is still null if Desenho.jCenario isn't set yet
        // This can happen if Posicao objects are created before Tela is fully initialized and set in Desenho
        if (this.tela == null || this.tela.c == null) {
             // System.err.println("Posicao.setPosicao: Tela or Tela.c is null. Bounds check skipped.");
             // Fallback or error:
             this.linhaAnterior = this.linha;
             this.linha = linha;
             this.colunaAnterior = this.coluna;
             this.coluna = coluna;
             return true; // Or handle error more strictly
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
        if (p == null) return Double.NaN; // Or throw an exception
        double dx = p.getColuna() - this.getColuna();
        double dy = p.getLinha() - this.getLinha(); 
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
