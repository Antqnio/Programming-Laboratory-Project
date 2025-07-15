/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.Serializable;

/**
 * Qui ci sono le parti riservate di LinguaggioResults.
 * @author Antonio
 */
@XStreamAlias("linguaggio_results")
public class LinguaggioResults extends LinguaggioAbstractTable implements Serializable {
    public String searchResultsLabel;
    public String addToFavoritesMI;
}
