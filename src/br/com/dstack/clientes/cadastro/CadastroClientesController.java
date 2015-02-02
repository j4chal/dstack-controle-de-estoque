package br.com.dstack.clientes.cadastro;

import java.io.IOException;
import java.time.LocalDate;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.dialog.Dialogs;

import br.com.dstack.clientes.Clientes;
import br.com.dstack.utils.Validacao;

public class CadastroClientesController {

	@FXML
	private TextField txtCodigo;
	@FXML
	private TextField txtNome;
	@FXML
	private TextField txtFantasia;
	@FXML
	private TextField txtCpf;
	@FXML
	private TextField txtRg;
	@FXML
	private ComboBox<String> cmbPessoa;
	@FXML
	private TextField txtEmail;
	@FXML
	private ComboBox<String> cmbSexo;
	@FXML
	private DatePicker pckDataNascimento;
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
	private TextField txtTelefone;
	@FXML
	private TextField txtFax;
	@FXML
	private TextField txtBanco;
	@FXML
	private TextField txtAgencia;

	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnFechar;
	@FXML
	private Button btnLimpar;
	@FXML
	private Label lblMensagem;

	private Clientes clientes = new Clientes();
	private static Logger logger = LogManager.getLogger(CadastroClientesController.class);

	/**
	 * Construtor que inicializa a View
	 * 
	 * @throws IOException
	 */
	public CadastroClientesController() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CadastroClientesView.fxml"));
			fxmlLoader.setController(this);
			Parent parent = fxmlLoader.load();
			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle("Cadastrar Clientes | DStack - Controle de Estoque");
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
		setComboBoxes();
		validacaoDeCampos();
		cssNosBotoes();
	}

	/**
	 * Seta css nos botões salvar e limpar
	 */
	public void cssNosBotoes() {
		btnSalvar.setStyle("-fx-background-image: url('salvar.png'); -fx-background-repeat: no-repeat; -fx-background-position: left, center; -fx-background-size: 32 32;");
		btnLimpar.setStyle("-fx-background-image: url('limpar.png'); -fx-background-repeat: no-repeat; -fx-background-position: left, center; -fx-background-size: 32 32;");
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
	private void btnFecharAction() {
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
		txtFantasia.clear();
		txtCpf.clear();
		txtRg.clear();
		txtEmail.clear();
		txtEndereco.clear();
		txtBairro.clear();
		txtNumero.clear();
		txtCidade.clear();
		txtCep.clear();
		txtTelefone.clear();
		txtFax.clear();
		txtBanco.clear();
		txtAgencia.clear();
		cmbPessoa.getItems().clear();
		cmbSexo.getItems().clear();
		cmbUf.getItems().clear();
		setComboBoxes();
		cmbPessoa.setPromptText("Selecione o tipo");
		cmbSexo.setPromptText("Selecione o sexo");
		cmbUf.setPromptText("Selecione");
		lblMensagem.setText("");
	}

	/**
	 * Método que pega o Stage da janela e chama o método close()
	 */
	public void fechaJanela() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}

	/**
	 * Seta os valores nos campos de editar, do item selecionado na tabela
	 * 
	 * @param codigo
	 * @param nome
	 * @param cpf
	 * @param rg
	 * @param fantasia
	 * @param pessoa
	 * @param sexo
	 * @param dataCadastro
	 * @param email
	 * @param dataNascimento
	 * @param cep
	 * @param endereco
	 * @param numero
	 * @param bairro
	 * @param cidade
	 * @param uf
	 * @param telefone
	 * @param fax
	 * @param banco
	 * @param agencia
	 */
	public void setValores(int codigo, String nome, String cpf, String rg, String fantasia, String pessoa, String sexo, String dataCadastro, String email, String dataNascimento, String cep,
			String endereco, int numero, String bairro, String cidade, String uf, String telefone, String fax, String banco, int agencia) {
		txtCodigo.setText(Integer.toString(codigo));
		txtNome.setText(nome);
		txtCpf.setText(cpf);
		txtRg.setText(rg);
		txtFantasia.setText(fantasia);
		cmbPessoa.setValue(pessoa);
		cmbSexo.setValue(sexo);
		txtEmail.setText(email);
		pckDataNascimento.setValue(LocalDate.parse(dataNascimento));
		txtEndereco.setText(endereco);
		txtNumero.setText(Integer.toString(numero));
		txtBairro.setText(bairro);
		txtCidade.setText(cidade);
		txtCep.setText(cep);
		cmbUf.setValue(uf);
		txtTelefone.setText(telefone);
		txtFax.setText(fax);
		txtBanco.setText(banco);
		txtAgencia.setText(Integer.toString(agencia));
	}

	/**
	 * Passa os dados e verifica se a inserção ocorreu com sucesso, se retornar true é mostrado a mensagem de sucesso, senão é mostrado uma mensagem com erro
	 */
	public void verificarInsert() {
		try {
			clientes = new Clientes(txtNome.getText(), txtCpf.getText(), txtRg.getText(), txtFantasia.getText(), cmbPessoa.getValue(), cmbSexo.getValue(), txtEmail.getText(), pckDataNascimento
					.getValue().toString(), txtCep.getText(), txtEndereco.getText(), Integer.parseInt(txtNumero.getText()), txtBairro.getText(), txtCidade.getText(), cmbUf.getValue(),
					txtTelefone.getText(), txtFax.getText(), txtBanco.getText(), Integer.parseInt(txtAgencia.getText()));

			if (clientes.insere()) {
				Dialogs.create().title("Sucesso").message("Cliente cadastrado com sucesso!").showInformation();
				fechaJanela();
			}
			else {
				Dialogs.create().title("Error").message("Erro ao inserir dados, verifique se todos os campos necessarios estão preenchidos!").showError();
			}
		}
		catch (Exception e2) {
			lblMensagem.setText("Campos não preenchidos");
		}
	}

	/**
	 * Passa os dados para alteração, se retornar true é recebido a mensagem de sucesso e a janela de cadastro é fechada
	 */
	public void alterar() {
		if (clientes.altera(Integer.parseInt(txtCodigo.getText()), txtNome.getText(), txtCpf.getText(), txtRg.getText(), txtFantasia.getText(), cmbPessoa.getValue(), cmbSexo.getValue(),
				txtEmail.getText(), pckDataNascimento.getValue().toString(), txtCep.getText(), txtEndereco.getText(), Integer.parseInt(txtNumero.getText()), txtBairro.getText(), txtCidade.getText(),
				cmbUf.getValue(), txtTelefone.getText(), txtFax.getText(), txtBanco.getText(), Integer.parseInt(txtAgencia.getText()))) {
			Dialogs.create().title("Sucesso").message("Cliente alterado com sucesso!").showInformation();
			fechaJanela();
		}
		else {
			Dialogs.create().title("Error").message("Erro ao inserir dados, verifique se todos os campos estão preenchidos corretamente!").showError();
		}
	}

	/**
	 * Método que verifica se os campos não estão vazios
	 */
	public boolean verificarCampos() {
		return !txtNome.getText().isEmpty() && !txtCpf.getText().isEmpty() && txtRg.getText().isEmpty() && !cmbPessoa.getValue().isEmpty() && !cmbSexo.getValue().isEmpty()
				&& !txtEmail.getText().isEmpty() && !txtCep.getText().isEmpty() && !txtEndereco.getText().isEmpty() && !txtNumero.getText().isEmpty() && !txtBairro.getText().isEmpty()
				&& !txtCidade.getText().isEmpty() && !cmbUf.getValue().isEmpty() && !txtTelefone.getText().isEmpty();
	}

	/**
	 * Método que seta UF's de uma ObservableList
	 */
	public void setComboBoxes() {
		cmbUf.setItems(clientes.consultaDeUf());
		cmbPessoa.getItems().addAll("Fisica", "Juridica");
		cmbSexo.getItems().addAll("Masculino", "Feminino");
	}

	/**
	 * Faz a validação dos campos, restringe characteres e quantidade de digitos
	 */
	public void validacaoDeCampos() {
		Validacao.restringirNumerosECharactereEspecial(txtNome, 100, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtCpf, 11, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtRg, 8, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtFantasia, 100, lblMensagem);
		Validacao.limitarCaracteres(txtEmail, 100, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtCep, 8, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtEndereco, 100, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtNumero, 10, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtBairro, 60, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtCidade, 60, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtTelefone, 10, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtFax, 11, lblMensagem);
		Validacao.restringirNumerosECharactereEspecial(txtBanco, 60, lblMensagem);
		Validacao.restringirLetrasECaractereEspecial(txtAgencia, 4, lblMensagem);
	}
}
