/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import java.time.LocalDate; //Questa dà problemi.
//import java.time.LocalDate; //per non portare il tempo





/**
 *
 * @author Antonio
 * JavaBean della classe Game.
 */

public class Game {
    private String name;
    private String description;
    private LocalDate releaseDate;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Game(String name, String description, LocalDate release_date) {
        this.name = name;
        this.description = description;
        this.releaseDate = release_date;
    }
    
    public Game() {
        
    }
}
