/**
 * SafeZone
 */
public class SafeZone extends Personagem{


    static int salvos = 0;
    

    public SafeZone(){
       super(); 
    }
    public SafeZone(int linInicial,int colInicial){
        super(0,"Safe",linInicial,colInicial);
        
    }

    public static int getSalvos(){
        return salvos;
    }






    @Override
    public void atualizaPosicao() {
        return;
    }

    @Override
    public void influenciaVizinhos() {
        
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
                        if (p != null && !(p instanceof SmartZombie) && !(p instanceof SafeZone)){
                            Jogo.getInstance().getPersonagens().remove(p);
                            p.getCelula().setPersonagem(null);
                            salvos++;
                        }
                    }
                }
            }
        }







        
    }

    @Override
    public void verificaEstado() {
      return ;
    }
    
}