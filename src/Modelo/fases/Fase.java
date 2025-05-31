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

/**
 *
 * @author PC
 */
public class Fase {
    public BufferedImage mapa;
    public int indice;
    public ArrayList<Entidade> entidades;
    public int[] direcoesFogo;
    
    public Fase(String path, int i){
        entidades = new ArrayList<Entidade>();
        this.indice = i;
        try {
            mapa = ImageIO.read(new File(new java.io.File(".").getCanonicalPath() + Consts.PATH + path));
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
}
