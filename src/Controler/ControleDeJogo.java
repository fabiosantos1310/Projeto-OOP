package Controler;

import Modelo.entidades.Botao;
import Modelo.entidades.Cadeado;
import Modelo.entidades.Chave;
import Modelo.entidades.Portal;
import Modelo.entidades.Entidade;
import Modelo.entidades.Fogo;
import Modelo.entidades.Gelo;
import Modelo.entidades.Hero;
import Modelo.entidades.Moeda;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;

public class ControleDeJogo implements Serializable {
    
     ArrayList<Entidade> eIntransponiveis;
     ArrayList<Entidade> eMortais;
     ArrayList<Gelo> eGelos;
    
    
    public ControleDeJogo(){
        eIntransponiveis = new ArrayList<>();
        eMortais = new ArrayList<>();
        eGelos = new ArrayList<>();
    }
    
    public void desenhaTudo(Fase fase) {
        for (Entidade e : fase.fogos) {
            e.autoDesenho();
        }
        for(Entidade e : fase.chaves){
            e.autoDesenho();
        }
        for(Entidade e : fase.moedas){
            e.autoDesenho();
        }
        for (Entidade e : eIntransponiveis) {
            if(!(e instanceof Hero))
                e.autoDesenho();
        }
        for(Entidade e : fase.entidades){
            if(e.isbTransponivel() && !(e instanceof Hero))
                e.autoDesenho();
        }
        if(fase.hasClone)
            fase.getClone().autoDesenho();
        fase.getHero().autoDesenho();
    }
    
    public void processaTudo(Fase fase) {
        if(eIntransponiveis.isEmpty()){
                for (Entidade e : fase.entidades) {
                    if (!e.isbTransponivel()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                        eIntransponiveis.add(e);
                }
            }
            eIntransponiveis.addAll(fase.paredes);
        }
        if(eMortais.isEmpty()){
                for (Entidade e : fase.entidades) {
                    if (e.isbMortal()) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                        eMortais.add(e);
                }
            }
        }
        if(eGelos.isEmpty()){
            for (Entidade e : fase.entidades) {
                    if (e instanceof Gelo g) /*TO-DO: verificar se o personagem eh mortal antes de retirar*/ {
                        eGelos.add(g);
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
                            eMortais.add(fase.getClone());
                        } 
                        fase.entidades.remove(e);   
                    }
                }
            }
                
        }
        switch(fase.indice){
            case 0 -> verificaFogo(fase);
            case 1 -> verificaChave(fase);
            case 2 -> {
                    verificaFogo(fase);
                    verificaChave(fase);
                }
            case 3 -> {
                    verificaFogo(fase);
                    verificaChave(fase);
                }
            case 4 -> {
                    verificaFogo(fase);
                    verificaChave(fase);
                    verificaMoeda(fase);
                }
        }
        if(ehGelo(hero.getPosicao())){
            switch(hero.direcao){
                case 0 -> {
                    hero.moveRight();
                }
                case 1 -> hero.moveDown();
                case 2 -> hero.moveLeft();
                case 3 -> hero.moveUp();
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
    
    public boolean ehGelo(Posicao p){
        for(Gelo g : eGelos){
            if(p.igual(g.getPosicao()))
                return true;
        }
        return false;
    }
    
    public void verificaFogo(Fase fase){
        
        for(Fogo f : fase.fogos){
            for(Entidade e : eMortais){
                if (f.getPosicao().igual(e.getPosicao())) {
                    if(e instanceof Hero){
                        fase.fogos.remove(f); 

                        fase.getHero().morrer();
                        
                        return;
                    }
                    fase.entidades.remove(e);
                    eMortais.remove(e);
                    fase.fogos.remove(f); 
                }
            }
            
            
            
        }
    }
    public void verificaCadeado(Posicao p, Fase fase, Cadeado c){
        Hero hero = fase.getHero();
        if(hero.temChave){
            this.eIntransponiveis.remove(c);
            hero.removeChave();
        } else if(hero.moedas >= 40){
            this.eIntransponiveis.remove(c);
            hero.removeMoedas(40);
        }
        
    }
    public void verificaChave(Fase fase){
        Hero hero = fase.getHero();
        Hero clone = fase.getClone();
        for(Entidade e : fase.chaves){
            if (e instanceof Chave c) {
                if ((c.getPosicao().igual(hero.getPosicao()))) {
                    fase.chaves.remove(c);
                    fase.getHero().temChave = true;
                    hero.chaves++;
                }
                if(clone != null){
                    if((c.getPosicao().igual(clone.getPosicao()))){
                        fase.chaves.remove(c);
                        fase.getHero().temChave = true;
                        hero.chaves++;
                    }
                }
            }
        }
    }
    public void verificaMoeda(Fase fase){
        Hero hero = fase.getHero();
        for(Entidade e : fase.moedas){
            if (e instanceof Moeda m) {
                if ((m.getPosicao().igual(hero.getPosicao()))) {
                    fase.entidades.remove(m);
                    fase.moedas.remove(m);
                    hero.addMoeda();
                }
            }
        }
    }
    
    public void limparListas(){
        this.eIntransponiveis.clear();
        this.eMortais.clear();
        this.eGelos.clear();
    }
}
