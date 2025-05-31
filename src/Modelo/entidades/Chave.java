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
public class Chave extends Entidade{ // d3dc00 
    protected String image = "chave.png";

    public Chave(Posicao p){
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = true;
        setPosicao(p);
    }
    
}
