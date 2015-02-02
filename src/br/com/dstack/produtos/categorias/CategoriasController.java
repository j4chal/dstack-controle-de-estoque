package br.com.dstack.produtos.categorias;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import br.com.dstack.utils.Validacao;

public class CategoriasController {

	@FXML
	private Button btnNovo;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnFechar;
	@FXML
	private TextField txtDescricao;
	@FXML
	private Label lblMensagem;

	private Categorias categorias = new Categorias();
	private static Logger logger = LogManager.getLogger(CategoriasController.class);

	public CategoriasController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CategoriasView.fxml"));
			fxmlLoader.setController(this);
			Parent parent;
			parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Cadastrar Categorias | DStack - Controle de Estoque");
			stage.getIcons().add(new Image("icon.jpg"));
			stage.setResizable(false);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	/**
	 * Inicia com a classe
	 */
	@FXML
	private void initialize() {
		Validacao.limitarCaracteres(txtDescricao, 150, lblMensagem);
	}

	/**
	 * Botão que limpa o campo de cadastro
	 */
	@FXML
	private void btnNovoAction() {
		txtDescricao.clear();
	}

	/**
	 * Botão que chama método de inserção e limpa o TextField
	 */
	@FXML
	private void btnSalvarAction() {
		verificarInsert();
		txtDescricao.clear();
	}

	/**
	 * Botão que chama o método para fechar janela
	 */
	@FXML
	private void btnFecharAction() {
		fechaJanela();
	}

	/**
	 * Passa os dados e verifica se a inserção ocorreu com sucesso, se retornar true é mostrado a mensagem de sucesso, senão é mostrado uma mensagem com erro
	 */
	public void verificarInsert() {
		try {
			categorias = new Categorias(txtDescricao.getText());

			if (categorias.insere()) {
				Dialogs.create().title("Sucesso").message("Categoria foi cadastrada com sucesso!").showInformation();
			}
			else {
				Dialogs.create().title("Error").message("Erro ao inserir dados, verifique se todos os campos necessarios estão preenchidos!").showError();
			}
		}
		catch (Exception e3) {
			lblMensagem.setText("Campo não preenchido");
		}
	}

	/**
	 * Método que pega o Stage da janela e chama o método close()
	 */
	public void fechaJanela() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}
}
