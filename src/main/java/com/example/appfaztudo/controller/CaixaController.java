package com.example.appfaztudo.controller;

import com.example.appfaztudo.model.Produto;
import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CaixaController {
    @FXML
    private TableView<Produto> tabelaProdutos;
    @FXML
    private TableColumn<Produto, String> colunaNome;
    @FXML
    private TableColumn<Produto, String> colunaCodigo;
    @FXML
    private TableColumn<Produto, Integer> colunaQuantidade;
    @FXML
    private TableColumn<Produto, Double> colunaPreco;
    @FXML
    private TableColumn<Produto, String> colunaMarca;
    @FXML
    private TextField inputCodigo;
    @FXML
    private TextField inputQuantidade;
    @FXML
    private Label labelTotal;
    @FXML
    private TextField inputValorPago;
    @FXML
    private Label labelTroco;

    private ObservableList<Produto> listaProdutos;
    private double total;

    @FXML
    public void initialize() {
        listaProdutos = FXCollections.observableArrayList();

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colunaQuantidade.setCellValueFactory(new PropertyValueFactory<>("quantidade"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        colunaMarca.setCellValueFactory(new PropertyValueFactory<>("marca"));

        tabelaProdutos.setItems(listaProdutos);

        total = 0.0;
    }

    @FXML
    private void adicionarProduto() {
        try {
            String codigo = inputCodigo.getText();
            int quantidade = Integer.parseInt(inputQuantidade.getText());

            Produto produto = buscarProdutoPorCodigo(codigo);
            if (produto != null) {
                produto.setQuantidade(quantidade);
                listaProdutos.add(produto);
                total += produto.getPreco() * quantidade;
                labelTotal.setText(String.format("R$ %.2f", total));
            } else {
                mostrarMensagem("Produto não encontrado ou indisponível.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void excluirProduto() {
        Produto produtoSelecionado = tabelaProdutos.getSelectionModel().getSelectedItem();
        if (produtoSelecionado != null) {
            listaProdutos.remove(produtoSelecionado);
            total -= produtoSelecionado.getPreco() * produtoSelecionado.getQuantidade();
            labelTotal.setText(String.format("R$ %.2f", total));
        } else {
            mostrarMensagem("Nenhum produto selecionado para exclusão.");
        }
    }

    @FXML
    private void calcularTroco() {
        try {
            double valorPago = Double.parseDouble(inputValorPago.getText());
            double troco = valorPago - total;
            labelTroco.setText(String.format("R$ %.2f", troco));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            labelTroco.setText("R$ 0.00");
        }
    }

    @FXML
    private void finalizarVenda() {
        Double valorPago = Double.parseDouble(inputValorPago.getText());
        if( inputValorPago.getCharacters().isEmpty()){
            mostrarMensagem("Erro ao finalizar venda o valor:"+valorPago +"E insuficiente!!!!!");
            return;
        }
        if(valorPago<total){
            mostrarMensagem("Erro ao finalizar venda o valor:"+valorPago +"E insuficiente!!!!!");
            return;
        }

        try {
            for (Produto produto : listaProdutos) {
                atualizarQuantidadeNoBanco(produto);
            }
            listaProdutos.clear();
            total = 0.0;
            labelTotal.setText("R$ 0.00");
            labelTroco.setText("R$ 0.00");
            inputValorPago.clear();
            mostrarMensagem("Venda finalizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            mostrarMensagem("Erro ao finalizar venda. Tente novamente.");
        }
    }

    private void atualizarQuantidadeNoBanco(Produto produto) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/produtos/atualizar/quantidade"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(new Gson().toJson(produto)))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.err.println("Falha ao atualizar quantidade no banco de dados. Código de status: " + response.statusCode());
                System.err.println("Corpo da resposta: " + response.body());
                throw new RuntimeException("Falha ao atualizar quantidade no banco de dados.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao atualizar quantidade no banco de dados.", e);
        }
    }

    private Produto buscarProdutoPorCodigo(String codigo) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/produtos/buscar/codigo?codigo=" + codigo))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            return gson.fromJson(response.body(), Produto.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
