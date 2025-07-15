/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

/**
 * Eccezione lanciata dal client se l'utente prova a fare sign-in o sign-up
 * con password vuota.
 * @author Antonio
 */
public class MalformedPasswordException extends Exception {
    public MalformedPasswordException() {
        super();
    }
    public MalformedPasswordException (String message) {
        super(message);
    }
}
