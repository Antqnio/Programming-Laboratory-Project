/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;





/**
 *
 * @author Antonio
 * JavaBean della classe Character.
 */

public class Character {
    private String name;
    private String description;
    private String gender;
    private String race;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }


    

    public Character(String name, String description, String gender, String race) {
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.race = race;
    }

    public Character() {
        
    }
}
