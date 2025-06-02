package Controler;

import Modelo.entidades.Botao;
import Modelo.entidades.Cadeado;
import Modelo.entidades.Chaser;
import Modelo.entidades.Chave;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Portal;
import Modelo.entidades.Entidade;
import Modelo.entidades.Fogo;
import Modelo.entidades.Hero;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class ControleDeJogo implements Serializable {
    
    ArrayList<Entidade> eIntransponiveis;
    
    public ControleDeJogo(){
        eIntransponiveis = new ArrayList<>();
    }
    
    public void desenhaTudo(ArrayList<Entidade> entidades) {
        for (Entidade e : entidades) {
            e.autoDesenho();
        }
    }
    
    public void processaTudo(Fase fase) {
        if(eIntransponiveis.isEmpty()){
                for (Entidade e : fase.entidades) {
                    if (!e.isbTransponivel()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                        eIntransponiveis.add(e);
                }
            }
        }
        Hero hero = fase.getHero();
        for (Entidade e : fase.entidades) {
            if (hero.getPosicao().igual(e.getPosicao())) {                
                if (e.isbTransponivel()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                    if(e instanceof Portal portal){
                        portal.atravessouPortal();
                        this.eIntransponiveis.clear();
                    }
                    if (e.isbMortal()){
                        if(e instanceof Botao){
                            fase.addClone();
                        } 
                        fase.entidades.remove(e);   
                    }
                }
            }
            if(e instanceof CospeFogo cf)
                cf.iContaIntervalos++;
        }
        switch(fase.indice){
            case 0 -> verificaFogo(fase);
            case 1 -> verificaChave(fase);
            case 2 -> {
                    verificaFogo(fase);
                    verificaChave(fase);
                }
        }
    }

    public boolean ehPosicaoValida(Fase fase, Posicao p, Entidade current) {
        for (Entidade e : eIntransponiveis) {
            if(e instanceof Cadeado cadeado){
                if (cadeado.getPosicao().igual(p)) {
                    verificaCadeado(p, fase, cadeado);
                }
            }
            if (e != current) {
                if (e.getPosicao().igual(p)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public void verificaFogo(Fase fase){
        for(Fogo f : fase.fogos){
            if (f.getPosicao().igual(fase.getHero().getPosicao()) && f.getPosicao().igual(fase.getHero().getPosicao())) {
                fase.entidades.remove(f);
                fase.fogos.remove(f);
                fase.getHero().morrer();
            }
            if(fase.getClone() != null){
                if (f.getPosicao().igual(fase.getClone().getPosicao()) && f.getPosicao().igual(fase.getClone().getPosicao())) {
                    fase.entidades.remove(f);
                    fase.fogos.remove(f);
                    fase.getHero().morrer();
                }
            }
            
            
        }
    }
    public void verificaCadeado(Posicao p, Fase fase, Cadeado c){
        Hero hero = fase.getHero();
        if(hero.temChave){
            fase.entidades.remove(c);
            this.eIntransponiveis.remove(c);
            hero.removeChave();
        }
        
    }
    public void verificaChave(Fase fase){
        Hero hero = fase.getHero();
        Hero clone = fase.getClone();
        for(Entidade e : fase.chaves){
            if (e instanceof Chave c) {
                if ((c.getPosicao().igual(hero.getPosicao()) && c.getPosicao().igual(hero.getPosicao()))) {
                    fase.entidades.remove(c);
                    fase.chaves.remove(c);
                    fase.getHero().temChave = true;
                    hero.chaves++;
                }
                if(clone != null){
                    if((c.getPosicao().igual(clone.getPosicao()) && c.getPosicao().igual(clone.getPosicao()))){
                        fase.entidades.remove(c);
                        fase.chaves.remove(c);
                        fase.getHero().temChave = true;
                        hero.chaves++;
                    }
                }
            }
        }
    }
}
