package com.example.appfaztudo.controller;

import com.example.appfaztudo.model.Produto;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class VerificarPrecosController {

    @FXML
    private TextField inputBusca;
    @FXML
    private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, String> colunaNome;
    @FXML
    private TableColumn<Produto, String> colunaCodigo;
    @FXML
    private TableColumn<Produto, Double> colunaPreco;
    @FXML
    private TableColumn<Produto, String> colunaMarca;

    private ObservableList<Produto> listaProdutos;

    @FXML
    public void initialize() {
        listaProdutos = FXCollections.observableArrayList();
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));
        tabelaProdutos.setItems(listaProdutos);
    }

    @FXML
    private void buscarProduto() {
        String busca = inputBusca.getText();

        if (busca.isEmpty()) {
            mostrarMensagem("Por favor, insira o código ou nome do produto.");
            return;
        }

        if (busca.matches("[A-Za-z]+")) {
            buscarProdutosPorNome(busca);
        } else {
            buscarProdutoPorCodigo(busca);
        }
    }

    private void buscarProdutoPorCodigo(String codigo) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/produtos/buscar/codigo?codigo=" + codigo))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Produto produto = gson.fromJson(response.body(), Produto.class);
            listaProdutos.clear();
            if (produto != null) {
                listaProdutos.add(produto);
            } else {
                mostrarMensagem("Produto não encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void buscarProdutosPorNome(String nome) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/produtos/buscar/nome?nome=" + nome))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Produto>>() {}.getType();
            List<Produto> produtos = gson.fromJson(response.body(), listType);
            listaProdutos.clear();
            if (!produtos.isEmpty()) {
                listaProdutos.addAll(produtos);
            } else {
                mostrarMensagem("Nenhum produto encontrado.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarMensagem(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensagem do Sistema");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}
