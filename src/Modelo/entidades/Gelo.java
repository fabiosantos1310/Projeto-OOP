package Modelo.entidades;

import Auxiliar.Consts;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Gelo extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String image = "gelo.png";

    public Gelo(Posicao p){
        super(); 
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = true;
        setPosicao(p);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (this.image != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para Gelo: " + e.getMessage());
            }
        }
    }
}