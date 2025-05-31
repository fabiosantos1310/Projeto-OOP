/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controler;

import Auxiliar.Consts;
import Modelo.entidades.*;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.util.Random;

/**
 *
 * @author PC
 */
public class Mundo {
    public void carregaMundo(Fase fase, Consts c){
        int WIDTH = fase.mapa.getWidth();
        int HEIGHT = fase.mapa.getHeight();
        c.setLargura(WIDTH);
        c.setAltura(HEIGHT);
        
        int[] pixels = new int[(WIDTH * HEIGHT)];
        fase.mapa.getRGB(0,0,WIDTH, HEIGHT, pixels, 0,WIDTH);
        var rand = new Random();
        for(int xx = 0; xx < WIDTH; xx++){
                for(int yy = 0; yy < HEIGHT; yy++){
                    if(pixels[xx + (yy*WIDTH)] == 0xFFffffff){ // parede
                        fase.entidades.add(new Parede(fase.indice, new Posicao(yy, xx)));
                    }
                        
                    switch(pixels[xx + (yy*WIDTH)]){
                        case 0xFF99e550 -> // horizontal
                            fase.entidades.add(new BichinhoVaiVemHorizontal(fase.indice,new Posicao(yy, xx)));
                        case 0xFFd77bba -> // vertical                             
                            fase.entidades.add(new BichinhoVaiVemVertical(fase.indice,new Posicao(yy, xx)));
                        case 0xFF76428a -> // chaser
                            fase.entidades.add(new Chaser(fase.indice,new Posicao(yy, xx)));
                        case 0xFFac3232 -> // cospefogo
                            fase.entidades.add(new CospeFogo(fase.indice,new Posicao(yy, xx), 0/*rand.nextInt(4)*/));
                        case 0xFFfbf236 -> // PLAYER
                            fase.entidades.add(new Hero(new Posicao(yy, xx)));
                        case 0xFFcbdbfc -> // portal
                            fase.entidades.add(new Portal(new Posicao(yy, xx))); 
                        case 0xFF5fcde4 -> // ziguezague
                            fase.entidades.add(new ZigueZague(fase.indice, new Posicao(yy, xx)));
                    }
                }
            }
    }
    
    public void apagaMundo(Fase fase){
        
    }
}
