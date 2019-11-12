public class SmartZombie extends Personagem{
    private Personagem alvo;
    private Personagem alvoAnterior;
    private int lin = this.getCelula().getLinha();
    private int col = this.getCelula().getColuna();



    public SmartZombie(int linInicial, int colInicial){
        super(10, "Zumbi", linInicial, colInicial);
        alvo = null;
    }

    private Personagem defineAlvo(){
        System.out.println("Procurando alvo");
        for(int l = 0; l < Jogo.NLIN; l++){
            for(int c = 0; c < Jogo.NCOL; c++){
                Personagem p = Jogo.getInstance().getCelula(l, c).getPersonagem();
                if(p != null && !(p instanceof SmartZombie) && !(p instanceof SafeZone) && !(p.infectado()) && (p.estaVivo())){
                    alvoAnterior = alvo;
                    alvo = p;
                    System.out.println("Alvo definido: "+alvo.getImage());
                    System.out.println("Posicao do meu alvo: LINHA = ["+p.getCelula().getLinha()+"] COLUNA = ["+p.getCelula().getColuna()+"]");
                    return p;
                }
            }
        }
        return null;
    }

    @Override
    public void atualizaPosicao() {
        if(!this.estaVivo()){
            return;
        }

        if (alvo == null || alvo.infectado()){
            alvo = defineAlvo();
            return;
        }

        // Pega posicao atual do ZumbiEsperto
        int oldLin = this.getCelula().getLinha();
        int oldCol = this.getCelula().getColuna();

        // Pega a posicao do alvo
        int linAlvo = alvo.getCelula().getLinha();
        int colAlvo = alvo.getCelula().getColuna();

        calculaDeslocamento(colAlvo, linAlvo);
        // Verifica se não quer ir para uma celula ocupada
        if (Jogo.getInstance().getCelula(lin, col).getPersonagem() != null){
            Personagem aq = Jogo.getInstance().getCelula(lin, col).getPersonagem();    
            int dirLin = Jogo.getInstance().aleatorio(3)-1;
            int dirCol = Jogo.getInstance().aleatorio(3)-1;
            oldLin = this.getCelula().getLinha();
            oldCol = this.getCelula().getColuna();
            lin = oldLin + dirLin;
            col = oldCol + dirCol;
            if (lin < 0) lin = 0;
            if (lin >= Jogo.NLIN) lin = Jogo.NLIN-1;
            if (col < 0) col = 0;
            if (col >= Jogo.NCOL) col = Jogo.NCOL-1;
            if (Jogo.getInstance().getCelula(lin, col).getPersonagem() != null){
                return;
            }else{
                // Limpa celula atual
                Jogo.getInstance().getCelula(oldLin, oldCol).setPersonagem(null);
                // Coloca personagem na nova posição
                Jogo.getInstance().getCelula(lin, col).setPersonagem(this);
             }
        }else{
            // Limpa celula atual
            Jogo.getInstance().getCelula(oldLin, oldCol).setPersonagem(null);
            // Coloca personagem na nova posição
            Jogo.getInstance().getCelula(lin, col).setPersonagem(this);
        }
    
    }


        private void linhaMove(int quanto){
        this.lin = lin+(quanto);
        if(lin < 0){
            lin = 2;
        }
        if(lin >= Jogo.NLIN){
            lin = Jogo.NLIN -2;
        }
    }

    private void colunaMove(int quanto){
        this.col = col+(quanto);
        if(col < 0){
            col = 2;
        }
        if(col >= Jogo.NCOL){
            col = Jogo.NCOL -2;
        }
    }



    private void calculaDeslocamento(int colAlvo, int linAlvo){
        if (lin < linAlvo) lin++;
        if (lin > linAlvo) lin--;
        if (col < colAlvo) col++;
        if (col > colAlvo) col--;

        // Verifica se não saiu dos limites do tabuleiro
        if (lin < 0) lin = 0;
        if (lin >= Jogo.NLIN) lin = Jogo.NLIN-1;
        if (col < 0) col = 0;
        if (col >= Jogo.NCOL) col = Jogo.NCOL-1;
    }




     @Override
    public void influenciaVizinhos() {
        if(!this.estaVivo()){
            return;
        }
        int lin = this.getCelula().getLinha();
        int col = this.getCelula().getColuna();
        for(int l=lin-1;l<=lin+1;l++){ // se trocar o condicional do for, aumenta o range de contato 
            for(int c=col-1;c<=col+1;c++){ // se trocar o condicional do for, aumenta o range de contato 
                // Se a posição é dentro do tabuleiro
                if (l>=0 && l<Jogo.NLIN && c>=0 && c<Jogo.NCOL){
                    // Se não é a propria celula
                    if (!( lin == l && col == c)){
                        // Recupera o personagem da célula vizinha
                        Personagem p = Jogo.getInstance().getCelula(l,c).getPersonagem();
                        // Se não for nulo, infecta
                        if (p != null && !(p instanceof SafeZone) && !(p instanceof SmartZombie)){
                            p.infecta();
                            alvo = defineAlvo();
                        }
                    }
                }
            }
        }
    }

     @Override
    public void verificaEstado() {
         if (this.getEnergia() == 0) {
                this.setImage("DeadZumbi");
                this.getCelula().setImageFromPersonagem();
        }
    }

}