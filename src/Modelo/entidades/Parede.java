package Modelo.entidades;

import Auxiliar.Consts;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;


public class Parede extends Entidade  implements Serializable{ 
    
    protected String image = "bricks.png";

    
    public Parede(Posicao p){
        super(p);
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = false;
    }
    
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        if (this.image != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            } catch (java.io.IOException e) {
                System.err.println("Erro ao recarregar imagem para Parede: " + this.image + " - " + e.getMessage());
            }
        } else {
            System.err.println("Alerta: Parede - Nome da imagem é nulo, não foi possível recarregar iImage.");
        }
    }
}