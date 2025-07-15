package it.unipi.applicazione;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.thoughtworks.xstream.XStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class PrimaryController extends AbstractController {
    @FXML
    private Button loginRegistrationButton;
    private LinguaggioPrimary lang; //lo metto come membro dato perché mi serve anche dopo la initialize().
    
    //usr e pwd messi a livello friendly (o package) per lo Unit Test
    @FXML
    TextField usr;
    @FXML
    PasswordField pwd; //il compilatore non conosce le classi TextField e PasswordField, quindi facciamo "Fix imports".
   
    
    @FXML
    private TextField message;
    @FXML
    private Label hiddenText;
    @FXML
    private Label usernameLabel;
    @FXML
    private Button populateButton;
    @FXML
    private Button switchToRegistrationOrLoginButton;
   
    
    

    
    /**
     * Deve esser public per essere chiamato dalla loader.getController() dell'oggetto FXMLoader loader.
     */
    public PrimaryController() {
        super("primary");
    }
    
    /**
     * Per fare l'inizializzazione della lingua.
     */
    @FXML
    protected void initialize() {
        XStream xstream = creaXStream(LinguaggioPrimary.class);
        lang = (LinguaggioPrimary) xstream.fromXML(getClass().getResource("languages/primary_it.xml"));
        generalInitialization();
    }
    
    /**
     * Per far cambiare la lingua mostrata all'utente da parte del PrimaryController.
     */
    @Override
    @FXML
    protected void switchLanguage() {
        XStream xstream = creaXStream(LinguaggioPrimary.class);
        if (linguaCorrente == LinguaEnum.italiano) {
            lang = (LinguaggioPrimary) xstream.fromXML(getClass().getResource("languages/primary_eng.xml"));
            linguaCorrente = LinguaEnum.inglese;
        }
        else {
            lang = (LinguaggioPrimary) xstream.fromXML(getClass().getResource("languages/primary_it.xml"));
            linguaCorrente = LinguaEnum.italiano;
        }
        initializeLanguageFields();
    }


    /**
     * Per inizializzare i vari campi con la lingua corrente.
     */
    @Override
    protected void initializeLanguageFields() {
        usr.setPromptText(lang.usr);
        usernameLabel.setText(lang.usernameLabel);
        //hiddenText.setText(lang.loginError); //metto più avanti questo errore
        pwd.setPromptText(lang.pwd);
        switchLanguageButton.setText(lang.switchLanguageButton);
        populateButton.setText(lang.populateButton);
        switchToRegistrationOrLoginButton.setText(lang.switchToRegistrationButton);
        loginRegistrationButton.setText(lang.loginButton);
    }

    /********
     * Uso gli stessi bottoni sia per login che per registrazione.
     * Cambio gli handler a essi associati in base a cosa decide di fare l'utente (se login o password).
     * Nella classe LinguaggioPrimary ho il contenuto testuale sia per quando il bottone deve permettere il login
     * sia quando deve permetter la registrazione.
     */
    @FXML
    private void switchToRegistrationMode() {
        loginRegistrationButton.setText(lang.sendRegistrationButton);
        switchToRegistrationOrLoginButton.setText(lang.switchToLoginButton);
        loginRegistrationButton.setOnAction(event -> sendRegistration());
        switchToRegistrationOrLoginButton.setOnAction(event -> switchToLoginMode());
    }
    
    @FXML
    private void switchToLoginMode() {
        loginRegistrationButton.setText(lang.loginButton);
        switchToRegistrationOrLoginButton.setText(lang.switchToRegistrationButton);
        loginRegistrationButton.setOnAction(event -> login(event));
        switchToRegistrationOrLoginButton.setOnAction(event -> switchToRegistrationMode());
    }
    

    /**
     * Segnale un errore sull'interfaccia grafica.
     * Lo colora di rosso e lo mostra nella lingua scelta all'utente.
     * @param error 
     */
    private void signalError(String error) {
        logger.error(error);
        hiddenText.setText(error);
        hiddenText.setStyle(("-fx-text-fill: red;"));
        hiddenText.setVisible(true);
    }
    
    
    /**
     * Controlla se username e password sono validi per il cliente (quindi se non sono vuoti).
     * Messo a livello friendly (o package) per lo Unit Test.
     * @throws MalformedUsernameException
     * @throws MalformedPasswordException 
     */
    void checkUsernamePassword() throws MalformedUsernameException, MalformedPasswordException {
        if (usr.getText().isEmpty())
            throw new MalformedUsernameException(lang.usernameErrorMessage);
        if (pwd.getText().isEmpty())
            throw new MalformedPasswordException(lang.passwordErrorMessage);
    }
    
    /**
     * crea una connessione col server appositamente per invio di credenziali (e recezione di relativa risposta).
     * @param method: metodo di richiesta (GET o POST).
     * @param sendingModality: signin o signup, rispettivamente, per loggare o registrarsi.
     * @return
     * @throws IOException 
     */
    
    private HttpURLConnection createConnectionToSendCredentials(String method, String sendingModality) throws IOException {
        URL url = new URL("http://localhost:8080/ZeldaWiki/" + sendingModality + "?language=" + linguaCorrente);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);
        return con;
    }
    
    /**
     * Invia le credenziali al server a cui sono connesso con la connessione "con".
     * @param con
     * @return
     * @throws IOException 
     */
    
    private Credenziali sendCredentialsToServer(HttpURLConnection con) throws IOException {
        Gson gson = new Gson();
        Credenziali c = new Credenziali(usr.getText(), pwd.getText());
        String credenzialiJSON = gson.toJson(c);


         //metto le intestazioni
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Content-Length", String.valueOf(credenzialiJSON.length()));

        //abilito la scrittura nella connessione
        con.setDoOutput(true);

        //scrivo i dati nel corpo della richiesta HTTP
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = credenzialiJSON.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return c;
    }
    
    /**
     * Le funzioni si signup e signin sono quasi uguali, quindi creo una funzione usata da entrambe.
     * @param sendingModality: signin o signup, rispettivamente, per loggarsi e registrarsi.
     * @param event: serve per il login, in particolare al meccanismo di cambio di finestra (passo alla 
     * schermata secondary.fxml).
     * Contiene un task per non bloccare l'interfaccia grafica.
     */
    private void sendCredentials(String sendingModality, ActionEvent event) {
        Task t = new Task<Void>() {
                @Override
                public Void call() {
                    try {
                        checkUsernamePassword();
                        //inviare con una post.
                        HttpURLConnection con = createConnectionToSendCredentials("POST", sendingModality);
                        credenziali = sendCredentialsToServer(con);

                        Messaggio m = getMessaggio(con);
                        if (sendingModality.equals("signup")) {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    if (m.statusCode == 200)
                                        hiddenText.setStyle(("-fx-text-fill: green;"));
                                    else
                                        hiddenText.setStyle(("-fx-text-fill: red;"));
                                    hiddenText.setText(m.contenuto);
                                    hiddenText.setVisible(true);
                                }
                            });
                        }
                        else { //sendingModality.equals("signin")
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (m.statusCode == 200) {
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("secondary.fxml"));
                                            Parent root = loader.load();
                                            SecondaryController secondaryController = loader.getController();
                                            secondaryController.credenziali = credenziali;
                                            //Stage stage = new Stage(); Con questo creo una nuova finestra. Utile per le descrizioni in futuro.
                                            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                                            stage.setScene(new Scene(root));
                                            stage.show();
                                        }
                                        else
                                            hiddenText.setStyle(("-fx-text-fill: red;"));
                                        hiddenText.setText(m.contenuto);
                                        hiddenText.setVisible(true);
                                    }
                                    catch (IOException e) {
                                        System.err.println("Errreeee");
                                        signalError(e.getMessage());
                                    }
                                }
                            });
                        }
                        
                    }
                    catch(IOException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                signalError(lang.connectionError);
                            }
                        });
                    }
                    catch (MalformedUsernameException | MalformedPasswordException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                signalError(e.getMessage());
                            }
                        });
                    }
                    return null;
                }
                
        };
        new Thread(t).start();
    }
    
    /**
     * Manda al server la richiesta per registrami.
     */
    @FXML
    private void sendRegistration() {
        sendCredentials("signup", null);
    }
    /**
     * Manda la server la richiesta per loggarsi.
     * @param event 
     */
    @FXML
    private void login(ActionEvent event) {
        sendCredentials("signin", event);
    }
    
    
    /**
     * Mando una GET al server per popolare le tabelle.
     * Ogni volta che apro il client, posso effettuare la populate() tramite l'apposito tasto.
     * Questo, potenzialmente, fa aggiornare i dati al database (li fa riscaricare dalle API).
     * Contiene un task per non bloccare l'interfaccia grafica
     */
    @FXML
    private void popolulate() {
        Task t = new Task<Void>() {
                @Override
                public Void call() {
                    try {

                        URL url = new URL("http://localhost:8080/ZeldaWiki/populate?language=" + linguaCorrente);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");


                        Messaggio m = getMessaggio(con);
                        logger.info(m.contenuto);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (m.statusCode == 200) {
                                    populateButton.setDisable(true);
                                    hiddenText.setStyle(("-fx-text-fill: green;"));
                                }
                                else
                                    hiddenText.setStyle(("-fx-text-fill: red;"));
                                hiddenText.setText(m.contenuto);
                                hiddenText.setVisible(true);
                            }
                        });
                        
                    }
                    catch(IOException e) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                signalError(lang.connectionError);
                            }
                        });
                    }
                    return null;
            }
        };
        new Thread(t).start();
    }
}




