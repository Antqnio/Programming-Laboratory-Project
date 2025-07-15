/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package it.unipi.applicazione;

import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Antonio
 */
public class SecondaryControllerTest {
    protected static final Logger logger = LogManager.getLogger(SecondaryControllerTest.class);
    public SecondaryControllerTest() {
    }
    
    @BeforeAll
    public static void initJavaFX() {
        JavaFXInitializer.initToolkit();
        try {
            Thread.sleep(1000); // Aspetta un momento per assicurarti che JavaFX sia inizializzato
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Test of switchLanguage method, of class SecondaryController.
     */
    /*
    @Test
    public void testSwitchLanguage() {
        System.out.println("switchLanguage");
        SecondaryController instance = new SecondaryController();
        instance.switchLanguage();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of initializeLanguageFields method, of class SecondaryController.
     */
    @Test
    public void testInitializeLanguageFields() {
        initJavaFX();
        System.out.println("initializeLanguageFields");
        SecondaryController instance = new SecondaryController();
        instance.logoutButton = new Button();
        instance.favoritesButton = new Button();
        instance.searchField = new TextField();
        instance.switchLanguageButton = new Button();
        instance.initialize(); //per inizializzare lang, i Button e il TextField, variabile di AbstractTableController da cui i vari Button e TextField prendono il testo.
        if (instance.logoutButton.getText().isEmpty() || instance.favoritesButton.getText().isEmpty() || 
                instance.searchField.getPromptText().isEmpty() || instance.switchLanguageButton.getText().isEmpty())
            fail("testInitializeLanguageFields failed");
        logger.info("testInitializeLanguageFields passed");
    }
    
}

//ChatGPT
class JavaFXInitializer extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Necessario solo per avviare JavaFX, non serve fare nulla qui
    }

    public static void initToolkit() {
        Thread thread = new Thread(() -> Application.launch(JavaFXInitializer.class));
        thread.setDaemon(true);
        thread.start();
    }
}