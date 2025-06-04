package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Game;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Hero extends Entidade implements Serializable {
    private static final long serialVersionUID = 1L;

    protected String[] images = { "robbo.png", "robbo.png", "donkeykong.png", "robbo.png", "diddykong.png" };
    protected String clonePng = "diddykong.png";
    private Queue<Posicao> posicoes;
    Posicao ultimaPosicao;
    public boolean temChave = false;
    public int chaves = 0;
    public int direcao = 0;
    public int moedas = 0;
    public boolean isClone;
    // private transient Fase fase; // REMOVIDO este campo
    private transient Tela tela;
    public int heroFaseAtualIndex; 

    public Hero(Posicao p, boolean isClone, int faseAtual) {
        super(p); 
        posicoes = new LinkedList<>();
        this.isClone = isClone;
        this.heroFaseAtualIndex = faseAtual; 
        
        this.tela = Desenho.acessoATelaDoJogo();

        String imageName = null;
        if(this.isClone) {
            imageName = this.clonePng;
        } else {
            if (this.images != null && this.heroFaseAtualIndex >= 0 && this.heroFaseAtualIndex < this.images.length) {
                 imageName = this.images[this.heroFaseAtualIndex];
            }
        }
        
        if (imageName != null) {
            try{
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            } catch(IOException e){
                System.out.println("Hero constructor image load error: " + e.getMessage());
            }
        } else {
            System.err.println("Hero constructor: Imagem não pode ser determinada.");
        }
        
        Posicao aux = new Posicao(p.getLinha(), p.getColuna());
        posicoes.add(aux);
        ultimaPosicao = p;
        this.bTransponivel = false;
        this.bMortal = true;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta: Hero - Desenho.acessoATelaDoJogo() nulo durante readObject.");
        }
        
        String imageNameForLoad = null;
        if (this.isClone) {
            imageNameForLoad = this.clonePng;
        } else {
            if (this.images != null && this.heroFaseAtualIndex >= 0 && this.heroFaseAtualIndex < this.images.length) {
                imageNameForLoad = this.images[this.heroFaseAtualIndex];
            }
        }

        if (imageNameForLoad != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageNameForLoad);
            } catch (java.io.IOException e) {
                System.err.println("Erro ao recarregar imagem para Hero: " + imageNameForLoad + " - " + e.getMessage());
            }
        } else {
             System.err.println("Alerta: Hero - Nome da imagem não pôde ser determinado para recarregar iImage.");
        }
    }
    
    public void voltaAUltimaPosicao(){
        if (this.pPosicao != null) {
            this.pPosicao.volta();
        }
    }
    
    public void removeChave(){
        chaves--;
        if(chaves == 0)
            temChave = false;
    }
    
    public void removeMoedas(int val){
        this.moedas -= val;
    }
    
    public void addMoeda(){
        this.moedas++;
    }
    
    public boolean setPosicao(int linha, int coluna){
        if(this.pPosicao.setPosicao(linha, coluna)){
            if (this.tela != null && !tela.ehPosicaoValida(this.getPosicao(), this)) {
                this.voltaAUltimaPosicao();
            }
            return true;
        }
        return false;    
    }
    
    private boolean validaPosicao(){
        if (this.tela != null && !tela.ehPosicaoValida(this.getPosicao(), this)) {
            this.voltaAUltimaPosicao();
            return false;
        }
        return true;    
    }
    
    public void morrer(){
        if (this.tela == null) {
             System.err.println("Hero.morrer(): Referência para tela é nula!");
             return;
        }
        JOptionPane.showMessageDialog(tela, "Você morreu! A fase será reiniciada.", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        tela.cj.limparListas();
        tela.reiniciar();    
    }
    
    public Posicao getPosicaoAntiga(){
        if(this.posicoes != null && !this.posicoes.isEmpty())
            return this.posicoes.poll();
        return ultimaPosicao;
    }
    
    public boolean moveUp() {
        direcao = 3;
        if(!this.isClone && this.pPosicao != null && this.posicoes != null){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveUp()){
            if(this.tela != null && this.tela.current != null && this.tela.current.hasClone && !this.isClone && this.tela.current.getClone() != null)
                this.tela.current.getClone().moveUp();
            if(validaPosicao()){
                if(this.tela != null) tela.atualizaCamera();
                return true;
            }        
        }
        return false;
    }

    public boolean moveDown() {
        direcao = 1;
        if(!this.isClone && this.pPosicao != null && this.posicoes != null){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveDown()){
            if(this.tela != null && this.tela.current != null && this.tela.current.hasClone && !this.isClone && this.tela.current.getClone() != null)
                this.tela.current.getClone().moveDown();
            if(validaPosicao()){
                if(this.tela != null) tela.atualizaCamera();
                return true;
            }        
        }
        return false;
    }

    public boolean moveRight() {
        direcao = 0;
        if(!this.isClone && this.pPosicao != null && this.posicoes != null){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveRight()){
            if(this.tela != null && this.tela.current != null && this.tela.current.hasClone && !this.isClone && this.tela.current.getClone() != null)
                this.tela.current.getClone().moveLeft(); 
            if(validaPosicao()){
                if(this.tela != null) tela.atualizaCamera();
                return true;
            }
        }
        return false;
    }

    public boolean moveLeft() {
        direcao = 2;
        if(!this.isClone && this.pPosicao != null && this.posicoes != null){
            Posicao aux = new Posicao(pPosicao.getLinha(), pPosicao.getColuna());
            posicoes.add(aux);
        }

        if(super.moveLeft()){
            if(this.tela != null && this.tela.current != null && this.tela.current.hasClone && !this.isClone && this.tela.current.getClone() != null)
                this.tela.current.getClone().moveRight(); 
            if(validaPosicao()){
                if(this.tela != null) tela.atualizaCamera();
                return true;
            }    
        }
        return false;
    }    
}