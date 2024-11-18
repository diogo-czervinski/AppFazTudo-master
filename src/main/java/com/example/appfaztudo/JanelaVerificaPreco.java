package com.example.appfaztudo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class JanelaVerificaPreco extends Stage {

    public JanelaVerificaPreco() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("verifica-precos.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 430, 390);
        this.setTitle("Verifica Preço");
        this.setScene(scene);
        this.show();
    }

}
