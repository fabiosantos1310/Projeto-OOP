package Controler;

import Modelo.entidades.Cadeado;
import Modelo.entidades.Chaser;
import Modelo.entidades.Chave;
import Modelo.entidades.Portal;
import Modelo.entidades.Entidade;
import Modelo.entidades.Fogo;
import Modelo.entidades.Hero;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class ControleDeJogo implements Serializable {
    
    public void desenhaTudo(ArrayList<Entidade> entidades) {
        for (Entidade e : entidades) {
            e.autoDesenho();
        }
    }
    
    public void processaTudo(Fase fase) {
        Hero hero = fase.getHero();
        for (Entidade e : fase.entidades) {
            if (hero.getPosicao().igual(e.getPosicao())) {
                if (e.isbTransponivel()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                    if (e.isbMortal())
                        fase.entidades.remove(e);
                }
            }
        }
        verificaFogo(fase);
        verificaChave(fase);
    }

    public boolean ehPosicaoValida(Fase fase, Posicao p, Entidade current) {
        for (Entidade e : fase.entidades) {
            if(e instanceof Cadeado cadeado){
                if (cadeado.getPosicao().igual(p)) {
                    verificaCadeado(p, fase, cadeado);
                }
            }
            if (!e.isbTransponivel() && e != current) {
                if (e.getPosicao().igual(p)) {
                    return false;
                }
            }
            if(e instanceof Portal portal){
                if (e.getPosicao().igual(p)) {
                    portal.atravessouPortal();
                }
            }
        }
        return true;
    }
    
    public void verificaFogo(Fase fase){
        for(Entidade e : fase.entidades){
            if (e instanceof Fogo f) {
                if (f.getPosicao().igual(fase.getHero().getPosicao()) && f.getPosicao().igual(fase.getHero().getPosicao())) {
                    fase.entidades.remove(f);
                    fase.getHero().morrer();
                }
            }
        }
    }
    public void verificaCadeado(Posicao p, Fase fase, Cadeado c){
        Hero hero = fase.getHero();
        if(hero.temChave){
            fase.entidades.remove(c);
            hero.removeChave();
        }
        
    }
    public void verificaChave(Fase fase){
        Hero hero = fase.getHero();
        for(Entidade e : fase.entidades){
            if (e instanceof Chave c) {
                if (c.getPosicao().igual(hero.getPosicao()) && c.getPosicao().igual(hero.getPosicao())) {
                    fase.entidades.remove(c);
                    hero.temChave = true;
                    hero.chaves++;
                }
            }
        }
    }
}
