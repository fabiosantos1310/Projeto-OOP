package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Game;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream; // Importar para readObject
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Portal extends Entidade implements Serializable {
    private static final long serialVersionUID = 1L; // Boa prática

    protected String image = "portal.png";

    public Portal(Posicao p) {
        super(p);
        try {
            if (this.image != null) {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar imagem no construtor de Portal: " + e.getMessage());
        }
        this.bTransponivel = true;
        this.bMortal = false;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();

        if (this.image != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para Portal em readObject: " + this.image + " - " + e.getMessage());
            }
        } else {
            System.err.println("Alerta em Portal.readObject: this.image é nulo, não foi possível recarregar iImage.");
        }
    }
    
    public void atravessouPortal() {
        System.out.println("atravessou portal!");
        Game.faseAtual++;
        if (Desenho.acessoATelaDoJogo() != null) {
            Desenho.acessoATelaDoJogo().passarFase();
        } else {
            System.err.println("Erro em Portal.atravessouPortal: Desenho.acessoATelaDoJogo() é nulo.");
        }
    }
}