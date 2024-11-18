package com.example.appfaztudo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JanelaCaixa extends Stage {

    public JanelaCaixa() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("caixa-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 430, 600);
        this.setTitle("Caixa");
        this.setScene(scene);
        this.show();
    }

}
