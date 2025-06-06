/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.fases;

import Auxiliar.Consts;
import Modelo.entidades.Chave;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Entidade;
import Modelo.entidades.Fogo;
import Modelo.entidades.Hero;
import Modelo.entidades.Moeda;
import Modelo.entidades.Parede;
import auxiliar.Posicao;
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

    public boolean hasClone;
    public ArrayList<Fogo> fogos = new ArrayList<>();
    public ArrayList<Chave> chaves = new ArrayList<>();
    public ArrayList<Parede> paredes = new ArrayList<>();
    public ArrayList<CospeFogo> cospeFogos = new ArrayList<>();
    public ArrayList<Moeda> moedas = new ArrayList<>();

    
    public Fase(String path, int i){
        entidades = new ArrayList<Entidade>();
        this.mapPath = path;
        hasClone =false;
        this.indice = i;
        try {
            mapa = ImageIO.read(new File(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.mapPath));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        switch(i){
            case 0 -> direcoesFogo = new int[] {0, 0, 0, 1, 3, 0};
            case 2 -> direcoesFogo = new int[] {0, 0, 2, 2, 2, 0, 0, 0, 2};
            case 3 ->  direcoesFogo = new int[] {3,3,3};
            case 4 ->  direcoesFogo = new int[] {0,0,0,0, 1,1,3};
        }
    }
    
    public Hero getHero(){
        for(Entidade e : entidades){
            if(e instanceof Hero hero && !hero.isClone)
                return hero;
        }
        return null;
    }
    
    public Hero getClone(){
        for(Entidade e : entidades){
            if(e instanceof Hero hero && hero.isClone)
                return hero;
        }
        return null;
    }
    
    public void addClone(){
        Hero clone = new Hero(new Posicao(1, 6), true, this.indice);
        this.entidades.add(clone);
        this.hasClone = true;
    }
    
    public ArrayList<CospeFogo> getCospeFogo(){
        return cospeFogos;
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
