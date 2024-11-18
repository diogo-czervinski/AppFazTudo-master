module com.example.appfaztudo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    // Abrir o pacote para JavaFX e Gson
    opens com.example.appfaztudo.model to javafx.base, com.google.gson;
    opens com.example.appfaztudo.controller to javafx.fxml;
    opens com.example.appfaztudo to javafx.fxml;

    exports com.example.appfaztudo;
}
