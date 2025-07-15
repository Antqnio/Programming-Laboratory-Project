/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.thoughtworks.xstream.XStream;
import static it.unipi.applicazione.AbstractController.logger;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Antonio
 */
public class ResultsController extends AbstractTableController {
    @FXML
    private MenuItem addGameToFavoritesMI;
    
    @FXML
    private MenuItem addCharacterToFavoritesMI;
    
    @FXML
    private MenuItem addMonsterToFavoritesMI;
    
    @FXML
    private MenuItem addPlaceToFavoritesMI;
    
    @FXML
    private MenuItem addItemToFavoritesMI;
    
    @FXML
    private MenuItem addDungeonToFavoritesMI;
    
    @FXML
    private MenuItem addBossToFavoritesMI;
    
    @FXML
    private Label searchResultsLabel;
    
    private LinguaggioResults lang;
    
    /**
     * Deve esser public per essere chiamato dalla loader.getController() dell'oggetto FXMLoader loader.
     */
    public ResultsController() {
        super("results");
    }
    
    /**
     * Per fare l'inizializzazione della lingua.
     */
    @FXML
    protected void initialize() {
        XStream xstream = creaXStream(LinguaggioResults.class);
        lang = (LinguaggioResults) xstream.fromXML(getClass().getResource("languages/results_it.xml"));
        generalInitialization();
    }
    
    /**
     * Per far cambiare la lingua mostrata all'utente da parte del ResultsController.
     */
    @Override
    protected void initializeLanguageFields() {
        
        switchLanguageButton.setText(lang.switchLanguageButton);
        getBackButton.setText(lang.getBackButton);
        searchResultsLabel.setText(lang.searchResultsLabel);
        
        gameLabel.setText(lang.gameLabel);
        characterLabel.setText(lang.characterLabel);
        itemLabel.setText(lang.itemLabel);
        placeLabel.setText(lang.placeLabel);
        dungeonLabel.setText(lang.dungeonLabel);
        monsterLabel.setText(lang.monsterLabel);
        bossLabel.setText(lang.bossLabel);
        
        showGameDescriptionMI.setText(lang.showDescriptionMI);
        showCharacterDescriptionMI.setText(lang.showDescriptionMI);
        showItemDescriptionMI.setText(lang.showDescriptionMI);
        showPlaceDescriptionMI.setText(lang.showDescriptionMI);
        showDungeonDescriptionMI.setText(lang.showDescriptionMI);
        showMonsterDescriptionMI.setText(lang.showDescriptionMI);
        showBossDescriptionMI.setText(lang.showDescriptionMI);
        
        addGameToFavoritesMI.setText(lang.addToFavoritesMI);
        addCharacterToFavoritesMI.setText(lang.addToFavoritesMI);
        addItemToFavoritesMI.setText(lang.addToFavoritesMI);
        addPlaceToFavoritesMI.setText(lang.addToFavoritesMI);
        addDungeonToFavoritesMI.setText(lang.addToFavoritesMI);
        addMonsterToFavoritesMI.setText(lang.addToFavoritesMI);
        addBossToFavoritesMI.setText(lang.addToFavoritesMI);
        
        /*
        removeGameFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeCharacterFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeItemFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removePlaceFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeDungeonFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeMonsterFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeBossFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        */
    }
    
    /**
     * Per inizializzare i vari campi con la lingua corrente.
     */
    @Override
    @FXML
    protected void switchLanguage() {
        XStream xstream = creaXStream(LinguaggioResults.class);
        if (linguaCorrente == LinguaEnum.italiano) {
            lang = (LinguaggioResults) xstream.fromXML(getClass().getResource("languages/results_eng.xml"));
            linguaCorrente = LinguaEnum.inglese;
        }
        else {
            lang = (LinguaggioResults) xstream.fromXML(getClass().getResource("languages/results_it.xml"));
            linguaCorrente = LinguaEnum.italiano;
        }
        initializeLanguageFields();
    }
    
    /**
     * Aggiungere un gioco ai preferiti è un'azione standard, che non varia al variare della tabella.
     * Pertanto, a ogni tabella associo un MenuItem che mi permette di ottenere informazioni sul record
     * su cui ho effettuato l'azione
     */
    
    @FXML
    private void addGameToFavorites() {
        addToFavorites("Game", gameTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addCharacterToFavorites() {
        addToFavorites("Character", characterTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addMonsterToFavorites() {
        addToFavorites("Monster", monsterTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addPlaceToFavorites() {
        addToFavorites("Place", placeTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addItemToFavorites() {
        addToFavorites("Item", itemTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addDungeonToFavorites() {
        addToFavorites("Dungeon", dungeonTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    @FXML
    private void addBossToFavorites() {
        addToFavorites("Boss", bossTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username);
    }
    
    
  
    
    
    /**
     * Si invia una POST al server a un indirizzo che dipende da className
     * @param className: deve essere con l'iniziale maiuscola e al singolare (Game, Character, item, Boss, Dungeon, Place, Monster).
     * @param elementName //In ogni tabella (tranne quelle utenti) la chiave è name. Potrebbe avere spazi, quindi lo
     * invio con una POST per evitare l'inserimento di caratteri speciali, facilitando la lettura da parte del server.
     * @param username //Deve essere minuscolo.
     * Contiene un Task per non bloccare l'interfaccia grafica.
     */
    private void addToFavorites(String className, String elementName, String username) {
        Task t = new Task<Void>() {
                @Override
                public Void call() {
                    try {

                        URL url = new URL("http://localhost:8080/ZeldaWiki/add"
                                + className + "ToFavorites?username=" + username);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        //mando elementName nel body.
                        sendSearchClassAsPOST(con, elementName);

                        Messaggio m = getMessaggio(con);
                        logger.info(m.contenuto); //informo sul logger riguardo all'esito della richiesta.
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
