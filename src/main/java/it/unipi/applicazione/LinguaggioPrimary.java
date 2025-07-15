/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 *
 * @author Antonio
 * Serve per inizializzare i campi del PrimaryController in base alla lingua
 * scelta dall'utente.
 */
@XStreamAlias("linguaggio_primary")
public class LinguaggioPrimary implements Serializable {
    public String usernameLabel;
    public String switchLanguageButton;
    public String usr; //per il campo dove scrivo lo username
    public String pwd; //per il campo dove scrivo la password
    public String loginError; //per i messaggi di errore in fase di login
    public String populateButton;
    public String switchToRegistrationButton;
    public String switchToLoginButton;
    public String sendRegistrationButton;
    public String passwordErrorMessage;
    public String usernameErrorMessage;
    public String connectionError;
    public String loginButton;
}
