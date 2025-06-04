/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 *
 * @author PC
 */
public class Moeda extends Entidade implements Serializable{ //004c61
    protected String image = "moeda.png";

    public Moeda(Posicao p){
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

        if (this.image != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para moeda em readObject: " + this.image + " - " + e.getMessage());
            }
        } else {
            System.err.println("Alerta em Moeda.readObject: this.image é nulo, não foi possível recarregar iImage.");
        }
    }
}
