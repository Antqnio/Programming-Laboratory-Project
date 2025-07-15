package it.unipi.applicazione;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


/*
 * 
 * @author Antonio
 * Permette all'utente, una volta loggato, di effettuare ricerche, di cambiare lingua e di
 * 
 */
public class SecondaryController extends AbstractController {
    private static final String genericSearchURL = "http://localhost:8080/ZeldaWiki/search";
    private static final String genericSearchFavoritesURL = genericSearchURL + "Favorite";
    public PrimaryController primaryController;
    private LinguaggioSecondary lang;
    //Messi a livello friendly di visibilità per i test.
    @FXML
    Button logoutButton;
    @FXML
    Button favoritesButton;
    @FXML
    TextField searchField;
    
    
    /**
     * Deve esser public per essere chiamato dalla loader.getController() dell'oggetto FXMLoader loader.
     */
    public SecondaryController() {
        super("secondary");
    }
    
    /**
     * Messa a visibilità friendly per il test.
     * Per fare l'inizializzazione della lingua.
     */
    @FXML
    void initialize() {
        XStream xstream = creaXStream(LinguaggioSecondary.class);
        lang = (LinguaggioSecondary) xstream.fromXML(getClass().getResource("languages/secondary_it.xml"));
        generalInitialization();
    }
    
    /**
     * Per far cambiare la lingua mostrata all'utente da parte del SecondaryController.
     */
    @Override
    @FXML
    protected void switchLanguage() {
        XStream xstream = creaXStream(LinguaggioSecondary.class);
        if (linguaCorrente == LinguaEnum.italiano) {
            lang = (LinguaggioSecondary) xstream.fromXML(getClass().getResource("languages/secondary_eng.xml"));
            linguaCorrente = LinguaEnum.inglese;
        }
        else {
            lang = (LinguaggioSecondary) xstream.fromXML(getClass().getResource("languages/secondary_it.xml"));
            linguaCorrente = LinguaEnum.italiano;
        }
        initializeLanguageFields();
    }
    
    /**
     * Per inizializzare i vari campi con la lingua corrente.
     */
    @Override
    protected void initializeLanguageFields() {
        logoutButton.setText(lang.logoutButton);
        favoritesButton.setText(lang.favoritesButton);
        searchField.setPromptText(lang.searchField);
        switchLanguageButton.setText(lang.switchLanguageButton);
    }
    
    /**
     * Fa effettuare il logout all'utente, tornado a primary.fxml
     * @param event
     * @throws IOException 
     */
    @FXML
    private void logout(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    

    /**
     * Forma la richiesta POST e ritorna la risposta.
     * @param con (connessione tramite la quale si fa la richiesta HTTP)
     * @param queryJSON (oggetto scritto in formato JSON)
     * @return
     * @throws IOException 
     */
    
    private StringBuffer getPOSTRespone(HttpURLConnection con, String queryJSON) throws IOException{
        //metto gli header della richiesta HTTP
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(queryJSON.length()));

        //per abilitare la scrittura nella connessione
        con.setDoOutput(true);

        //scrivo i dati nel corpo della richiesta
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = queryJSON.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuffer content = getResponse(con); //risposta HTTP
        return content;
    }
    
    /**
     * Usata per cercare nelle varie tabelle una corrispondenza col testo scritto dall'Utente.
     * I risultati saranno passati alla schermata results.fxml
     * @param <T>
     * @param clazz
     * @param searchUrl: se genericSearchURL, cerca nelle tabelle generiche; se genericSearchFavoritesURL, cerca nei preferiti dell'utente.
     * searchUrl deve essere una stringa concatenata nella forma:
     * genericSearchURL (membro final static di questa classe)
     * +
     * nome di tabella con iniziale maiuscola (Game, Boss, Dungeon, Place, Item, Monster, Character) +
     * 
     * @return
     * @throws IOException 
     */
    
    private<T> JsonArray searchGeneric(String searchUrl) throws IOException {
        URL url = new URL(searchUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        //mando la query come JSON
        Gson gson = new Gson();
        SearchClass c = new SearchClass(searchField.getText());
        String queryJSON = gson.toJson(c);
        //risultato della richiesta. Uso la getPOSTResponse per maggiore leggibilità
        StringBuffer content = getPOSTRespone(con, queryJSON);
        
        
        logger.info(content);
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonArray elements = json.getAsJsonArray();
        return elements;
    }
    
    /**
     * Serve per passare la String contente l'URL a cui la searchGeneric farà la richiesta HTTP.
     * @param <T>
     * @param clazz
     * @return
     * @throws IOException 
     */
    //genericSearchURL == "http://localhost:8080/ZeldaWiki/search" 
    private<T> JsonArray search(Class<T> clazz) throws IOException {
        return searchGeneric(genericSearchURL + clazz.getSimpleName());
    }
    
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    private JsonArray searchGame() throws IOException{
        return search(Game.class);
    }
    
    
    /**
     * Ritorna i risultati della ricerca come JsonArray.
     * @return
     * @throws IOException 
     */
    private JsonArray searchCharacter() throws IOException{
        return search(Character.class);
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    private JsonArray searchBoss() throws IOException{
        return search(Boss.class);
    }
    
    /**
     * 
     * @return
     * @throws IOException 
     */
    private JsonArray searchItem() throws IOException{
        return search(Item.class);
    }
    
    /**
     * Ritorna i risultati della ricerca come JsonArray.
     * @return
     * @throws IOException 
     */
    private JsonArray searchPlace() throws IOException{
        return search(Place.class);
    }
    
    /**
     * Ritorna i risultati della ricerca come JsonArray.
     * @return
     * @throws IOException 
     */
    private JsonArray searchDungeon() throws IOException{
        return search(Dungeon.class);
    }
    
    /**
     * Ritorna i risultati della ricerca come JsonArray.
     * @return
     * @throws IOException 
     */
    private JsonArray searchMonster() throws IOException{
        return search(Monster.class);
    }
    
    
    /**
     * Manda richieste al server per cercare recordi corrispondenti alla keyword inserita dall'utente
     * nel campo di ricerca. Se ha successo, si passa a results.fxml
     * Usa un task per non bloccare l'interfaccia grafica, la quale sarà modificata appena possibile
     * da runLater.
     * @param event: usato per passare a results.fxml 
     */
    @FXML
    private void openResultsTab(ActionEvent event) {
        Task t;
        t = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    JsonArray games = searchGame();
                    JsonArray characters = searchCharacter();
                    JsonArray bosses = searchBoss();
                    JsonArray items = searchItem();
                    JsonArray places = searchPlace();
                    JsonArray dungeons = searchDungeon();
                    JsonArray monsters = searchMonster();
                    
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("results.fxml"));
                                Parent root = loader.load();
                                //Inizializzo il controller
                                ResultsController resultsController = loader.getController();
                                //resultsController.secondaryController = thisSecondaryController;
                                resultsController.games = games;
                                resultsController.characters = characters;
                                resultsController.bosses = bosses;
                                resultsController.items = items;
                                resultsController.places = places;
                                resultsController.dungeons = dungeons;
                                resultsController.monsters = monsters;
                                //resultsController.credenziali = thisSecondaryController.credenziali;
                                resultsController.credenziali = credenziali;
                                //funzione usata perché la load chiama la initialize, e FXMLoader usa il costruttore di defualt (non si possono usare costruttori parametrici).
                                //Mi serviva un modo per passare i parametri e inizializzare le tabelle.
                                resultsController.initializeTables();
                                
                                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            }
                            catch (IOException e) {
                                logger.error(e.getMessage());
                            }
                        }
                    });
                    
                }
                catch(IOException e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        };
        new Thread(t).start();
        
        
        
    }
    
    /**
     * La ricerca nei preferiti è standard, dunque non cambia da tabella a tabella.
     * In questo caso, il server restituisce una stringa vuota se non ci sono corrispondenze.
     * Per far in modo che le initizialeTable() della classe AbstractTableController processino
     * senza errori i JsonArray, se content è vuoto, inserisco "[]"(array vuoto in formato JSON).
     * JPA inserisce automaticamente "[]" quando ritorna un JSON vuoto, ma le "query normali", senza JPA,
     * non lo fanno, quindi ho messo questa particolarità.
     * @param searchUrl
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavorite(String searchUrl) throws IOException{
        URL url = new URL(searchUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        StringBuffer content = getResponse(con);
        if (content.length() == 0)
            content.append("[]");
        Gson gson = new Gson();
        JsonElement json = gson.fromJson(content.toString(), JsonElement.class);
        JsonArray elements = json.getAsJsonArray();
        return elements;
    }
    
    /**
     * La ricerca nei preferiti è standard, dunque si usa questa funzione assieme alla searchFavorite.
     * @param <T>
     * @param clazz: Nome della classe
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private<T> JsonArray searchFavoriteGeneric(Class<T> clazz, String username) throws IOException {
        return searchFavorite(genericSearchFavoritesURL + clazz.getSimpleName() + "?username="  + username);
    }
    
    
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteGame(String username) throws IOException {
        return searchFavoriteGeneric(Game.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteCharacter(String username) throws IOException {
        return searchFavoriteGeneric(Character.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteBosse(String username) throws IOException {
        return searchFavoriteGeneric(Boss.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteItem(String username) throws IOException {
        return searchFavoriteGeneric(Item.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoritePlace(String username) throws IOException {
        return searchFavoriteGeneric(Place.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteDungeon(String username) throws IOException {
        return searchFavoriteGeneric(Dungeon.class, username);
    }
    
    /**
     * 
     * @param username: tutto minuscolo
     * @return
     * @throws IOException 
     */
    private JsonArray searchFavoriteMonster(String username) throws IOException {
        return searchFavoriteGeneric(Monster.class, username);
    }
    
    
    /**
     * Fa passare alla schermata favorites.fxml, mostrando i preferiti dell'utente.
     * @param event: serve per mostrare la nuova schermata
     * Usa un task al fine di non bloccare l'interfaccia grafica.
     * Le modifiche all'interfaccia grafica saranno fatte appena possibile grazie a runLater().
     */
    @FXML
    private void goToFavoritesMenu(ActionEvent event) {
        Task t;
        t = new Task<Void>() {
            @Override
            public Void call() {
                try {
                    JsonArray games = searchFavoriteGame(credenziali.username);
                    JsonArray characters = searchFavoriteCharacter(credenziali.username);
                    JsonArray bosses = searchFavoriteBosse(credenziali.username);
                    JsonArray items = searchFavoriteItem(credenziali.username);
                    JsonArray places = searchFavoritePlace(credenziali.username);
                    JsonArray dungeons = searchFavoriteDungeon(credenziali.username);
                    JsonArray monsters = searchFavoriteMonster(credenziali.username);
                    
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("favorites.fxml"));
                                Parent root = loader.load();
                                //Inizializzo il controller
                                FavoritesController favoritesController = loader.getController();
                                //favoritesController.secondaryController = thisSecondaryController;
                                favoritesController.games = games;
                                favoritesController.characters = characters;
                                favoritesController.bosses = bosses;
                                favoritesController.items = items;
                                favoritesController.places = places;
                                favoritesController.dungeons = dungeons;
                                favoritesController.monsters = monsters;
                                //favoritesController.credenziali = thisSecondaryController.credenziali;
                                favoritesController.credenziali = credenziali;
                                //funzione usata perché la load chiama la initialize, e FXMLoader usa il costruttore di defualt (non si possono usare costruttori parametrici).
                                //Mi serviva un modo per passare i parametri e inizializzare le tabelle.
                                favoritesController.initializeTables();
                                
                                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                stage.setScene(new Scene(root));
                                stage.show();
                            }
                            catch (IOException e) {
                                logger.error(e.getMessage());
                            }
                        }
                    });
                    
                }
                catch(IOException e) {
                    logger.error(e.getMessage());
                }
                return null;
            }
        };
        new Thread(t).start();
        
        
        
    }
}