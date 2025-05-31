/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Auxiliar.Game;
import auxiliar.Posicao;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author PC
 */
public class Portal extends Entidade{ // 0xFFcbdbfc
    protected String image = "portal.png";

    
    public Portal(Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = true;
        this.bMortal = false;
        setPosicao(p);
    }
    
    public void atravessouPortal(){
        System.out.println("atravessou portal!");
        Game.faseAtual++;
        Desenho.acessoATelaDoJogo().passarFase();
    }
    
}
