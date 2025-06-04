package Controler;

import Modelo.entidades.Botao;
import Modelo.entidades.Cadeado;
import Modelo.entidades.Chaser;
import Modelo.entidades.Chave;
import Modelo.entidades.CospeFogo;
import Modelo.entidades.Portal;
import Modelo.entidades.Entidade;
import Modelo.entidades.Fogo;
import Modelo.entidades.Gelo;
import Modelo.entidades.Hero;
import Modelo.entidades.Moeda;
import Modelo.entidades.Parede;
import Modelo.fases.Fase;
import auxiliar.Posicao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator; 

public class ControleDeJogo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    ArrayList<Entidade> eIntransponiveis;
    ArrayList<Entidade> eMortais;
    ArrayList<Gelo> eGelos;
    private HashSet<Entidade> desenhadosNesteFrame; 
    
    public ControleDeJogo(){
        eIntransponiveis = new ArrayList<>();
        eMortais = new ArrayList<>();
        eGelos = new ArrayList<>();
        desenhadosNesteFrame = new HashSet<>(); 
    }
    
    public void desenhaTudo(Fase fase) {
        if (fase == null || Auxiliar.Desenho.getGraphicsDaTela() == null) {
            return;
        }
        desenhadosNesteFrame.clear(); 

        if (fase.paredes != null) {
            for (Parede p : fase.paredes) {
                if (p != null && desenhadosNesteFrame.add(p)) {
                    p.autoDesenho();
                }
            }
        }
        
        if (this.eGelos != null) { 
            for (Gelo g : this.eGelos) {
                if (g != null && desenhadosNesteFrame.add(g)) {
                    g.autoDesenho();
                }
            }
        }
        
        if (this.eIntransponiveis != null) {
            // Iterar sobre uma cópia se eIntransponiveis puder ser modificado indiretamente
            // por chamadas a autoDesenho de seus elementos, embora seja menos comum para esta lista cache.
            // Por segurança, ou se souber que pode ser modificado: new ArrayList<>(this.eIntransponiveis)
            for (Entidade e : this.eIntransponiveis) { 
                if (e != null && !(e instanceof Parede) && desenhadosNesteFrame.add(e)) {
                    e.autoDesenho(); 
                }
            }
        }

        if (fase.entidades != null) {
            // Iterar sobre uma cópia de fase.entidades, pois os métodos autoDesenho()
            // de algumas entidades (ex: Chaser se ele se remove) podem modificar esta lista.
            for (Entidade e : new ArrayList<>(fase.entidades)) { 
                if (e != null && !(e instanceof Hero) && !(e instanceof Parede) && desenhadosNesteFrame.add(e)) { 
                    e.autoDesenho();
                }
            }
        }
        
        if (fase.chaves != null) {
            for (Chave ch : fase.chaves) {
                if (ch != null && desenhadosNesteFrame.add(ch)) { ch.autoDesenho(); }
            }
        }
        if (fase.moedas != null) {
            for (Moeda m : fase.moedas) {
                if (m != null && desenhadosNesteFrame.add(m)) { m.autoDesenho(); }
            }
        }
        if (fase.fogos != null) {
            // Iterar sobre uma cópia de fase.fogos, pois Fogo.autoDesenho() remove a si mesmo,
            // e CospeFogo.autoDesenho() adiciona a esta lista.
            for (Fogo f : new ArrayList<>(fase.fogos)) { 
                if (f != null && desenhadosNesteFrame.add(f)) { 
                    f.autoDesenho(); 
                }
            }
        }
        if (fase.cospeFogos != null) { 
            for (CospeFogo cf : fase.cospeFogos) {
                if (cf != null && desenhadosNesteFrame.add(cf)) { cf.autoDesenho(); }
            }
        }

        Hero currentHero = fase.getHero();
        if (currentHero != null) {
            if (desenhadosNesteFrame.add(currentHero)) { 
                currentHero.autoDesenho();
            } else { 
                currentHero.autoDesenho(); 
            }
        }
        Hero currentClone = fase.getClone();
        if (fase.hasClone && currentClone != null) {
            if (desenhadosNesteFrame.add(currentClone)) {
                currentClone.autoDesenho();
            } else {
                currentClone.autoDesenho();
            }
        }
    }
    
    public void processaTudo(Fase fase) {
        if (fase == null) return;
        Hero hero = fase.getHero();
        if (hero == null) return;

        if (eIntransponiveis.isEmpty()) {
            if (fase.entidades != null) {
                for (Entidade e : fase.entidades) {
                    if (e != null && !e.isbTransponivel() && !(e instanceof Hero)) {
                        eIntransponiveis.add(e);
                    }
                }
            }
            if (fase.paredes != null) {
                eIntransponiveis.addAll(fase.paredes);
            }
        }
        if (eMortais.isEmpty()) {
            if (fase.entidades != null) {
                for (Entidade e : fase.entidades) {
                    if (e != null && e.isbMortal()) { 
                        eMortais.add(e);
                    }
                }
            }
            if (hero.isbMortal() && !eMortais.contains(hero)) {
                 eMortais.add(hero);
            }
            if (fase.getClone() != null && fase.getClone().isbMortal() && !eMortais.contains(fase.getClone())) {
                 eMortais.add(fase.getClone());
            }
        }
        if (eGelos.isEmpty()) {
            if (fase.entidades != null) {
                for (Entidade e : fase.entidades) {
                    if (e instanceof Gelo g) {
                        eGelos.add(g);
                    }
                }
            }
        }

        boolean heroiDeveMorrerNesteTick = false;

        if (fase.entidades != null) {
            Iterator<Entidade> iterEntidades = fase.entidades.iterator();
            while(iterEntidades.hasNext()){
                Entidade e = iterEntidades.next();
                if (e == hero || e == fase.getClone() || e.getPosicao() == null || hero.getPosicao() == null) continue;

                if (hero.getPosicao().igual(e.getPosicao())) {
                    if (e instanceof Portal portal) {
                        portal.atravessouPortal(); 
                        return; 
                    } else if (e instanceof Botao botao) {
                        fase.addClone(); 
                        iterEntidades.remove(); 
                        eMortais.remove(botao);
                    } else if (e.isbMortal()) {
                        if (!(e instanceof Chaser) && !(e instanceof Fogo)) { 
                            heroiDeveMorrerNesteTick = true;
                        }
                    }
                }
            }
        }
        
        if (heroiDeveMorrerNesteTick) {
            hero.morrer();
            return; 
        }

        switch(fase.indice){
            case 0: verificaFogo(fase, hero); break;
            case 1: verificaChave(fase, hero); break;
            case 2: verificaFogo(fase, hero); verificaChave(fase, hero); break;
            case 3: verificaFogo(fase, hero); verificaChave(fase, hero); break;
            case 4: verificaFogo(fase, hero); verificaChave(fase, hero); verificaMoeda(fase, hero); break;
        }
        
        if(hero.getPosicao() != null && ehGelo(hero.getPosicao())){
            switch(hero.direcao){
                case 0: hero.moveRight(); break;
                case 1: hero.moveDown(); break;
                case 2: hero.moveLeft(); break;
                case 3: hero.moveUp(); break;
            }
        }
    }

    public boolean ehPosicaoValida(Fase fase, Posicao p, Entidade entidadeAtual) {
        if (fase == null || p == null) return false;
        
        if (Auxiliar.Game.tTela != null && Auxiliar.Game.tTela.c != null) {
            if (p.getLinha() < 0 || p.getLinha() >= Auxiliar.Game.tTela.c.MUNDO_ALTURA ||
                p.getColuna() < 0 || p.getColuna() >= Auxiliar.Game.tTela.c.MUNDO_LARGURA) {
                return false;
            }
        }

        for (Entidade e : eIntransponiveis) { 
            if (e == entidadeAtual || e.getPosicao() == null) continue; 

            if (e.getPosicao().igual(p)) {
                if(e instanceof Cadeado cadeado){ 
                    verificaCadeado(p, fase, cadeado); 
                    return !eIntransponiveis.contains(cadeado); 
                }
                return false; 
            }
        }
        return true;
    }
    
    public boolean ehGelo(Posicao p){
        if (p == null || eGelos == null) return false;
        for(Gelo g : eGelos){ 
            if(g.getPosicao() != null && p.igual(g.getPosicao()))
                return true;
        }
        return false;
    }
    
    public void verificaFogo(Fase fase, Hero hero){
        if (fase == null || fase.fogos == null || hero == null || hero.getPosicao() == null) return;

        boolean heroiMortoPeloFogoNesteMetodo = false;
        Iterator<Fogo> fogoIterator = fase.fogos.iterator();
        while (fogoIterator.hasNext()) {
            Fogo f = fogoIterator.next();
            if (f.getPosicao() == null) continue;

            if (f.getPosicao().igual(hero.getPosicao())) {
                fogoIterator.remove(); 
                heroiMortoPeloFogoNesteMetodo = true; 
                break; 
            }
            
            Iterator<Entidade> mortalIterator = eMortais.iterator();
            boolean fogoConsumidoPorOutroMortal = false;
            while (mortalIterator.hasNext()) {
                Entidade m = mortalIterator.next();
                // Evita colisão do fogo com o próprio herói (já tratado), com o clone, ou com nulos/ele mesmo.
                if (m == hero || (fase.getClone() != null && m == fase.getClone()) || m.getPosicao() == null || m == f) continue;

                if (f.getPosicao().igual(m.getPosicao())) {
                    mortalIterator.remove();    
                    fase.entidades.remove(m); 
                    fogoConsumidoPorOutroMortal = true;
                    break; 
                }
            }
            if (fogoConsumidoPorOutroMortal) {
                fogoIterator.remove(); 
            }
        }
        if (heroiMortoPeloFogoNesteMetodo) { 
             if(!hero.isClone){ 
                hero.morrer();
             } else { 
                 fase.entidades.remove(hero); 
                 eMortais.remove(hero);
                 if (fase.getHero() != null && hero == fase.getClone()) { // Garante que estamos removendo o clone
                    fase.hasClone = false; 
                 }
             }
        }
    }

    public void verificaCadeado(Posicao p, Fase fase, Cadeado c){
        if (fase == null || fase.getHero() == null || c == null) return;
        Hero hero = fase.getHero();
        boolean removeCadeado = false;
        if(hero.temChave){
            removeCadeado = true;
            hero.removeChave();
        } else if(hero.moedas >= 40){ 
            removeCadeado = true;
            hero.removeMoedas(40);
        }
        if (removeCadeado) {
            this.eIntransponiveis.remove(c); 
            fase.entidades.remove(c); 
        }
    }

    public void verificaChave(Fase fase, Hero hero){
        if (fase == null || fase.chaves == null || hero == null) return;
        Hero clone = fase.getClone();

        Iterator<Chave> iterator = fase.chaves.iterator();
        while (iterator.hasNext()) {
            Chave c = iterator.next();
            if (c.getPosicao() == null) continue;
            boolean coletada = false;
            if (hero.getPosicao() != null && c.getPosicao().igual(hero.getPosicao())) {
                coletada = true;
            }
            if (!coletada && clone != null && clone.getPosicao() != null && c.getPosicao().igual(clone.getPosicao())) {
                 coletada = true;
            }
            if (coletada) {
                iterator.remove(); 
                fase.entidades.remove(c); 
                hero.temChave = true;
                hero.chaves++;
            }
        }
    }

    public void verificaMoeda(Fase fase, Hero hero){
         if (fase == null || fase.moedas == null || hero == null || hero.getPosicao() == null) return;
        
        Iterator<Moeda> iterator = fase.moedas.iterator();
        while(iterator.hasNext()){
            Moeda m = iterator.next();
            if(m.getPosicao() != null && m.getPosicao().igual(hero.getPosicao())){
                iterator.remove(); 
                fase.entidades.remove(m); 
                hero.addMoeda();
            }
        }
    }
    
    public void limparListas(){
        this.eIntransponiveis.clear();
        this.eMortais.clear();
        this.eGelos.clear();
    }
}