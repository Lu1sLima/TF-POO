
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.Random;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList; //mainsterPLAY
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Jogo extends Application {
    public static final int CELL_WIDTH = 35; // LARGURA DA CELULA
    public static final int CELL_HEIGHT = 35; // COMPRIMENTO DA CELULA
    public static final int qntdadeZumbi = 2;
    public static final int qntdadeMedico = 2;
    public static final int qntdadePolicial = 2;
    public static final int qtdPessoa = 2;
    public static final int NLIN = (int)(10); // numero de linhas de celula
    public static final int NCOL = (int)(10); // numero de colunas de celulas



    public Text label = new Text("Rodadas: ");
    public Text lSalvos = new Text("Salvos: ");
    public Text ZumbisMortos = new Text();
    public  int rodadas;

    public static Jogo jogo = null;

    private Random random;
    private Map<String, Image> imagens;
    private List<Celula> celulas;
    private List<Personagem> personagens;

    public static Jogo getInstance(){
        return jogo;
    }

    public Jogo(){
        jogo = this;
        random = new Random();
        rodadas = 0;
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Retorna um número aleatorio a partir do gerador unico
    public int aleatorio(int limite){
        return random.nextInt(limite);
    }

    // Retorna a celula de uma certa linha,coluna
    public Celula getCelula(int nLin,int nCol){
        int pos = (nLin*NCOL)+nCol;
        return celulas.get(pos);
    }

    private void loadImagens() {
        imagens = new HashMap<>();

        // Armazena as imagens dos personagens
        Image aux = new Image("file:Imagens\\bobaoEdited.jpg");
        imagens.put("Normal", aux);
        aux = new Image("file:Imagens\\doenteEdited.jpg");
        imagens.put("Infectado", aux);
        aux = new Image("file:Imagens\\Ghoul.gif");
        imagens.put("Zumbi", aux);
        aux = new Image("file:Imagens\\ghoulDeadS.png");
        imagens.put("DeadZumbi", aux);
        aux = new Image("file:Imagens\\deadEdited.jpg");
        imagens.put("Morto", aux);
        aux = new Image("file:Imagens\\back.jpg");
        imagens.put("Vazio", aux);
        aux = new Image("file:Imagens\\medicEdited.jpg");
        imagens.put("Medico", aux);
        aux = new Image("file:Imagens\\policialEdited.jpg");
        imagens.put("Policial", aux);
        aux = new Image("file:Imagens\\img5.jpg");
        imagens.put("Safe", aux);



        // Armazena a imagem da celula ula
        imagens.put("Null", null);
    }

    public Image getImage(String id){
        return imagens.get(id);
    }

    @Override
    public void start(Stage primaryStage) {
        // Carrega imagens
        loadImagens();

        // Configura a interface com o usuario



        primaryStage.setTitle("Simulador de Zumbi");
        GridPane tab = new GridPane();
        tab.setAlignment(Pos.CENTER);
        tab.setHgap(10);
        tab.setVgap(10);
        tab.setPadding(new Insets(25, 25, 25, 25));

        primaryStage.setTitle("Teste");
        GridPane inicio = new GridPane();
        inicio.setAlignment(Pos.CENTER);
        inicio.setHgap(10);
        inicio.setVgap(10);
        inicio.setPadding(new Insets(25, 25, 25, 25));

        

        // Monta o "tabuleiro"
        celulas = new ArrayList<>(NLIN*NCOL);
        for (int lin = 0; lin < NLIN; lin++) {
            for (int col = 0; col < NCOL; col++) {
                Celula cel = new Celula(lin,col);
                celulas.add(cel);
                tab.add(cel, col, lin);
            }
        }

        // Cria a lista de personagens
        personagens = new ArrayList<>(NLIN*NCOL);
        


        //Cria safezone
        personagens.add(new SafeZone(0,0));
        personagens.add(new SafeZone(0,1));
        personagens.add(new SafeZone(1,0));
        personagens.add(new SafeZone(1,1));



        // Cria boboes aleatorios
        for(int i=0;i<qtdPessoa;i++){
            // Lembrte: quando um personagem é criado ele se vincula
            // automaticamente na célula indicada nos parametros
            // linha e coluna (ver o construtor de Personagem)
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Pessoa(lin,col));
                    posOk = true;
                }
            }
        }

            for(int i=0;i<qntdadeZumbi;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new SmartZombie(lin,col));
                    posOk = true;
                }
            }
        }

        // Cria Zumbis aleatórios
        // for(int i=0;i<qntdadeZumbi;i++){
        //     boolean posOk = false;
        //     while(!posOk){
        //         int lin = random.nextInt(NLIN);
        //         int col = random.nextInt(NCOL);
        //         if (this.getCelula(lin, col).getPersonagem() == null){
        //             personagens.add(new Zumbi(lin,col));
        //             posOk = true;
        //         }
        //     }
        // }

        // Cria Medicos Aleatorios
        for(int i=0;i<qntdadeMedico;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Medico(lin,col));
                    posOk = true;
                }
            }
        }

        // Cria Policiais Aleatorios
        for(int i=0;i<qntdadePolicial;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Policial(lin,col));
                    posOk = true;
                }
            }
        }

        // criando elementos da primeira pagina
        Button iniciar = new Button("START");
        iniciar.setMaxSize(150,150);

        Button back = new Button("Back");
        back.setMaxSize(150,150);

        Button historia = new Button("CONTEXT");
        historia.setMaxSize(150,150);

        

        
        
        //criando layout da primeira pagina
        VBox um = new VBox(10);
        um.setAlignment(Pos.CENTER);
        um.getChildren().add(iniciar);
        um.getChildren().add(historia);

        Text texto = new Text("aaa");
        VBox dois = new VBox(10);
        dois.setAlignment(Pos.CENTER);
        dois.getChildren().add(texto);
        dois.getChildren().add(back);

        
        

        
        
        // criando first scene
        Scene primeira = new Scene(um,500,500);
        Scene scene2 = new Scene(dois,500,500);




        // Define o botao que avança a simulação
        

        Button avanca = new Button("NextStep");
        avanca.setOnAction(
            e-> {avancaSimulacao();
            label.setText("Rodadas: "+Integer.toString(rodadas));
            lSalvos.setText("Salvos: "+Integer.toString(SafeZone.getSalvos()));
            ZumbisMortos.setText("Zumbis mortos: "+Policial.getzMortos());
            }
            );



            VBox vb = new VBox(10);
            vb.setAlignment(Pos.CENTER);
            vb.getChildren().add(avanca);
            vb.getChildren().add(label);
            vb.getChildren().add(lSalvos);
            vb.getChildren().add(ZumbisMortos);
            vb.getChildren().add(back);

    


        // Define outros botoes
          HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER);
        hb.setPadding(new Insets(25, 25, 25, 25));
        hb.getChildren().add(tab);
        hb.getChildren().add(vb);
        // hb.getChildren().add(label);
        // hb.getChildren().add(lSalvos);


        Scene segunda = new Scene(hb);
       
        iniciar.setOnAction(e -> primaryStage.setScene(segunda));
        historia.setOnAction(e -> primaryStage.setScene(scene2));
        back.setOnAction(e-> primaryStage.setScene(primeira));
        primaryStage.setScene(primeira);
        primaryStage.show();
        
        // Monta a cena e exibe
      




    }


    public ObservableList<Personagem> getPersonagens(){
        return FXCollections.observableList(personagens);
    }

    public void avancaSimulacao(){
        // Avança um passo em todos os personagens
            rodadas++;
            getPersonagens().forEach(p->{
            p.atualizaPosicao();
            p.verificaEstado();
            p.influenciaVizinhos();
        });
        // Verifica se o jogo acabou
        long vivos = getPersonagens()
                    .stream()
                    .filter(p->(!(p instanceof SmartZombie) && !(p instanceof SafeZone)))
                    .filter(p->p.estaVivo())
                    .count() ;

          long ZombieVivos = getPersonagens()
                    .stream()
                    .filter(p->(p instanceof SmartZombie))
                    .filter(p->p.estaVivo())
                    .count();
        if (vivos == 0){
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Fim de Jogo");
            msgBox.setContentText("Os humanos em campo foram mortos!\n"+SafeZone.getSalvos()+" Humanos foram salvos.\n"+(qntdadeZumbi - Policial.getzMortos())+ " Zumbis ainda ficaram a solta.\n"+
            Policial.getzMortos()+" Zumbis foram mortos.");
            msgBox.showAndWait();
            System.exit(0);
        }
        else if (ZombieVivos == 0){
            Alert msgBox = new Alert(AlertType.INFORMATION);
            msgBox.setHeaderText("Fim de Jogo ");
            msgBox.setContentText("Todos os zombies morreram!\n"+(vivos+SafeZone.getSalvos())+" humanos continuaram vivos.\n"+Policial.getzMortos()+" Zumbis foram mortos.");
            msgBox.showAndWait();
            System.exit(0);
        }
    }
}
