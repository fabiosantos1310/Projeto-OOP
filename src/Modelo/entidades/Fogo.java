package Modelo.entidades;

import Auxiliar.Consts;
import Auxiliar.Desenho;
import Controler.Tela;
import auxiliar.Posicao;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import javax.swing.ImageIcon;

public class Fogo extends Entidade implements Serializable{
    private static final long serialVersionUID = 1L;
    protected String[] images = { "fire.png",  null,  "barrel.png", "fire.png", "barrel.png" };
    protected int direcaoFogo; // Renomeado para evitar conflito com Entidade.direcao se existir
    private int iDirectionV;
    private int iDirectionH;
    transient Tela tela;
    private int entidadeFaseAtualIndex; // Para armazenar o índice da fase para a imagem
    
    public Fogo(int faseAtual, Posicao p, int direcao) {
        super();
        this.tela = Desenho.acessoATelaDoJogo();
        this.entidadeFaseAtualIndex = faseAtual;
        this.direcaoFogo = direcao;

        String imageName = null;
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            imageName = this.images[this.entidadeFaseAtualIndex];
        }
        
        if (imageName != null) {
            try{
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
        
        this.bMortal = true;
        this.bTransponivel = true;
        this.pPosicao = p; // Atribuição direta, já que não chamou super(p)
        
        aplicarRotacaoDaImagem();
        configurarDirecoesDeMovimento();
    }

    private void aplicarRotacaoDaImagem() {
        if (this.iImage == null) return;
        switch(this.direcaoFogo){ // Usar direcaoFogo aqui
            // case 0: não gira ou gira 0 graus (direita)
            case 1: this.iImage = this.girarImagem(this.iImage, 90); break;  // baixo
            case 2: this.iImage = this.girarImagem(this.iImage, 180); break; // esquerda
            case 3: this.iImage = this.girarImagem(this.iImage, 270); break; // cima
        }
    }
    
    private void configurarDirecoesDeMovimento() {
        switch(this.direcaoFogo){ // Usar direcaoFogo aqui
            case 0: // direita
                this.iDirectionH = 1;
                this.iDirectionV = 2; // 2 significa sem movimento vertical
                break;
            case 1: // baixo
                this.iDirectionH = 2; // 2 significa sem movimento horizontal
                this.iDirectionV = 1;
                break;
            case 2: // esquerda
                this.iDirectionH = 0;
                this.iDirectionV = 2; // 2 significa sem movimento vertical
                break;
            case 3: // cima
                this.iDirectionH = 2; // 2 significa sem movimento horizontal
                this.iDirectionV = 0;
                break;            
        }
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if (Desenho.acessoATelaDoJogo() != null) {
            this.tela = Desenho.acessoATelaDoJogo();
        } else {
            System.err.println("Alerta em Fogo.readObject: Desenho.acessoATelaDoJogo() nulo.");
        }

        String imageName = null;
        if (this.images != null && this.entidadeFaseAtualIndex >= 0 && this.entidadeFaseAtualIndex < this.images.length && this.images[this.entidadeFaseAtualIndex] != null) {
            imageName = this.images[this.entidadeFaseAtualIndex];
        }
        if (imageName != null) {
            try {
                this.iImage = new ImageIcon(new java.io.File(".").getCanonicalPath() + Consts.PATH + imageName);
                aplicarRotacaoDaImagem();
                configurarDirecoesDeMovimento(); // Reconfigurar direções após carregar
            } catch (IOException e) {
                System.err.println("Erro ao recarregar imagem para Fogo: " + e.getMessage());
            }
        }
    }

    public void autoDesenho() {
        boolean moveu = false;
        if (iDirectionH == 0) { 
            if (podeMover(-1, 0)) {
                this.moveLeft();
                moveu = true;
            }
        } else if (iDirectionH == 1) {
            if (podeMover(1, 0)) {
                this.moveRight();
                moveu = true;
            }
        }

        if (!moveu) { // Só tenta mover verticalmente se não moveu horizontalmente (ou se iDirectionH == 2)
            if (iDirectionV == 0) {
                if (podeMover(0, -1)) {
                    this.moveUp();
                    moveu = true;
                }
            } else if (iDirectionV == 1) {
                if (podeMover(0, 1)) {
                    this.moveDown();
                    moveu = true;
                }
            }
        }
        
        if (!moveu && (this.tela != null && this.tela.current != null && this.tela.current.fogos != null)) {
             this.tela.current.fogos.remove(this);
        }
        super.autoDesenho();
    }

    private boolean podeMover(int dx, int dy) {
        if (this.pPosicao == null || this.tela == null) return false;
        
        Posicao proximaPosicao = new Posicao(this.pPosicao.getLinha() + dy, this.pPosicao.getColuna() + dx);
        
        // Verifica se a próxima posição é válida no mundo ANTES de verificar colisões específicas do fogo
        if (proximaPosicao.getLinha() < 0 || proximaPosicao.getLinha() >= this.tela.c.MUNDO_ALTURA ||
            proximaPosicao.getColuna() < 0 || proximaPosicao.getColuna() >= this.tela.c.MUNDO_LARGURA) {
            return false; 
        }
        // Não precisa mais da lógica de salvar e restaurar posição aqui se 'verificaPos' não a altera.
        // A lógica de verificaPos precisaria ser robusta.
        // Temporariamente, assumimos que ehPosicaoValida do ControleDeJogo é suficiente
        // para paredes, mas não para outras entidades (que o fogo deveria atravessar ou destruir).
        // A lógica de colisão do Fogo com outras entidades é feita em ControleDeJogo.verificaFogo.
        // Aqui, apenas verificamos se a célula em si é transitável (ex: não é uma parede intransponível para fogo).
        // Se o fogo deve passar por cima de tudo exceto os limites do mapa,
        // a validação de posição aqui pode ser mais simples ou até removida,
        // e a remoção do fogo ocorreria por ControleDeJogo ou ao sair dos limites.

        // Para simplificar, vamos assumir que o fogo só é barrado pelos limites do mapa por enquanto.
        // A lógica de colisão específica do fogo com o herói ou outras entidades é tratada em ControleDeJogo.verificaFogo().
        // E a remoção do fogo quando atinge algo é feita lá ou no autoDesenho se não puder mover.
        return this.tela.cj.ehPosicaoValida(this.tela.current, proximaPosicao, this); // Usa ehPosicaoValida para consistência.

    }
    
    public boolean verificaPos(){ // Este método é chamado por podeMover implicitamente através de ehPosicaoValida
        if (this.tela == null || this.tela.cj == null || this.tela.current == null) return false;
        this.tela.cj.verificaFogo(this.tela.current); 
        return this.tela.cj.ehPosicaoValida(this.tela.current, pPosicao, this);
    }
    
    private boolean validaPosicao(Posicao p){ // Este parece um helper mais genérico
         if (Desenho.acessoATelaDoJogo() == null) return false;
        return Desenho.acessoATelaDoJogo().ehPosicaoValida(p, this);    
    }
    
    @Override
    public boolean setPosicao(int linha, int coluna){
        if (this.pPosicao == null) { // Se pPosicao for nulo, crie um novo
            this.pPosicao = new Posicao(linha, coluna);
            return true;
        }
        // A lógica original de Fogo.setPosicao validava antes de mover.
        // Entidade.setPosicao não valida, apenas Posicao.setPosicao (que valida limites do mapa).
        // Vamos manter uma validação aqui se for importante para Fogo especificamente.
        Posicao p = new Posicao(linha, coluna);
        if(this.validaPosicao(p)){ // Usa o helper local validaPosicao
            return this.pPosicao.setPosicao(p);
        }
        return false;        
    }
}