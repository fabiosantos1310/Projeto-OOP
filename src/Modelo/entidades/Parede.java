/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import auxiliar.Posicao;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author PC
 */
public class Parede extends Entidade { // 0xFFffffff
    
    protected String[] images = { "bricks.png", null, null, null, null };

    
    public Parede(int faseAtual, Posicao p){
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.images[faseAtual]);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = false;
        setPosicao(p);
    }
}
    
