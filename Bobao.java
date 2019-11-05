public class Bobao extends Personagem {
    public Bobao(int linInicial, int colInicial) {
        super(30, "Normal", linInicial, colInicial); // int energiaInicial, String imagemInicial,int linInicial,int colInicial
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
        // Não se mexe
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