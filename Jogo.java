
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.control.TextField;


public class Jogo extends Application {
    public static final int CELL_WIDTH = 25; // LARGURA DA CELULA
    public static final int CELL_HEIGHT = 25; // COMPRIMENTO DA CELULA
    public static final int qntdadeZumbi = 5;
    public static final int qntdadeMedico = 2;
    public static final int qntdadePolicial = 2;
    public static final int qtdPessoa = 5;
    public static final int NLIN = (int)(10); // numero de linhas de celula
    public static final int NCOL = (int)(10); // numero de colunas de celulas
    // public GridPane tab = new GridPane();
    int screenWidth = (int) Screen.getPrimary().getBounds().getWidth();
    int screenHeight = (int) Screen.getPrimary().getBounds().getHeight();



    public Text label = new Text("Rodadas: ");
    public Text lSalvos = new Text("Salvos: ");
    public Text ZumbisMortos = new Text();
    public  Integer rodadas;
    public Integer salvosC;

    public Integer ZombiesMortos;

    public static Jogo jogo = null;

    private Random random;
    private Map<String, Image> imagens;
    private List<Celula> celulas;
    private List<Personagem> personagens;

    private List<Celula> celulasCarregadas;
    private List<Personagem> personagensCarregados;
    private List<Integer> numeros;

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
        Image aux = new Image("file:Imagens\\bobao.gif");
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
    
        GridPane tab = new GridPane();
        primaryStage.setTitle("Simulador de Zumbi");
        tab.setAlignment(Pos.CENTER);
        tab.setHgap(10);
        tab.setVgap(10);
        tab.setPadding(new Insets(10, 10, 10, 10));

        // criando elementos da primeira pagina
        Button iniciar = new Button("NEW GAME");
        iniciar.setMaxSize(100,100);
        iniciar.setOnAction(
            e -> {
                input(primaryStage);
            }
        );

        Button historia = new Button("CONTEXT");
        historia.setMaxSize(100,100);
        
        // Scene nova = new Scene(tab);

        Button loadGame = new Button("CONTINUE");
        loadGame.setMaxSize(100, 100);
        loadGame.setOnAction(
            e -> {
                load(primaryStage);
            }
        );


        
        
        //criando layout da primeira pagina
        VBox um = new VBox(10);
        um.setAlignment(Pos.CENTER);
        um.setPadding(new Insets(25, 25, 25, 25));
        um.getChildren().add(iniciar);
        um.getChildren().add(historia);
        um.getChildren().add(loadGame);
        um.getChildren().add(tab);
        

        Scene scene = new Scene(um);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();

        
        // Monta a cena e exibe
      




    }

    private void newGame(Stage primaryStage){
        // Carrega imagens
        rodadas = 0;
        salvosC = 0;
        ZombiesMortos = 0;
        Policial.zMortos = 0;
        SafeZone.salvos = 0;
        loadImagens();

        // Configura a interface com o usuario


        GridPane tab = new GridPane();
        primaryStage.setTitle("Simulador de Zumbi");
        tab.setAlignment(Pos.CENTER);
        tab.setHgap(10);
        tab.setVgap(10);
        tab.setPadding(new Insets(25, 25, 25, 25));
        

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
                    personagens.add(new Pessoa(10, lin,col, false));
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
                    personagens.add(new SmartZombie(10, lin,col));
                    posOk = true;
                }
            }
        }

        // Cria Medicos Aleatorios
        for(int i=0;i<qntdadeMedico;i++){
            boolean posOk = false;
            while(!posOk){
                int lin = random.nextInt(NLIN);
                int col = random.nextInt(NCOL);
                if (this.getCelula(lin, col).getPersonagem() == null){
                    personagens.add(new Medico(10,lin,col, false));
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
                    personagens.add(new Policial(15,lin,col,false));
                    posOk = true;
                }
            }
        }

        
        Button back = new Button("  Back  ");
        back.setOnAction(
            e ->{
                start(primaryStage);
            }
        );
        label.setText("Rodadas: "+Integer.toString(rodadas));
        lSalvos.setText("Salvos: "+salvosC);
        ZumbisMortos.setText("Zumbis mortos: "+ZombiesMortos);
        Button avanca = new Button("NextStep");
        avanca.setOnAction(
            e-> {avancaSimulacao();
            label.setText("Rodadas: "+Integer.toString(rodadas));
            lSalvos.setText("Salvos: "+Integer.toString(SafeZone.getSalvos()));
            ZumbisMortos.setText("Zumbis mortos: "+ZombiesMortos);
            }
            );
        
        Button save = new Button("  Save  ");
        save.setOnAction(
            e -> {save();}
        );

        
        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().add(avanca);
        vb.getChildren().add(label);
        vb.getChildren().add(lSalvos);
        vb.getChildren().add(ZumbisMortos);
        vb.getChildren().add(save);
        vb.getChildren().add(back);

        HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(tab);
        hb.getChildren().add(vb);




        Scene scene = new Scene(hb);
        primaryStage.setScene(scene);
        // primaryStage.setFullScreen(true);
        primaryStage.show();
        primaryStage.setHeight(screenHeight);
        primaryStage.setWidth(screenWidth);
        primaryStage.setMaximized(true);
    }

    public void input(Stage primaryStage){

        GridPane tab = new GridPane();
        primaryStage.setTitle("Menu de Opcoes");
        tab.setAlignment(Pos.CENTER);
        tab.setHgap(10);
        tab.setVgap(10);
        tab.setPadding(new Insets(25, 25, 25, 25));


        TextField userTextField;
        Button start = new Button("  START    ");
        start.setMaxSize(100, 100);
        start.setOnAction(
            e ->{
                newGame(primaryStage);
            }
        );
        Button back = new Button("  BACK  ");
        back.setMaxSize(100, 100);
        back.setOnAction(
            e ->{
                start(primaryStage);
            }
        );

        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().add(start);
        vb.getChildren().add(back);



        HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(vb);
        hb.getChildren().add(tab);

        Scene scene = new Scene(hb);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setHeight(screenHeight);
        primaryStage.setWidth(screenWidth);
        primaryStage.setMaximized(true);
        

    }

    private void save(){
            try(ObjectOutputStream pSalvos = new ObjectOutputStream(new FileOutputStream("personagens.txt"))){
                pSalvos.writeObject(personagens);
                pSalvos.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }

            try(ObjectOutputStream cSalvas = new ObjectOutputStream(new FileOutputStream("celulas.txt"))){
                cSalvas.writeObject(celulas);
                cSalvas.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
            try(ObjectOutputStream data = new ObjectOutputStream(new FileOutputStream("data.txt"))){
                data.writeObject(numeros);
                data.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
    }


    private void load(Stage primaryStage){
        try(ObjectInputStream cSalvas = new ObjectInputStream(new FileInputStream("celulas.txt"))){
            this.celulasCarregadas = (List)cSalvas.readObject();
        }
        catch(IOException  | ClassNotFoundException e){
            e.printStackTrace();
        }

        try(ObjectInputStream pSalvas = new ObjectInputStream(new FileInputStream("personagens.txt"))){
            this.personagensCarregados = (List)pSalvas.readObject();
        }
        catch(IOException  | ClassNotFoundException e){
            e.printStackTrace();
        }
        try(ObjectInputStream data = new ObjectInputStream(new FileInputStream("data.txt"))){
            numeros = (List)data.readObject();
            data.close();
        }
        catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }

        // Carrega imagens
        loadImagens();

        // Configura a interface com o usuario

        rodadas = numeros.get(0);
        SafeZone.salvos = numeros.get(1);
        Policial.zMortos = numeros.get(2);
        salvosC = SafeZone.salvos;
        ZombiesMortos = Policial.zMortos;
        

        GridPane tab = new GridPane();
        primaryStage.setTitle("Simulador de Zumbi");
        tab.setAlignment(Pos.CENTER);
        tab.setHgap(10);
        tab.setVgap(10);
        tab.setPadding(new Insets(25, 25, 25, 25));
        
        // Monta o "tabuleiro"
        celulas = new ArrayList<>(NLIN*NCOL);
        for (int lin = 0; lin < NLIN; lin++) {
            for (int col = 0; col < NCOL; col++) {
                Celula cel = new Celula(lin,col);
                celulas.add(cel);
                tab.add(cel, col, lin);
            }
        }
        personagens = new ArrayList<>(NLIN*NCOL);
        for (Personagem p : personagensCarregados) {
            if(p instanceof SafeZone){
                personagens.add(new SafeZone(p.getCelula().getLinha(), p.getCelula().getColuna()));
            }
            else if (p instanceof SmartZombie){
                SmartZombie p1 = new SmartZombie(p.getEnergia(), p.getCelula().getLinha(), p.getCelula().getColuna());
                p1.verificaEstado();
                personagens.add(p1);
            }
            else if(p instanceof Policial){
                Policial p1 = new Policial(p.getEnergia(), p.getCelula().getLinha(), p.getCelula().getColuna(), p.infectado());
                personagens.add(p1);
            }
            else if(p instanceof Medico){
                Medico p1 = new Medico(p.getEnergia(), p.getCelula().getLinha(), p.getCelula().getColuna(), p.infectado());
                personagens.add(p1);
            }
            else{
                Pessoa p1 = new Pessoa(p.getEnergia(), p.getCelula().getLinha(), p.getCelula().getColuna(), p.infectado());
                personagens.add(p1);
            }
        }

        Button back = new Button("  Back  ");
        back.setOnAction(
            e ->{
                start(primaryStage);
            }
        );

        label.setText("Rodadas: "+Integer.toString(rodadas));
        lSalvos.setText("Salvos: "+Integer.toString(salvosC));
        ZumbisMortos.setText("Zumbis mortos: "+ZombiesMortos);
        Button avanca = new Button("NextStep");
        avanca.setOnAction(
            e-> {avancaSimulacao();
            label.setText("Rodadas: "+Integer.toString(rodadas));
            lSalvos.setText("Salvos: "+Integer.toString(salvosC));
            ZumbisMortos.setText("Zumbis mortos: "+Integer.toString(ZombiesMortos));
            }
            );
        
        Button save = new Button("  Save  ");
        save.setOnAction(
            e -> {save();}
        );

        
        VBox vb = new VBox(10);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().add(avanca);
        vb.getChildren().add(label);
        vb.getChildren().add(lSalvos);
        vb.getChildren().add(ZumbisMortos);
        vb.getChildren().add(save);
        vb.getChildren().add(back);

        HBox hb = new HBox(10);
        hb.setAlignment(Pos.CENTER);
        hb.getChildren().add(tab);
        hb.getChildren().add(vb);


        Scene scene = new Scene(hb);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setHeight(screenHeight);
        primaryStage.setWidth(screenWidth);
        primaryStage.setMaximized(true);


        
    } // fim do load




    public ObservableList<Personagem> getPersonagens(){
        return FXCollections.observableList(personagens);
    }

    public void avancaSimulacao(){
        // Avança um passo em todos os personagens
            rodadas++;
            salvosC = SafeZone.getSalvos();
            ZombiesMortos = Policial.getzMortos();
            numeros = Arrays.asList(rodadas, salvosC, ZombiesMortos);
            System.out.println(numeros);
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
                    .count();

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
