/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 * Serve per inizializzare i campi del PrimaryController in base alla lingua
 * scelta dall'utente.
 * @author Antonio
 */
@XStreamAlias("linguaggio_secondary")
public class LinguaggioSecondary implements Serializable {
    public String logoutButton;
    public String favoritesButton;
    public String searchField;
    public String switchLanguageButton;
}
