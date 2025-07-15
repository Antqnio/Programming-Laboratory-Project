/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unipi.applicazione;

import com.google.gson.Gson;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Antonio
 * Usata per fornire una base a tutti i controller (tranne DescriptionController, che è estremamente banale).
 * Dà le basi per il cambio di lingua e contiene funzioni usate da più controller (getMessaggio() e getResponse()).
 */
public abstract class AbstractController {
    
    protected static final Logger logger = LogManager.getLogger(AbstractController.class); //Lo creo direttamente nei figli perché, altrimenti, non potrebbe essere final (se lo metto static final, i figli non possono inizializzarlo).
    protected LinguaEnum linguaCorrente;
    protected Credenziali credenziali;
    private final String controllerName;
    
    
    @FXML
    protected Button switchLanguageButton;
    
    /**
     * 
     * @param controllerName deve essere "primary", "secondary", "tertiary"...
     */
    
    protected AbstractController(String controllerName) {
        this.controllerName = controllerName;
    }
    
    /**
     * 
     */
    @FXML
    protected void generalInitialization() {//chiamata quando si passa a questa schermata.\
        linguaCorrente = LinguaEnum.italiano;
        initializeLanguageFields();
    }
    
    /**
     * 
     * @param clazz
     * @return 
     */
    protected XStream creaXStream(Class<?> clazz) {//lo crea per la classe LinguaggioPrimary
        XStream xstream = new XStream();
        xstream.addPermission(AnyTypePermission.ANY);
        xstream.alias("linguaggio_" + controllerName, clazz);
        return xstream;
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws IOException 
     */
    protected StringBuffer getResponse(HttpURLConnection con) throws IOException{
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        logger.info(content);
        return content;
    }
    
    /**
     * 
     * @param con
     * @return
     * @throws IOException 
     */
    protected Messaggio getMessaggio(HttpURLConnection con) throws IOException{
        StringBuffer content = getResponse(con);
        Gson gson = new Gson();
        return gson.fromJson(content.toString(), Messaggio.class);
    }
    
    
    
    /**
     * 
     */
    protected abstract void initializeLanguageFields();
    
    /**
     * 
     */
    protected abstract void switchLanguage();
}
