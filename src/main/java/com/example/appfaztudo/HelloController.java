package com.example.appfaztudo;

import javafx.fxml.FXML;

import java.io.IOException;

public class HelloController {

    @FXML
    protected void onCaixa() throws IOException {
        new JanelaCaixa();
    }

    @FXML
    protected void onVerificaPreco() throws IOException {
        new JanelaVerificaPreco();
    }
    @FXML
    protected void onSobre() throws IOException {
        new JanelaSobre();
    }


    @FXML
    protected void onSairButtonClick(){
        System.exit(0);
    }





}