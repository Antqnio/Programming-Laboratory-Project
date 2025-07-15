/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;





/**
 *
 * @author Antonio
 * JavaBean della classe Boss.
 */

public class Boss {

    private String name;
    private String description;


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

    

    public Boss(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public Boss() {
        
    }
}
