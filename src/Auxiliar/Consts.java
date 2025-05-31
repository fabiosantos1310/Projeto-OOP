package Auxiliar;

import java.io.File;

public class Consts {
    public static int CELL_SIDE = 48;
    public static final int RES = 15; // visível na tela
    public int MUNDO_LARGURA; // total do mundo
    public int MUNDO_ALTURA;
    public static final int PERIOD = 200;
    public static final String PATH = File.separator+"imgs"+File.separator;
    public static final int TIMER = 15;
    
    public void setLargura(int val){
        MUNDO_LARGURA = val;
    }
    public void setAltura(int val){
        MUNDO_ALTURA = val;
    }
}