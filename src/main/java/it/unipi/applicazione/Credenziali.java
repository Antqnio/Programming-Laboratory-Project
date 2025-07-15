/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Antonio
 * JavaBean della classe Credenziali.
 * Comprende override di metodi equals() e hashCode().
 * Serviva un override di equals(), quindi ho fatto l'overload di hashCode()
 * per mantenere consistenza tra i due.
 */
public class Credenziali implements Serializable {
    public String username;
    public String password;
    public Credenziali(String n, String p) {
        username = n;
        password = p;
    }
    
    public Credenziali() {
        
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Credenziali))
            return false;
        Credenziali other = (Credenziali)o;
        return username.equals(other.username) && password.equals(other.password);
    }
    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
