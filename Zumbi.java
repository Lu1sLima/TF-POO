public class Zumbi extends Personagem {

    private boolean morto = false;

    public Zumbi(int linInicial,int colInicial){
        super(10,"Zumbi",linInicial,colInicial);
    }

    @Override
    public void atualizaPosicao() {
        if(!this.estaVivo()){
            return;
        }
        int dirLin = Jogo.getInstance().aleatorio(3)-1;
        int dirCol = Jogo.getInstance().aleatorio(3)-1;
        int oldLin = this.getCelula().getLinha();
        int oldCol = this.getCelula().getColuna();
        int lin = oldLin + dirLin;
        int col = oldCol + dirCol;
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
                        if (p != null & !(p instanceof SafeZone)){
                            p.infecta();
                        }
                    }
                }
            }
        }
        
    }

    @Override
    public void verificaEstado() {
         if (this.getEnergia() == 0) {
                this.setImage("Morto");
                this.getCelula().setImageFromPersonagem();
            }
    }
}