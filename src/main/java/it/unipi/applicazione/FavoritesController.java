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
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;

/**
 * Usato per mostrare una schermata dove sono presenti i preferiti dell'utente.
 * @author Antonio
 */

public class FavoritesController extends AbstractTableController {
    
    @FXML
    private Label favoritesLabel;
    
        
    @FXML
    private MenuItem removeGameFromFavoritesMI;
    @FXML
    private MenuItem removeCharacterFromFavoritesMI;
    @FXML
    private MenuItem removeItemFromFavoritesMI;
    @FXML
    private MenuItem removeDungeonFromFavoritesMI;
    @FXML
    private MenuItem removeBossFromFavoritesMI;
    @FXML
    private MenuItem removeMonsterFromFavoritesMI;
    @FXML
    private MenuItem removePlaceFromFavoritesMI;
    
    private LinguaggioFavorites lang;
    
    /**
     * Deve esser public per essere chiamato dalla loader.getController() dell'oggetto FXMLoader loader.
     */
    public FavoritesController() {
        super("favorites");
    }
    
    /**
     * Per fare l'inizializzazione della lingua.
     */
    @FXML
    private void initialize() {
        XStream xstream = creaXStream(LinguaggioFavorites.class);
        lang = (LinguaggioFavorites) xstream.fromXML(getClass().getResource("languages/favorites_it.xml"));
        generalInitialization();
    }
    
    /**
     * Per inizializzare i vari campi con la lingua corrente.
     */
    @Override
    protected void initializeLanguageFields() {
        
        switchLanguageButton.setText(lang.switchLanguageButton);
        getBackButton.setText(lang.getBackButton);
        favoritesLabel.setText(lang.favoritesLabel);
        
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
        
        removeGameFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeCharacterFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeItemFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removePlaceFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeDungeonFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeMonsterFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        removeBossFromFavoritesMI.setText(lang.removeFromFavoritesMI);
        
    }
    
    /**
     * Per cambiare la lingua mostrata all'Utente dal parte del ResultsController
     */
    @Override
    @FXML
    protected void switchLanguage() {
        XStream xstream = creaXStream(LinguaggioFavorites.class);
        if (linguaCorrente == LinguaEnum.italiano) {
            lang = (LinguaggioFavorites) xstream.fromXML(getClass().getResource("languages/favorites_eng.xml"));
            linguaCorrente = LinguaEnum.inglese;
        }
        else {
            lang = (LinguaggioFavorites) xstream.fromXML(getClass().getResource("languages/favorites_it.xml"));
            linguaCorrente = LinguaEnum.italiano;
        }
        initializeLanguageFields();
    }
    
    
    
    
    /**
     * Rimuove dai preferiti il record con name == elementName dalla tabella tableName, dove tableName è il nome
     * della tabella nel server.
     * Usa i generics, visto che si adatta bene a tutte le tabelle.
     * @param <T>
     * @param ol
     * @param table
     * @param elementName: si manda in una POST perché può contenere spazi
     * @param tableName: deve essere una stringa concaneta della forma username + tableName, dove username è minuscolo tutto attacato
     * e tableName è minuscolo e singolare (game, place, item, dungeon, monster, boss, character).
     */
    private<T> void removeFromFavorites(ObservableList<T> ol, TableView<T> table, String elementName, String tableName) {
        Task t = new Task<Void>() {
                @Override
                public Void call() {
                    try {

                        URL url = new URL("http://localhost:8080/ZeldaWiki/removeFromFavorites?tableName=" + tableName);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        //Visto che elementName può contenere spazi, lo mando nel body con una POST.
                        sendSearchClassAsPOST(con, elementName);


                        Messaggio m = getMessaggio(con);
                        logger.info(m.contenuto);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (m.statusCode == 200) {
                                    //Rimuovo dalla tabella mostrata all'utente solo se effettivamente
                                    //è stato cancellato il record dal database.
                                    ol.remove(table.getSelectionModel().getSelectedItem());
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
     * Tutte queste funzioni del tipo removeItemFromFavorites() chiamano la funzione removeFromFavorites
     * seguendo il formato da esso definito. La rimozione è resa standardizzata.
     */
    @FXML
    private void removeGameFromFavorites() {
        removeFromFavorites(gameOl, gameTable,
                gameTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "game");
    }
    
    @FXML
    private void removeCharacterFromFavorites() {
        removeFromFavorites(characterOl, characterTable,
                characterTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "character");
    }
    
    @FXML
    private void removeItemFromFavorites() {
        removeFromFavorites(itemOl, itemTable,
                itemTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "item");
    }
    
    @FXML
    private void removePlaceFromFavorites() {
        removeFromFavorites(placeOl, placeTable,
                placeTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "place");
    }
    
    @FXML
    private void removeMonsterFromFavorites() {
        removeFromFavorites(monsterOl, monsterTable,
                monsterTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "monster");
    }
    
    @FXML
    private void removeDungeonFromFavorites() {
        removeFromFavorites(dungeonOl, dungeonTable,
                dungeonTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "dungeon");
    }
    
    @FXML
    private void removeBossFromFavorites() {
        removeFromFavorites(bossOl, bossTable,
                bossTable.getSelectionModel().getSelectedItem().getName(),
                credenziali.username + "boss");
    }
    
}
