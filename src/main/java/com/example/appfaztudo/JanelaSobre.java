package com.example.appfaztudo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JanelaSobre extends Stage {
    public JanelaSobre() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sobre-wiew.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 420, 390);
        this.setTitle("Sobre o sistema");
        this.setScene(scene);
        this.show();
    }
}
