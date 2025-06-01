/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.fases;

import Auxiliar.Consts;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Entidade;
import Modelo.entidades.Hero;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.io.ObjectInputStream; 
import java.io.Serializable;

/**
 *
 * @author PC
 */
public class Fase implements Serializable {
    public transient BufferedImage mapa;
    public int indice;
    public ArrayList<Entidade> entidades;
    public int[] direcoesFogo;
    private String mapPath;
    private static final long serialVersionUID = 1L;

    public Fase(String path, int i){
        entidades = new ArrayList<Entidade>();
        this.mapPath = path;
        this.indice = i;
        try {
            mapa = ImageIO.read(new File(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.mapPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        switch(i){
            case 0 -> direcoesFogo = new int[] {0, 0, 0, 1, 3, 1, 3, 1};
        }
    }
    
    public Hero getHero(){
        for(Entidade e : entidades){
            if(e instanceof Hero hero)
                return hero;
        }
        return null;
    }
    
    public ArrayList<CospeFogo> getCospeFogo(){
        ArrayList<CospeFogo> lista = new ArrayList<CospeFogo>();
        for(Entidade e : entidades){
            if(e instanceof CospeFogo cf)
                lista.add(cf);
        }
        return lista;
    }
    
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        if (this.mapPath != null) {
            try {
                mapa = ImageIO.read(new File(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.mapPath));
            } catch (IOException e) {
                System.out.println("Erro ao recarregar imagem do mapa durante desserializacao: " + e.getMessage());
            }
        }
    }
}
