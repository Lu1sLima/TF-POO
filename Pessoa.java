public class Pessoa extends Personagem {
    public Pessoa(int linInicial, int colInicial) {
        super(10, "Normal", linInicial, colInicial); // int energiaInicial, String imagemInicial,int linInicial,int colInicial
    }



    // TODA CLASSE PERSONAGEM SEM SER OS ZUMBIS DEVEM IMPLEMENTAR O METODO INFECTA E CURA NO MESMO ESTILO DESSES
    @Override
    public void infecta(){
        if (this.infectado()){
            return;
        }
        super.infecta();
        this.setImage("Infectado");
        this.getCelula().setImageFromPersonagem();   
    }

    @Override
    public void cura(){
        if (this.infectado() == false){
            return;
        }
        super.cura();
        this.setImage("Normal");
        this.getCelula().setImageFromPersonagem();   
    }

    // END


    @Override
    public void atualizaPosicao() {
        if (!this.estaVivo()){
            return;
        }else{
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
    }

    @Override
    public void influenciaVizinhos() {
        // Não influencia ninguém
    }

    @Override
    public void verificaEstado() {
        // Se esta morto retorna
        if (!this.estaVivo()){
            return;
        }
        // Se esta infectado perde energia a cada passo
        if (this.infectado()) {
            diminuiEnergia(2);
            // Se não tem mais energia morre
            if (this.getEnergia() == 0) {
                this.setImage("Morto");
                this.getCelula().setImageFromPersonagem();
            }
        }
    }
}