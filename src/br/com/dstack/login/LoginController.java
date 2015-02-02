package br.com.dstack.login;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.dstack.menu.MenuController;
import br.com.dstack.utils.CriarBDComTabelas;

public class LoginController {

	/**
	 * Componentes injetados
	 */
	@FXML
	private Label lblUsuarioMensagem;
	@FXML
	private Label lblSenhaMensagem;
	@FXML
	private TextField txtUsuario;
	@FXML
	private PasswordField txtSenha;
	@FXML
	private Button btnEntrar;
	@FXML
	private Button btnFechar;
	@FXML
	private AnchorPane anpLogin;
	@FXML
	private ImageView imgLogo;
	@FXML
	private Label lblMensagem;

	private static Logger logger = LogManager.getLogger(LoginController.class);

	public LoginController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			fxmlLoader.setController(this);
			fxmlLoader.load();
		}
		catch (IOException e1) {
			logger.log(Level.TRACE, "IOException", e1);
		}
	}

	public LoginController(Stage stage) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginView.fxml"));
			fxmlLoader.setController(this);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("Login.css").toExternalForm());
			stage.setScene(scene);
			stage.setTitle("Login | DStack - Controle de Estoque");
			stage.getIcons().add(new Image("icon.jpg"));
			stage.setResizable(false);
			stage.show();
		}
		catch (IOException e2) {
			logger.log(Level.TRACE, "IOException", e2);
		}
	}

	/**
	 * Método que inicializa com a classe
	 */
	@FXML
	private void initialize() {
		imgLogo.setImage(new Image(ClassLoader.getSystemResourceAsStream("logo.png")));
		CriarBDComTabelas criarDb = new CriarBDComTabelas();
		criarDb.criar(lblMensagem);
	}

	/**
	 * Botão para fechar a tela de login
	 */
	@FXML
	private void btnFecharAction(ActionEvent event) {
		Platform.exit();
	}

	/**
	 * Botão para chamar o método de validação de login
	 */
	@FXML
	public void btnEntrarAction(ActionEvent event) {
		loginValidacao(event);
	}

	/**
	 * Método que envia o login e senha digitados, faz a comparação, caso retornar true, o menu principal é aberto e a janela de login é escondida
	 */
	public void loginValidacao(ActionEvent event) {
		validarCampos();

		Login lm = new Login(txtSenha.getText());
		Login.setUsuarioProperty(txtUsuario.getText());

		// Pega key Situacao de um HashMap
		String situacao = lm.getSituacaoEPerfil(txtUsuario.getText()).get("Situacao");

		try {
			if (lm.verificaHash() && situacao.equals("Ativo")) {
				menu();
				((Node) (event.getSource())).getScene().getWindow().hide(); // Esconde a janela
			}

			if (lm.verificaHash() && situacao.equals("Inativo")) {
				lblUsuarioMensagem.setText("Conta inativa, contate um administrador");
				lblSenhaMensagem.setText("");
			}
		}
		catch (SQLException e1) {
			logger.log(Level.TRACE, "SQLException", e1);
			lblUsuarioMensagem.setText("");
			lblSenhaMensagem.setText("");
			lblMensagem.setText("Não foi possivel realizar a conexão com banco de dados");
		}
	}

	/**
	 * Abre o form do menu principal
	 */
	public void menu() {
		new MenuController(null);
	}

	/**
	 * Método que realiza algumas validações dos campos de usuario e senha
	 */
	public void validarCampos() {

		if (!txtUsuario.getText().isEmpty() && txtSenha.getText().isEmpty()) {
			lblUsuarioMensagem.setText("");
			lblSenhaMensagem.setText("Campo senha vazio, digite sua senha");
		}
		if (txtUsuario.getText().isEmpty() && !txtSenha.getText().isEmpty()) {
			lblUsuarioMensagem.setText("Campo usuario vazio, digite seu usuario");
			lblSenhaMensagem.setText("");
		}
		if (txtUsuario.getText().isEmpty() && txtSenha.getText().isEmpty()) {
			lblUsuarioMensagem.setText("Campo usuario vazio, digite seu usuario");
			lblSenhaMensagem.setText("Campo senha vazio, digite sua senha");
		}
		if (!txtUsuario.getText().isEmpty() && !txtSenha.getText().isEmpty()) {
			lblUsuarioMensagem.setText("Usuario e/ou senha incorreto");
			lblSenhaMensagem.setText("Usuario e/ou senha incorreto");
		}
	}
}
