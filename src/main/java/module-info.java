module it.unipi.applicazione {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires com.google.gson;
    requires xstream;
    requires java.sql;

    opens it.unipi.applicazione to javafx.fxml;
    exports it.unipi.applicazione;
}
