package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
// Controler.Tela não é usado diretamente aqui, pode ser removido se não for necessário para outros imports
// import Controler.Tela; 
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
// import java.awt.Toolkit; // Não utilizado diretamente
import java.awt.image.BufferedImage;
import java.io.IOException; // Importe apenas se alguma subclasse lançar explicitamente
import java.io.Serializable;
import javax.swing.ImageIcon;
// javax.swing.JFrame e javax.swing.JPanel não são usados, podem ser removidos
// import javax.swing.JFrame;
// import javax.swing.JPanel;

public abstract class Entidade implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ImageIcon iImage; // Inicializado pelas subclasses ou construtor com Posicao
    protected Posicao pPosicao;
    protected boolean bTransponivel; /*Pode passar por cima?*/
    protected boolean bMortal;       /*Se encostar, morre?*/

    /**
     * Construtor base para Entidade.
     * Inicializa flags padrão. pPosicao e iImage devem ser definidos pela subclasse
     * ou por um construtor que receba a posição.
     */
    protected Entidade() {
        this.bTransponivel = true;
        this.bMortal = false;
        // iImage pode ser inicializado como um ImageIcon vazio simples,
        // já que as subclasses geralmente carregam suas próprias imagens específicas.
        this.iImage = new ImageIcon(); 
        // pPosicao permanece null e deve ser definido pela subclasse,
        // por exemplo, chamando setPosicao(new Posicao(...)) ou usando o construtor com Posicao.
    }

    /**
     * Construtor para Entidade que define uma posição inicial.
     * @param p A posição inicial da entidade.
     */
    protected Entidade(Posicao p) {
        this(); // Chama o construtor padrão para inicializar bTransponivel e bMortal e iImage padrão
        if (p == null) {
            System.err.println("Alerta: Tentativa de criar Entidade com Posicao nula. pPosicao permanecerá nulo.");
            // Considerar lançar IllegalArgumentException se pPosicao nula não for permitida
            // throw new IllegalArgumentException("Posicao não pode ser nula para Entidade.");
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
            // System.err.println("Alerta: Tentativa de desenhar Entidade com pPosicao nula.");
            return; // Não pode desenhar sem posição
        }
        if (this.iImage == null || this.iImage.getIconWidth() <= 0) {
            // System.err.println("Alerta: Tentativa de desenhar Entidade com iImage nula ou inválida.");
            return; // Não pode desenhar sem imagem válida
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
            // Considerar lançar IllegalArgumentException se pPosicao nula não for permitida
        }
        this.pPosicao = p;
    }

    // Métodos de movimento (moveUp, moveDown, etc.)
    // Adicionam verificação para pPosicao nulo para evitar NullPointerException
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
            // Retorna um ícone vazio ou o original se o ícone de entrada for inválido
            return (icon != null) ? icon : new ImageIcon(); 
        }
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        // Passar null como Component para paintIcon é aceitável se o Icon não depender dele.
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