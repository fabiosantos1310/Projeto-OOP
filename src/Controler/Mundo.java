package Controler;

import Auxiliar.Consts;
import Modelo.entidades.BichinhoVaiVemHorizontal;
import Modelo.entidades.BichinhoVaiVemVertical;
import Modelo.entidades.Botao;
import Modelo.entidades.Cadeado;
import Modelo.entidades.Chaser;
import Modelo.entidades.Chave;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Entidade;
import Modelo.entidades.Gelo;
import Modelo.entidades.Hero;
import Modelo.entidades.Moeda;
import Modelo.entidades.Parede;
import Modelo.entidades.Portal;
import Modelo.entidades.ZigueZague;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.util.ArrayList; 


public class Mundo {

    private boolean entidadeExisteNaPosicao(ArrayList<? extends Entidade> lista, Class<? extends Entidade> tipoEntidade, Posicao pos) {
        if (lista == null || pos == null || tipoEntidade == null) return false;
        for (Entidade e : lista) {
            if (tipoEntidade.isInstance(e) && e.getPosicao() != null && e.getPosicao().igual(pos)) {
                return true;
            }
        }
        return false;
    }

    public void carregaMundo(Fase fase, Consts c) {
        if (fase == null || fase.mapa == null || c == null) {
            System.err.println("Erro: Fase, mapa ou Consts nulos em Mundo.carregaMundo.");
            return;
        }
        
        apagarMundo(fase); 

        int WIDTH = fase.mapa.getWidth();
        int HEIGHT = fase.mapa.getHeight();
        c.setLargura(WIDTH);
        c.setAltura(HEIGHT);
        
        int[] pixels = new int[(WIDTH * HEIGHT)];
        fase.mapa.getRGB(0, 0, WIDTH, HEIGHT, pixels, 0, WIDTH);
        
        Hero heroInitial = null; 
        int cospeFogoCounter = 0;

        for (int xx = 0; xx < WIDTH; xx++) {
            for (int yy = 0; yy < HEIGHT; yy++) {
                int pixelColor = pixels[xx + (yy * WIDTH)];
                Posicao pos = new Posicao(yy, xx);

                if (pixelColor == 0xFFFFFFFF) { 
                    if (!entidadeExisteNaPosicao(fase.paredes, Parede.class, pos)) {
                        fase.paredes.add(new Parede(pos));
                    }
                } else {
                    switch (pixelColor) {
                        case 0xFF99e550: 
                            if (!entidadeExisteNaPosicao(fase.entidades, BichinhoVaiVemHorizontal.class, pos)) {
                                fase.entidades.add(new BichinhoVaiVemHorizontal(fase.indice, pos));
                            }
                            break;
                        case 0xFFd77bba:                             
                            if (!entidadeExisteNaPosicao(fase.entidades, BichinhoVaiVemVertical.class, pos)) {
                                fase.entidades.add(new BichinhoVaiVemVertical(fase.indice, pos));
                            }
                            break;
                        case 0xFF76428a: 
                            if (!entidadeExisteNaPosicao(fase.entidades, Chaser.class, pos)) {
                                fase.entidades.add(new Chaser(fase.indice, pos));
                            }
                            break;
                        case 0xFFac3232: 
                            if (!entidadeExisteNaPosicao(fase.cospeFogos, CospeFogo.class, pos)) {
                                if (fase.direcoesFogo != null && cospeFogoCounter < fase.direcoesFogo.length) {
                                    CospeFogo cf = new CospeFogo(fase.indice, pos, fase.direcoesFogo[cospeFogoCounter]);
                                    fase.cospeFogos.add(cf);
                                    if(!entidadeExisteNaPosicao(fase.entidades, CospeFogo.class, pos)){
                                         fase.entidades.add(cf);
                                    }
                                    cospeFogoCounter++;
                                }
                            }
                            break;
                        case 0xFFfbf236: 
                            if (heroInitial == null) { 
                                heroInitial = new Hero(pos, false, fase.indice);
                            }
                            break;
                        case 0xFFcbdbfc: 
                            if (!entidadeExisteNaPosicao(fase.entidades, Portal.class, pos)) {
                                fase.entidades.add(new Portal(pos));
                            }
                            break;
                        case 0xFF5fcde4: 
                            if (!entidadeExisteNaPosicao(fase.entidades, ZigueZague.class, pos)) {
                                fase.entidades.add(new ZigueZague(fase.indice, pos));
                            }
                            break;
                        case 0xFFd3dc00:
                            if (!entidadeExisteNaPosicao(fase.chaves, Chave.class, pos)) {
                                Chave chave = new Chave(pos);
                                fase.chaves.add(chave);
                                 if(!entidadeExisteNaPosicao(fase.entidades, Chave.class, pos)){
                                     fase.entidades.add(chave); 
                                }
                            }
                            break;
                        case 0xFFff45ff: 
                            if (!entidadeExisteNaPosicao(fase.entidades, Cadeado.class, pos)) {
                                fase.entidades.add(new Cadeado(pos));
                            }
                            break;
                        case 0xFFffc500: 
                            if (!entidadeExisteNaPosicao(fase.entidades, Botao.class, pos)) {
                                fase.entidades.add(new Botao(pos));
                            }
                            break; 
                        case 0xFFf1fbfc:  
                            if (!entidadeExisteNaPosicao(fase.entidades, Gelo.class, pos)) {
                                fase.entidades.add(new Gelo(pos));
                            }
                            break;
                        case 0xFF004c61:
                            if (!entidadeExisteNaPosicao(fase.moedas, Moeda.class, pos)) {
                                Moeda moeda = new Moeda(pos);
                                fase.moedas.add(moeda);
                                if(!entidadeExisteNaPosicao(fase.entidades, Moeda.class, pos)){
                                    fase.entidades.add(moeda);
                                }
                            }
                            break;
                    }
                }
            }
        }

        boolean heroInstanceExists = false;
        for(Entidade e : fase.entidades){
            if(e instanceof Hero && !((Hero)e).isClone){ // Verifica se já existe uma instância de Hero (não clone)
                heroInstanceExists = true;
                break;
            }
        }
        if(!heroInstanceExists){
            if (heroInitial != null && heroInitial.getPosicao() != null) {
                fase.entidades.add(new Hero(heroInitial.getPosicao(), false, fase.indice));
            } else {
                // Adiciona um herói em posição padrão se nenhum foi definido no mapa
                // para garantir que a fase sempre tenha um herói.
                fase.entidades.add(new Hero(new Posicao(1, 1), false, fase.indice)); 
            }
        }
        
        // DEBUG: Inserir no final de Mundo.carregaMundo
        int debugTotalEntidadesInit = 0;
        int debugBVHInit = 0;
        int debugBVVInit = 0;
        if (fase.entidades != null) {
            debugTotalEntidadesInit = fase.entidades.size();
            for (Modelo.entidades.Entidade ent : fase.entidades) {
                if (ent instanceof Modelo.entidades.BichinhoVaiVemHorizontal) debugBVHInit++;
                if (ent instanceof Modelo.entidades.BichinhoVaiVemVertical) debugBVVInit++;
            }
        }
        System.out.println("DEBUG_MUNDO_INIT: Fase " + fase.indice +
                           " | Entidades: " + debugTotalEntidadesInit +
                           " | BVH: " + debugBVHInit +
                           " | BVV: " + debugBVVInit +
                           " | Paredes: " + (fase.paredes != null ? fase.paredes.size() : 0) +
                           " | Chaves: " + (fase.chaves != null ? fase.chaves.size() : 0) +
                           " | Moedas: " + (fase.moedas != null ? fase.moedas.size() : 0));
    }

    public void apagarMundo(Fase fase) {
        if (fase == null) return;
        fase.entidades.clear();
        fase.chaves.clear();
        fase.cospeFogos.clear();
        fase.fogos.clear();
        fase.paredes.clear();
        fase.moedas.clear();
    }
    
    public void recomecarFase(Fase fase, Consts c) {
        if (fase == null || c == null) return;
        carregaMundo(fase, c); 
    }
}