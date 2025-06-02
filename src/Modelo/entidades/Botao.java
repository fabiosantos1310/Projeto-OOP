/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import javax.swing.ImageIcon;

/**
 *
 * @author PC
 */
public class Botao extends Entidade{ // ffc500
    Tela tela = Desenho.acessoATelaDoJogo();
    
    protected String image = "botao.png";

    
    public Botao(Posicao p) {
        try{
            this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + this.image);
        } catch(IOException e){
            System.out.println(e.getMessage());
        }
        this.bTransponivel = true;
        this.bMortal = true;
        setPosicao(p);
    }
}
