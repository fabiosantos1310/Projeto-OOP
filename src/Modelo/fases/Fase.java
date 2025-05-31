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
    
    public Fase(String path){
        entidades = new ArrayList<Entidade>();
        try {
            mapa = ImageIO.read(new File(new java.io.File(".").getCanonicalPath() + Consts.PATH + path));
        } catch (IOException e) {
            System.out.println(e.getMessage());
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
