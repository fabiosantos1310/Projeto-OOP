package Modelo.entidades;

import Auxiliar.Desenho;
import auxiliar.Posicao;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import javax.swing.ImageIcon;

public abstract class Entidade implements Serializable {
    private static final long serialVersionUID = 1L;

    protected transient ImageIcon iImage;
    protected  Posicao pPosicao;
    protected  boolean bTransponivel;
    protected  boolean bMortal;

    /**
     * Construtor base para Entidade.
     * Inicializa flags padrão. pPosicao e iImage devem ser definidos pela subclasse
     * ou por um construtor que receba a posição.
     */
    protected Entidade() {
        this.bTransponivel = true;
        this.bMortal = false;
        this.iImage = new ImageIcon(); 
    }

    /**
     * Construtor para Entidade que define uma posição inicial.
     * @param p A posição inicial da entidade.
     */
    protected Entidade(Posicao p) {
        this();
        if (p == null) {
            System.err.println("Alerta: Tentativa de criar Entidade com Posicao nula. pPosicao permanecerá nulo.");
        }
        this.pPosicao = p;
    }

    public boolean isbMortal() {
        return bMortal;
    }

    public ImageIcon getImage() {
        return iImage;
    }

    public Posicao getPosicao() {
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho() {
        if (this.pPosicao == null) {
            return; 
        }
        if (this.iImage == null || this.iImage.getIconWidth() <= 0) {
            return;
        }
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());
    }

    /**
     * Define a posição da entidade. Cuidado: requer que pPosicao já esteja instanciado.
     * Se pPosicao for nulo, este método falhará. Use setPosicao(Posicao p) para definir o objeto pPosicao.
     * @param linha A nova linha.
     * @param coluna A nova coluna.
     * @return true se a posição foi definida com sucesso pela instância de Posicao, false caso contrário.
     */
    public boolean setPosicao(int linha, int coluna) {
        if (pPosicao == null) {
            System.err.println("Erro: pPosicao é nulo em Entidade.setPosicao(int, int). Use setPosicao(Posicao p) primeiro.");
            return false;
        }
        return pPosicao.setPosicao(linha, coluna);
    }

    /**
     * Define o objeto Posicao para esta entidade.
     * @param p O novo objeto Posicao.
     */
    public void setPosicao(Posicao p) {
        if (p == null) {
            System.err.println("Alerta: Tentativa de definir pPosicao como nulo em Entidade.");
        }
        this.pPosicao = p;
    }

    public boolean moveUp() {
        if (pPosicao == null) return false;
        return this.pPosicao.moveUp();
    }

    public boolean moveDown() {
        if (pPosicao == null) return false;
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        if (pPosicao == null) return false;
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        if (pPosicao == null) return false;
        return this.pPosicao.moveLeft();
    }

    public ImageIcon girarImagem(ImageIcon icon, double angulo) {
        if (icon == null || icon.getIconWidth() <= 0 || icon.getIconHeight() <= 0) {
            return (icon != null) ? icon : new ImageIcon(); 
        }
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        icon.paintIcon(null, g2d, 0, 0); 
        g2d.dispose();

        BufferedImage rotacionada = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = rotacionada.createGraphics();
        g2d.rotate(Math.toRadians(angulo), w / 2.0, h / 2.0);
        g2d.drawImage(buffer, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(rotacionada);
    }
}