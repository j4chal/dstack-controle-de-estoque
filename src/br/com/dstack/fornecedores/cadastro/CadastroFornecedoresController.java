package br.com.dstack.fornecedores.cadastro;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import br.com.dstack.fornecedores.Fornecedores;
import br.com.dstack.utils.Validacao;

public class CadastroFornecedoresController {

	@FXML
	private Button btnCancelar;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnLimpar;

	@FXML
	private TextField txtCodigo;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtEndereco;
	@FXML
	private TextField txtBairro;
	@FXML
	private TextField txtNumero;
	@FXML
	private TextField txtCidade;
	@FXML
	private ComboBox<String> cmbUf;
	@FXML
	private TextField txtCep;
	@FXML
	private TextField txtCnpj;
	@FXML
	private TextField txtInscricaoEstadual;
	@FXML
	private TextField txtTelefone;
	@FXML
	private TextField txtCelular;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtContato;
	@FXML
	private TextField txtFax;
	@FXML
	private TextField txtObservacoes;

	@FXML
	private Label lblMensagem;

	private Fornecedores fornecedores = new Fornecedores();
	private static Logger logger = LogManager.getLogger(CadastroFornecedoresController.class);

	public CadastroFornecedoresController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CadastroFornecedoresView.fxml"));
			fxmlLoader.setController(this);
			Parent parent;
			parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Cadastrar Fornecedores | DStack - Controle de Estoque");
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
	 * Método que inicializa toda vez que essa classe é chamada
	 */
	@FXML
	private void initialize() {
		setUf();
		validacaoDeCampos();
	}

	/**
	 * Botão que salva ou altera baseado no campo codigo, se estiver vazio, cadastra, senão altera
	 */
	@FXML
	private void btnSalvarAction() {
		if (txtCodigo.getText().trim().length() != 0) {
			alterar();
		}
		if (txtCodigo.getText().trim().length() == 0) {
			verificarInsert();
		}
	}

	/**
	 * Botão que chama o método para fechar a janela
	 */
	@FXML
	private void btnCancelarAction() {
		fechaJanela();
	}

	/**
	 * Botão que chama o método para limpar os campos
	 */
	@FXML
	private void btnLimparAction() {
		limparCampos();
	}

	/**
	 * Limpa os campos
	 */
	private void limparCampos() {
		txtCodigo.clear();
		txtNome.clear();
		txtEndereco.clear();
		txtBairro.clear();
		txtNumero.clear();
		txtCidade.clear();
		txtCep.clear();
		txtCnpj.clear();
		txtInscricaoEstadual.clear();
		txtEmail.clear();
		txtTelefone.clear();
		txtCelular.clear();
		txtFax.clear();
		txtContato.clear();
		txtObservacoes.clear();
		cmbUf.getItems().clear();
		setUf();
		cmbUf.setPromptText("Selecione");
		lblMensagem.setText("");
	}

	/**
	 * Método que pega o Stage da janela e chama o método close()
	 */
	public void fechaJanela() {
		Stage stage = (Stage) btnCancelar.getScene().getWindow();
		stage.close();
	}

	/**
	 * Seta os valores nos campos de editar, do item selecionado na tabela
	 * 
	 * @param codigo
	 * @param nome
	 * @param endereco
	 * @param numero
	 * @param bairro
	 * @param cidade
	 * @param estado
	 * @param cep
	 * @param cnpj
	 * @param ie
	 * @param email
	 * @param telefone
	 * @param celular
	 * @param fax
	 * @param contato
	 * @param observacoes
	 */
	public void setValores(String codigo, String nome, String endereco, int numero, String bairro, String cidade, String estado, int cep, long cnpj, long ie, String email, long telefone,
			long celular, long fax, String contato, String observacoes) {
		txtCodigo.setText(codigo);
		txtNome.setText(nome);
		txtEndereco.setText(endereco);
		txtBairro.setText(bairro);
		txtNumero.setText(Integer.toString(numero));
		txtCidade.setText(cidade);
		cmbUf.getSelectionModel().select(estado);
		txtCep.setText(Integer.toString(cep));
		txtCnpj.setText(Long.toString(cnpj));
		txtInscricaoEstadual.setText(Long.toString(ie));
		txtEmail.setText(email);
		txtTelefone.setText(Long.toString(telefone));
		txtCelular.setText(Long.toString(celular));
		txtFax.setText(Long.toString(fax));
		txtContato.setText(contato);
		txtObservacoes.setText(observacoes);
	}

	/**
	 * Passa os dados e verifica se a inserção ocorreu com sucesso, se retornar true é mostrado a mensagem de sucesso, senão é mostrado uma mensagem com erro
	 */
	public void verificarInsert() {
		try {
			fornecedores = new Fornecedores(txtCodigo.getText(), txtNome.getText(), txtCnpj.getText(), txtInscricaoEstadual.getText(), txtTelefone.getText(), txtCelular.getText(), txtFax.getText(),
					txtCep.getText(), txtEndereco.getText(), txtNumero.getText(), txtBairro.getText(), txtCidade.getText(), cmbUf.getValue(), txtEmail.getText(), txtContato.getText(),
					txtObservacoes.getText(), null);

			if (fornecedores.insere()) {
				Dialogs.create().title("Sucesso").message("Fornecedor cadastrado com sucesso!").showInformation();
				fechaJanela();
			}
			else {
				Dialogs.create().title("Error").message("Erro ao inserir dados, verifique se todos os campos necessarios estão preenchidos!").showError();
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
		if (fornecedores.altera(Integer.parseInt(txtCodigo.getText()), txtNome.getText(), Long.parseLong(txtCnpj.getText()), Long.parseLong(txtInscricaoEstadual.getText()),
				Long.parseLong(txtTelefone.getText()), Long.parseLong(txtCelular.getText()), Long.parseLong(txtFax.getText()), Integer.parseInt(txtCep.getText()), txtEndereco.getText(),
				Integer.parseInt(txtNumero.getText()), txtBairro.getText(), txtCidade.getText(), cmbUf.getValue(), txtEmail.getText(), txtContato.getText(), txtObservacoes.getSelectedText())) {
			Dialogs.create().title("Sucesso").message("Fornecedor alterado com sucesso!").showInformation();
			fechaJanela();
		}
		else {
			Dialogs.create().title("Error").message("Verifique se todos os campos estão preenchidos corretamente!").showError();
		}
	}

	/**
	 * Método que verifica se os campos não estão vazios
	 */
	public boolean verificarCampos() {
		return txtNome.getText().trim().length() != 0 && txtCnpj.getText().trim().length() != 0 && txtInscricaoEstadual.getText().trim().length() != 0 && txtTelefone.getText().trim().length() != 0
				&& txtCep.getText().trim().length() != 0 && txtEndereco.getText().trim().length() != 0 && txtNumero.getText().trim().length() != 0 && txtBairro.getText().trim().length() != 0
				&& txtCidade.getText().trim().length() != 0 && !cmbUf.getValue().isEmpty();
	}

	/**
	 * Método para autocomplete de UF's que faz uso do ObservableList
	 */
	public void setUf() {
		cmbUf.setItems(fornecedores.consultaDeUf());
	}

	/**
	 * Faz a validação dos campos, restringe characteres e quantidade de digitos
	 */
	public void validacaoDeCampos() {
		Validacao.restringirNumerosECharactereEspecial(txtNome, 100, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtCnpj, 14, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtInscricaoEstadual, 13, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtTelefone, 10, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtCelular, 11, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtFax, 11, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtCep, 8, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtEndereco, 100, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtNumero, 10, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtBairro, 60, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtCidade, 50, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtContato, 100, lblMensagem);
		Validacao.limitarCaracteres(txtEmail, 80, lblMensagem);
		Validacao.limitarCaracteres(txtObservacoes, 100, lblMensagem);
	}
}
