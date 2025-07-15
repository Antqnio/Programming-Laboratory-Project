/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Antonio
 * Fornisce la logica comune a ResultsController e FavoritesController (ovvero il come creare e inizializzare le tabelle,
 * e crearle solo se, effettivamente, ci sono dei record da mostrare. Comprende anche le funzioni
 * per visualizzare la descrizione dei record delle tabelle).
 */
//fargli estendere AbstractController
public abstract class AbstractTableController extends AbstractController {
    //protected SecondaryController secondaryController;
    protected JsonArray games;
    protected JsonArray characters;
    protected JsonArray bosses;
    protected JsonArray items;
    protected JsonArray places;
    protected JsonArray dungeons;
    protected JsonArray monsters;
    
    /**
     * 
     * @param controllerName
     * Per inizializzare lang e i vari elementi FXML, mi serve il costruttore di AbstractController, a cui serve
     * la stringa corrispondente al nome del controller tutto minuscolo. Glielo passo così.
     */
    protected AbstractTableController(String controllerName) {
        super(controllerName);
    }

    //Sono tutti protected, visto che la classe viene ereditata.
    @FXML
    protected VBox myVBox;
    
    @FXML
    protected Button getBackButton;
    @FXML
    protected MenuItem showGameDescriptionMI;
    @FXML
    protected MenuItem showCharacterDescriptionMI;
    @FXML
    protected MenuItem showItemDescriptionMI;
    @FXML
    protected MenuItem showDungeonDescriptionMI;
    @FXML
    protected MenuItem showBossDescriptionMI;
    @FXML
    protected MenuItem showMonsterDescriptionMI;
    @FXML
    protected MenuItem showPlaceDescriptionMI;
    

    
    
    
    @FXML
    protected Label gameLabel;
    @FXML
    protected TableView<Game> gameTable = new TableView<>();
    protected ObservableList<Game> gameOl;
    
    
    @FXML
    protected Label bossLabel;
    @FXML
    protected TableView<Boss> bossTable = new TableView<>();
    protected ObservableList<Boss> bossOl;
    
    @FXML
    protected Label characterLabel;
    @FXML
    protected TableView<Character> characterTable = new TableView<>();
    protected ObservableList<Character> characterOl;
    
    
    @FXML
    protected Label dungeonLabel;
    @FXML
    protected TableView<Dungeon> dungeonTable = new TableView<>();
    protected ObservableList<Dungeon> dungeonOl;
    
    @FXML
    protected Label itemLabel;
    @FXML
    protected TableView<Item> itemTable = new TableView<>();
    protected ObservableList<Item> itemOl;
    
    
    @FXML
    protected Label monsterLabel;
    @FXML
    protected TableView<Monster> monsterTable = new TableView<>();
    protected ObservableList<Monster> monsterOl;
    
    
    @FXML
    protected Label placeLabel;
    @FXML
    protected TableView<Place> placeTable = new TableView<>();
    protected ObservableList<Place> placeOl;
    
    

    /**
     * Questa crea le tabelle dungeonTable, itemTable, bossTable, placeTable, monsterTable, che hanno
     * gli stessi attributi, ovvero solo l'attributo name.
     * Crea l'atributo name anche per le tabelle game e characters
     * @param <T>
     * @param tableView
     * @param ol
     * @return 
     */
    private<T> ObservableList<T> createGeneric(TableView<T> tableView, ObservableList<T> ol) {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        
        //La descrizione sarà mostrata solo su richiesta dell'utente, tramite MenuItem.
        //TableColumn descriptionCol = new TableColumn("Description");
        //descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        //tableView.getColumns().addAll(nameCol, descriptionCol);
        tableView.getColumns().addAll(nameCol);

        ol = FXCollections.observableArrayList();

        tableView.setItems(ol);
        
        return ol; //in Java non esiste il passaggio per riferimento, quindi devo restituire la ol che ho creato.
    }
    
    /**
     * Tutte queste funzioni creano la tabelle e non le inizializzano.
     * Questa crea la tabella gameTable, che ha attributi diversi dalle altre
     * Chiama la createGeneric per creare l'attributo name.
     */
    private void createGamesTable() {
        createGeneric(gameTable, gameOl); //Non uso il ritorno di createGeneric(). Inizializzo dopo gameOl.

        TableColumn releasedDateCol = new TableColumn("ReleasedDate");
        releasedDateCol.setCellValueFactory(new PropertyValueFactory<>("releaseDate"));

        gameTable.getColumns().addAll(releasedDateCol);

        gameOl = FXCollections.observableArrayList();

        gameTable.setItems(gameOl);
       
    }
    
    
    /**
     * Si limita a chiamare createGeneric, vista che contiene solo l'attributo nome.
     */
    private void createBossesTable() {
        bossOl = createGeneric(bossTable, bossOl);
    }
    
    /**
     * Si limita a chiamare createGeneric, vista che contiene solo l'attributo nome.
     */
    private void createDungeonsTable() {
        dungeonOl = createGeneric(dungeonTable, dungeonOl);
    }
    
    /**
     * Si limita a chiamare createGeneric, vista che contiene solo l'attributo nome.
     */
    private void createItemsTable() {
        itemOl = createGeneric(itemTable, itemOl);
    }
    
    /**
     * Si limita a chiamare createGeneric, vista che contiene solo l'attributo nome.
     */
    private void createMonstersTable() {
        monsterOl = createGeneric(monsterTable, monsterOl);
    }
    
    /**
     * Si limita a chiamare createGeneric, vista che contiene solo l'attributo nome.
     */
    private void createPlacesTable() {
        placeOl = createGeneric(placeTable, placeOl);
    }
    
    /**
     * Come la tabella games, questa ha una struttura a sé.
     * Chiama la createGeneric per creare l'attributo name.
     */
    private void createCharactersTable() {
        createGeneric(characterTable, characterOl); //Non uso il ritorno di createGeneric(). Inizializzo dopo gameOl.
        TableColumn genderCol = new TableColumn("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        
        TableColumn raceCol = new TableColumn("Race");
        raceCol.setCellValueFactory(new PropertyValueFactory<>("race"));

        characterTable.getColumns().addAll(genderCol, raceCol);

        characterOl = FXCollections.observableArrayList();

        characterTable.setItems(characterOl);
    }
    
    /**
     * Se la tabella non dovesse contenere elementi, la cancella (di default è creata una tabella vuota dai file
     * results.fxml e favorites.fxml).
     * @param <T>
     * @param table
     * @param label 
     */
    private<T> void maskTable(TableView<T> table, Label label) {
        myVBox.getChildren().remove(table);
        myVBox.getChildren().remove(label);
    }
    /**
     * ChatGPT: mi serviva perché Hibernate dà le date comprendendo l'ora, i minuti e i secondi,
     * mentre fare manualmente la query no.
     * @param date
     * @return 
     */
    
    public boolean isValidMySQLDateFormat(String date) {
        // Regex per il formato yyyy-MM-dd
        String regex = "^\\d{4}-\\d{2}-\\d{2}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(date).matches();
    }
    
    /**
     * Se la tabella è stat creata, la inizializzo.
     * Questa funzione è usata sia quando si passa alla schermata favorites.fxml che quando si passa alla
     * results.fxml.
     * I dati ricevuti nel JsonArray games saranno inseriti da questa funzione nella tabella gameTable.
     * Il problema è che:
     * - Quando passo a results.fxml, i JSON sono recuperati dal server tramite query fatte con JPA, il quale
     *   include le ore, i minuti e i secondi nella released_date.
     * - Quando passo a favorites.fxml, i JSON sono inviati dal server con query fatte senza JPA, che escludono
     *   ore, minuti e secondi.
     * Pertanto, gestisco la cosa.
     * Si noti che, nonostante l'attributo "description" non appaia nelle tabelle, negli elementi appartenti
     * alle ObservableList<> è presente, permettendo che essa venga mostrata su domanda con la showDescription().
     */
    private void initializeGamesTable() {
        Game game;
        JsonObject jo;
        DateTimeFormatter dateFormatFromHibernate = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");//per catturare la data restituia dalla getAsString().
        DateTimeFormatter dateFormatFromMySQL = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateString;
        ZonedDateTime zonedDateTime; //per gestire la parte del tempo restituita dalla data.
        LocalDate localDate;
        try {    
            for (int i = 0; i < games.size(); ++i) {
                jo = games.get(i).getAsJsonObject();
                dateString = jo.get("releaseDate").getAsString();
                if (isValidMySQLDateFormat(dateString)) {
                    localDate = LocalDate.parse(dateString, dateFormatFromMySQL);
                }
                else {
                    zonedDateTime = ZonedDateTime.parse(dateString, dateFormatFromHibernate); //catturo l'intera stringa come data, compresa la parte del tempo.
                    localDate = zonedDateTime.toLocalDate(); //salvo solo la parte senza il tempo.
                }    
                game = new Game(jo.get("name").getAsString(), jo.get("description").getAsString(), localDate);
                gameOl.add(game);
            }
        }
        catch (DateTimeParseException e) {
            logger.error(e.getMessage());
        }
    }
    /**
     * Come la tabella gameTable, la tabella characterTable ha una struttura particolare, dunque la inizializzo
     * con una funzione apposita.
     */
    private void initializeCharactersTable() {
        Character character;
        JsonObject jo;
        for (int i = 0; i < characters.size(); ++i) {
            jo = characters.get(i).getAsJsonObject();
            character = new Character(jo.get("name").getAsString(), jo.get("description").getAsString(),
                    !jo.get("gender").isJsonNull() ? jo.get("gender").getAsString() : "No gender",
                    !jo.get("race").isJsonNull() ? jo.get("race").getAsString() : "No race");
            characterOl.add(character);
        }
    }
    
    /**
     * Usata per inizializzare le tabelle con struttura comune (itemTable, dungeonTable, monsterTable, bossTable,
     * placeTable). Si usa Class<T> per superare il type erasure dovuto ai generics.
     * Si noti che, nonostante l'attributo "description" non appaia nelle tabelle, negli elementi appartenti
     * alle ObservableList<> è presente, permettendo che essa venga mostrata su domanda con la showDescription().
     * @param <T>
     * @param clazz
     * @param ol
     * @param ja 
     */
    private <T> void initializeGeneric(Class<T> clazz, ObservableList<T> ol, JsonArray ja) {
        T t;
        JsonObject jo;
        try {    
            for (int i = 0; i < ja.size(); ++i) {
                jo = ja.get(i).getAsJsonObject();
                //uso la riflessione per oltrepassare la type erasure dei generics.
                t = clazz.getDeclaredConstructor(String.class, String.class)
                         .newInstance(jo.get("name").getAsString(), jo.get("description").getAsString());
                ol.add(t);
            }
        }
        catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * La bossTable ha solo l'attributo name, quindi uso la initializeGeneric().
     */
    private void initializeBossesTable() {
        initializeGeneric(Boss.class, bossOl, bosses);
    }
    
    /**
     * La placeTable ha solo l'attributo name, quindi uso la initializeGeneric().
     */
    private void initializePlacesTable() {
        initializeGeneric(Place.class, placeOl, places);
    }
    
    /**
     * La itemTable ha solo l'attributo name, quindi uso la initializeGeneric().
     */
    private void initializeItemsTable() {
        initializeGeneric(Item.class, itemOl, items);
    }
    
    /**
     * La dungeonTable ha solo l'attributo name, quindi uso la initializeGeneric().
     */
    private void initializeDungeonsTable() {
        initializeGeneric(Dungeon.class, dungeonOl, dungeons);
    }
    
    /**
     * La monsterTable ha solo l'attributo name, quindi uso la initializeGeneric().
     */
    private void initializeMonstersTable() {
        initializeGeneric(Monster.class, monsterOl, monsters);
    }
    
    /**
     * Funziona chiamata da un oggetto SecondaryController quando deve passare a un oggetto
     * ResultsController o FavoritesController.
     * Si usa questa e non la initialize() perché la initialize() viene chiamata dalla
     * loader.getController() dell'oggetto FXMLoader loader, e tale funzione, per costruire il
     * controller, chiama il costruttore di default, impedendo il passaggio dei parametri.
     * Pertanto, chiamo manualmente questa funzione dopo aver inizializzato i JsonArray e il campo
     * credenziali (ereditato da AbstractController).
     */
    protected void initializeTables() {
        if (games.size() > 0) {
            createGamesTable();
            initializeGamesTable();
        }
        else {
            maskTable(gameTable, gameLabel); //cancello la tabella altrimenti.
        }
        
        
        if (bosses.size() > 0) {
            createBossesTable();
            initializeBossesTable();
        }
        else {
            maskTable(bossTable, bossLabel); //cancello la tabella altrimenti.
        }
        
        
        if (characters.size() > 0) {
            createCharactersTable();
            initializeCharactersTable();
        }
        else {
            maskTable(characterTable, characterLabel); //cancello la tabella altrimenti.
        }
        
        
        if (dungeons.size() > 0) {
            createDungeonsTable();
            initializeDungeonsTable();
        }
        else {
            maskTable(dungeonTable, dungeonLabel); //cancello la tabella altrimenti.
        }
        
        
        if (items.size() > 0) {
            createItemsTable();
            initializeItemsTable();
        }
        else {
            maskTable(itemTable, itemLabel); //cancello la tabella altrimenti.
        }
        
        
        if (monsters.size() > 0) {
            createMonstersTable();
            initializeMonstersTable();
        }
        else {
            maskTable(monsterTable, monsterLabel); //cancello la tabella altrimenti.
        }
        
        
        if (places.size() > 0) {
            createPlacesTable();
            initializePlacesTable();
        }
        else {
            maskTable(placeTable, placeLabel); //cancello la tabella altrimenti.
        }
    }
    
    
    /**
     * Chiamata da FavoritesController e ResultsController per tornare al SecondaryController,
     * così da permettere all'utente di o fare logout, o di andare ai preferiti o di fare una
     * nuova ricerca.
     * @param event
     * @throws IOException 
     */
    @FXML
    protected void getBack(ActionEvent event) throws IOException {
        try {    
            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
            Parent root = loader.load();
            SecondaryController secondaryController = loader.getController();
            secondaryController.credenziali = credenziali;
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * Mostra la descrizione di un record di una tabella.
     * È uguale per tutti i record
     * @param description 
     */
    
    private void showDescription(String description) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("description.fxml"));
            Parent root = loader.load();
            DescriptionController descriptionController = loader.getController();
            descriptionController.displayDescription(description);
            Stage stage = new Stage(); //Con questo creo una nuova finestra.
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * Vari metodi fanno delle POST perché devono inviare delle String aventi spazi, che non si possono
     * inviare normalmente (senza caratteri speciali) nelle richieste GET.
     * Rende più semplice l'utilizzo della String inviata al server, perché non si sono inseriti caratteri speciali.
     * @param con
     * @param elementName
     * @throws IOException 
     */
      protected void sendSearchClassAsPOST(HttpURLConnection con, String elementName) throws IOException {
        con.setRequestMethod("POST");
        //mando la query come JSON
        Gson gson = new Gson();
        //invio elementName nel body perché contiene degli spazi. Lo invio come SearchClass
        SearchClass c = new SearchClass(elementName);
        String queryJSON = gson.toJson(c);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(queryJSON.length()));
        //per abilitare la scrittura nella connessione
        con.setDoOutput(true);

        //scrivo i dati nel corpo della richiesta
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = queryJSON.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }
    
      
    /**
     * La descrizione è uguale, dal punto di vista strutturale, per ogni record,
     * quindi associo a ogni tabella un MenuItem che cattura l'elemento della tabella.
     * La descrizione è presente negli elementi della ObservableList associata a ogni tabella.
     */
    @FXML
    protected void showGameDescription() {
        showDescription(gameTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showCharacterDescription() {
        showDescription(characterTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showItemDescription() {
        showDescription(itemTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showMonsterDescription() {
        showDescription(monsterTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showPlaceDescription() {
        showDescription(placeTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showBossDescription() {
        showDescription(bossTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    @FXML
    protected void showDungeonDescription() {
        showDescription(dungeonTable.getSelectionModel().getSelectedItem().getDescription());
    }
    
    
    
}
