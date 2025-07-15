/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

/**
 *
 * @author Antonio
 * Serve per scegliere la lingua nel client. Usata da tutti i Controller,
 * tranne DescriptionController (che è solo in inglese, visto che l'API
 * fornisce la descrizione in inglese).
 */
public enum LinguaEnum {
    italiano, inglese;
    @Override
    public String toString() {
        switch(this) {
            case italiano: 
                return "it";
            case inglese:
                return "eng";
            default:
                return "";
        }
    }
}