package br.com.dstack.menu.alterarsenha;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

public class AlterarSenhaController {

	@FXML
	private Button btnAlterarSenha;
	@FXML
	private Button btnCancelar;
	@FXML
	private TextField txtNovaSenha;
	@FXML
	private TextField txtNovaSenhaConfirmacao;
	@FXML
	private Label lblMensagem;
	@FXML
	private ImageView imgAlterarSenha;

	private static Logger logger = LogManager.getLogger(AlterarSenhaController.class);

	public AlterarSenhaController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AlterarSenhaView.fxml"));
			fxmlLoader.setController(this);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Alterar Senha | DStack - Controle de Estoque");
			stage.getIcons().add(new Image("icon.jpg"));
			stage.setResizable(false);
			stage.setAlwaysOnTop(true);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	/**
	 * Método que inicializa com a classe
	 */
	@FXML
	private void initialize() {
		imgAlterarSenha.setImage(new Image(ClassLoader.getSystemResourceAsStream("chave.png")));
	}

	/**
	 * Botão para alterar senha
	 */
	@FXML
	private void btnAlterarSenhaAction() {
		confirmarSenha();
	}

	/**
	 * Confirma se a nova senha foi digitada corretamente, caso foi, a nova senha é passada pelo metodo set e se tudo ocorrer sem problemas, é recebido um dialogo de sucesso
	 */
	public void confirmarSenha() {
		if (txtNovaSenha.getText().equals(txtNovaSenhaConfirmacao.getText()) && !txtNovaSenha.getText().isEmpty()) {
			AlterarSenha as = new AlterarSenha();
			as.setNovaSenha(txtNovaSenha.getText());

			if (as.enviarNovaSenha()) {
				Dialogs.create().title("Sucesso").message("Senha alterada com sucesso!").showInformation();
				fecharJanela();
			}
		}
		else {
			validarCampos();
		}
	}

	/**
	 * Valida campos
	 */
	public void validarCampos() {
		if (!txtNovaSenha.getText().equals(txtNovaSenhaConfirmacao.getText())) {
			lblMensagem.setText("Senhas digitadas estão diferentes, digite novamente");
		}
		if (txtNovaSenha.getText().isEmpty() && txtNovaSenhaConfirmacao.getText().isEmpty()) {
			lblMensagem.setText("Campos estão vazios, digite para continuar");
		}
		if (txtNovaSenha.getText().isEmpty() && !txtNovaSenhaConfirmacao.getText().isEmpty()) {
			lblMensagem.setText("Campo nova senha está vazio, digite para continuar");
		}
		if (!txtNovaSenha.getText().isEmpty() && txtNovaSenhaConfirmacao.getText().isEmpty()) {
			lblMensagem.setText("Campo nova senha (confirmação) está vazio, digite para continuar");
		}
	}

	/**
	 * Botão para cancelar operação de atualizar senha
	 */
	@FXML
	private void btnCancelarAction() {
		fecharJanela();
	}

	/**
	 * Método para fechar janela
	 */
	public void fecharJanela() {
		Stage stage = (Stage) btnCancelar.getScene().getWindow();
		stage.close();
	}
}
