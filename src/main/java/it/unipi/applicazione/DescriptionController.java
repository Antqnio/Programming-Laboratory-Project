/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Usata per mostrare le descrizioni dei record delle tabelle in una finestra apposita.
 * La descrizione è solo in inglese, visto che l'API fornisce la descrizione in inglese.
 * @author Antonio
 */
public class DescriptionController {
    @FXML
    public Text descriptionField;
    
    //Il TextFlow presente nel file XML serve sempre per andare a capo e far cambiare dinamicamente la dimensione
    //delle righe in base
    
    /**
     * Usato per mostrare la descrizione di un record di una tabellain modo che
     * questo si adatti dinamicamente alle dimensioni della finestra.
     * @param description 
     */
    public void displayDescription(String description) {
        descriptionField.setText(description);
        descriptionField.setDisable(true);
        descriptionField.setTextAlignment(TextAlignment.JUSTIFY); //va a capo in base alla larghezza della finestra. Usato inisieme al textFlow.
    }
}
