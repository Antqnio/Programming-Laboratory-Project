/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import java.io.Serializable;

/**
 * Non ha bisgono di @XStreamAlias perché, nel codice utilizzato per questo software, si accede a questa classe
 * solo tramite i figli (anche per recuperare le istanze da JSON/XML).
 * Qui ci sono le parti comuni a LinguaggioFavorites e LinguaggioResults.
 * @author Antonio
 */
public abstract class LinguaggioAbstractTable implements Serializable {
    public String getBackButton;
    public String switchLanguageButton;
    public String gameLabel;
    public String characterLabel;
    public String monsterLabel;
    public String placeLabel;
    public String dungeonLabel;
    public String bossLabel;
    public String itemLabel;
    public String showDescriptionMI;
}
