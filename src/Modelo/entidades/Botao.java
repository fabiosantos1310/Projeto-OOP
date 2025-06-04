package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Botao extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    transient Tela tela;
    protected String image = "botao.png";
    
    public Botao(Posicao p) {
        super();
        this.tela = Desenho.acessoATelaDoJogo();
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = true;
        this.bMortal = true;
        setPosicao(p);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta em Botao.readObject: Desenho.acessoATelaDoJogo() nulo.");
        }
        if (this.image != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para Botao: " + e.getMessage());
            }
        }
    }
}