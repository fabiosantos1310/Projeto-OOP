package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class Entidade implements Serializable {

    protected ImageIcon iImage = new ImageIcon();
    protected Posicao pPosicao;
    protected boolean bTransponivel; /*Pode passar por cima?*/
    protected boolean bMortal;       /*Se encostar, morre?*/

    public boolean isbMortal() {
        return bMortal;
    }


    protected Entidade() {
        this.bTransponivel = true;
        this.bMortal = false;

        Image img = iImage.getImage();
        BufferedImage bi = new BufferedImage(Consts.CELL_SIDE, Consts.CELL_SIDE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, Consts.CELL_SIDE, Consts.CELL_SIDE, null);
        iImage = new ImageIcon(bi);
        
    }
    
    public ImageIcon getImage(){
        return iImage;
    }

    public Posicao getPosicao() {
        /*TODO: Retirar este método para que objetos externos nao possam operar
         diretamente sobre a posição do Personagem*/
        return pPosicao;
    }

    public boolean isbTransponivel() {
        return bTransponivel;
    }

    public void setbTransponivel(boolean bTransponivel) {
        this.bTransponivel = bTransponivel;
    }

    public void autoDesenho(){
        Desenho.desenhar(this.iImage, this.pPosicao.getColuna(), this.pPosicao.getLinha());        
    }

    public boolean setPosicao(int linha, int coluna) {
        return pPosicao.setPosicao(linha, coluna);
    }
    
    public void setPosicao(Posicao p) {
        pPosicao = p;
    }

    public boolean moveUp() {
        return this.pPosicao.moveUp();
    }

    public boolean moveDown() {
        return this.pPosicao.moveDown();
    }

    public boolean moveRight() {
        return this.pPosicao.moveRight();
    }

    public boolean moveLeft() {
        return this.pPosicao.moveLeft();
    }
    
    public ImageIcon girarImagem(ImageIcon icon, double angulo) {
        int w = icon.getIconWidth();
        int h = icon.getIconHeight();

        BufferedImage buffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = buffer.createGraphics();
        icon.paintIcon(null, g2d, 0, 0);
        g2d.dispose();

        BufferedImage rotacionada = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        g2d = rotacionada.createGraphics();
        g2d.rotate(Math.toRadians(angulo), w / 2.0, h / 2.0); // gira no centro da imagem
        g2d.drawImage(buffer, 0, 0, null);
        g2d.dispose();

        return new ImageIcon(rotacionada);
    }
}
