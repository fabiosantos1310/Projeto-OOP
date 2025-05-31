package Controler;

import Modelo.entidades.Chaser;
import Modelo.entidades.Portal;
import Modelo.entidades.Entidade;
import Modelo.entidades.Hero;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.util.ArrayList;

public class ControleDeJogo {
    
    public void desenhaTudo(ArrayList<Entidade> e) {
        for (int i = 0; i < e.size(); i++) {
            e.get(i).autoDesenho();
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
        for (Entidade e : fase.entidades) {
            if (e instanceof Chaser) {
                ((Chaser) e).computeDirection(hero.getPosicao());
            }
        }
    }

    /*Retorna true se a posicao p é válida para Hero com relacao a todos os personagens no array*/
    public boolean ehPosicaoValida(Fase fase, Posicao p, Entidade current) {
        for (Entidade e : fase.entidades) {
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
}
