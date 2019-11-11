import java.util.Random;
public class Policial extends Personagem{
    private static int zMortos = 0;
    public Policial(int linInicial, int colInicial){
        super(15, "Policial", linInicial, colInicial);
        super.setTemArma();
    }

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
        this.setImage("Policial");
        this.getCelula().setImageFromPersonagem();   
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
    /**
     * @return the zMortos
     */
    public static int getzMortos() {
        return zMortos;
    }

    @Override
    public void influenciaVizinhos(){
        if(!this.estaVivo() || this.infectado()){
            return;
        }
        int lin = this.getCelula().getLinha();
        int col = this.getCelula().getColuna();
        for(int l=lin-2;l<=lin+2;l++){
            for(int c=col-2;c<=col+2;c++){
                // Se a posição é dentro do tabuleiro
                if (l>=0 && l<Jogo.NLIN && c>=0 && c<Jogo.NCOL){
                    // Se não é a propria celula
                    if (!( lin == l && col == c)){
                        // Recupera o personagem da célula vizinha
                        Personagem p = Jogo.getInstance().getCelula(l,c).getPersonagem();
                        // Se não for nulo, infecta
                        Random r = new Random();
                        int numero = r.nextInt(6);
                        if (p != null && (p instanceof SmartZombie) &&  (numero == 0 || numero == 1) && p.estaVivo()){
                            System.out.println("Policial atirando em: LINHA = ["+p.getCelula().getLinha()+"] COLUNA = ["+p.getCelula().getColuna()+"]");
                            p.diminuiEnergia(p.getEnergia());
                            zMortos++;
                        }
                    }
                }
            }
        }
    }
}
