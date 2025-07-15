/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 * Qui ci sono le parti riservate di LinguaggioFavorites
 * @author Antonio
 */
@XStreamAlias("linguaggio_favorites")
public class LinguaggioFavorites extends LinguaggioAbstractTable implements Serializable {
    public String favoritesLabel;
    public String removeFromFavoritesMI;
}
