/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

/**
 * Eccezione lanciata dal client se l'utente prova a fare sign-in o sign-up
 * con username vuoto.
 * @author Antonio
 */
public class MalformedUsernameException extends Exception {
    public MalformedUsernameException() {
        super();
    }
    public MalformedUsernameException (String message) {
        super(message);
    }
}
