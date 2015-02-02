package br.com.dstack.usuarios.cadastro;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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

import br.com.dstack.usuarios.Usuarios;
import br.com.dstack.utils.Validacao;

public class CadastroUsuariosController {

	/*
	 * Componentes injetados
	 */
	@FXML
	private TextField txtCodigo;
	@FXML
	private TextField txtUsuario;
	@FXML
	private TextField txtNomeCompleto;
	@FXML
	private TextField txtSenha;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<String> cmbPerfil;
	@FXML
	private ComboBox<String> cmbSituacao;

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnLimpar;
	@FXML
	private Button btnCancelar;
	@FXML
	private Label lblMensagem;
	@FXML
	private ImageView imgAddUsuario;

	private Usuarios usuarios = new Usuarios();
	private static Logger logger = LogManager.getLogger(CadastroUsuariosController.class);

	/**
	 * Construtor que seta o controller explicitamente para a view.
	 * Isso é feito para evitar o problema de em caso outra classe tentar acessar esse objeto, não vai estar acessando outra instancia.
	 */
	public CadastroUsuariosController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CadastroUsuariosView.fxml"));
			fxmlLoader.setController(this);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Cadastrar Usuarios | DStack - Controle de Estoque");
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
	 * Método que é iniciado com a classe
	 */
	@FXML
	private void initialize() {
		imgAddUsuario.setImage(new Image(ClassLoader.getSystemResourceAsStream("add_usuario.png")));
		adicionaItems();
		validacaoDeCampos();
	}

	/**
	 * Valida os campos de cadastro de usuario
	 */
	private void validacaoDeCampos() {
		Validacao.restringirNumerosECharactereEspecial(txtNomeCompleto, 70, lblMensagem);
		Validacao.limitarCaracteres(txtEmail, 70, lblMensagem);
		Validacao.restringirNaoAlfaNumericos(txtUsuario, 50, lblMensagem);
		Validacao.limitarCaracteres(txtSenha, 60, lblMensagem);
	}

	/**
	 * Seta os valores recebidos pelos parametros nos campos
	 */
	public void setValores(int codigo, String nomeCompleto, String email, String usuario, String senha, String perfil, String situacao) throws IOException {
		txtCodigo.setText(Integer.toString(codigo));
		txtNomeCompleto.setText(nomeCompleto);
		txtEmail.setText(email);
		txtUsuario.setText(usuario);
		txtSenha.setText(senha);
		cmbPerfil.setValue(perfil);
		cmbSituacao.setValue(situacao);
	}

	/**
	 * Botão para fechar a janela
	 */
	@FXML
	private void btnCancelarAction() {
		fechaJanela();
	}

	/**
	 * Botão para limpar os campos
	 */
	@FXML
	private void btnLimparAction() {
		limparCampos();
	}

	/**
	 * Botão que verifica, caso o campo codigo não estiver vazio, o método alterar é invocado, senão é invocado o método inserir
	 */
	@FXML
	public void btnSalvarAction() {
		if (txtCodigo.getText().trim().length() != 0) {
			alterar();
		}
		if (txtCodigo.getText().trim().length() == 0) {
			verificaInsert();
		}
	}

	/**
	 * Método que fecha essa janela
	 */
	public void fechaJanela() {
		Stage stage = (Stage) btnCancelar.getScene().getWindow();
		stage.close();
	}

	/**
	 * Passa os dados e verifica se a inserção ocorreu com sucesso, se retornar true é mostrado a mensagem de sucesso, senão é mostrado uma mensagem com erro
	 */
	public void verificaInsert() {
		try {
			usuarios = new Usuarios(txtUsuario.getText(), txtNomeCompleto.getText(), txtSenha.getText(), txtEmail.getText(), cmbSituacao.getValue(), cmbPerfil.getValue());

			if (verificaCampos() && usuarios.insere()) {
				Dialogs.create().title("Sucesso").message("Usuario cadastrado com sucesso!").showInformation();
				fechaJanela();
			}
			else {
				Dialogs.create().title("Error").message("Erro ao inserir dados, verifique se todos os campos estão preenchidos!").showError();
			}
		}
		catch (Exception e3) {
			lblMensagem.setText("Campos não preenchidos");
		}
	}

	/**
	 * Passa os dados para alteração, se retornar true é recebido a mensagem de sucesso e a janela de cadastro é fechada
	 */
	public void alterar() {
		if (usuarios
				.altera(Integer.parseInt(txtCodigo.getText()), txtUsuario.getText(), txtNomeCompleto.getText(), txtSenha.getText(), txtEmail.getText(), cmbSituacao.getValue(), cmbPerfil.getValue())) {
			Dialogs.create().title("Alterar").masthead(null).message("Usuario alterado com sucesso!").showInformation();
			fechaJanela();
		}
		else {
			Dialogs.create().title("Error").message("Verifique se todos os campos estão preenchidos corretamente!").showError();
		}
	}

	/**
	 * Método que verifica se os campos não estão vazios
	 */
	public boolean verificaCampos() {
		return txtUsuario.getText().trim().length() != 0 && txtNomeCompleto.getText().trim().length() != 0 && txtSenha.getText().trim().length() != 0 && cmbSituacao.getValue().trim().length() != 0
				&& cmbPerfil.getValue().trim().length() != 0;
	}

	/**
	 * Método para limpar os campos
	 */
	public void limparCampos() {
		txtCodigo.clear();
		txtUsuario.clear();
		txtNomeCompleto.clear();
		txtSenha.clear();
		txtEmail.clear();
		cmbPerfil.getItems().clear();
		cmbSituacao.getItems().clear();
		adicionaItems();
	}

	/**
	 * Adiciona items aos dois combobox
	 */
	public void adicionaItems() {
		cmbPerfil.getItems().addAll("Administrador", "Vendedor");
		cmbSituacao.getItems().addAll("Ativo", "Inativo");
	}
}
